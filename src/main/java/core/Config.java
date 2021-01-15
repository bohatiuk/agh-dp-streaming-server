package core;

import com.google.gson.Gson;

import java.io.*;

public class Config {
    public static String storageLocation = null;
    public static String uploadDir = null;
    public static String ffmpegLocation = null;


    public static String dbName = null;
    public static String dbVideoTable = null;
    public static String dbUserTable = null;
    public static String dbUser = null;
    public static String dbPass = null;

    public static String hashMethod = null;

    public Config() {

        Gson gson = new Gson();

        try {
            JsonObject object = gson.fromJson(new FileReader("../../../config.json"), JsonObject.class);

            storageLocation = object.storageLocation;
            uploadDir = object.uploadDir;
            ffmpegLocation = object.ffmpegLocation;

            dbName = object.dbName;
            dbVideoTable = object.dbVideoTable;
            dbUserTable = object.dbUserTable;
            dbUser = object.dbUser;
            dbPass = object.dbPass;
            hashMethod = object.hashMethod;

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }



    }

}

class JsonObject {
    String storageLocation = null;
    String uploadDir = null;
    String ffmpegLocation = null;
    String dbName = null;
    String dbVideoTable = null;
    String dbUserTable = null;
    String dbUser = null;
    String dbPass = null;
    String hashMethod = null;

    public String getStorageLocation() {
        return storageLocation;
    }

    public void setStorageLocation(String storageLocation) {
        this.storageLocation = storageLocation;
    }

    public String getUploadDir() {
        return uploadDir;
    }

    public void setUploadDir(String uploadDir) {
        this.uploadDir = uploadDir;
    }

    public String getFfmpegLocation() {
        return ffmpegLocation;
    }

    public void setFfmpegLocation(String ffmpegLocation) {
        this.ffmpegLocation = ffmpegLocation;
    }

    public String getDbName() {
        return dbName;
    }

    public void setDbName(String dbName) {
        this.dbName = dbName;
    }

    public String getDbVideoTable() {
        return dbVideoTable;
    }

    public void setDbVideoTable(String dbVideoTable) {
        this.dbVideoTable = dbVideoTable;
    }

    public String getDbUserTable() {
        return dbUserTable;
    }

    public void setDbUserTable(String dbUserTable) {
        this.dbUserTable = dbUserTable;
    }

    public String getDbUser() {
        return dbUser;
    }

    public void setDbUser(String dbUser) {
        this.dbUser = dbUser;
    }

    public String getDbPass() {
        return dbPass;
    }

    public void setDbPass(String dbPass) {
        this.dbPass = dbPass;
    }

    public String getHashMethod() {
        return hashMethod;
    }

    public void setHashMethod(String hashMethod) {
        this.hashMethod = hashMethod;
    }
}