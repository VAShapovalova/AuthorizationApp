##!/bin/bash
rm -rf out
mkdir out
kotlinc -d out/AppCli.jar -include-runtime src -cp lib/kotlinx-cli-0.2.1.jar