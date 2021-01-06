package core.db;

import core.VideoListItem;


import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;



public class VideoMapper extends AbstractDataMapper{

    public VideoMapper(Connection con){
        super.conn = con;
    }

    public String token(String username,String videoname) throws SQLException {
        String selectQuery = "SELECT video_token AS token from videoDB.Users WHERE user_name = " + username + " AND video_name = "+ videoname + ";" ;
        Statement stmtSelect = conn.createStatement();
        ResultSet resultSet = stmtSelect.executeQuery(selectQuery);
        String token = (resultSet.getObject("token", java.util.UUID.class)).toString();

        return token;
    }

    boolean insert(String username, String videoname, String initialVisiilty) throws SQLException {

        String selectQuery = "SELECT user_id AS userID from videoDB.Users WHERE user_name = " + username + ";" ;
        Statement stmtSelect = conn.createStatement();
        ResultSet resultSet = stmtSelect.executeQuery(selectQuery);
        int userID =resultSet.getInt("userID");
        stmtSelect.close();


        String selectCheckQuery = "SELECT COUNT (*) AS videoCount  FROM videoDB.Videos WHERE user_id = " + userID + " and video_name = "
                + videoname+ ";";
        Statement stmtSelectCheck = conn.createStatement();
        ResultSet resultSetCheck = stmtSelectCheck.executeQuery(selectCheckQuery);
        resultSetCheck.next();
        int videoCount = resultSetCheck.getInt("videoCount");
        stmtSelectCheck.close();

        if (videoCount!=0){
            return false;
        }

        String insertQuery = "INSERT INTO videoDB.Videos(user_id ,video_name, video_token, is_visible) VALUES ("+
                userID + ", "+ videoname + "," + "md5("+videoname+")," + initialVisiilty +";";
        Statement stmtInsert = conn.createStatement();
        stmtInsert.executeUpdate(insertQuery);
        stmtInsert.close();

        return true;
    }

    boolean update(String username, String videoname, String type) throws SQLException {

        String selectQuery = "SELECT user_id AS userID from videoDB.Users WHERE user_name = " + username + ";" ;
        Statement stmtSelect = conn.createStatement();
        ResultSet resultSet = stmtSelect.executeQuery(selectQuery);
        int userID = resultSet.getInt("userID");
        stmtSelect.close();


        String selectCheckQuery = "SELECT COUNT (*) AS videoCount  FROM videoDB.Videos WHERE user_id = " + userID + " AND video_name = "
                + videoname + "AND is_visible = " + type  +";";
        Statement stmtSelectCheck = conn.createStatement();
        ResultSet resultSetCheck = stmtSelectCheck.executeQuery(selectCheckQuery);
        resultSetCheck.next();
        int videoCount = resultSetCheck.getInt("videoCount");
        stmtSelectCheck.close();

        if (videoCount!=0){
            return false;
        }

        String updateQuery = "UPDATE videoDB.Videos SET is_visible = " + type + "WHERE user_id = " + userID + " AND video_name = " +
                 videoname + ";";
        Statement stmtUpdate = conn.createStatement();
        stmtUpdate.executeUpdate(updateQuery);
        stmtUpdate.close();

        return true;

    }

    boolean delete(String username, String videoname) throws SQLException {

        String selectQuery = "SELECT user_id AS userID from videoDB.Users WHERE user_name = " + username + ";" ;
        Statement stmtSelect = conn.createStatement();
        ResultSet resultSet = stmtSelect.executeQuery(selectQuery);
        int userID = resultSet.getInt("userID");
        stmtSelect.close();;

        String selectCheckQuery = "SELECT COUNT (*) AS videoCount  FROM videoDB.Videos WHERE user_id = " + userID + " AND video_name = "
                + videoname + ";";
        Statement stmtSelectCheck = conn.createStatement();
        ResultSet resultSetCheck = stmtSelectCheck.executeQuery(selectCheckQuery);
        resultSetCheck.next();
        int videoCount = resultSetCheck.getInt("videoCount");
        stmtSelectCheck.close();

        if (videoCount==0){
            return false;
        }

        String deleteQuery = "DELETE from videoDB.Videos where video_name =" + videoname + "and " +
                "user_id = "+ userID + ";" ;
        Statement stmtDelete = conn.createStatement();
        stmtDelete.executeUpdate(deleteQuery);
        stmtDelete.close();

        return true;
    }

    ArrayList<VideoListItem> allUserVideos(String username) throws SQLException {

        String selectIDQuery = "SELECT user_id AS userID from videoDB.Users WHERE user_name = " + username + ";" ;
        Statement stmtIDSelect = conn.createStatement();
        ResultSet resultSet = stmtIDSelect.executeQuery(selectIDQuery);
        int userID = resultSet.getInt("userID");
        stmtIDSelect.close();

        String selectQuery = "SELECT u.user_name AS userName, v.video_name AS videoName FROM videoDB.Users JOIN videoDB.Videos  ON (user_id) WHERE user_id = " + userID + ";";
        Statement stmtSelect = conn.createStatement();
        ResultSet resultSetCheck = stmtSelect.executeQuery(selectQuery);

        ArrayList<VideoListItem> allUserVideosList = new ArrayList<>();
        while(resultSetCheck.next()) {
            allUserVideosList.add(new VideoListItem(resultSetCheck.getString("userName"), resultSetCheck.getString("videoName")));
        }
        stmtSelect.close();
        return allUserVideosList;
    }

    ArrayList<VideoListItem> allStoryVideos(String username) throws SQLException {

        String selectIDQuery = "SELECT user_id AS userID from videoDB.Users WHERE user_name = " + username + ";" ;
        Statement stmtIDSelect = conn.createStatement();
        ResultSet resultSet = stmtIDSelect.executeQuery(selectIDQuery);
        int userID = resultSet.getInt("userID");
        stmtIDSelect.close();

        String selectQuery = "SELECT u.user_name AS userName, v.video_name AS videoName FROM videoDB.Users JOIN videoDB.Videos  ON (user_id) WHERE user_id != " + userID + ";";
        Statement stmtSelect = conn.createStatement();
        ResultSet resultSetCheck = stmtSelect.executeQuery(selectQuery);

        ArrayList<VideoListItem> allStoryVideos = new ArrayList<>();
        while(resultSetCheck.next()) {
            allStoryVideos.add(new VideoListItem(resultSetCheck.getString("userName"), resultSetCheck.getString("videoName")));
        }
        stmtSelect.close();
        return allStoryVideos;
    }

    ArrayList<VideoListItem> allVideos(String username) throws SQLException {

        ArrayList<VideoListItem> allStoryVideos = allStoryVideos(username);
        ArrayList<VideoListItem> allUserVideosList = allUserVideos(username);
        ArrayList<VideoListItem> allVideos = new  ArrayList<>();
        allVideos.addAll(allStoryVideos);
        allVideos.addAll(allUserVideosList);

        return allVideos;
    }


}
