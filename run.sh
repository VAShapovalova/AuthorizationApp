##!/bin/bash

java -Dlog4j.configurationFile=src/resources/log4j2.xml \
        -cp "lib/kotlinx-cli-0.2.1.jar:lib/log4j-core-2.13.1.jar:lib/log4j-api-kotlin-1.0.0.jar:lib/log4j-api-2.13.1.jar:out/AppCli.jar" \
        Main $@