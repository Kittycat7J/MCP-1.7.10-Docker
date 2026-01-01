#!/bin/bash
set -e
export DISPLAY=:99
export LIBGL_ALWAYS_SOFTWARE=1
export LIBGL_DRI3_DISABLE=1
export LD_LIBRARY_PATH=/usr/lib/x86_64-linux-gnu:$LD_LIBRARY_PATH

# Ensure /tmp/.X11-unix exists (needed by Xvfb)
mkdir -p /tmp/.X11-unix
chmod 1777 /tmp/.X11-unix

# Start Xvfb (virtual X server)
Xvfb :99 -screen 0 1024x768x24 -ac +extension GLX +render -noreset &
XVFB_PID=$!

# Give Xvfb time to start
sleep 2

# Optional: test OpenGL
echo "OpenGL info:"
glxinfo | grep "OpenGL renderer" || echo "glxinfo not available"

# Start x11vnc to bridge virtual X to VNC
x11vnc -display :99 -nopw -forever -shared -bg

# Start noVNC (web client)
cd /opt/novnc
./utils/novnc_proxy --vnc localhost:5900 --listen 6080 &
echo "noVNC is running on http://localhost:6080"
export DISPLAY=:99