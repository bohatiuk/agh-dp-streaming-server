package core;

// response przy pobraniu listy video
public class VideoListItem {
    private String username;
    private String videoname;
    private String visibility;

    public VideoListItem(String username, String videoname, String visibility) {
        this.username = username;
        this.videoname = videoname;
        this.visibility = visibility;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getVisibility() { return visibility; }

    public void setVisibility(String visibility) { this.visibility = visibility; }

    public String getVideoname() {
        return videoname;
    }

    public void setVideoname(String videoname) {
        this.videoname = videoname;
    }
}
