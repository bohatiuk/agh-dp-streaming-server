
package core.db;

import core.VideoManager;
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
        String selectID = "SELECT user_id AS userID from " + VideoManager.config.dbUserTable + " WHERE user_name = '" + username + "';";
        Statement stmtID = null;
        Integer userID = null;
        try {
            stmtID = conn.createStatement();

            ResultSet resultSet = stmtID.executeQuery(selectID);
            if (resultSet.next()) {
                userID = resultSet.getInt("userID");
//                System.out.println(userID);
            }
            stmtID.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        String selectQuery = "SELECT video_token AS token from " + VideoManager.config.dbVideoTable + " WHERE user_id = '" + userID + "' AND video_name = '" + videoname + "';";
        Statement stmtSelect = null;
        String token = null;
        try {
            stmtSelect = conn.createStatement();

            ResultSet resultSet = stmtSelect.executeQuery(selectQuery);
            if (resultSet.next()) {
                token = resultSet.getString("token");
            }
            return token;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }


    public boolean insert(String username, String videoname, String initialVisiilty) {

        String selectQuery = "SELECT user_id AS userID from " + VideoManager.config.dbUserTable + " WHERE user_name = '" + username + "';";
        Statement stmtSelect = null;
        Integer userID = null;
        try {
            stmtSelect = conn.createStatement();

            ResultSet resultSet = stmtSelect.executeQuery(selectQuery);
            if (resultSet.next()) {
                userID = resultSet.getInt("userID");
//                System.out.println(userID);
            }
            stmtSelect.close();


            String selectCheckQuery = "SELECT COUNT (*) AS videoCount  FROM " + VideoManager.config.dbVideoTable + " WHERE user_id = '" + userID + "' and video_name = '"
                    + videoname + "';";
            Statement stmtSelectCheck = conn.createStatement();
            ResultSet resultSetCheck = stmtSelectCheck.executeQuery(selectCheckQuery);
            resultSetCheck.next();
            int videoCount = resultSetCheck.getInt("videoCount");
            stmtSelectCheck.close();

            if (videoCount > 0) {
                return false;
            }

            String insertQuery = "INSERT INTO " + VideoManager.config.dbVideoTable + " (user_id ,video_name, video_token, is_visible) VALUES (" +
                    userID + ", '" + videoname + "'," + VideoManager.config.hashMethod + "('" + videoname + "')," + initialVisiilty + ");";
            Statement stmtInsert = conn.createStatement();
            stmtInsert.executeUpdate(insertQuery);
            stmtInsert.close();


        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return true;
    }

    public boolean update(String username, String videoname, String type) {

        String selectQuery = "SELECT user_id AS userID from " + VideoManager.config.dbUserTable + " WHERE user_name = '" + username + "';";
        Statement stmtSelect = null;
        Integer userID = null;
        try {
            stmtSelect = conn.createStatement();

            ResultSet resultSet = stmtSelect.executeQuery(selectQuery);
            if (resultSet.next()) {
                userID = resultSet.getInt("userID");
//                System.out.println(userID);
            }
            stmtSelect.close();


            String selectCheckQuery = "SELECT COUNT (*) AS videoCount  FROM " + VideoManager.config.dbVideoTable + " WHERE user_id = '" + userID + " 'AND video_name = '"
                    + videoname + "'AND is_visible = " + type + ";";
            Statement stmtSelectCheck = conn.createStatement();
            ResultSet resultSetCheck = stmtSelectCheck.executeQuery(selectCheckQuery);
            resultSetCheck.next();
            int videoCount = resultSetCheck.getInt("videoCount");
            stmtSelectCheck.close();

            if (videoCount != 0) {
                return false;
            }

            String updateQuery = "UPDATE " + VideoManager.config.dbVideoTable + " SET is_visible = " + type + "WHERE user_id = " + userID + " AND video_name = '" +
                    videoname + "';";
            Statement stmtUpdate = conn.createStatement();
            stmtUpdate.executeUpdate(updateQuery);
            stmtUpdate.close();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return true;

    }

    public boolean delete(String username, String videoname) {

        String selectQuery = "SELECT user_id AS userID from " + VideoManager.config.dbUserTable + " WHERE user_name = '" + username + "';";
        Statement stmtSelect = null;
        Integer userID = null;
        Integer videoCount = null;
        try {
            stmtSelect = conn.createStatement();

            ResultSet resultSet = stmtSelect.executeQuery(selectQuery);
            if (resultSet.next()) {
                userID = resultSet.getInt("userID");
//                System.out.println(userID);
            }
            stmtSelect.close();


            String selectCheckQuery = "SELECT COUNT (*) AS videoCount  FROM " + VideoManager.config.dbVideoTable + " WHERE user_id = " + userID + " AND video_name ='"
                    + videoname + "';";
            System.out.println(selectCheckQuery);
            Statement stmtSelectCheck = conn.createStatement();
            ResultSet resultSetCheck = stmtSelectCheck.executeQuery(selectCheckQuery);
            if (resultSetCheck.next()) {
                videoCount = resultSetCheck.getInt("videoCount");
                System.out.println(videoCount);
            }
            stmtSelectCheck.close();

            if (videoCount < 1) {
                return false;
            }

            String deleteQuery = "DELETE from " + VideoManager.config.dbVideoTable + " where video_name ='" + videoname + "'and " +
                    "user_id = " + userID + ";";
            System.out.println(deleteQuery);
            Statement stmtDelete = conn.createStatement();
            stmtDelete.execute(deleteQuery);
            stmtDelete.close();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return true;
    }

    public boolean checkAccessibility(String username, String videoname) {
        String selectIDQuery = "SELECT user_id AS userID from " + VideoManager.config.dbUserTable + " WHERE user_name = '" + username + "';";
        Statement stmtIDSelect = null;
        Integer userID = null;
        Integer videoCount = null;
        try {
            stmtIDSelect = conn.createStatement();

            ResultSet resultSet = stmtIDSelect.executeQuery(selectIDQuery);
            if (resultSet.next()) {
                userID = resultSet.getInt("userID");
                System.out.println("tu:" + userID);
            }
            stmtIDSelect.close();

            String selectQuery = "SELECT COUNT (*) AS videoCount, u.user_name AS userName, v.video_name AS videoName FROM " + VideoManager.config.dbUserTable + " JOIN " +
                    VideoManager.config.dbVideoTable + "  USING (user_id) WHERE" +
                    "(is_visible = TRUE OR user_id = " + userID + ") AND video_name = '" + videoname + "';";
            Statement stmtSelect = conn.createStatement();
            ResultSet resultSetCheck = stmtSelect.executeQuery(selectQuery);

            if (resultSetCheck.next()) {
                videoCount = resultSetCheck.getInt("videoCount");
                System.out.println("tu2:" + userID);
            }
            return videoCount > 0;

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return false;
    }

    public ArrayList<VideoListItem> allUserVideos(String username) {

        String selectIDQuery = "SELECT user_id AS userID from " + VideoManager.config.dbUserTable + " WHERE user_name = '" + username + "';";
        Statement stmtIDSelect = null;
        Integer userID = null;
        try {
            stmtIDSelect = conn.createStatement();

            ResultSet resultSet = stmtIDSelect.executeQuery(selectIDQuery);
            if (resultSet.next()) {
                userID = resultSet.getInt("userID");
//                System.out.println(userID);
            }
            stmtIDSelect.close();

            String selectQuery = "SELECT u.user_name AS userName, v.video_name AS videoName FROM " +
                    VideoManager.config.dbUserTable + " u JOIN " + VideoManager.config.dbVideoTable + " v USING (user_id) WHERE v.user_id = " + userID + ";";
            Statement stmtSelect = conn.createStatement();
            ResultSet resultSetCheck = stmtSelect.executeQuery(selectQuery);

            ArrayList<VideoListItem> allUserVideosList = new ArrayList<>();
            while (resultSetCheck.next()) {
                allUserVideosList.add(new VideoListItem(resultSetCheck.getString("userName"), resultSetCheck.getString("videoName")));
//                System.out.println(resultSetCheck.getString("userName") + resultSetCheck.getString("videoName"));
            }
            stmtSelect.close();


            return allUserVideosList;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return null;
    }

    public ArrayList<VideoListItem> allStoryVideos(String username) {

        String selectIDQuery = "SELECT user_id AS userID from " + VideoManager.config.dbUserTable + " WHERE user_name = '" + username + "';";
        Statement stmtIDSelect = null;
        Integer userID = null;
        try {
            stmtIDSelect = conn.createStatement();

            ResultSet resultSet = stmtIDSelect.executeQuery(selectIDQuery);
            if (resultSet.next()) {
                userID = resultSet.getInt("userID");
                System.out.println(userID);
            }
            stmtIDSelect.close();

            String selectQuery = "SELECT u.user_name AS userName, v.video_name AS videoName FROM " +
                    VideoManager.config.dbUserTable + " u JOIN " + VideoManager.config.dbVideoTable + " v USING (user_id) WHERE v.user_id != " + userID +
                    " AND v.is_visible = TRUE" + ";";
            System.out.println(selectQuery);
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
