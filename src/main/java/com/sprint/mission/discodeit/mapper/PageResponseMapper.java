package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.response.PageResponse;
import java.util.List;
import java.util.function.Function;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Component;

@Component
public class PageResponseMapper {

  public <T> PageResponse<T> fromSlice(Slice<T> slice, Function<T, Object> cursorExtractor) {
    List<T> content = slice.getContent();
    Object nextCursor = null;
    if (slice.hasNext() && !content.isEmpty()) {
      nextCursor = cursorExtractor.apply(content.get(content.size() - 1));
    }
    return new PageResponse<>(content, nextCursor, slice.getSize(), slice.hasNext(), null);
  }

  public <T> PageResponse<T> fromPage(Page<T> page, Function<T, Object> cursorExtractor) {
    List<T> content = page.getContent();
    Object nextCursor = null;
    if (page.hasNext() && !content.isEmpty()) {
      nextCursor = cursorExtractor.apply(content.get(content.size() - 1));
    }
    return new PageResponse<>(
        content,
        nextCursor,
        page.getSize(),
        page.hasNext(),
        page.getTotalElements());
  }
}