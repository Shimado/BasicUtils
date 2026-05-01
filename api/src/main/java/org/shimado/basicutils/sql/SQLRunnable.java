package org.shimado.basicutils.sql;

import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

@FunctionalInterface
public interface SQLRunnable {
    List<PreparedStatement> run(@NotNull Connection conn) throws SQLException;
}
