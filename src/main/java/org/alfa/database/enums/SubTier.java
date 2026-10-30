package org.alfa.database.enums;

public enum SubTier {
    FREE(0),
    SUBSCRIBER_PREMIUM(1),
    SUBSCRIBER_SUPER_PREMIUM(2),
    SUBSCRIBER_SUPER_MEGA_PREMIUM(3);

    private final int level;

    SubTier(int level) { this.level = level; }

    public boolean atLeast(SubTier other) {
        return this.level >= other.level;
    }
}
