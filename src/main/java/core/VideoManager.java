package core;

import com.google.gson.Gson;
import core.db.DatabaseConnector;
import core.db.VideoMapper;
import io.github.techgnious.exception.VideoException;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.util.ArrayList;
import java.util.Map;

// najprawdopodobniej to bedzie signleton, bo
// trzeba by tu otworzyc sesje z baza i tryzmac ja zamiast tego by caly czas otwierac i zamykac
public class VideoManager {
    private static final VideoManager INSTANCE = new VideoManager();
    public static Config config;
    private DatabaseConnector dbConn;


    private static VideoManager getInstance() {
        return INSTANCE;
    }


    private VideoManager() {
        VideoManager.config = new Config();
        dbConn = new DatabaseConnector();
    }

    public enum GetVideoListRequestType {
        User,
        Story,
        All
    }

    public enum VisibilityType {
        Private, // false
        Public // true w bazie
    }

    static public DatabaseConnector getDatabaseConnector(){
        return getInstance().dbConn;
    }

    // Wlasciwe API
    public static boolean onUploadVideo(String username, String videoname,VisibilityType initialVisiilty){
        return getInstance()._onUploadVideo(username, videoname, initialVisiilty);
    }

    public static boolean onDeleteVideo(String username, String videoname) {
        return getInstance()._onDeleteVideo(username, videoname);
    }

    public static Map.Entry<Boolean, String> onStreamVideo(String username, String videoname) {
        return getInstance()._onStreamVideo(username, videoname);
    }


    public static boolean onLogin(String username, String userpass) {
        return getInstance()._onLogin(username, userpass);
    }

    public static String onGetVideoList(String username, GetVideoListRequestType type) {
        return getInstance()._onGetVideoList(username, type);
    }

    public static boolean onChangeVisibility(String username, String videoname, VisibilityType type) {
        return getInstance()._onChangeVisibility(username, videoname, type);
    }


    // funckje implementujace

    private boolean _onUploadVideo(String username, String videoname, VisibilityType initialVisiilty){
        String isVisible = "TRUE";
        if (initialVisiilty == VisibilityType.Private) {
            isVisible = "FALSE";
        }
        return dbConn.videoMapper.insert(username, videoname, isVisible);
    }

    // nalezy usunac wpis z bazy
    private boolean _onDeleteVideo(String username, String videoname) {
        return dbConn.videoMapper.delete(username, videoname);
    }

    private Map.Entry<Boolean, String> _onStreamVideo(String username, String videoname) {
        String token = null;

        boolean accessible = dbConn.videoMapper.checkAccessibility(username, videoname);
        if (accessible)
            token = dbConn.videoMapper.token(username, videoname);

        String uri = "{uri: \"10.0.2.2:8081/" + token + "/index.m3u8\"}";
        return Map.entry(accessible, uri);


        // false jesli nie znaleziono videoname dla username lub niema videoname w publicznych
        //return Map.entry(false, null);
    }

    // nalezy sprawdzic czy uzytkownik istnieje
    private boolean _onLogin(String username, String userpass) {
        return dbConn.userMapper.select(username, userpass);
    }

    private String _onGetVideoList(String username, GetVideoListRequestType type) {

        ArrayList<VideoListItem> list = null;

        if (type == GetVideoListRequestType.All)
            list = dbConn.videoMapper.allVideos(username);
        else if (type == GetVideoListRequestType.Story)
            list = dbConn.videoMapper.allStoryVideos(username);
        else
            list = dbConn.videoMapper.allUserVideos(username);

        Gson gson = new Gson();
        String result = gson.toJson(list);
        return result;
    }

    private boolean _onChangeVisibility(String username, String videoname, VisibilityType type) {
        String isVisible = "TRUE";
        if (type == VisibilityType.Private) {
            isVisible = "FALSE";
        }

        return dbConn.videoMapper.update(username, videoname, isVisible);
    }

}
