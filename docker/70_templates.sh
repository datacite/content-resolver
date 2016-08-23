#!/bin/sh
dockerize -template /home/app/docker/config.properties.tmpl:/home/app/src/main/resources/config.properties
dockerize -template /home/app/docker/log4j.xml.tmpl:/home/app/src/main/resources/log4j.xml
