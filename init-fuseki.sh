#!/bin/bash

# Initialize Fuseki with required datasets
# This script creates the cim_workshop dataset on Fuseki startup

echo "Waiting for Fuseki to be ready..."
until curl -sf http://fuseki:3030/$/ping > /dev/null 2>&1; do
    sleep 2
done

echo "Fuseki is ready. Creating cim_workshop dataset..."

# Create the dataset
curl -X POST "http://fuseki:3030/$/datasets" \
    -u admin:admin \
    -H "Content-Type: application/x-www-form-urlencoded" \
    -d "dbName=cim_workshop&dbType=tdb2" \
    > /dev/null 2>&1

if [ $? -eq 0 ]; then
    echo "✓ Dataset 'cim_workshop' created successfully"
else
    echo "⚠ Dataset creation may have failed (it might already exist)"
fi

# Verify dataset exists
if curl -sf "http://fuseki:3030/cim_workshop/query?query=ASK+%7B%3Fs+%3Fp+%3Fo%7D" > /dev/null 2>&1; then
    echo "✓ Dataset 'cim_workshop' is accessible"
else
    echo "✗ Dataset 'cim_workshop' is not accessible"
    exit 1
fi

echo "Fuseki initialization complete!"