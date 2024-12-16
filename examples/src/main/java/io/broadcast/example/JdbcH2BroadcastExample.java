package io.broadcast.example;

import io.broadcast.engine.BroadcastEngine;
import io.broadcast.engine.BroadcastPipeline;
import io.broadcast.engine.PreparedMessage;
import io.broadcast.engine.dispatch.STDOUTBroadcastDispatcher;
import io.broadcast.engine.record.extract.Extractors;
import io.broadcast.wrapper.jdbc.JdbcRecordMetadata;
import io.broadcast.wrapper.jdbc.JdbcRecordSelector;
import net.bytebuddy.utility.RandomString;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class JdbcH2BroadcastExample {

    public static void main(String[] args) {
        JdbcRecordMetadata<Integer, String> jdbcRecordMetadata =
                JdbcRecordMetadata.<Integer, String>builder()
                        .connection(createH2Connection())
                        .table("players")
                        .idColumn("id")
                        .chunkSize(10) // [effectivity minimal: 1000-2000]
                        .autoCloseable(false)
                        .jdbcEntityFactory((id, resultSet) -> resultSet.getString(2))
                        .build();

        PreparedMessage<Integer, String> preparedMessage
                = PreparedMessage.serializeContent((record) -> String.format("[ID: %s] -> \"Hello world, %s!\"", record.getId(), record.getEntity()));

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
                PreparedStatement statement = connection.prepareStatement("insert into players (name) values ('" + RandomString.make(16) + "')");
                statement.execute();
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
