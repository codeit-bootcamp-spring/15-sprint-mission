package com.sprint.mission.discodeit.dto.response;

import java.util.List;

public record MessageCursorResponse<T>(
        List<T> content,
        String nextCursor,
        int size,
        boolean hasNext
) {

}