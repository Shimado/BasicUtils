package org.shimado.basicutils.sql;

import org.shimado.basicutils.BasicUtils;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
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

    public SQLConnection(boolean isMySQL, boolean isSQLite, @Nonnull Map<String, Object> SQLData, @Nonnull List<String> tables){
        reload(isMySQL, isSQLite, SQLData, tables);
    }


    public void reload(boolean isMySQL, boolean isSQLite, @Nonnull Map<String, Object> SQLData, @Nonnull List<String> tables){
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
            tables.forEach(it -> initTable(it));
        }
    }


    /**
     * ОТКРЫВАЕТ СОЕДИНЕНИЕ MYSQL
     * **/

    @Nonnull
    private Connection createMySQLConnection(@Nonnull String host, int port, @Nonnull String database, @Nonnull String username, @Nonnull String password){
        Properties properties = new Properties();
        properties.setProperty("user", username);
        properties.setProperty("password", password);
        properties.setProperty("autoReconnect", "true");
        properties.setProperty("verifyServerCertificate", "false");
        properties.setProperty("useSSL", "false");
        try {
            return DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + database, properties);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * ОТКРЫВАЕТ СОЕДИНЕНИЕ SQLITE
     * **/

    @Nonnull
    private Connection createSQLiteConnection(){
        try {
            sqliteFile = new File(BasicUtils.getPlugin().getDataFolder(), "storage.db");
            if(!sqliteFile.exists()){
                sqliteFile.createNewFile();
            }

            Class.forName("org.sqlite.JDBC");
            return DriverManager.getConnection("jdbc:sqlite:" + sqliteFile.getAbsolutePath());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * ПОЛУЧАЕТ СОЕДИНЕНИЕ
     * **/

    @Nonnull
    public Connection getConnection(){
        try {
            if(connection == null || !connection.isValid(2)){
                dbInit();
                return connection;
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
        try {
            if(connection != null && !connection.isClosed()){
                connection.close();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * ИНИЦИИРУЕТ ТАБЛИЦУ
     * **/

    private void initTable(@Nonnull String type){
        try {
            Class clazz = Class.forName(BasicUtils.getPlugin().getClass().getName());
            InputStream inputStream = null;
            if(isMySQL){
                inputStream = clazz.getResourceAsStream("/sql/" + type + ".sql");
            }
            if(inputStream == null){
                inputStream = clazz.getResourceAsStream("/sqlite/" + type + ".sql");
            }
            String setup = new BufferedReader(new InputStreamReader(inputStream)).lines().collect(Collectors.joining("\n"));

            for(String query : setup.split(";")){
                if(query != null && !query.trim().isEmpty()){
                    connection.prepareStatement(query.trim()).execute();
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    public void closeStatement(@Nullable PreparedStatement preparedStatement){
        try {
            if(preparedStatement != null){
                preparedStatement.close();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
