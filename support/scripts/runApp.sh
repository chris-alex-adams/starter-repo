#!/bin/bash
nohup java -jar -Dspring.profiles.active=prod DataService-0.0.1-SNAPSHOT.jar > log.txt 2>&1 & echo $! > pid.file