FROM ghcr.io/navikt/baseimages/temurin:18

COPY build/libs/*.jar ./
COPY build/libs/arbeidssoker-profilering-all.jar /app/app.jar
