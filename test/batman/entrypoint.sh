#!/bin/bash

set -e  # Exit on error

echo "=== Starting Advanced BATMAN Configuration ==="

# 1. Load batman-adv module
echo "Loading batman-adv module..."
modprobe batman-adv || {
    echo "ERROR: Could not load batman-adv"
    exit 1
}

sleep 1

# 2. Bring up eth0 FIRST
echo "Flushing IP address from eth0 to force BATMAN routing..."
ip address flush dev eth0

# 3. Bring up eth0
echo "Bringing up interface eth0..."
ip link set dev eth0 up

# 4. Add eth0 to batman (this automatically creates bat0)
echo "Adding eth0 to BATMAN..."
batctl if add eth0 || {
    echo "ERROR: Could not add eth0 to batman"
    exit 1
}

# 5. Wait for batman to stabilize
echo "Waiting for batman stabilization..."
sleep 5

# 6. Configure IP on bat0
echo "Configuring IP on bat0: $IP_ADDR/24"
ip addr add $IP_ADDR/24 dev bat0 || {
    echo "ERROR: Could not assign IP to bat0"
    exit 1
}

# 7. Bring up bat0
echo "Bringing up interface bat0..."
ip link set bat0 up

# 8. Show status
echo ""
echo "=== BATMAN Status ==="
echo "Interface:"
batctl if
echo ""
echo "Network configuration:"
ip addr show eth0 # eth0 should now have no IP
ip addr show bat0
echo ""
echo "Detected neighbors:"
batctl n
echo ""
echo "=== Mesh network ready at $IP_ADDR ==="
echo ""

# --- KEEP THE CONTAINER ALIVE ---
sleep infinity