FROM navikt/java:18

COPY build/libs/*.jar ./
COPY build/classes/* ./
COPY build/libs/arbeidssoker-profilering-all.jar /app/app.jar
