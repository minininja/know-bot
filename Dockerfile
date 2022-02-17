FROM openjdk:14.0.2-jdk
ADD target/know-bot-java-1.0-SNAPSHOT.jar know-bot.jar
CMD ["java", "-jar", "know-bot.jar"]
