package com.sprint.mission.discodeit.dto.response;

import com.sprint.mission.discodeit.entity.BinaryContent;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class BinaryContentResponse {
    private final String fileName;
    private final Long size;
    private final String contentType;
    private final byte[] bytes;

    public static BinaryContentResponse from(BinaryContent content) {
        return new BinaryContentResponse(content.getFileName(), content.getSize(), content.getContentType(), content.getBytes());
    }
}
