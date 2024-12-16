package io.broadcast.engine.announcement;

public class DecoratedAnnouncement implements Announcement {

    @Override
    public String beatifyToString() {
        return toString().replace("\n", "\\n");
    }
}
