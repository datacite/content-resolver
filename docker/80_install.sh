#!/bin/sh
mvn clean deploy
cp /home/app/target/*.war /var/lib/tomcat7/webapps/
