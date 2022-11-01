package com.vld;

import java.sql.*;

public class ConnectionDB {

    public String getURL() {
        return URL;
    }

    public void checkConnection(){

        if(connection == null){
            StaticFunctions.showMessage("Connection is failed!");
            System.exit(2);
        }
    }

    final private String URL = "jdbc:mysql://localhost:3306/time_manager" + "?useUnicode=true&serverTimezone="
           + StaticFunctions.getTimeZone() ;
    final private String NAME = "root";
    final private String PASSWORD = "root";

    private Connection connection;
    private Statement statement;

    public ConnectionDB(){
        initConnection();
        initStatement();
    }

    private void initConnection() {
        try {
            connection = DriverManager.getConnection(URL,NAME,PASSWORD);
        } catch (SQLException throwables) {
           // throwables.printStackTrace();
            checkConnection();
        }
    }

    private void initStatement() {

        checkConnection();

        try {
            statement = connection.createStatement();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }

    public Connection getConnection() {
        checkConnection();
        return connection;
    }

    public Statement getStatement() {
        checkConnection();
        return statement;
    }

    public ResultSet getResultSet(String query){
        checkConnection();

        try {
            return statement.executeQuery(query);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return  null;
    }

    public void executeQuery(String query){

        checkConnection();

        if(statement == null) return;

        try {
            statement.execute(query);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }



    @Override
    protected void finalize() throws Throwable {
        try {
            statement.close();
            connection.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}
