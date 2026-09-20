#!/bin/bash
set -e
if [ -n "$1" ] ; then
    mvn archetype:generate -DgroupId=com.alexswd.torrent.$1 -DartifactId=$1 -DinteractiveMode=False
    cd "$1"
else
    echo "usage: $0 <app>"
fi
