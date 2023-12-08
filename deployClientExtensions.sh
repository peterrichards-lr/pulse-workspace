#!/bin/zsh
./gradlew :client-extensions:pulse-batch-object-dependencies:clean :client-extensions:pulse-batch-object-dependencies:deploy
sleep 15
./gradlew :client-extensions:pulse-batch-object-definition:clean :client-extensions:pulse-batch-object-definition:deploy
sleep 15
./gradlew :client-extensions:pulse-micro-service:clean :client-extensions:pulse-micro-service:deploy
sleep 15
./gradlew :client-extensions:pulse-batch-object-entry:clean :client-extensions:pulse-batch-object-entry:deploy
./gradlew :client-extensions:pulse-reporting:clean :client-extensions:pulse-reporting:deploy
./gradlew :client-extensions:pulse-user-interface-components:clean :client-extensions:pulse-user-interface-components:deploy