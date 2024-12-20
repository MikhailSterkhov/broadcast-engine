package io.broadcast.wrapper.spigot;

import io.broadcast.engine.AbstractBroadcastPipelineWrapper;
import io.broadcast.engine.announcement.Announcement;
import io.broadcast.engine.announcement.AnnouncementExtractor;
import io.broadcast.engine.announcement.StringAnnouncement;
import io.broadcast.engine.scheduler.Scheduler;
import io.broadcast.wrapper.spigot.dispatcher.SpigotDispatcher;
import io.broadcast.wrapper.spigot.extractor.announcement.PermissibleStringAnnouncement;
import io.broadcast.wrapper.spigot.extractor.record.SpigotRecordExtractor;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SpigotBroadcastPipeline<I, A extends Announcement>
        extends AbstractBroadcastPipelineWrapper<I, A, SpigotBroadcastPipeline<I, A>> {

    @Contract(" -> new")
    public static <I, A extends Announcement> @NotNull SpigotBroadcastPipeline<I, A> createPipeline() {
        return new SpigotBroadcastPipeline<>();
    }

    public static SpigotBroadcastPipeline<Player, PermissibleStringAnnouncement> preparePermissibleOnlinePlayers(
            @Nullable AnnouncementExtractor<PermissibleStringAnnouncement> announcementExtractor
    ) {
        SpigotBroadcastPipeline<Player, PermissibleStringAnnouncement> preparedPipeline =
                new SpigotBroadcastPipeline<Player, PermissibleStringAnnouncement>()
                        .setDispatcher(SpigotDispatcher.permissibleOnlinePlayers())
                        .setRecordExtractor(SpigotRecordExtractor.onlinePlayers())
                        .setScheduler(Scheduler.defaultScheduler());

        if (announcementExtractor != null) {
            preparedPipeline.setAnnouncementExtractor(announcementExtractor);
        }
        return preparedPipeline;
    }

    public static SpigotBroadcastPipeline<Player, PermissibleStringAnnouncement> preparePermissibleOnlinePlayers() {
        return preparePermissibleOnlinePlayers(null);
    }

    public static SpigotBroadcastPipeline<String, PermissibleStringAnnouncement> preparePermissibleOnlinePlayersByName(
            @Nullable AnnouncementExtractor<PermissibleStringAnnouncement> announcementExtractor
    ) {
        SpigotBroadcastPipeline<String, PermissibleStringAnnouncement> preparedPipeline =
                new SpigotBroadcastPipeline<String, PermissibleStringAnnouncement>()
                        .setDispatcher(SpigotDispatcher.permissibleOnlinePlayersByName())
                        .setRecordExtractor(SpigotRecordExtractor.onlinePlayersByName())
                        .setScheduler(Scheduler.defaultScheduler());

        if (announcementExtractor != null) {
            preparedPipeline.setAnnouncementExtractor(announcementExtractor);
        }
        return preparedPipeline;
    }

    public static SpigotBroadcastPipeline<String, PermissibleStringAnnouncement> preparePermissibleOnlinePlayersByName() {
        return preparePermissibleOnlinePlayersByName(null);
    }

    public static SpigotBroadcastPipeline<UUID, PermissibleStringAnnouncement> preparePermissibleOnlinePlayersByUniqueId(
            @Nullable AnnouncementExtractor<PermissibleStringAnnouncement> announcementExtractor
    ) {
        SpigotBroadcastPipeline<UUID, PermissibleStringAnnouncement> preparedPipeline =
                new SpigotBroadcastPipeline<UUID, PermissibleStringAnnouncement>()
                        .setDispatcher(SpigotDispatcher.permissibleOnlinePlayersByUniqueId())
                        .setRecordExtractor(SpigotRecordExtractor.onlinePlayersByUniqueId())
                        .setScheduler(Scheduler.defaultScheduler());

        if (announcementExtractor != null) {
            preparedPipeline.setAnnouncementExtractor(announcementExtractor);
        }
        return preparedPipeline;
    }

    public static SpigotBroadcastPipeline<UUID, PermissibleStringAnnouncement> preparePermissibleOnlinePlayersByUniqueId() {
        return preparePermissibleOnlinePlayersByUniqueId(null);
    }

    public static SpigotBroadcastPipeline<Player, StringAnnouncement> prepareOnlinePlayers(
            @Nullable AnnouncementExtractor<StringAnnouncement> announcementExtractor
    ) {
        SpigotBroadcastPipeline<Player, StringAnnouncement> preparedPipeline =
                new SpigotBroadcastPipeline<Player, StringAnnouncement>()
                        .setDispatcher(SpigotDispatcher.onlinePlayers())
                        .setRecordExtractor(SpigotRecordExtractor.onlinePlayers())
                        .setScheduler(Scheduler.defaultScheduler());

        if (announcementExtractor != null) {
            preparedPipeline.setAnnouncementExtractor(announcementExtractor);
        }
        return preparedPipeline;
    }

    public static SpigotBroadcastPipeline<Player, StringAnnouncement> prepareOnlinePlayers() {
        return prepareOnlinePlayers(null);
    }

    public static SpigotBroadcastPipeline<String, StringAnnouncement> prepareOnlinePlayersByName(
            @Nullable AnnouncementExtractor<StringAnnouncement> announcementExtractor
    ) {
        SpigotBroadcastPipeline<String, StringAnnouncement> preparedPipeline =
                new SpigotBroadcastPipeline<String, StringAnnouncement>()
                        .setDispatcher(SpigotDispatcher.onlinePlayersByName())
                        .setRecordExtractor(SpigotRecordExtractor.onlinePlayersByName())
                        .setScheduler(Scheduler.defaultScheduler());

        if (announcementExtractor != null) {
            preparedPipeline.setAnnouncementExtractor(announcementExtractor);
        }
        return preparedPipeline;
    }

    public static SpigotBroadcastPipeline<String, StringAnnouncement> prepareOnlinePlayersByName() {
        return prepareOnlinePlayersByName(null);
    }

    public static SpigotBroadcastPipeline<UUID, StringAnnouncement> prepareOnlinePlayersByUniqueId(
            @Nullable AnnouncementExtractor<StringAnnouncement> announcementExtractor
    ) {
        SpigotBroadcastPipeline<UUID, StringAnnouncement> preparedPipeline =
                new SpigotBroadcastPipeline<UUID, StringAnnouncement>()
                        .setDispatcher(SpigotDispatcher.onlinePlayersByUniqueId())
                        .setRecordExtractor(SpigotRecordExtractor.onlinePlayersByUniqueId())
                        .setScheduler(Scheduler.defaultScheduler());

        if (announcementExtractor != null) {
            preparedPipeline.setAnnouncementExtractor(announcementExtractor);
        }
        return preparedPipeline;
    }

    public static SpigotBroadcastPipeline<UUID, StringAnnouncement> prepareOnlinePlayersByUniqueId() {
        return prepareOnlinePlayersByUniqueId(null);
    }
}
