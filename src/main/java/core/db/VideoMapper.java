
package core.db;

import core.Config;
import core.VideoListItem;


import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;


public class VideoMapper extends AbstractDataMapper {

    public VideoMapper(Connection con) {
        super.conn = con;
    }

    public String token(String username, String videoname) {
        String selectQuery = "SELECT video_token AS token from " + Config.dbUserTable + " WHERE user_name = " + username + " AND video_name = " + videoname + ";";
        Statement stmtSelect = null;
        try {
            stmtSelect = conn.createStatement();

            ResultSet resultSet = stmtSelect.executeQuery(selectQuery);
            String token = (resultSet.getObject("token", java.util.UUID.class)).toString();

            return token;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }



    public boolean insert(String username, String videoname, String initialVisiilty) {

        String selectQuery = "SELECT user_id AS userID from " + Config.dbUserTable + " WHERE user_name = " + username + ";";
        Statement stmtSelect = null;
        try {
            stmtSelect = conn.createStatement();

            ResultSet resultSet = stmtSelect.executeQuery(selectQuery);
            int userID = resultSet.getInt("userID");
            stmtSelect.close();


            String selectCheckQuery = "SELECT COUNT (*) AS videoCount  FROM " + Config.dbVideoTable + " WHERE user_id = " + userID + " and video_name = "
                    + videoname + ";";
            Statement stmtSelectCheck = conn.createStatement();
            ResultSet resultSetCheck = stmtSelectCheck.executeQuery(selectCheckQuery);
            resultSetCheck.next();
            int videoCount = resultSetCheck.getInt("videoCount");
            stmtSelectCheck.close();

            if (videoCount > 0) {
                return false;
            }

            String insertQuery = "INSERT INTO " + Config.dbVideoTable + "(user_id ,video_name, video_token, is_visible) VALUES (" +
                    userID + ", " + videoname + "," + Config.hashMethod + "(" + videoname + ")," + initialVisiilty + ";";
            Statement stmtInsert = conn.createStatement();
            stmtInsert.executeUpdate(insertQuery);
            stmtInsert.close();


        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return true;
    }

    public boolean update(String username, String videoname, String type) {

        String selectQuery = "SELECT user_id AS userID from " + Config.dbUserTable + " WHERE user_name = " + username + ";";
        Statement stmtSelect = null;
        try {
            stmtSelect = conn.createStatement();

            ResultSet resultSet = stmtSelect.executeQuery(selectQuery);
            int userID = resultSet.getInt("userID");
            stmtSelect.close();


            String selectCheckQuery = "SELECT COUNT (*) AS videoCount  FROM " + Config.dbVideoTable + " WHERE user_id = " + userID + " AND video_name = "
                    + videoname + "AND is_visible = " + type + ";";
            Statement stmtSelectCheck = conn.createStatement();
            ResultSet resultSetCheck = stmtSelectCheck.executeQuery(selectCheckQuery);
            resultSetCheck.next();
            int videoCount = resultSetCheck.getInt("videoCount");
            stmtSelectCheck.close();

            if (videoCount != 0) {
                return false;
            }

            String updateQuery = "UPDATE " + Config.dbVideoTable + " SET is_visible = " + type + "WHERE user_id = " + userID + " AND video_name = " +
                    videoname + ";";
            Statement stmtUpdate = conn.createStatement();
            stmtUpdate.executeUpdate(updateQuery);
            stmtUpdate.close();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return true;

    }

    public boolean delete(String username, String videoname) {

        String selectQuery = "SELECT user_id AS userID from " + Config.dbUserTable + " WHERE user_name = " + username + ";";
        Statement stmtSelect = null;
        try {
            stmtSelect = conn.createStatement();

            ResultSet resultSet = stmtSelect.executeQuery(selectQuery);
            int userID = resultSet.getInt("userID");
            stmtSelect.close();
            ;

            String selectCheckQuery = "SELECT COUNT (*) AS videoCount  FROM " + Config.dbVideoTable + " WHERE user_id = " + userID + " AND video_name = "
                    + videoname + ";";
            Statement stmtSelectCheck = conn.createStatement();
            ResultSet resultSetCheck = stmtSelectCheck.executeQuery(selectCheckQuery);
            resultSetCheck.next();
            int videoCount = resultSetCheck.getInt("videoCount");
            stmtSelectCheck.close();

            if (videoCount > 0) {
                return false;
            }

            String deleteQuery = "DELETE from " + Config.dbVideoTable + " where video_name =" + videoname + " and " +
                    "user_id = " + userID + ";";
            Statement stmtDelete = conn.createStatement();
            stmtDelete.executeUpdate(deleteQuery);
            stmtDelete.close();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return true;
    }

    public boolean checkAccessibility(String username, String videoname) {
        String selectIDQuery = "SELECT user_id AS userID from " + Config.dbUserTable + " WHERE user_name = " + username + ";";
        Statement stmtIDSelect = null;
        try {
            stmtIDSelect = conn.createStatement();

            ResultSet resultSet = stmtIDSelect.executeQuery(selectIDQuery);
            int userID = resultSet.getInt("userID");
            stmtIDSelect.close();

            String selectQuery = "SELECT COUNT (*) AS videoCount, u.user_name AS userName, v.video_name AS videoName FROM " + Config.dbUserTable + " JOIN " +
                    Config.dbVideoTable + "  ON (user_id) WHERE" +
                    "(is_visible = TRUE OR user_id = " + userID + ") AND video_name = " + videoname + ";";
            Statement stmtSelect = conn.createStatement();
            ResultSet resultSetCheck = stmtSelect.executeQuery(selectQuery);

            int videoCount = resultSetCheck.getInt("videoCount");

            return videoCount > 0;

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return false;
    }

    public ArrayList<VideoListItem> allUserVideos(String username) {

        String selectIDQuery = "SELECT user_id AS userID from " + Config.dbUserTable + " WHERE user_name = " + username + ";";
        Statement stmtIDSelect = null;
        try {
            stmtIDSelect = conn.createStatement();

            ResultSet resultSet = stmtIDSelect.executeQuery(selectIDQuery);
            int userID = resultSet.getInt("userID");
            stmtIDSelect.close();

            String selectQuery = "SELECT u.user_name AS userName, v.video_name AS videoName FROM " + Config.dbUserTable + " JOIN " + Config.dbVideoTable + "  ON (user_id) WHERE user_id = " + userID + ";";
            Statement stmtSelect = conn.createStatement();
            ResultSet resultSetCheck = stmtSelect.executeQuery(selectQuery);

            ArrayList<VideoListItem> allUserVideosList = new ArrayList<>();
            while (resultSetCheck.next()) {
                allUserVideosList.add(new VideoListItem(resultSetCheck.getString("userName"), resultSetCheck.getString("videoName")));
            }
            stmtSelect.close();


            return allUserVideosList;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return null;
    }

    public ArrayList<VideoListItem> allStoryVideos(String username) {

        String selectIDQuery = "SELECT user_id AS userID from " + Config.dbUserTable + " WHERE user_name = " + username + ";";
        Statement stmtIDSelect = null;
        try {
            stmtIDSelect = conn.createStatement();

            ResultSet resultSet = stmtIDSelect.executeQuery(selectIDQuery);
            int userID = resultSet.getInt("userID");
            stmtIDSelect.close();

            String selectQuery = "SELECT u.user_name AS userName, v.video_name AS videoName FROM " +
                    Config.dbUserTable + " JOIN " + Config.dbVideoTable + "  ON (user_id) WHERE user_id != " + userID +
                    " AND is_visible = TRUE" + ";";
            Statement stmtSelect = conn.createStatement();
            ResultSet resultSetCheck = stmtSelect.executeQuery(selectQuery);

            ArrayList<VideoListItem> allStoryVideos = new ArrayList<>();
            while (resultSetCheck.next()) {
                allStoryVideos.add(new VideoListItem(resultSetCheck.getString("userName"), resultSetCheck.getString("videoName")));
            }
            stmtSelect.close();
            return allStoryVideos;

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return null;
    }

    public ArrayList<VideoListItem> allVideos(String username) {

        ArrayList<VideoListItem> allStoryVideos = allStoryVideos(username);
        ArrayList<VideoListItem> allUserVideosList = allUserVideos(username);
        ArrayList<VideoListItem> allVideos = new ArrayList<>();
        allVideos.addAll(allStoryVideos);
        allVideos.addAll(allUserVideosList);

        return allVideos;
    }



}
