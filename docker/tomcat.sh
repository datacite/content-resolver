#!/bin/sh
exec 2>&1

exec $CATALINA_HOME/bin/catalina.sh run
