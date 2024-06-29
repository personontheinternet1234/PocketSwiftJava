package com.personontheinternet.pocket.sql;

import com.personontheinternet.pocket.Server;
import org.mariadb.jdbc.Connection;

import java.sql.DriverManager;
import java.sql.SQLException;

public class SQLConnectionManager {
    public static SQLConnectionManager INSTANCE;

    public Connection connection;

    public SQLConnectionManager(){
        INSTANCE = this;
    }

    public void connect(){
        getConnection();
    }

    public void getConnection(){
        if(connection == null || connection.isClosed()){
            try {
                Class.forName("org.mariadb.jdbc.Driver");
                connection = (Connection) DriverManager.getConnection(
                        Server.url,
                        Server.superUser, Server.password
                );
            } catch (SQLException e) {
                throw new RuntimeException(e);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
