# RESTful-Web-Service-Using-SpringBoot-Docker

This assignment creates a RESTful web-service that runs in a Docker container. I have used Java Spring boot framework for this task.
The web service contains four GET routes:
1. One that displays all the song records
2. One that displays all the song records corresponding to a particular artist
3. One that displays all the song records corresponding to a particular year and a particular artist
4. One that displays the song records corresponding to a particular year, a particular artist and a particular id.

The web-service is developed for a songs.json file which includes following attributes for every song:
1. Id
2. Year
3. Artist
4. Web_url
5. Img_url

To run this project you need to do the following steps:
Start the docker and do a Maven clean build for the project.

Build the docker to generate an execuatable image of the web-service using commanad-
docker build -f Dockerfile -t spring-boot-docker .

Then run the generate image using the command-
docker run -p 8085:8085 spring-boot-docker

