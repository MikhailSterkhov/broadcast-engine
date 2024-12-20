package io.broadcast.wrapper.spigot.extractor.announcement;

import io.broadcast.engine.announcement.DecoratedAnnouncement;
import lombok.*;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

@Getter
@ToString
@EqualsAndHashCode(callSuper = false)
@RequiredArgsConstructor
@Builder
public class PermissibleStringAnnouncement extends DecoratedAnnouncement {

    @Contract("_ -> new")
    public static @NotNull PermissibleStringAnnouncement nonPermissible(@NotNull String message) {
        return new PermissibleStringAnnouncement(null, message);
    }

    @Contract("_, _ -> new")
    public static @NotNull PermissibleStringAnnouncement withPermission(@NonNull String permission, @NotNull String message) {
        return new PermissibleStringAnnouncement(permission, message);
    }

    private final String permission;
    private final String message;

    public boolean hasPermission(@NonNull Player player) {
        return permission == null || player.hasPermission(permission);
    }
}
