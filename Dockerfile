FROM 11.0.13-oraclelinux8
ADD target/know-bot-java-1.0.SNAPSHOT.jar know-bot.jar
CMD ["java", "-jar", "know-bot.jar"]

