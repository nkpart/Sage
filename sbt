#!/bin/bash

java $SBT_OPTS -Dfile.encoding=UTF-8 -Xss16M -Xmx1024M -XX:MaxPermSize=128M -XX:NewSize=128M -XX:NewRatio=3 -jar `dirname $0`/etc/xsbt-launch-0.6.10.jar "$@"
