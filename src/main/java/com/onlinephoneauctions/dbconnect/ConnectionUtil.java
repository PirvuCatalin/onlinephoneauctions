package com.onlinephoneauctions.dbconnect;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Class that contains the necessary static methods to retrieve the Connection with the database from VCAP_SERVICES.
 */
public class ConnectionUtil {
    private static String DB_URL = "jdbc:h2:mem:testdb;USER=sa;PASSWORD=password";
    private static Connection conn;

    static {
        try {
            conn = DriverManager.getConnection(DB_URL);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private ConnectionUtil() {
    }

    /**
     * Returns the Connection object.
     * Requires the JDBC URL as input.
     */
    public static Connection getConnection() {
        if (conn == null) {
            try {
                conn = DriverManager.getConnection(DB_URL);
            } catch (Exception e) {
                System.out.print("Connection Error: ");
                e.printStackTrace();
            }
        }
        return conn;
    }

    /**
     * The output of this method is not guaranteed to work properly when
     * the given query is not returning the requested number of columns
     */
    public static List<HashMap<Integer, String>> getMultipleColumns(String query, int numberOfColumns) {
        List<HashMap<Integer, String>> response = new ArrayList<>();
        HashMap<Integer, String> mMap;
        try {
            PreparedStatement prepareStatement = conn.prepareStatement(query);
            ResultSet resultSet = prepareStatement.executeQuery();
            while (resultSet.next()) {
                mMap = new HashMap<>();
                for (int i = 1; i <= numberOfColumns; i++) {
                    mMap.put(i, resultSet.getString(i));
                }
                response.add(mMap);
            }
        } catch (SQLException e) {
            System.out.println("Couldn't parse the select query!");
            e.printStackTrace();
        }
        return response;
    }

    public static String parseUpdateQuery(String query) {
        int rowCount = 0;
        try {
            Statement stmt = conn.createStatement();
            stmt.executeUpdate(query);
            rowCount = stmt.getUpdateCount();
        } catch (SQLException e) {
            System.out.println("Couldn't parse the update query!");
            e.printStackTrace();
        }

        return "Succesfully updated/inserted " + rowCount + " rows!";
    }
}
