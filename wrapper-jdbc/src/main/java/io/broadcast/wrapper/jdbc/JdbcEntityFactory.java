package io.broadcast.wrapper.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;

@FunctionalInterface
public interface JdbcEntityFactory<I, T> {

    T create(I id, ResultSet resultSet) throws SQLException;
}
