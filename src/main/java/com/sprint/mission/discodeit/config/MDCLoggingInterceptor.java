package com.sprint.mission.discodeit.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.UUID;
import org.slf4j.MDC;
import org.springframework.lang.Nullable;
import org.springframework.web.servlet.HandlerInterceptor;

public class MDCLoggingInterceptor implements HandlerInterceptor {

  private static final String REQUEST_ID_HEADER = "Discodeit-Request-ID";

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
      Object handler) {
    String requestId = UUID.randomUUID().toString();

    MDC.put("requestId", requestId);
    MDC.put("requestMethod", request.getMethod());
    MDC.put("requestUri", request.getRequestURI());

    response.setHeader(REQUEST_ID_HEADER, requestId);
    return true;
  }

  @Override
  public void afterCompletion(HttpServletRequest request, HttpServletResponse response,
      Object handler, @Nullable Exception ex) {
    MDC.remove("requestId");
    MDC.remove("requestMethod");
    MDC.remove("requestUri");
  }
}