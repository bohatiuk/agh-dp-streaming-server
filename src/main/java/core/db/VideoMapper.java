package core.db;

import core.VideoListItem;
import core.VideoManager;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import static core.VideoManager.VisibilityType.Public;

public class VideoMapper extends AbstractDataMapper{

    public VideoMapper(Connection con){
        super.conn = con;
    }

    public String token(String username,String videoName) throws SQLException {
        String selectQuery = "SELECT video_token AS token from videoDB.Users WHERE user_name = " + username + " AND video_name = "+ videoName + ";" ;
        Statement stmtSelect = conn.createStatement();
        ResultSet resultSet = stmtSelect.executeQuery(selectQuery);
        String token = (resultSet.getObject("token", java.util.UUID.class)).toString();

        return token;
    }

    boolean insert(String username, String videoname, String initialVisiilty) throws SQLException {
        //TODO:
        // dopisać sprawdzenie czy takie userid + videoname jest juz w bazie, jesli tak to return false

        String selectQuery = "SELECT user_id AS userID from videoDB.Users WHERE user_name = " + username + ";" ;
        Statement stmtSelect = conn.createStatement();
        ResultSet resultSet = stmtSelect.executeQuery(selectQuery);
        int userID = resultSet.getInt("userID");


        String insertQuery = "INSERT INTO videoDB.Videos(user_id ,video_name, video_token, is_visible) VALUES ("+
                userID + ", "+ videoname + "," + "md5("+videoname+")," + initialVisiilty +";";


        Statement stmtInsert = conn.createStatement();
        stmtInsert.executeUpdate(insertQuery);
        stmtInsert.close();
        return true;
    }

    void update(String username, String videoname, String type) throws SQLException {
        // jeśli juz bylo takie visibility to false a tak to true po tym jak zmienie :D
        String selectQuery = "SELECT user_id AS userID from videoDB.Users WHERE user_name = " + username + ";" ;
        Statement stmtSelect = conn.createStatement();
        ResultSet resultSet = stmtSelect.executeQuery(selectQuery);
        int userID = resultSet.getInt("userID");



    }

    void delete(String videoName, int userID) throws SQLException {
        //jeśli nieistnieje takie video name lub innego użyhtkownika to false, a tak to delete i true
        String query = "DELETE from videoDB.Videos where video_name =" + videoName + "and " +
                "user_id = "+ userID + ";" ;


    }

    ArrayList<VideoListItem> allUserVideos(String username){

    }

    ArrayList<VideoListItem> allStoryVideos(String username){
     // publiczne innych select .... from videos where user_id != username or isvisible = "true"

    }

    ArrayList<VideoListItem> allVideos(String username){
        // allUserVIdeos +allStoryVideos

    }
}
