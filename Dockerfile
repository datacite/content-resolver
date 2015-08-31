FROM datacite1/tomcat7

VOLUME [ "/var/log/tomcat7/" ]

WORKDIR /src
RUN mvn clean compile war:war
COPY /src/target/*.war /var/lib/tomcat7/webapps/

WORKDIR /
CMD [ "run" ]
