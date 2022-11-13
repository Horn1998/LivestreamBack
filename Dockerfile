FROM java8
COPY *.jar /app.jar
CMD ["--server.port=8088"]
EXPOSE 8088