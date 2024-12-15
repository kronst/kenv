#!/bin/bash

echo "Preparing artifacts for publication..."
./gradlew clean publish

echo "Creating JReleaser directory..."
mkdir -p build/jreleaser

echo "Deploying to Maven Central..."
./gradlew jreleaserDeploy

echo "Publication process completed"
