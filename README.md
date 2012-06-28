DataCite Content Resolver
=========================

This software is deployed at [https://data.datacite.org](https://data.datacite.org)

For documentation please check [http://crosscite.org/cn](http://crosscite.org/cn])

Building and running locally
----------------------------

### Check out the code and adjust configuration parameters:

    cp src/main/resources/config.properties.template src/main/resources/config.properties
    vi src/main/resources/config.properties

### Check if all test are ok:

    mvn test

### Run locally:

    mvn tomcat7:run

### If you want to debug:

    export MAVEN_OPTS="-Xdebug -Xrunjdwp:transport=dt_socket,server=y,suspend=n,address=5005"
    mvn tomcat7:run

and connect with your debugger

### Build .war (for deployment in your web container)

    mvn package


