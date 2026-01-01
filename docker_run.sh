#!/bin/bash
# docker_run.sh: Build and run the java-1.7.10 Docker container if not running

IMAGE=java-1.7.10
CONTAINER=java-1.7.10-dev

rebuild=false
if [[ "$1" == "--build" ]]; then
    rebuild=true
    echo "Forcing rebuild of $IMAGE..."
    docker build -t $IMAGE .
else
    # Build the image if it doesn't exist
    docker image inspect $IMAGE > /dev/null 2>&1 || docker build -t $IMAGE .
fi

# If container is running
if docker ps --format '{{.Names}}' | grep -q "^${CONTAINER}\$"; then
    if $rebuild; then
        echo "Stopping and removing running container $CONTAINER..."
        docker stop $CONTAINER >/dev/null
        docker rm $CONTAINER >/dev/null
    else
        echo "Container $CONTAINER is already running."
        exit 0
    fi
fi

# If stopped container exists
if docker ps -a --format '{{.Names}}' | grep -q "^${CONTAINER}\$"; then
    docker rm $CONTAINER >/dev/null
fi

# Start the container in detached mode, mounting the workspace
docker run -d --name $CONTAINER -v "$(pwd)":/workspace -p 25566:25565 -w /workspace $IMAGE tail -f /dev/null
echo "Container $CONTAINER started."
