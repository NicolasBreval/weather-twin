FROM debian:stable

RUN apt update && \
    apt install -y iproute2 iputils-ping batctl net-tools kmod isc-dhcp-client && \
    apt clean && \
    rm -rf /var/lib/apt/lists/*

COPY ./entrypoint.sh .

ENTRYPOINT [ "/entrypoint.sh" ]