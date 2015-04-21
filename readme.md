# Embbeded Jetty web application #

This project shows how to build a WAR-less Java web application with Jetty.

Only a single JVM is required to start the web app. No web container or JEE server is required.

Instead of building a war file, an auto-executable JAR file is built with Maven (ie. jetty-webapp-1.0.0-SNAPSHOT.jar)

This JAR contains all resources required by a web application: web.xml, index.jsp, images, js, css...
The JettyServer class provides both a start and a stop method. Its main method starts the server. To stop it, you may use the Stop class.

Thanks to the [Application Assembler Maven Plugin](http://mojo.codehaus.org/appassembler/appassembler-maven-plugin/) a start.sh and a stop.sh script are available.

All JAR dependencies are available in a lib\ sub-directory.


## Try it ##

Download the code with git:
```git clone git://github.com/arey/embedded-jetty-webapp.git```

Build and package the web application

```
cd embedded-jetty-webapp
mvn clean install
```

Start the web application

```
target/appassembler/bin/start.sh &
```

Stop the web application

```
target/appassembler/bin/stop.sh
```

Browse to [http://localhost:8080/HelloWorld](http://localhost:8080/HelloWorld)

Web port could be changed at startup:

```
target/appassembler/bin/start.sh 80 8090 &
```

Another possibility is to build a JAR that include all its dependencies (web app, jetty dependencies, logger, spring ...) :
```
mvn clean install -Pfatjar
java -jar target/jetty-webapp-1.0.0-SNAPSHOT-jar-with-dependencies.jar
```

## Configuration rules ##

Compared to the [Maven Standard Directory Layout](https://maven.apache.org/guides/introduction/introduction-to-the-standard-directory-layout.html)
of a WAR, the web application sources is not put into the ```src/main/webapp``` but into the ```src/main/resources/webapp```.
Thus the webapp resources are copied into the JAR in a webapp/ sub-directory.

## Credits ##

* Uses [Maven](http://maven.apache.org/) as a build tool
* Uses [Maven Jetty plugin](https://github.com/eclipse/jetty.project) source code
