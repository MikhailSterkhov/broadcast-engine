package io.broadcast.wrapper.jdbc;

import lombok.RequiredArgsConstructor;
import io.broadcast.engine.record.ChunkyRecordSelector;
import io.broadcast.engine.record.Record;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@RequiredArgsConstructor
public class JdbcRecordSelector<I, T> implements ChunkyRecordSelector<I, T> {

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
    public Iterable<Record<I, T>> select(long index) {
        Connection connection = metadata.getConnection();
        JdbcEntityFactory<I, T> jdbcEntityFactory = metadata.getJdbcEntityFactory();

        String sql = String.format("SELECT %s FROM %s LIMIT %s OFFSET %s",
                jdbcEntityFactory == null ? metadata.getIdColumn() : "*",
                metadata.getTable(),
                chunkSize(),
                chunkSize() * index);

        try (PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            Set<Record<I, T>> selectionSet = new HashSet<>();

            while (resultSet.next()) {
                I id = (I) resultSet.getObject(1);
                T entity = null;

                if (jdbcEntityFactory != null) {
                    entity = jdbcEntityFactory.create(id, resultSet);
                }

                selectionSet.add(new Record<>(id, entity));
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
