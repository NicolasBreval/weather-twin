# weather-twin
Personal project to collect and exploit meteorological data and make comparisons between indoors and outdoors.

## Requirements

### RabbitMQ certificates
Before to initialize docker-based infrastructure using docker-compose, you must to create some certificates to enable TLS in RabbitMQ server. To create it, you can use the task "create-rabbitmq-server-certs", or "rmq-certs". When you initializes the docker-compose infrastructure, RabbitMQ container changes permissions over server certificates files to allow server to use it on configuration.