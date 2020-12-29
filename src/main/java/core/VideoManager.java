package core;

import com.google.gson.Gson;

import java.sql.*;
import java.util.ArrayList;
import java.util.Map;

// najprawdopodobniej to bedzie signleton, bo
// trzeba by tu otworzyc sesje z baza i tryzmac ja zamiast tego by caly czas otwierac i zamykac
public class VideoManager {
    private static final VideoManager INSTANCE = new VideoManager();
    private DatabaseConnector dbConn = new DatabaseConnector();

    private static VideoManager getInstance() {
        return INSTANCE;
    }


    private VideoManager() {

    }

    public enum GetVideoListRequestType {
        User,
        Story,
        All
    }

    public enum VisibilityType {
        Private,
        Public
    }


    // Wlasciwe API
    public static boolean onUploadVideo(String username, String videoname) {
        return getInstance()._onUploadVideo(username, videoname);
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

    // nalezy dodac wpis do bazy, zhashowana wersje nazwy video
    // zhashowana wersja to moze byc jais prostu md5 hash + sol
    private boolean _onUploadVideo(String username, String videoname) {
        // TODO:
        // 1. zgenerowac hash na pdostawie username i videoname
        // 2. sprwadzic czy para username i hash juz istnieje
        // 3. jesli tak to false zwracac nalezy
        // 4. jesli nie istnieje to dodac pare username i hash do tabeli videos z bazy i zworcic true

        return false;

    }

    // nalezy usunac wpis z bazy
    private boolean _onDeleteVideo(String username, String videoname) {
        // TODO:


        // false jesli video o takiej nazwie nie istnieje lub istnieje ale nie jest tego uzytkownika
        return false;
    }

    // nalezy zwrocic token video z bazy jesli istnieje
    private Map.Entry<Boolean, String> _onStreamVideo(String username, String videoname) {
        String token = null;
        // TODO:
        // ...

        return Map.entry(true, token);


        // false jesli nie znaleziono videoname dla username lub niema videoname w publicznych
        //return Map.entry(false, null);
    }

    // nalezy sprawdzic czy uzytkownik istnieje
    private boolean _onLogin(String username, String userpass) {
        // TODO
        return true;
    }

    // w zaleznosci od typu wyslac response
    private String _onGetVideoList(String username, GetVideoListRequestType type) {
        ArrayList<VideoListItem> list = new ArrayList<>();

        // TODO:
        // do tej listy nalezy dodac wpisy z bazy, np list.add(new VideoListItem("username", "videoname"))
        // tu twoja logika

        Gson gson = new Gson();
        String result = gson.toJson(list);
        return result;
    }

    private boolean _onChangeVisibility(String username, String videoname, VisibilityType type) {
        // TODO:

        // zwracasz true jesli zmieniono pomyslnie, false jesli nie bylo zmienione bo juz byla taka ustawiona
        return true;
    }

}
