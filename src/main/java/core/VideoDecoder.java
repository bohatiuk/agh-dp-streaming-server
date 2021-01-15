package core;

import core.db.DatabaseConnector;
import io.github.techgnious.IVCompressor;
import io.github.techgnious.dto.IVAudioAttributes;
import io.github.techgnious.dto.IVSize;
import io.github.techgnious.dto.IVVideoAttributes;
import io.github.techgnious.dto.VideoFormats;
import io.github.techgnious.exception.VideoException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;

public class VideoDecoder{

    public static String compress(String userName, String videoName) throws IOException, VideoException, NoSuchAlgorithmException, SQLException {
        IVCompressor compressor = new IVCompressor();
        IVSize customRes = new IVSize();
        setSize(customRes);
        IVAudioAttributes audioAttribute = new IVAudioAttributes();
        setAudioAttribute(audioAttribute);
        IVVideoAttributes videoAttribute = new IVVideoAttributes();
        setVideoAttribute(videoAttribute,customRes);


        String inputFileLocation = VideoManager.config.uploadDir + videoName;
        byte[] oldVideoBytes = Files.readAllBytes(Paths.get(inputFileLocation));
        byte[] newVideoBytes = compressor.encodeVideoWithAttributes(oldVideoBytes, VideoFormats.MP4,audioAttribute, videoAttribute);

        String videoToken = VideoManager.getDatabaseConnector().videoMapper.token(userName,videoName);
        String outputFileLocation = VideoManager.config.uploadDir + videoToken + ".mp4";
        File newVideoFile = new File(outputFileLocation);

        try {
            OutputStream os = new FileOutputStream(newVideoFile);
            os.write(newVideoBytes);
            os.close();
        }
        catch (Exception e) {
            System.out.println("Exception: " + e);
        }

        Files.deleteIfExists(Paths.get(inputFileLocation));
        return videoToken;
    }

    private static IVSize setSize(IVSize customRes){
        customRes.setWidth(1360);
        customRes.setHeight(768);
        return customRes;
    }

    private static IVAudioAttributes setAudioAttribute(IVAudioAttributes audioAttribute){
        audioAttribute.setBitRate(64000);
        audioAttribute.setChannels(1);
        audioAttribute.setSamplingRate(44100);
        return audioAttribute;
    }

    private static IVVideoAttributes setVideoAttribute(IVVideoAttributes videoAttribute, IVSize customRes){
        videoAttribute.setBitRate(160000);
        videoAttribute.setFrameRate(30);
        videoAttribute.setSize(customRes);
        return videoAttribute;
    }
    // zakladam tu ze funkcja wywolujaca poda hash tego video
    public static void toHls(String videoToken) {
        File directory = new File("./");
        String absolutePath = directory.getAbsolutePath();
        absolutePath = absolutePath.substring(0, absolutePath.length() - 1);
        final String inputFileLocation = absolutePath + VideoManager.config.uploadDir + videoToken + ".mp4";
        final String outputFileLocation = absolutePath + VideoManager.config.storageLocation + videoToken + "/index.m3u8";

        File newDir = new File(VideoManager.config.storageLocation + videoToken);
        if (!newDir.exists()){
            newDir.mkdirs();
        }



        try {
            Runtime.getRuntime().exec(
                    "cmd /c start" +
                            VideoManager.config.ffmpegLocation +
                            " -i " + inputFileLocation +
                            " -profile:v baseline -level 3.0 -s 640x360 -start_number 0 -hls_time 10 -hls_list_size 0 "
                            + " -f hls " + outputFileLocation);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
