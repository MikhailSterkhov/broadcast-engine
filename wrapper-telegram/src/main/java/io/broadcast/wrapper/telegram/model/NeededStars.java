package io.broadcast.wrapper.telegram.model;

import lombok.*;

@Getter
@ToString
@EqualsAndHashCode
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class NeededStars {

    public static NeededStars unlockByCount(int stars) {
        return new NeededStars(stars);
    }

    private final int count;

    public boolean needsStars() {
        return count > 0;
    }
}
