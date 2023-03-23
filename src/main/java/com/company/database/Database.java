package com.company.database;

import com.company.model.Request;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.ArrayList;
import java.util.List;

public interface Database {
    List<Request> requestList = new ArrayList<>();

    static Connection getConnection() {


        final String DB_USERNAME = "";
        final String DB_PASSWORD = "";
        final String DB_URL = "jdbc:postgresql://localhost/";

        Connection conn = null;
        try {

            Class.forName("org.postgresql.Driver");

            conn = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
            if (conn != null) {
                System.out.println("Connection worked");
            } else {
                System.out.println("Connection failed");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return conn;
    }
}
