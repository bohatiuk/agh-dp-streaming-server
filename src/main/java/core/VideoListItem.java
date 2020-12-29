package core;

// response przy pobraniu listy video
public class VideoListItem {
    private String username;
    private String videoname;

    public VideoListItem(String username, String videoname) {
        this.username = username;
        this.videoname = videoname;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getVideoname() {
        return videoname;
    }

    public void setVideoname(String videoname) {
        this.videoname = videoname;
    }
}
