package io.broadcast.example;

import io.broadcast.engine.BroadcastEngine;
import io.broadcast.engine.BroadcastPipeline;
import io.broadcast.engine.PreparedMessage;
import io.broadcast.engine.dispatch.STDOUTBroadcastDispatcher;
import io.broadcast.engine.record.extract.Extractors;
import io.broadcast.engine.record.map.RecordsMap;
import io.broadcast.wrapper.jdbc.JdbcRecordMetadata;
import io.broadcast.wrapper.jdbc.JdbcRecordSelector;
import net.bytebuddy.utility.RandomString;

import java.sql.*;

public class JdbcH2BroadcastExample {

    private static final RecordsMap<Integer, String> writtenPlayersById = RecordsMap.newHashMap();

    public static void main(String[] args) {
        JdbcRecordMetadata jdbcRecordMetadata =
                JdbcRecordMetadata.builder()
                        .connection(createH2Connection())
                        .table("players")
                        .idColumn("id")
                        .chunkSize(10) // [effectivity minimal: 1000-2000]
                        .autoCloseable(false)
                        .build();

        PreparedMessage<Integer> preparedMessage
                = PreparedMessage.serializeContent((record) -> String.format("[ID: %s] -> \"Hello world, %s!\"", record.getId(), writtenPlayersById.get(record)));

        BroadcastPipeline broadcastPipeline = BroadcastPipeline.createPipeline()
                .setDispatcher(new STDOUTBroadcastDispatcher<>())
                .setRecordExtractor(Extractors.chunkyParallelAsync(new JdbcRecordSelector<>(jdbcRecordMetadata)))
                .setPreparedMessage(preparedMessage);

        BroadcastEngine broadcastEngine = new BroadcastEngine(broadcastPipeline);
        broadcastEngine.broadcastNow();
    }

    private static Connection createH2Connection() {
        try {
            Connection connection = DriverManager.getConnection("jdbc:h2:mem:default", "username", "password");

            PreparedStatement setMode = connection.prepareStatement("set mode MySQL");
            setMode.execute();
            setMode.close();

            PreparedStatement createTable = connection.prepareStatement("create table players (id int not null auto_increment, name varchar(255))");
            createTable.execute();
            createTable.close();

            connection.setAutoCommit(false);
            for (int i = 0; i < 100; i++) {
                String playerName = RandomString.make(16);

                PreparedStatement statement = connection.prepareStatement("insert into players (name) values ('" + playerName + "')", PreparedStatement.RETURN_GENERATED_KEYS);
                statement.execute();

                try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        int generatedId = generatedKeys.getInt(1);

                        writtenPlayersById.put(generatedId, playerName);
                    }
                }

                statement.close();
            }

            connection.commit();
            connection.setAutoCommit(true);

            return connection;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
