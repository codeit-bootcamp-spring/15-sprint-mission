package com.sprint.mission.discodeit.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Getter
public class BinaryContent extends Common{

    private Instant updateAt;

    public BinaryContent(UUID id) {
        super(id);
    }
}
