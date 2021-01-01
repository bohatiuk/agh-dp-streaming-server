package core;

import java.io.IOException;

public class VideoDecoder {
    // TODO:
    // compress() -> encodeVideoWithAttributes()
    // toHLS()

    public static void compress() {
        // TODO Julka..
    }

    // zakladam tu ze funkcja wywolujaca poda hash tego video
    public static void toHls(String videoToken) {
        final String inputFileLocation = Config.uploadDir + videoToken + ".mp4";
        final String outputFileLocation = Config.storageLocation + videoToken + "\\index.m3u8";
        try {
            Runtime.getRuntime().exec(
                    "cmd /c start" +
                    Config.ffmpegLocation +
                    "-i" + inputFileLocation +
                    "-profile:v baseline -level 3.0 -s 640x360 -start_number 0 -hls_time 10 -hls_list_size 0"
                    + "-f hls" + outputFileLocation);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
