FROM navikt/java:13
COPY build/libs/event-test-producer.jar /app/app.jar
ENV PORT=8080
EXPOSE $PORT
