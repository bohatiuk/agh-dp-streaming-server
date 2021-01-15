package core.db;

import core.VideoManager;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class UserMapper extends AbstractDataMapper {

    public UserMapper(Connection con) {
        super.conn = con;
    }

    public boolean select(String userName, String password) {
        String query = "SELECT COUNT (*) AS rowcount FROM " + VideoManager.config.dbUserTable + " WHERE Users.user_name ='" + userName + "' AND Users.user_password = '"
                + password + "';";
        System.out.println(query);
        Statement stmt = null;
        try {
            stmt = conn.createStatement();

            ResultSet resultSet = stmt.executeQuery(query);
            resultSet.next();
            int rowCount = resultSet.getInt("rowcount");
            stmt.close();
            return rowCount > 0;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return false;
    }



}
