package io.broadcast.wrapper.jdbc;

import lombok.RequiredArgsConstructor;
import io.broadcast.engine.record.ChunkyRecordSelector;
import io.broadcast.engine.record.Record;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

@RequiredArgsConstructor
public class JdbcRecordSelector<I> implements ChunkyRecordSelector<I> {

    private final JdbcRecordMetadata metadata;

    @Override
    public long totalSize() {
        Connection connection = metadata.getConnection();
        String sql = String.format("SELECT COUNT(%s) FROM %s", metadata.getIdColumn(), metadata.getTable());

        try (PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            if (resultSet.next()) {
                return resultSet.getLong(1);
            }
        } catch (SQLException e) {
            throw new BroadcastJdbcException("Failed calculate a total record-selector size", e);
        } finally {
            if (metadata.isAutoCloseable()) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    throw new BroadcastJdbcException("Failed calculate a total record-selector size", e);
                }
            }
        }
        throw new BroadcastJdbcException("Failed calculate a total record-selector size");
    }

    @Override
    public long chunkSize() {
        Integer chunkSize = metadata.getChunkSize();
        if (chunkSize == null) {
            throw new BroadcastJdbcException("metadata.chunkSize is not initialized [example=2000]");
        }
        return chunkSize;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Iterable<Record<I>> select(long index) {
        Connection connection = metadata.getConnection();

        String sql = String.format("SELECT %s FROM %s LIMIT %s OFFSET %s",
                metadata.getIdColumn(),
                metadata.getTable(),
                chunkSize(),
                chunkSize() * index);

        try (PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            Set<Record<I>> selectionSet = new HashSet<>();

            while (resultSet.next()) {
                selectionSet.add(new Record<>((I) resultSet.getObject(1)));
            }

            return selectionSet;
        } catch (SQLException e) {
            throw new BroadcastJdbcException("Failed select a records", e);
        } finally {
            if (metadata.isAutoCloseable()) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    throw new BroadcastJdbcException("Failed select a records", e);
                }
            }
        }
    }
}
