package sag_projekt.agents.tester.actions;

import com.mysql.cj.jdbc.MysqlDataSource;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.sql.*;
import java.util.*;

public class DatabaseInvestigation{
    private List<Long> investigationResults;
    private List<String> investigationActions;

    public DatabaseInvestigation(String dbToInvestigate) {
        /* Todo: MECHANIZM DO NADAWANIA AKCJI DO SPRAWDZENIA BAZY ZALEZNIE OD TEGO, KTORY AGENT WYWOLUJE,
                 MOZE BYC NA PODSTAWIE NAZWY BAZY LUB POSZUKIWANEGO SLOWA, PACHNIE JAKIMS FACTORY CZY COS
                 AKTUALNIE NASTAWIONE U GORY PO NAZWIE BAZY, ALE WSZYSTKIE DANE BADANIA PODSTAWIANE
                 JEST TO POLE DBNAME W TESTRZE - JAK ZMIANA TO REFACTOR
                 TAK SAMO WYNIKI TESTOW SA AKTUALNIE LADOWANE W DOUBLE - JAK BEDZIEMY PYTAC O LICZBE REKORDOW TRZEBA TO ZMIENIC NA INT
         */
        this.investigationActions = Arrays.asList("SELECT * FROM articles_100;", "SELECT * FROM articles_k;");
        makeDBInvestigation(dbToInvestigate);
    }

    private void makeDBInvestigation(String dbToInvestigate){
        investigationResults = new ArrayList<>();
        for (String action : this.investigationActions){
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
