package com.sprint.mission.discodeit.dto.response;

import com.sprint.mission.discodeit.entity.BinaryContent;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BinaryContentResponse {
    private String fileName;
    private Long size;
    private String contentType;
    private byte[] bytes;

    public static BinaryContentResponse from(BinaryContent content) {
        return new BinaryContentResponse(content.getFileName(), content.getSize(), content.getContentType(), content.getBytes());
    }
}
