Gatling plugin for Maven - Java demo project
============================================

A simple showcase of a Maven project using the Gatling plugin for Maven. Refer to the plugin documentation
[on the Gatling website](https://gatling.io/docs/current/extensions/maven_plugin/) for usage.

This project is written in Java,  

It includes:

* [Maven Wrapper](https://maven.apache.org/wrapper/), so that you can immediately run Maven with `./mvnw` without having
  to install it on your computer
* minimal `pom.xml`
* latest version of `io.gatling:gatling-maven-plugin` applied
* sample [Simulation](https://gatling.io/docs/gatling/reference/current/general/concepts/#simulation) class,
  demonstrating sufficient Gatling functionality
* proper source file layout


Maven Wrapper
-------------
Easy Maven installation: No need to install Maven on your system. The wrapper downloads and configures Maven automatically.
Project-specific Maven: Allows multiple projects to use different Maven versions without conflicts.

The easiest way to setup the Maven Wrapper for your project is to use the Maven Wrapper Plugin with its provided wrapper goal. To add or update all the necessary Maven Wrapper files to your project execute the following command:

mvn wrapper:wrapper
Normally you instruct users to install a specific version of Apache Maven, put it on the PATH and then run the mvn command like the following:

mvn clean install
But now, with a Maven Wrapper setup, you can instruct users to run wrapper scripts:

./mvnw clean install
or on Windows

mvnw.cmd clean install
