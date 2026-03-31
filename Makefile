.PHONY: build run run-dist

build:
    ./app/gradlew build

run-dist:
    ./app/build/install/app/bin/app

run: build run-dist