package com.sprint.mission.discodeit.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record BinaryContentCreateRequest(
        @NotBlank(message = "파일명은 필수입니다.")
        @Pattern(regexp = "^(?=.*[^\\s.].*\\.)[^\\r\\n]+\\.[^\\s.]+$",
                message = "파일명은 이름.확장자 형식이어야 합니다.")
        @Size(min = 3, max = 255, message = "파일명은 최대 255글자 입니다.")
        String fileName,

        @NotBlank(message = "콘텐츠타입은 필수입니다.")
        @Size(max = 100,message = "콘텐츠타입은 최대 100글자 입니다.")
        String contentType,

        @NotEmpty(message = "빈 파일 입니다.")
        @Size(max = 5 * 1024 * 1024, message = "파일 크기는 5MB 이하여야 합니다.")
        byte[] bytes
) {
}
