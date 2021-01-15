package core.db;

import core.VideoManager;

import java.sql.*;

public class DatabaseConnector {

    private  Connection conn = null;
    public UserMapper userMapper = null;
    public VideoMapper videoMapper = null;

    public DatabaseConnector() {
        try {
            try {
                Class.forName("org.postgresql.Driver");
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            conn = DriverManager.getConnection(
                    "jdbc:postgresql://localhost:5432/" + VideoManager.config.dbName,
                    VideoManager.config.dbUser,
                    VideoManager.config.dbPass);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        userMapper = new UserMapper(conn);
        videoMapper = new VideoMapper(conn);
    }

}
