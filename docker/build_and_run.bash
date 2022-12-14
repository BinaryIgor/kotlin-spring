#!/bin/bash

docker build . -t kotlin-postgres-db

docker rm kotlin-postgres-db

exec docker run -p "5678:5432" --name kotlin-postgres-db kotlin-postgres-db