FROM navikt/java:18

COPY build/libs/*.jar ./
COPY arbeidssoker-profilering-0.0.1.jar app.jar
