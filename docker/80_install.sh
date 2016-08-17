#!/bin/sh
mvn clean deploy -Dmaven.test.skip=true
cp /home/app/target/*.war /var/lib/tomcat7/webapps/
