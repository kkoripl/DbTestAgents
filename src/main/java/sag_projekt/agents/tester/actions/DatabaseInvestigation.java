package sag_projekt.agents.tester.actions;

import com.mysql.cj.jdbc.MysqlDataSource;

import java.sql.*;
import java.util.*;

public class DatabaseInvestigation{
    private List<Long> investigationResults;
    static public List<String> investigationActions = Arrays.asList("SELECT * FROM articles_100;", "SELECT * FROM articles_k;");

    public DatabaseInvestigation(String dbToInvestigate) {
        makeDBInvestigation(dbToInvestigate);
    }

    private void makeDBInvestigation(String dbToInvestigate){
        investigationResults = new ArrayList<>();
        for (String action : investigationActions){
            long executionTime = Long.MAX_VALUE;
            try {
                Connection connection = null;
                switch(dbToInvestigate) {
                    case "MySQL":
                        connection = getMySQLConnection();
                        break;
                    case "PostgreSQL":
                        connection = getPostgreSQLConnection();
                        break;
                    default:
                        connection = getOracleConnection();
                        break;
                }
                Statement statement = connection.createStatement();
                long startTime = System.currentTimeMillis();
                ResultSet rs = statement.executeQuery(action);
                long endTime = System.currentTimeMillis();
                executionTime = endTime - startTime;
                rs.close();
                statement.close();
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            investigationResults.add(executionTime);
        }

    }

    private Connection getOracleConnection() {
        return getMySQLConnection();
    }

    private Connection getMySQLConnection() {
        MysqlDataSource dataSource = new MysqlDataSource();
        dataSource.setUser("root");
        dataSource.setPassword("toor");
        dataSource.setDatabaseName("mysql");
        dataSource.setServerName("localhost");
        Connection connection = null;
        try {
             connection = dataSource.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }

    private Connection getPostgreSQLConnection() {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres", "postgres", "toor");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }

    public List<Long> getInvestigationResults() {
        return investigationResults;
    }
}
