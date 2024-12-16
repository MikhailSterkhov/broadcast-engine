package io.broadcast.wrapper.telegram.objects;

import com.pengrad.telegrambot.model.MessageEntity;
import lombok.*;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Getter
@ToString
@EqualsAndHashCode
@RequiredArgsConstructor
public class Text {

    @Contract(" -> new")
    public static @NotNull Builder builder() {
        return new Builder();
    }

    private final String text;
    private final List<MessageEntity> messageEntityList;

    public static class Builder {

        private final StringBuilder text = new StringBuilder();
        private final List<MessageEntity> messageEntityList = new ArrayList<>();

        private int currentOffset = 0;

        public Builder text(String text) {
            if (text == null || text.isEmpty()) {
                return this;
            }

            this.text.append(new String(text.getBytes(StandardCharsets.UTF_16), StandardCharsets.UTF_16));
            this.currentOffset += text.codePointCount(0, text.length());
            return this;
        }

        public Builder newline() {
            return text("\n");
        }

        private MessageEntity addEntityToText(String entityText, MessageEntity.Type type) {
            if (entityText == null || entityText.isEmpty()) {
                throw new IllegalArgumentException("Entity text cannot be null or empty.");
            }

            int entityLength = entityText.codePointCount(0, entityText.length());

            MessageEntity messageEntity = new MessageEntity(
                    type,
                    currentOffset,
                    entityLength
            );

            messageEntityList.add(messageEntity);
            return messageEntity;
        }

        public Builder mention(String text) {
            addEntityToText(text, MessageEntity.Type.mention);
            return text(text);
        }

        public Builder hashTag(String text) {
            addEntityToText(text, MessageEntity.Type.hashtag);
            return text(text);
        }

        public Builder cashTag(String text) {
            addEntityToText(text, MessageEntity.Type.cashtag);
            return text(text);
        }

        public Builder botCommand(String text) {
            addEntityToText(text, MessageEntity.Type.bot_command);
            return text(text);
        }

        public Builder url(String text, String url) {
            addEntityToText(text, MessageEntity.Type.url).url(url);
            return text(text);
        }

        public Builder email(String text) {
            addEntityToText(text, MessageEntity.Type.email);
            return text(text);
        }

        public Builder phoneNumber(String text) {
            addEntityToText(text, MessageEntity.Type.phone_number);
            return text(text);
        }

        public Builder bold(String text) {
            addEntityToText(text, MessageEntity.Type.bold);
            return text(text);
        }

        public Builder italic(String text) {
            addEntityToText(text, MessageEntity.Type.italic);
            return text(text);
        }

        public Builder code(String text) {
            addEntityToText(text, MessageEntity.Type.code);
            return text(text);
        }

        public Builder pre(String text) {
            addEntityToText(text, MessageEntity.Type.pre);
            return text(text);
        }

        public Builder textLink(String text) {
            addEntityToText(text, MessageEntity.Type.text_link);
            return text(text);
        }

        public Builder textMention(String text) {
            addEntityToText(text, MessageEntity.Type.text_mention);
            return text(text);
        }

        public Builder underline(String text) {
            addEntityToText(text, MessageEntity.Type.underline);
            return text(text);
        }

        public Builder strikethrough(String text) {
            addEntityToText(text, MessageEntity.Type.strikethrough);
            return text(text);
        }

        public Builder spoiler(String text) {
            addEntityToText(text, MessageEntity.Type.spoiler);
            return text(text);
        }

        public Builder customEmoji(String text) {
            addEntityToText(text, MessageEntity.Type.custom_emoji);
            return text(text);
        }

        public Builder blockquote(String text) {
            addEntityToText(text, MessageEntity.Type.blockquote);
            return text(text);
        }

        public Builder expandableBlockquote(String text) {
            addEntityToText(text, MessageEntity.Type.expandable_blockquote);
            return text(text);
        }

        public Text build() {
            return new Text(text.toString(), messageEntityList);
        }
    }
}
