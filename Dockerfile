FROM openjdk:11.0.13-jdk
ADD target/know-bot-java-1.0.SNAPSHOT.jar know-bot.jar
CMD ["java", "-jar", "know-bot.jar"]

