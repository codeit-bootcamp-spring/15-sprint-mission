FROM ubuntu:latest
LABEL authors="chichi"

ENTRYPOINT ["top", "-b"]