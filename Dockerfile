# Dockerfile for mcp 1.8.8 GUI dev with VNC
FROM docker-registry.wikimedia.org/openjdk-8-jdk:8.462-1-20251123

ENV DEBIAN_FRONTEND=noninteractive

# Install required packages
RUN apt update && \
    apt install -y --no-install-recommends \
        python2.7 python2.7-dev \
        x11-apps mesa-utils \
        libgl1-mesa-dri libgl1-mesa-glx libglu1-mesa \
        libxext6 libxrender1 libxtst6 libxi6 libxrandr2 \
        x11vnc xvfb wget sudo unzip git patch scala wine \
        x11-xserver-utils && \
    wget -qO- https://github.com/novnc/noVNC/archive/refs/tags/v1.4.0.tar.gz | tar xz -C /opt && \
    ln -s /opt/noVNC-1.4.0 /opt/novnc && \
    wget -qO- https://github.com/novnc/websockify/archive/refs/tags/v0.10.0.tar.gz | tar xz -C /opt && \
    ln -s /opt/websockify-0.10.0 /opt/novnc/utils/websockify && \
    ln -sf /usr/bin/python2.7 /usr/bin/python && \
    rm -rf /var/lib/apt/lists/*

# Create non-root user
RUN useradd -ms /bin/bash mcp && echo "mcp ALL=(ALL) NOPASSWD:ALL" >> /etc/sudoers
USER mcp
WORKDIR /home/mcp
RUN mkdir -p /home/mcp/.minecraft/versions/1.7.10/
RUN mkdir -p /home/mcp/.minecraft/libraries/
COPY jars/ /home/mcp/.minecraft/
COPY dockerVNC.sh /home/mcp/dockerVNC.sh
RUN sudo chmod +x /home/mcp/dockerVNC.sh

CMD ["/bin/bash"]
