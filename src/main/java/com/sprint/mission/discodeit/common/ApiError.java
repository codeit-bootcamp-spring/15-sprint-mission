package com.sprint.mission.discodeit.common;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ApiError {
    private final String code;
    private final String message;
}
