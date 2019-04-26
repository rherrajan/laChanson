#!/bin/sh
set -x #echo on

mvn clean
#ln -s ../../../../frontend/metalsmith/markdown/
mvn install
heroku local


