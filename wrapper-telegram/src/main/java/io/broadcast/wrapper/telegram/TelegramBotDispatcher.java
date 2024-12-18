package io.broadcast.wrapper.telegram;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.MessageEntity;
import com.pengrad.telegrambot.model.request.InputMedia;
import com.pengrad.telegrambot.model.request.InputPaidMedia;
import com.pengrad.telegrambot.request.BaseRequest;
import com.pengrad.telegrambot.request.SendMediaGroup;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.request.SendPaidMedia;
import com.pengrad.telegrambot.response.BaseResponse;
import io.broadcast.engine.dispatch.BroadcastDispatcher;
import io.broadcast.engine.record.Record;
import io.broadcast.wrapper.telegram.model.Multimedia;
import io.broadcast.wrapper.telegram.model.NeededStars;
import io.broadcast.wrapper.telegram.model.media.Photo;
import io.broadcast.wrapper.telegram.model.TelegramMessage;
import io.broadcast.wrapper.telegram.model.Text;
import io.broadcast.wrapper.telegram.model.media.Video;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("rawtypes")
public class TelegramBotDispatcher implements BroadcastDispatcher<Long, TelegramMessage> {

    private final TelegramBot telegramBot;

    public TelegramBotDispatcher(@NotNull String apiToken) {
        this(new TelegramBot(apiToken));
    }

    public TelegramBotDispatcher(@NotNull TelegramBot.Builder builder) {
        this(builder.build());
    }

    public TelegramBotDispatcher(@NotNull TelegramBot telegramBot) {
        this.telegramBot = telegramBot;
    }

    private BaseRequest createBaseRequest(Record<Long> record, TelegramMessage telegramMessage) {
        Text text = telegramMessage.getText();

        if (telegramMessage.isOnlyText()) {
            return new SendMessage(record.getId(), text.getText())
                    .entities(text.getMessageEntityList().toArray(new MessageEntity[0]));
        }

        if (telegramMessage.getPaidMediaStars() != null) {
            return createPaidMedia(record, telegramMessage);
        } else {
            return createMediaGroup(record, telegramMessage);
        }
    }

    private SendMediaGroup createMediaGroup(Record<Long> record, TelegramMessage telegramMessage) {
        Multimedia<Photo> photoMultimedia = telegramMessage.getAttachPhoto();
        Multimedia<Video> videoMultimedia = telegramMessage.getAttachVideo();

        List<InputMedia<?>> inputMediaList = new ArrayList<>();

        if (photoMultimedia != null && photoMultimedia.hasContents()) {
            inputMediaList.addAll(photoMultimedia.mapToList(Photo::createInputMedia));
        }

        if (videoMultimedia != null && videoMultimedia.hasContents()) {
            inputMediaList.addAll(videoMultimedia.mapToList(Video::createInputMedia));
        }

        Text text = telegramMessage.getText();
        if (text != null) {
            InputMedia<?> inputMedia = inputMediaList.get(0);
            inputMedia = inputMedia
                    .caption(text.getText())
                    .captionEntities(text.getMessageEntityList().toArray(new MessageEntity[0]));

            inputMediaList.set(0, inputMedia);
        }

        return new SendMediaGroup(
                record.getId(),
                inputMediaList.toArray(new InputMedia[0]));
    }

    private SendPaidMedia createPaidMedia(Record<Long> record, TelegramMessage telegramMessage) {
        NeededStars neededStars = telegramMessage.getPaidMediaStars();
        Multimedia<Photo> photoMultimedia = telegramMessage.getAttachPhoto();
        Multimedia<Video> videoMultimedia = telegramMessage.getAttachVideo();

        List<InputPaidMedia> inputMediaList = new ArrayList<>();

        if (photoMultimedia != null && photoMultimedia.hasContents()) {
            inputMediaList.addAll(photoMultimedia.mapToList(Photo::createInputPaidMedia));
        }

        if (videoMultimedia != null && videoMultimedia.hasContents()) {
            inputMediaList.addAll(videoMultimedia.mapToList(Video::createInputPaidMedia));
        }

        SendPaidMedia sendPaidMedia = new SendPaidMedia(
                record.getId(),
                neededStars.getCount(),
                inputMediaList.toArray(new InputPaidMedia[0]));

        Text text = telegramMessage.getText();
        if (text != null) {
            sendPaidMedia = sendPaidMedia
                    .caption(text.getText())
                    .captionEntities(text.getMessageEntityList().toArray(new MessageEntity[0]));
        }

        return sendPaidMedia;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void dispatch(@NotNull Record<Long> record, @NotNull TelegramMessage announcement) {
        BaseRequest request = createBaseRequest(record, announcement);
        BaseResponse response = telegramBot.execute(request);

        if (!response.isOk()) {
            int errorCode = response.errorCode();
            String description = response.description();

            throw new BroadcastTelegramBotException("Failed dispatch message to telegram bot [errorCode=" + errorCode + ", description=" + description + "]");
        }
    }
}
