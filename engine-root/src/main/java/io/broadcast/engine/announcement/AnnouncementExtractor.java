package io.broadcast.engine.announcement;

import io.broadcast.engine.record.Record;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;
import java.util.function.Supplier;

public interface AnnouncementExtractor<A extends Announcement> {

    @Nullable
    <I> A extractAnnouncement(Record<I> record);

    @Contract(pure = true)
    static <A extends Announcement> @NotNull AnnouncementExtractor<A> constant(@Nullable A announcement) {
        return new AnnouncementExtractor<A>() {
            @Override
            public <I> @Nullable A extractAnnouncement(Record<I> record) {
                return announcement;
            }
        };
    }

    @Contract(pure = true)
    static <A extends Announcement> @NotNull AnnouncementExtractor<A> mutable(@NotNull Supplier<A> announcement) {
        return new AnnouncementExtractor<A>() {
            @Override
            public <I> @Nullable A extractAnnouncement(Record<I> record) {
                return announcement.get();
            }
        };
    }

    @Contract(pure = true)
    static <I, A extends Announcement> @NotNull AnnouncementExtractor<A> fromRecord(@NotNull Class<I> idClass, @NotNull Function<Record<I>, A> announcement) {
        return new AnnouncementExtractor<A>() {
            @SuppressWarnings("unchecked")
            @Override
            public <K> @Nullable A extractAnnouncement(Record<K> record) {
                return announcement.apply((Record<I>) record);
            }
        };
    }

    @Contract(pure = true)
    static <I, A extends Announcement> @NotNull AnnouncementExtractor<A> fromID(@NotNull Class<I> idClass, @NotNull Function<I, A> announcement) {
        return fromRecord(idClass, record -> announcement.apply(record.getId()));
    }

    @Contract(pure = true)
    static <T> @NotNull AnnouncementExtractor<ContentedAnnouncement<T>> contented(@Nullable T subject, @Nullable T content) {
        return constant(new ContentedAnnouncement<>(subject, content));
    }

    @Contract(pure = true)
    static <T> @NotNull AnnouncementExtractor<ContentedAnnouncement<T>> contentedNonSubject(@Nullable T content) {
        return constant(new ContentedAnnouncement<>(null, content));
    }

    @Contract(pure = true)
    static @NotNull AnnouncementExtractor<ContentedAnnouncement<String>> contentedString(@Nullable String subject, @Nullable String content) {
        return constant(new ContentedAnnouncement<>(subject, content));
    }

    @Contract(pure = true)
    static @NotNull AnnouncementExtractor<ContentedAnnouncement<String>> contentedStringNonSubject(@Nullable String content) {
        return constant(new ContentedAnnouncement<>(null, content));
    }

    @Contract(pure = true)
    static <I, T> @NotNull AnnouncementExtractor<AnnouncementAndID<I, T>> withID(@NotNull I id, @Nullable T message) {
        return constant(new AnnouncementAndID<>(id, message));
    }

    @Contract(pure = true)
    static <I, T> @NotNull AnnouncementExtractor<AnnouncementAndID<I, T>> withID(@NotNull Record<I> record, @Nullable T message) {
        return constant(new AnnouncementAndID<>(record.getId(), message));
    }

    @Contract(pure = true)
    static <I, T> @NotNull AnnouncementExtractor<AnnouncementAndRecord<I, T>> withRecord(@NotNull I id, @Nullable T message) {
        return constant(new AnnouncementAndRecord<>(new Record<>(id), message));
    }

    @Contract(pure = true)
    static <I, T> @NotNull AnnouncementExtractor<AnnouncementAndRecord<I, T>> withRecord(@NotNull Record<I> record, @Nullable T message) {
        return constant(new AnnouncementAndRecord<>(record, message));
    }
}
