#!/bin/sh
CERT_DIR="/etc/rabbitmq/certs"
chown -R rabbitmq:rabbitmq "${CERT_DIR}"
chmod 600 "${CERT_DIR}/server_key.pem"
chmod 644 "${CERT_DIR}/ca_certificate.pem"
chmod 644 "${CERT_DIR}/server_certificate.pem"
chmod 755 "${CERT_DIR}"
echo "$@"
exec docker-entrypoint.sh rabbitmq-server