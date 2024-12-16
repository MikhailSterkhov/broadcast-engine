package io.broadcast.wrapper.jdbc;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.sql.Connection;

@Getter
@Builder
@ToString
public class JdbcRecordMetadata<I, T> {

    private final Connection connection;
    private final String table;
    private final String idColumn;
    private final boolean autoCloseable;
    private final Integer chunkSize;
    private final JdbcEntityFactory<I, T> jdbcEntityFactory;
}
