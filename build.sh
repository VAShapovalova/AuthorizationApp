##!/bin/bash
rm -rf out
mkdir out
kotlinc -d out/AppCli.jar -include-runtime src \
        -cp "lib/kotlinx-cli-0.2.1.jar:lib/log4j-core-2.13.1.jar:lib/log4j-api-kotlin-1.0.0.jar:lib/log4j-api-2.13.1.jar"