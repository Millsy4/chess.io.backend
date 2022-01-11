FROM java:8-jdk-alpine

COPY /target/chess.io-0.0.1-SNAPSHOT.jar /chess-io-backend.jar

CMD ["java", "-jar", "chess-io-backend.jar"]