package io.broadcast.engine;

import io.broadcast.engine.record.Record;
import io.broadcast.engine.record.RecordToStringSerializer;
import io.broadcast.engine.record.RecordToTextSerializer;
import io.broadcast.engine.spi.ApplicablePreparedMessage;
import io.broadcast.engine.spi.ImmutableStringPreparedMessage;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.function.Function;

/**
 * Functional interface representing the preparation of a message to be broadcasted.
 *
 * <p>The {@code PreparedMessage} interface defines a mechanism for creating announcements
 * based on records. It supports both static and dynamic preparation of messages
 * through predefined or functional approaches.</p>
 *
 * @param <I> The type of the record identifier.
 * @param <T> The type of the record entity.
 */
@FunctionalInterface
public interface PreparedMessage<I, T> {

    /**
     * Creates an announcement based on the provided record.
     *
     * @param record The {@link Record} from which the announcement is prepared. Must not be {@code null}.
     * @return The created {@link Announcement} for the provided record. Never {@code null}.
     */
    @NotNull
    Announcement<I, T> createAnnouncement(@NotNull Record<I, T> record);

    @Contract("_, _ -> new")
    static <I, T> @NotNull PreparedMessage<I, T> immutable(String subject, String content) {
        return immutable(TextMessage.builder()
                .subject(subject)
                .content(content)
                .build());
    }

    @Contract("_ -> new")
    static <I, T> @NotNull PreparedMessage<I, T> immutableSubject(String subject) {
        return immutable(subject, null);
    }

    @Contract("_ -> new")
    static <I, T> @NotNull PreparedMessage<I, T> immutableContent(String content) {
        return immutable(null, content);
    }

    @Contract("_ -> new")
    static <I, T> @NotNull PreparedMessage<I, T> immutable(TextMessage textMessage) {
        return new ImmutableStringPreparedMessage<>(textMessage);
    }

    @Contract("_ -> new")
    static <I, T> @NotNull PreparedMessage<I, T> serializeText(RecordToTextSerializer<I, T> serializer) {
        return new ApplicablePreparedMessage<>(serializer);
    }

    @Contract("_, _ -> new")
    static <I, T> @NotNull PreparedMessage<I, T> serialize(RecordToStringSerializer<I, T> subjectSerializer,
                                                           RecordToStringSerializer<I, T> contentSerializer) {
        return serializeText(record ->
                TextMessage.builder()
                        .subject(Optional.ofNullable(subjectSerializer).map(s -> s.serializeToString(record)).orElse(null))
                        .content(Optional.ofNullable(contentSerializer).map(s -> s.serializeToString(record)).orElse(null))
                        .build());
    }

    @Contract("_ -> new")
    static <I, T> @NotNull PreparedMessage<I, T> serializeSubject(RecordToStringSerializer<I, T> subjectSerializer) {
        return serialize(subjectSerializer, null);
    }

    @Contract("_ -> new")
    static <I, T> @NotNull PreparedMessage<I, T> serializeContent(RecordToStringSerializer<I, T> contentSerializer) {
        return serialize(null, contentSerializer);
    }
}

