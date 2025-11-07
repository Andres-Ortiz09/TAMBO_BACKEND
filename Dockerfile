FROM bellsoft/liberica-runtime-container:jdk-17-musl
VOLUME /tmp
COPY target/app.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/app.jar"]
