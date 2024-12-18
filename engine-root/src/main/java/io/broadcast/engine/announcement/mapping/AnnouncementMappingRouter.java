package io.broadcast.engine.announcement.mapping;

import io.broadcast.engine.announcement.Announcement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public final class AnnouncementMappingRouter<V extends Announcement> {

    private final Map<Class<?>, AnnouncementMappingFactory<V, ?>> mappings = new ConcurrentHashMap<>();
    private AnnouncementMappingFactory<V, ?> defaultValueFactory;

    public AnnouncementMappingRouter<V> setDefault(@NotNull AnnouncementMappingFactory<V, ?> defaultValueFactory) {
        this.defaultValueFactory = defaultValueFactory;
        return this;
    }

    public AnnouncementMappingRouter<V> setDefault(@NotNull V singletonValue) {
        this.defaultValueFactory = AnnouncementMappingFactory.constant(singletonValue);
        return this;
    }

    public <T extends Announcement> AnnouncementMappingRouter<V> put(@NotNull Class<T> announcementType, @NotNull AnnouncementMappingFactory<V, T> factory) {
        mappings.put(announcementType, factory);
        return this;
    }

    public <T extends Announcement> AnnouncementMappingRouter<V> put(@NotNull Class<T> announcementType, @NotNull V singletonValue) {
        put(announcementType, AnnouncementMappingFactory.constant(singletonValue));
        return this;
    }

    @SuppressWarnings("unchecked")
    @Nullable
    public <T extends Announcement> AnnouncementMappingFactory<V, T> getFactory(@NotNull T announcement) {
        return (AnnouncementMappingFactory<V, T>) mappings.getOrDefault(announcement.getClass(), defaultValueFactory);
    }

    @NotNull
    public <T extends Announcement> Optional<V> getOpt(@NotNull T announcement) {
        return Optional.ofNullable(getFactory(announcement)).map(factory -> factory.createBy(announcement));
    }

    @Nullable
    public <T extends Announcement> V get(@NotNull T announcement) {
        return getOpt(announcement).orElse(null);
    }
}
