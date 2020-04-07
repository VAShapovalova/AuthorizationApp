##!/bin/bash

kotlinc -d out/AppCli.jar -include-runtime src -cp lib/kotlinx-cli-0.2.1.jar:lib/h2-1.4.200.jar:lib/flyway-core-6.3.2.jar:lib/log4j-core-2.13.1.jar:lib/log4j-api-kotlin-1.0.0.jar:lib/log4j-api-2.13.1.jar
