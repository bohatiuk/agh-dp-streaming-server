package core;

import com.google.gson.Gson;
import core.db.DatabaseConnector;

import java.io.File;
import java.util.ArrayList;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


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

        createTokenUpdateTask();
    }

    void createTokenUpdateTask() {
        Runnable runnable = new Runnable() {
            public void run() {
                ArrayList<String> tokens = dbConn.videoMapper.getTokens();

                for (int i = 0; i < tokens.size(); i++) {
                    String oldToken = tokens.get(i);

                    // regenerate token based on UUID
                    String newToken = UUID.randomUUID().toString();

                    // update token
                    dbConn.videoMapper.updateToken(oldToken, newToken);

                    // update dir name
                    String path = VideoManager.config.storageLocation;
                    File sourceFile = new File(path + oldToken);
                    File destFile = new File(path + newToken);

                    sourceFile.renameTo(destFile);
                }
            }
        };

        ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
        executor.scheduleAtFixedRate(runnable, 0, Integer.parseInt(VideoManager.config.tokenTTL), TimeUnit.MINUTES);
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


    private boolean _onUploadVideo(String username, String videoname, VisibilityType initialVisiilty){
        String isVisible = "TRUE";
        if (initialVisiilty == VisibilityType.Private) {
            isVisible = "FALSE";
        }
        return dbConn.videoMapper.insert(username, videoname, isVisible);
    }

    private boolean _onDeleteVideo(String username, String videoname) {
        return dbConn.videoMapper.delete(username, videoname);
    }

    private Map.Entry<Boolean, String> _onStreamVideo(String username, String videoname) {
        String token = null;

        boolean accessible = dbConn.videoMapper.checkAccessibility(username, videoname);
        if (accessible)
            token = dbConn.videoMapper.token(videoname);

        String uri = "{url: \"http://10.0.2.2:8081/" + token + "/index.m3u8\"}";
        return Map.entry(accessible, uri);
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
