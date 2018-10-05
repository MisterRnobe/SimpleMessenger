package server.database;

import java.sql.SQLException;

@FunctionalInterface
interface Function<I,O>
{
    O apply(I value) throws SQLException;
}