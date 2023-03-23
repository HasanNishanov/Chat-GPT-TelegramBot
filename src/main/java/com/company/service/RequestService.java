package com.company.service;

import com.company.database.Database;
import com.company.model.Request;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static com.company.controller.Controller.globalchat_id;

public class RequestService {
    public static void loadRequestList() {
        Connection connection = Database.getConnection();
        if(connection != null){

            try (Statement statement = connection.createStatement()) {

                Database.requestList.clear();

                String query =  ("select * from  where tg_id =" +  "'" + globalchat_id + "'");

                ResultSet resultSet = statement.executeQuery(query);

                while (resultSet.next()) {
                    int id = resultSet.getInt("id");
                    String question = resultSet.getString("question");
                    String answer = resultSet.getString("answer");
                    String tg_id = resultSet.getString("tg_id");


                    Request request = new Request(id, question, answer, tg_id);

                    Database.requestList.add(request);
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }

        }
    }
}
