#!/bin/bash
set -e

docker run --name server-db -e POSTGRES_PASSWORD=tempPassword -e POSTGRES_USER=server -e POSTGRES_DB=server -v ./docker-entrypoint-initdb.d:/docker-entrypoint-initdb.d postgres:alpine3.19