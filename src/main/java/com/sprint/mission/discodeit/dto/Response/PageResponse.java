package com.sprint.mission.discodeit.dto.Response;

import java.util.List;

public record PageResponse<T>(
        List<T> content,
        int nember,
        int size,
        boolean hasNext,
        Long totalElements
) {

}
