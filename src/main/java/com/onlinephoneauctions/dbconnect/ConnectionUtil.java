package com.onlinephoneauctions.dbconnect;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Class that contains the necessary static methods to retrieve the Connection with the database and make the specific requests.
 * This class will automatically connect to the database on server start-up (using the static initializer block), if not it will
 * probably fail miserably and will require the administrator to check some logs.
 */
public class ConnectionUtil {
    /**
     * {@link String} that contains the database url with the user and password defined in
     * <a href="file:../application.properties">/resources/application.properties</a>
     */
    private static String DB_URL = "jdbc:h2:mem:testdb;USER=sa;PASSWORD=password";
    /**
     * {@link Connection} containing the connection to the database that will be initialized on server start-up in the static initializer block
     */
    private static Connection conn;

    static {
        try {
            conn = DriverManager.getConnection(DB_URL);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Method used for selecting from the database.
     * The output of this method is not guaranteed to work properly when the given query is not returning the requested number of columns!
     * This is a helper method that lets the user work with classic Java objects, rather than {@link ResultSet} and other {@link java.sql} objects
     *
     * @param query           the query
     * @param numberOfColumns the number of columns expected to be returned
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

    /**
     * Method used for updating / inserting into the database.
     */
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
