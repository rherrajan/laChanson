#!/bin/sh
set -x #echo on

pwd
ls -la ../frontend/metalsmith/markdown

mvn clean
#ln -s ../../frontend/metalsmith/markdown/ src/main/resources/markdown
mvn install
heroku local


