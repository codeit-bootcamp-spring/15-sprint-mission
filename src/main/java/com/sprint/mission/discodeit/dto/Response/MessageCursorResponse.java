package com.sprint.mission.discodeit.dto.Response;

import java.util.List;

public record MessageCursorResponse<T>(
        List<T> content,
        String nextCursor,
        int size,
        boolean hasNext
) {

}