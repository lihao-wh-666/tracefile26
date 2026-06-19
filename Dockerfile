FROM maven:3.8.6-jdk-8 AS builder

WORKDIR /app

COPY ../cms/pom.xml ./pom.xml
COPY ../cms/src ./src

RUN mvn clean package -DskipTests

FROM tomcat:8.5.88-jdk8-temurin

ENV TZ=Asia/Shanghai
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone

RUN rm -rf /usr/local/tomcat/webapps/*

COPY --from=builder /app/target/opening-cms.war /usr/local/tomcat/webapps/ROOT.war

COPY ./docker/tomcat/db.properties /usr/local/tomcat/webapps/ROOT/WEB-INF/classes/db.properties

RUN mkdir -p /usr/local/tomcat/webapps/ROOT/WEB-INF/classes

EXPOSE 8080

CMD ["catalina.sh", "run"]
