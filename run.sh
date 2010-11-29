#!/bin/sh
export MAVEN_OPTS="-Xms256m -Xmx1024m -Djava.library.path=lib/jnotify"
mvn jetty:run
