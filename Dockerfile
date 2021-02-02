FROM openjdk:15

EXPOSE 9083
ARG JAR_FILE=target.nosync/stocks-batch-9.0.0.jar

ADD ${JAR_FILE} stocks-batch-9.0.0.jar

ENTRYPOINT ["java","-jar","/stocks-batch-9.0.0.jar"]