#!/bin/bash

COMPOSE_FILE="docker-compose.yml"

NODE1_IP="192.168.255.1"
NODE2_IP="192.168.255.2"
NODE1_NAME="batman-node1-1"
NODE2_NAME="batman-node2-1"

# Function to remove docker-compose deployment
cleanup() {
    echo ""
    echo "=================================================="
    echo "🧹 Cleaning Docker resources..."
    docker-compose -f "$COMPOSE_FILE" down --remove-orphans
    echo "✅ Cleanup completed."
    echo "=================================================="
}

# Configures the script to run cleanup on exit or interruption signal
trap cleanup EXIT INT TERM

echo "=================================================="
echo "🚀 Starting up BATMAN-based mesh net..."
docker-compose -f "$COMPOSE_FILE" up -d --build

echo "⌛ Waiting for initialization..."
sleep 20

if [ "$(docker ps -q -f status=running -f name=${NODE1_NAME})" ] && \
   [ "$(docker ps -q -f status=running -f name=${NODE2_NAME})" ]; then
    echo "✅ Nodes up and running"
else
    echo "❌ Some node couln't be started"
    exit 1
fi

echo "=================================================="
echo "🧐 Checking nodes connectivity..."

# Check ping works fine
docker exec -it "$NODE1_NAME" ping -c 5 -W 1 "$NODE2_IP"

# Obtains the result of last executed command
PING_STATUS=$?

if [ $PING_STATUS -eq 0 ]; then
    echo "✅ Ping test successfull, BATMAN works fine."
else
    echo "❌ Ping test failed, BATMAN connectivity error."
    exit 1
fi

echo "=================================================="
echo "🔬 Checking BATMAN status..."

MAC_NODE1=$(docker exec $NODE1_NAME ip link show eth0 | grep link/ether | awk '{print $2}')
MAC_NODE2=$(docker exec $NODE2_NAME ip link show eth0 | grep link/ether | awk '{print $2}')

echo $MAC_NODE1
echo $MAC_NODE2

echo "Checking neighbors in $NODE1_NAME..."

docker exec $NODE1_NAME batctl n | grep "$MAC_NODE2"
NEIGHBOR_STATUS=$?

if [ $NEIGHBOR_STATUS -eq 0 ]; then
    echo "✅ Check successfull."
else
    echo "❌ Error checking neighbors for node."
    exit 1
fi

echo "Checking neighbors in $NODE2_NAME..."
docker exec $NODE2_NAME batctl n | grep "$MAC_NODE1"
NEIGHBOR_STATUS=$?

if [ $NEIGHBOR_STATUS -eq 0 ]; then
    echo "✅ Check successfull."
else
    echo "❌ Error checking neighbors for node."
    exit 1
fi