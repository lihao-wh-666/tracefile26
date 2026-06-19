FROM maven:3.8.6-eclipse-temurin-11

ENV TZ=Asia/Shanghai
ENV LANG=C.UTF-8
ENV CATALINA_HOME=/usr/local/tomcat
ENV PATH=$CATALINA_HOME/bin:$PATH

WORKDIR /usr/local/tomcat

COPY tomcat.tar.gz /tmp/tomcat.tar.gz

RUN mkdir -p /usr/local/tomcat && \
    tar -xf /tmp/tomcat.tar.gz -C /usr/local/tomcat --strip-components=1 && \
    rm -f /tmp/tomcat.tar.gz && \
    rm -rf /usr/local/tomcat/webapps/*

COPY cms/target/opening-cms.war /usr/local/tomcat/webapps/ROOT.war

RUN mkdir -p /usr/local/tomcat/conf/resources
COPY docker/tomcat/db.properties /usr/local/tomcat/conf/resources/db.properties

ENV CATALINA_OPTS="-Ddb.config=/usr/local/tomcat/conf/resources/db.properties -Dfile.encoding=UTF-8 -Xms512m -Xmx1024m"

EXPOSE 8080

CMD ["catalina.sh", "run"]
