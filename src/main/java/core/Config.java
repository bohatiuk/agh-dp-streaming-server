package core;

public class Config {
    public static String storageLocation = "C:\\Users\\" + System.getProperty("user.name") + "\\IdeaProjects\\dp-server-test\\storage\\";
    public static String ffmpegLocation = "C:\\Users\\" + System.getProperty("user.name") + "\\IdeaProjects\\dp-server-test\\vendor\\ffmpeg.exe ";
    public static String uploadDir = "C:\\Users\\" + System.getProperty("user.name") + "\\IdeaProjects\\dp-server-test\\storage\\tmp";

    public static String dbName = "postgres";
    public static String dbVideoTable = "videoDB.Videos";
    public static String dbUserTable = "videoDB.Users";
    public static String dbUser = "postgres";
    public static String dbPass = "password";

    public static String hashMethod = "md5";


}
