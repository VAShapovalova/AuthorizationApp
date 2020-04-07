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

java -Dlog4j.configurationFile=src/resources/log4j2.xml \
        -cp "lib/h2-1.4.200.jar:lib/flyway-core-6.3.2.jar:lib/kotlinx-cli-0.2.1.jar:lib/log4j-core-2.13.1.jar:lib/log4j-api-kotlin-1.0.0.jar:lib/log4j-api-2.13.1.jar:out/AppCli.jar" \
        Main $@
