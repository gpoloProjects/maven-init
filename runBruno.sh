#!/bin/bash

# Check if all three arguments are provided
if [ "$#" -ne 3 ]; then
    echo "Usage: $0 <env> <host> <port>"
    echo "Example: $0 local localhost 8080"
    exit 1
fi

# Get arguments
ENV="$1"
HOST="$2"
PORT="$3"

echo "My script is running"
echo "ENV: $ENV"
echo "HOST: $HOST"
echo "PORT: $PORT"
