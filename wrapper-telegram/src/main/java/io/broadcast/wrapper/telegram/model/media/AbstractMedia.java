package io.broadcast.wrapper.telegram.model.media;

import com.pengrad.telegrambot.model.request.InputMedia;
import com.pengrad.telegrambot.model.request.InputPaidMedia;
import io.broadcast.wrapper.telegram.BroadcastTelegramBotException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;

@Getter
@ToString
@RequiredArgsConstructor
public abstract class AbstractMedia implements Media {

    private final Class<? extends InputPaidMedia> inputPaidMediaClass;
    private final Class<? extends InputMedia<?>> inputMediaClass;

    private final Object media;

    public InputPaidMedia createInputPaidMedia() {
        try {
            return createInputMedia(inputPaidMediaClass, media);
        } catch (Throwable exception) {
            throw new BroadcastTelegramBotException("Could not create input paid media", exception);
        }
    }

    public InputMedia<?> createInputMedia() {
        try {
            return createInputMedia(inputMediaClass, media);
        } catch (Throwable exception) {
            throw new BroadcastTelegramBotException("Could not create input media", exception);
        }
    }

    private static <T> T createInputMedia(Class<T> inputMediaClass, Object media)
            throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {

        if (media instanceof String) {
            return inputMediaClass.getConstructor(String.class).newInstance(media);
        }
        else if (media instanceof File) {
            return inputMediaClass.getConstructor(File.class).newInstance(media);
        }
        else if (media instanceof byte[]) {
            return inputMediaClass.getConstructor(byte[].class).newInstance(media);
        }

        if (media instanceof InputStream) {
            byte[] mediaBytes = toBytesContent((InputStream) media);
            return createInputMedia(inputMediaClass, mediaBytes);
        }

        throw new IllegalArgumentException("Sending data should be String, File or byte[]");
    }

    private static byte[] toBytesBySize(InputStream inputStream) {
        try {
            return new byte[inputStream.available()];
        } catch (IOException exception) {
            throw new BroadcastTelegramBotException("Can`t convert java.io.InputStream to bytes array", exception);
        }
    }

    private static byte[] toBytesContent(InputStream inputStream) {
        try {
            byte[] array = toBytesBySize(inputStream);
            //noinspection ResultOfMethodCallIgnored
            inputStream.read(array);

            return array;
        } catch (IOException exception) {
            throw new BroadcastTelegramBotException("Can`t parse java.io.InputStream to java.lang.String", exception);
        }
    }
}
