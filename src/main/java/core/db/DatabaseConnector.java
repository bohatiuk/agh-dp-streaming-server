package core.db;

import core.Config;

import java.sql.*;

public class DatabaseConnector {

    private  Connection conn = null;
    public UserMapper userMapper = null;
    public VideoMapper videoMapper = null;

    public DatabaseConnector() {
        try {
            conn = DriverManager.getConnection(
                    "jdbc:postgresql://localhost:5432/" + Config.dbName,
                    Config.dbUser,
                    Config.dbPass);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        userMapper = new UserMapper(conn);
        videoMapper = new VideoMapper(conn);
    }

}
