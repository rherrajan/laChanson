#!/bin/sh
set -x #echo on

mvn clean
ln -s ../../../../frontend/metalsmith/markdown/ src/main/resources/markdown
mvn install
heroku local


