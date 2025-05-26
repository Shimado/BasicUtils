package org.shimado.basicutils.sql;

import org.bukkit.plugin.Plugin;
import org.shimado.basicutils.BasicUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.*;
import java.util.*;
import java.util.stream.Collectors;

public class SQLConnection {

    public SQLConnection(boolean isMySQL, boolean isSQLite, Map<String, Object> SQLData, List<String> tables){
        reload(isMySQL, isSQLite, SQLData, tables);
    }

    private Connection connection = null;
    private File sqliteFile = null;
    private boolean isMySQL = false;
    private boolean isSQLite = true;
    private Map<String, Object> SQLData;
    private List<String> tables;

    public void reload(boolean isMySQL, boolean isSQLite, Map<String, Object> SQLData, List<String> tables){
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
        if(this.isMySQL){
            this.connection = createMySQLConnection(
                    (String) this.SQLData.get("host"),
                    (int) this.SQLData.get("port"),
                    (String) this.SQLData.get("database"),
                    (String) this.SQLData.get("user"),
                    (String) this.SQLData.get("password")
            );
        }
        //SQLite
        else if(this.isSQLite){
            this.connection = createSQLiteConnection();
        }

        if(this.connection != null){
            this.tables.forEach(it -> initTable(it));
        }
    }


    /**
     * ОТКРЫВАЕТ СОЕДИНЕНИЕ MYSQL
     * **/

    private Connection createMySQLConnection(String host, int port, String database, String username, String password){
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

    private Connection createSQLiteConnection(){
        try {
            if(this.connection != null && !this.connection.isClosed()){
                return this.connection;
            }

            if(this.sqliteFile == null){
                this.sqliteFile = new File(BasicUtils.getPlugin().getDataFolder(), "storage.db");
                if(!this.sqliteFile.exists()){
                    this.sqliteFile.createNewFile();
                }
            }

            Class.forName("org.sqlite.JDBC");
            return DriverManager.getConnection("jdbc:sqlite:" + this.sqliteFile.getAbsolutePath());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * ПРОВЕРЯЕТ СОЕДИНЕНИЕ
     * **/

    private Connection checkConnection(){
        try {
            if(this.connection == null || !this.connection.isValid(2)){
                dbInit();
                return this.connection;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return this.connection;
    }



    /**
     * ПОЛУЧАЕТ СОЕДИНЕНИЕ
     * **/

    public Connection getConnection(){
        return checkConnection();
    }


    /**
     * ЗАКРЫВАЕТ СОЕДИНЕНИЕ
     * **/

    public void closeConnection(){
        try {
            if(this.connection != null && !this.connection.isClosed()){
                this.connection.close();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * ИНИЦИИРУЕТ ТАБЛИЦУ
     * **/

    private void initTable(String type){
        try {
            Class clazz = Class.forName(BasicUtils.getPlugin().getClass().getName());
            InputStream inputStream = null;
            if(this.isMySQL){
                inputStream = clazz.getResourceAsStream("/sql/" + type + ".sql");
            }
            if(inputStream == null){
                inputStream = clazz.getResourceAsStream("/sqlite/" + type + ".sql");
            }
            String setup = new BufferedReader(new InputStreamReader(inputStream)).lines().collect(Collectors.joining("\n"));

            for(String query : setup.split(";")){
                if(query != null && !query.trim().isEmpty()){
                    this.connection.prepareStatement(query.trim()).execute();
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    public void closeStatement(PreparedStatement preparedStatement){
        try {
            if(preparedStatement != null){
                preparedStatement.close();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
