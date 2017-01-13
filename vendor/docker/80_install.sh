#!/bin/sh
mvn clean package -D-Xmaven.test.skip=true
cp /home/app/target/*.war /var/lib/tomcat7/webapps/
