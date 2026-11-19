## 빌드만을 담당하는 레이어
FROM amazoncorretto:17 AS builder
WORKDIR /app

RUN yum install -y findutils && yum clean all

COPY gradlew .
COPY gradle gradle
COPY build.gradle settings.gradle ./
RUN chmod +x gradlew
RUN ./gradlew dependencies --no-daemon

COPY src src
RUN ./gradlew build --no-daemon -x test

## 실행만을 담당하는 레이어
FROM amazoncorretto:17

HEALTHCHECK --interval=5s --timeout=3s --start-period=20s --retries=3 CMD curl -f http://localhost:${SERVER_PORT}/actuator/health || exit 1

WORKDIR /app

ENV PROJECT_NAME=discodeit \
    PROJECT_VERSION=1.2-M8 \
    JVM_OPTS="" \
    SERVER_PORT=80

COPY --from=builder /app/build/libs/${PROJECT_NAME}-${PROJECT_VERSION}.jar ./

EXPOSE 80
CMD ["sh", "-c", "exec java $JVM_OPTS -jar \"${PROJECT_NAME}-${PROJECT_VERSION}.jar\""]
