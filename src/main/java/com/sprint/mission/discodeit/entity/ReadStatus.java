package com.sprint.mission.discodeit.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Getter
public class ReadStatus extends Common{

    private Instant updateAt;

    public ReadStatus(UUID id) {
        super(id);
    }
}
