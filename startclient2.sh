#!/bin/bash
set -e

docker run --rm -it \
    -v "$(pwd)":/workspace -w /workspace \
    -p 5005:5005 \
    -p 6080:6080 \
    -p 5900:5900 \
    java-1.7.10 \
    sh -c "sudo chmod +x /home/mcp/dockerVNC.sh && /home/mcp/dockerVNC.sh && export DISPLAY=:99 && python runtime/startclient.py \"$@\""
