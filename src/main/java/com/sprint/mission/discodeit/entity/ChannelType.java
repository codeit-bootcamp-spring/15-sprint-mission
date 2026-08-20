package com.sprint.mission.discodeit.entity;

public enum ChannelType {
    PUBLIC(true),
    PRIVATE(false);

    boolean isPublic;

    ChannelType(boolean b) {
        this.isPublic = b;
    }

    public boolean isPublic() {
        return isPublic;
    }
}
