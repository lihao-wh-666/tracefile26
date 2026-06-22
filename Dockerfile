FROM maven:3.8.6-jdk-8 AS build

ENV TZ=Asia/Shanghai \
    LANG=C.UTF-8

WORKDIR /app

COPY cms/pom.xml ./pom.xml
COPY cms/src ./src

RUN mvn clean package -DskipTests -q

FROM tomcat:9.0.93-jdk11-temurin

ENV TZ=Asia/Shanghai \
    LANG=C.UTF-8

RUN rm -rf /usr/local/tomcat/webapps/*

COPY --from=build /app/target/opening-cms.war /usr/local/tomcat/webapps/ROOT.war

RUN mkdir -p /usr/local/tomcat/conf/resources
COPY docker/tomcat/db.properties /usr/local/tomcat/conf/resources/db.properties

ENV CATALINA_OPTS="-Ddb.config=/usr/local/tomcat/conf/resources/db.properties \
    -Dfile.encoding=UTF-8 \
    -Xms512m \
    -Xmx1024m"

EXPOSE 8080

CMD ["catalina.sh", "run"]
