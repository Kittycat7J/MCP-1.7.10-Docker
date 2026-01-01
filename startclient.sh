#!/bin/bash

IMAGE=java-1.7.10
set -x
# Detect host OS and set up GUI forwarding
OS=$(uname -s)
DOCKER_ARGS="-v $(pwd):/workspace -w /workspace"

if [[ "$OS" == "Linux" ]]; then
    if [[ -n "$DISPLAY" && -S /tmp/.X11-unix/X0 ]]; then
        echo "Using X11 forwarding"
        DOCKER_ARGS="$DOCKER_ARGS -e DISPLAY=$DISPLAY -v /tmp/.X11-unix:/tmp/.X11-unix -v $HOME/.Xauthority:/root/.Xauthority:ro -e XAUTHORITY=/root/.Xauthority"
    elif [[ -n "$WAYLAND_DISPLAY" && -S /run/user/$(id -u)/$WAYLAND_DISPLAY ]]; then
        echo "Using Wayland forwarding (XWayland may still be required)"
        DOCKER_ARGS="$DOCKER_ARGS -e WAYLAND_DISPLAY=$WAYLAND_DISPLAY -v /run/user/$(id -u)/$WAYLAND_DISPLAY:/run/user/$(id -u)/$WAYLAND_DISPLAY"
    else
        echo "Warning: no display detected, client may not render"
    fi
elif [[ "$OS" == "Darwin" ]]; then
    echo "Detected macOS (requires XQuartz)"
    DOCKER_ARGS="$DOCKER_ARGS -e DISPLAY=host.docker.internal:0"
elif [[ "$OS" == MINGW* || "$OS" == CYGWIN* || "$OS" == MSYS* ]]; then
    echo "Detected Windows (requires VcXsrv)"
    DOCKER_ARGS="$DOCKER_ARGS -e DISPLAY=host.docker.internal:0.0"
fi

# Run client in a disposable container
exec docker run --rm -it \
    $DOCKER_ARGS \
    -p 5006:5005 \
    -p 46799:46798 \
    $IMAGE \
    python runtime/startclient.py "$@"
