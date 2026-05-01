package org.shimado.basicutils.sql;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.shimado.basicutils.BasicUtils;
import org.shimado.basicutils.cycles.SchedulerAdapter;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;

public class SQLConnection {

    private Connection connection = null;
    private File sqliteFile = null;
    private boolean isMySQL = false;
    private boolean isSQLite = true;
    private Map<String, Object> SQLData;
    private List<String> tables;

    private final Class<?> pluginClass;

    public SQLConnection(boolean isMySQL, boolean isSQLite, @NotNull Map<String, Object> SQLData, @NotNull List<String> tables){
        this.pluginClass = BasicUtils.getPlugin().getClass();
        reload(isMySQL, isSQLite, SQLData, tables);
    }


    public void reload(boolean isMySQL, boolean isSQLite, @NotNull Map<String, Object> SQLData, @NotNull List<String> tables){
        this.isMySQL = isMySQL;
        this.isSQLite = isSQLite;
        this.SQLData = SQLData;
        this.tables = tables;
        closeConnection();
        dbInit();
    }


    /**
     * ИНИЦИИРУЕТ ЗАПУСК
     * **/

    private void dbInit(){
        //MySQL
        if(isMySQL){
            connection = createMySQLConnection(
                    (String) SQLData.get("host"),
                    (int) SQLData.get("port"),
                    (String) SQLData.get("database"),
                    (String) SQLData.get("user"),
                    (String) SQLData.get("password")
            );
        }
        //SQLite
        else if(isSQLite){
            connection = createSQLiteConnection();
        }

        if(connection != null){
            tables.forEach(this::initTable);
        }
    }


    /**
     * ОТКРЫВАЕТ СОЕДИНЕНИЕ MYSQL
     * **/

    @NotNull
    private Connection createMySQLConnection(@NotNull String host, int port, @NotNull String database, @NotNull String username, @NotNull String password){
        Properties properties = new Properties();
        properties.setProperty("user", username);
        properties.setProperty("password", password);
        properties.setProperty("autoReconnect", "true");
        properties.setProperty("verifyServerCertificate", "false");
        properties.setProperty("useSSL", "false");
        try {
            return DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + database, properties);
        } catch (SQLException e) {
            throw new RuntimeException("Failed to connect to MySQL: " + host + ":" + port + "/" + database, e);
        }
    }


    /**
     * ОТКРЫВАЕТ СОЕДИНЕНИЕ SQLITE
     * **/

    @NotNull
    private Connection createSQLiteConnection(){
        try {
            sqliteFile = new File(BasicUtils.getPlugin().getDataFolder(), "storage.db");
            if (!sqliteFile.exists() && !sqliteFile.createNewFile()) {
                throw new RuntimeException("Cannot create SQLite file: " + sqliteFile.getAbsolutePath());
            }

            Class.forName("org.sqlite.JDBC");
            return DriverManager.getConnection("jdbc:sqlite:" + sqliteFile.getAbsolutePath());
        } catch (Exception e) {
            throw new RuntimeException("Failed to open SQLite: " + e.getMessage(), e);
        }
    }


    /**
     * ПОЛУЧАЕТ СОЕДИНЕНИЕ
     * **/

    @Nullable
    public Connection getConnection(){
        try {
            if (connection == null || !connection.isValid(2)) {
                dbInit();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return connection;
    }


    /**
     * ЗАКРЫВАЕТ СОЕДИНЕНИЕ
     * **/

    public void closeConnection(){
        if (connection == null) return;
        try {
            if (!connection.isClosed()) connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            connection = null;
        }
    }


    /**
     * ИНИЦИИРУЕТ ТАБЛИЦУ
     * **/

    private void initTable(@NotNull String type){
        try {
            InputStream inputStream = null;
            if(isMySQL){
                inputStream = pluginClass.getResourceAsStream("/sql/" + type + ".sql");
            }
            if(inputStream == null){
                inputStream = pluginClass.getResourceAsStream("/sqlite/" + type + ".sql");
            }
            if (inputStream == null) {
                throw new RuntimeException("SQL resource not found for table: " + type);
            }

            String setup = new BufferedReader(new InputStreamReader(inputStream)).lines().collect(Collectors.joining("\n"));

            for(String query : setup.split(";")){
                if(query != null && !query.trim().isEmpty()){
                    connection.prepareStatement(query.trim()).execute();
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to init table '" + type + "': " + e.getMessage(), e);
        }
    }


    public void closeStatement(@Nullable PreparedStatement preparedStatement) {
        if (preparedStatement == null) return;
        try {
            preparedStatement.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    public void runSQLTask(@NotNull SQLRunnable task){
        SchedulerAdapter.runTaskAsynchronously(() -> {
            Connection conn = getConnection();

            List<PreparedStatement> preparedStatements = new ArrayList<>();
            try {
                preparedStatements = task.run(conn);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            } finally {
                preparedStatements.forEach(this::closeStatement);
            }
        });
    }

}
