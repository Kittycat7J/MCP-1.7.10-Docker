#!/bin/bash
./docker_run.sh
exec docker exec -it java-1.7.10-dev python runtime/updatemd5.py "$@"
