# BATMAN (Better Approach To Mobile Adhoc Networking)
BATMAN is a mesh protocol used to communicate devices throught the second layer (ethernet), and is a good solution to intercommunicate all elements in the Weather-Twin project, becuse is a descentralized communication.

This folder contains a test, using docker-compose, of two nodes connected using BATMAN.

## Run test
To check the BATMAN protocol is correct, first, you need to ensure the batman module is enabled:

```bash
modprobe batman-adv
lsmod | grep batman
```

The last command should return something like this:

```
batman_adv            344064  0
cfg80211             1388544  1 batman_adv
crc16                  12288  1 batman_adv
bridge                454656  1 batman_adv
libcrc32c              12288  5 nf_conntrack,nf_nat,batman_adv,btrfs,nf_tables
```

Normally, a docker container shares the Linux kernet with the host, but, in this case, the host modules are not shared, so, the containers needs some special configurations:

```yml
services:
    nodeX:
    # Some configurations...
    privileged: true
    volumes:
        - /lib/modules:/lib/modules:ro
        - /sys:/sys:ro
```

* **privileged**: Allows container to access some capabilities, like mount file systems, modify host kernel or interact directly with hardware.
* **volumes**: The privileged mode is not sufficient to interact with BATMAN modules, so it's required to map host modules to container's modules folder.

To start the test, simply run the docker-compose command:

```bash
docker-compose up -d --build
```

Then, to check if there are any communication, first, run the command "batctl o" on each node:

```bash
# Command run in node1
docker exec batman-node1-1 batctl o
```

    [B.A.T.M.A.N. adv 2024.2, MainIF/MAC: eth0/16:36:e5:76:de:c2 (bat0/42:35:19:0b:1a:22 BATMAN_IV)]
       Originator        last-seen (#/255) Nexthop           [outgoingIF]
     * 36:1b:49:3b:52:50    0.747s   (255) 36:1b:49:3b:52:50 [      eth0]

```bash
# Command run in node2
docker exec batman-node2-1 batctl o
```

    [B.A.T.M.A.N. adv 2024.2, MainIF/MAC: eth0/36:1b:49:3b:52:50 (bat0/fa:4e:fc:9a:a3:54 BATMAN_IV)]
       Originator        last-seen (#/255) Nexthop           [outgoingIF]
     * 16:36:e5:76:de:c2    0.504s   (255) 16:36:e5:76:de:c2 [      eth0]

To check if the BATMAN protocol works correctly, the MAC addresses must match between command results. In the previous commands, the first node has the MAC address *16:36:e5:76:de:c2* (MainIF/MAC), and shows the remote address (Originator) *36:1b:49:3b:52:50*. On the other hand, the second node has the MAC *36:1b:49:3b:52:50*, which is the same as node1 remote MAC address, and shows a remote address the remote addess *16:36:e5:76:de:c2*, which is the same as node1 main MAC address. So, the protocol is work successfully.

If the previous check works, the last check is a simply ping command, it's required to check if the first node can ping to second one, and vice-versa.

```bash
# Command run in node1
docker exec batman-node1-1 ping -c 3 192.168.255.2
```

    PING 192.168.255.2 (192.168.255.2) 56(84) bytes of data.
    64 bytes from 192.168.255.2: icmp_seq=1 ttl=64 time=0.253 ms
    64 bytes from 192.168.255.2: icmp_seq=2 ttl=64 time=0.078 ms
    64 bytes from 192.168.255.2: icmp_seq=3 ttl=64 time=0.085 ms

    --- 192.168.255.2 ping statistics ---
    3 packets transmitted, 3 received, 0% packet loss, time 2015ms
    rtt min/avg/max/mdev = 0.078/0.138/0.253/0.080 ms

```bash
# Command run in node2
docker exec batman-node1-1 ping -c 3 192.168.255.1
```

    PING 192.168.255.1 (192.168.255.1) 56(84) bytes of data.
    64 bytes from 192.168.255.1: icmp_seq=1 ttl=64 time=0.080 ms
    64 bytes from 192.168.255.1: icmp_seq=2 ttl=64 time=0.080 ms
    64 bytes from 192.168.255.1: icmp_seq=3 ttl=64 time=0.076 ms

    --- 192.168.255.1 ping statistics ---
    3 packets transmitted, 3 received, 0% packet loss, time 2037ms
    rtt min/avg/max/mdev = 0.076/0.078/0.080/0.002 m