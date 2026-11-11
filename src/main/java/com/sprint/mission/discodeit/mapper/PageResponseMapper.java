package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.response.PageResponse;
import org.springframework.stereotype.Component;

import java.util.List;
@Component
public class PageResponseMapper {

    private PageResponseMapper() {
    }

    public static <T> PageResponse<T> from(
            List<T> content,
            String nextCursor,
            int size,
            boolean hasNext,
            long totalElements
    ) {
        return new PageResponse<>(content, nextCursor, size, hasNext, totalElements);
    }
}