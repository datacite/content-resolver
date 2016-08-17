#!/bin/sh
mvn clean compile war:war
cp /home/app/target/*.war /var/lib/tomcat7/webapps/
