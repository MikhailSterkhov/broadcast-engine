package io.broadcast.wrapper.telegram.model.media;

import com.pengrad.telegrambot.model.request.InputMediaVideo;
import com.pengrad.telegrambot.model.request.InputPaidMediaVideo;

import java.io.File;
import java.io.InputStream;

public class Video extends AbstractMedia {

    public static Video fromString(String video) {
        return new Video(video);
    }

    public static Video fromFile(File video) {
        return new Video(video);
    }

    public static Video fromStream(InputStream video) {
        return new Video(video);
    }

    public static Video fromBytes(byte[] video) {
        return new Video(video);
    }

    private Video(Object media) {
        super(InputPaidMediaVideo.class, InputMediaVideo.class, media);
    }
}
