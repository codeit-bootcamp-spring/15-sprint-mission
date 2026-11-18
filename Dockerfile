FROM amazoncorretto:17 AS build
WORKDIR /app

RUN yum install -y findutils && yum clean all

COPY . .
RUN chmod +x gradlew && ./gradlew bootJar --no-daemon

FROM amazoncorretto:17
WORKDIR /app

ENV PROJECT_NAME=discodeit \
    PROJECT_VERSION=1.2-M8 \
    JVM_OPTS="" \
    SERVER_PORT=80

COPY --from=build /app/build/libs/ ./build/libs/

EXPOSE 80
CMD ["sh", "-c", "exec java $JVM_OPTS -jar \"build/libs/${PROJECT_NAME}-${PROJECT_VERSION}.jar\""]
