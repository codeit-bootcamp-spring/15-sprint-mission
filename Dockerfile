# 빌드 스테이지: Gradle Wrapper로 실행 가능한 JAR 생성
FROM amazoncorretto:17 AS builder
WORKDIR /app

# Gradle Wrapper가 사용하는 xargs 설치
RUN yum install -y findutils \
    && yum clean all \
    && rm -rf /var/cache/yum /var/cache/dnf

COPY gradlew .
COPY gradle gradle
COPY build.gradle settings.gradle ./
RUN chmod +x gradlew
RUN ./gradlew dependencies --no-daemon

COPY src src
RUN ./gradlew build --no-daemon -x test

# 실행 스테이지: 빌드 결과물만 복사
FROM amazoncorretto:17
WORKDIR /app

RUN if ! command -v curl >/dev/null 2>&1; then yum install -y curl; fi \
    && yum clean all \
    && rm -rf /var/cache/yum /var/cache/dnf

ENV PROJECT_NAME=discodeit \
    PROJECT_VERSION=1.2-M8 \
    JVM_OPTS="" \
    SERVER_PORT=80

COPY --from=builder /app/build/libs/${PROJECT_NAME}-${PROJECT_VERSION}.jar ./

EXPOSE 80

HEALTHCHECK --interval=5s --timeout=3s --start-period=20s --retries=3 \
    CMD curl -fsS "http://localhost:${SERVER_PORT}/actuator/health" || exit 1

CMD ["sh", "-c", "exec java $JVM_OPTS -jar /app/${PROJECT_NAME}-${PROJECT_VERSION}.jar"]
