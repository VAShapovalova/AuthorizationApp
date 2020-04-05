##!/bin/bash
if [ -z "$DBURL" ]; then
    export DBURL=jdbc:h2:./db/AuthorizationApp
fi
if [ -z "$DBLOGIN" ]; then
    export DBLOGIN=sa
fi
if [ -z "$DBPASS" ]; then
    export DBPASS=
fi

java -classpath "out/AppCli.jar:lib/kotlinx-cli-0.2.1.jar:lib/h2-1.4.200.jar:lib/flyway-core-6.3.2.jar" MainKt $@