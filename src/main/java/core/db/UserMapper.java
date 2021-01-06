package core.db;

import core.VideoDecoder;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class UserMapper extends AbstractDataMapper{

    public UserMapper(Connection con){
        super.conn = con;
    }

    public boolean select(String userName, String password) throws SQLException {
        String query = "SELECT COUNT (*) AS rowcount FROM videoDB.Users WHERE user_name = " + userName + " AND user_password = "
                + password + ";" ;

        Statement stmt = conn.createStatement();
        ResultSet resultSet = stmt.executeQuery(query);
        resultSet.next();
        int rowCount = resultSet.getInt("rowcount");
        stmt.close();
        return rowCount!=0 ;
    }


}
