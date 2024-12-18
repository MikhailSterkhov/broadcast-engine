package io.broadcast.wrapper.telegram.model.media;

import com.pengrad.telegrambot.model.request.InputMediaPhoto;
import com.pengrad.telegrambot.model.request.InputPaidMediaPhoto;

import java.io.File;
import java.io.InputStream;

public class Photo extends AbstractMedia {

    public static Photo fromString(String photo) {
        return new Photo(photo);
    }

    public static Photo fromFile(File photo) {
        return new Photo(photo);
    }

    public static Photo fromStream(InputStream photo) {
        return new Photo(photo);
    }

    public static Photo fromBytes(byte[] photo) {
        return new Photo(photo);
    }

    private Photo(Object media) {
        super(InputPaidMediaPhoto.class, InputMediaPhoto.class, media);
    }
}
