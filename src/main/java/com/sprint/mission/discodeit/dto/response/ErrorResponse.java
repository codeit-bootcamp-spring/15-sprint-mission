package com.sprint.mission.discodeit.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.Instant;
import java.util.Map;

public record ErrorResponse(

    @Schema(description = "에러 발생 시각")
    Instant timestamp,

    @Schema(description = "HTTP 상태 코드")
    int status,

    @Schema(description = "에러 코드")
    String code,

    @Schema(description = "에러 메시지")
    String message,

    @Schema(description = "예외 클래스 이름")
    String exceptionType,

    @Schema(description = "상세 정보 (검증 실패 시 필드별 메시지)")
    Map<String, Object> details) {

}
