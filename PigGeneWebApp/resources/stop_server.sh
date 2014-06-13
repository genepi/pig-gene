#!/bin/bash

PID=$(pgrep -f 'java -jar pigGene_server.jar')

if [ -n "$PID" ]; then
	kill -9 $PID
	echo "previously running pigGene process was killed"
else
	echo "no pigGene process was running"
fi