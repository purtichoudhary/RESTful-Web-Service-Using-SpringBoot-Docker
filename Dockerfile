FROM openjdk:8
ADD target/docker-spring-boot.jar springBootDocker/docker-spring-boot.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "springBootDocker/docker-spring-boot.jar"]