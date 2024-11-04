#!/bin/bash

# Check and delete existing files
if [ -f "src/main/resources/private-key.pem" ]; then
  rm "src/main/resources/private-key.pem"
fi

if [ -f "src/main/resources/public-key.pem" ]; then
  rm "src/main/resources/public-key.pem"
fi

# Generate new keys
openssl genpkey -algorithm RSA -out "src/main/resources/private-key.pem"
openssl rsa -pubout -in "src/main/resources/private-key.pem" -out "src/main/resources/public-key.pem"