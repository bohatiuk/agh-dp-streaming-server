package core;

import javax.xml.crypto.Data;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnector {
    private Connection conn = null;

    public DatabaseConnector() {
        try {
            conn = DriverManager.getConnection(
                    "jdbc:postgresql://localhost:5432/" + Config.dbName,
                    Config.dbUser,
                    Config.dbPass);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

}
