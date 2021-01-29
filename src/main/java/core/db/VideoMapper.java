
package core.db;

import core.VideoManager;
import core.VideoListItem;


import java.sql.*;
import java.util.ArrayList;
import java.util.Map;


public class VideoMapper extends AbstractDataMapper {

    public VideoMapper(Connection con) {
        super.conn = con;
    }

    public String token(String videoname) {
        String selectQuery = "SELECT video_token AS token from " + VideoManager.config.dbVideoTable + " WHERE video_name = '" + videoname + "';";
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

    public ArrayList<String> getTokens() {
        ArrayList<String> result = new ArrayList<>();

        String selectQuery = "SELECT video_token from " + VideoManager.config.dbVideoTable + ";";
        Statement stmtSelect = null;
        try {
            stmtSelect = conn.createStatement();

            ResultSet resultSet = stmtSelect.executeQuery(selectQuery);
            while (resultSet.next()) {
                result.add(resultSet.getString("video_token"));
            }
            stmtSelect.close();



        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return result;
    }

    public void updateToken(String oldToken, String newToken) {
        try {
            String updateQuery = "UPDATE " + VideoManager.config.dbVideoTable + " SET video_token = " + newToken + " WHERE video_token = " + oldToken + ";";
            Statement stmtUpdate = conn.createStatement();
            stmtUpdate.executeUpdate(updateQuery);
            stmtUpdate.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
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
                    + videoname + "' AND is_visible = " + type + ";";
            System.out.println(selectCheckQuery);
            Statement stmtSelectCheck = conn.createStatement();
            ResultSet resultSetCheck = stmtSelectCheck.executeQuery(selectCheckQuery);
            resultSetCheck.next();
            int videoCount = resultSetCheck.getInt("videoCount");
            stmtSelectCheck.close();
            System.out.println(videoCount);
            if (videoCount != 0) {
                return false;
            }

            String updateQuery = "UPDATE " + VideoManager.config.dbVideoTable + " SET is_visible = " + type + " WHERE user_id = " + userID + " AND video_name = '" +
                    videoname + "';";
            System.out.println(updateQuery);
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

            String selectQuery = "SELECT COUNT (*) AS videoCount FROM " + VideoManager.config.dbVideoTable + " v  WHERE" +
                    "(v.is_visible = TRUE OR v.user_id = " + userID + ") AND v.video_name = '" + videoname + "' GROUP BY v.user_id;";
            System.out.println(selectQuery);
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

            String selectQuery = "SELECT u.user_name AS userName, v.video_name AS videoName, v.is_visible AS visibility FROM " +
                    VideoManager.config.dbUserTable + " u JOIN " + VideoManager.config.dbVideoTable + " v USING (user_id) WHERE v.user_id = " + userID + ";";
            Statement stmtSelect = conn.createStatement();
            ResultSet resultSetCheck = stmtSelect.executeQuery(selectQuery);

            ArrayList<VideoListItem> allUserVideosList = new ArrayList<>();
            while (resultSetCheck.next()) {
                if (resultSetCheck.getBoolean("visibility") == true) {
                    allUserVideosList.add(new VideoListItem(resultSetCheck.getString("userName"), resultSetCheck.getString("videoName"),"public"));
                    System.out.println(resultSetCheck.getString("userName") + resultSetCheck.getString("videoName"));
                }
                else {
                    allUserVideosList.add(new VideoListItem(resultSetCheck.getString("userName"), resultSetCheck.getString("videoName"), "private"));
                }
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

            String selectQuery = "SELECT u.user_name AS userName, v.video_name AS videoName,  v.is_visible AS visibility  FROM " +
                    VideoManager.config.dbUserTable + " u JOIN " + VideoManager.config.dbVideoTable + " v USING (user_id) WHERE " +
                    " v.is_visible = TRUE" + ";";
            System.out.println(selectQuery);
            Statement stmtSelect = conn.createStatement();
            ResultSet resultSetCheck = stmtSelect.executeQuery(selectQuery);

            ArrayList<VideoListItem> allStoryVideos = new ArrayList<>();
            while (resultSetCheck.next()) {
                if (resultSetCheck.getBoolean("visibility") == true) {
                    allStoryVideos.add(new VideoListItem(resultSetCheck.getString("userName"), resultSetCheck.getString("videoName"),"public"));
//                System.out.println(resultSetCheck.getString("userName") + resultSetCheck.getString("videoName"));
                }
                else {
                    allStoryVideos.add(new VideoListItem(resultSetCheck.getString("userName"), resultSetCheck.getString("videoName"), "private"));
                }
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
