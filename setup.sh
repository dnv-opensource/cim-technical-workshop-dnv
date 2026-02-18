#!/bin/bash

# CIM Technical Workshop Setup Script
# This script helps set up and verify the workshop environment

set -e

echo "╔═══════════════════════════════════════════════════════════╗"
echo "║                                                           ║"
echo "║         CIM/CGMES Technical Workshop Setup                ║"
echo "║                                                           ║"
echo "╚═══════════════════════════════════════════════════════════╝"
echo ""

# Color codes
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

# Check if Docker is installed
echo -n "Checking Docker installation... "
if command -v docker &> /dev/null; then
    echo -e "${GREEN}✓${NC}"
else
    echo -e "${RED}✗${NC}"
    echo "Docker is not installed. Please install Docker first."
    echo "Visit: https://docs.docker.com/get-docker/"
    exit 1
fi

# Check if Docker Compose is installed
echo -n "Checking Docker Compose installation... "
if command -v docker-compose &> /dev/null; then
    echo -e "${GREEN}✓${NC}"
else
    echo -e "${RED}✗${NC}"
    echo "Docker Compose is not installed. Please install Docker Compose first."
    echo "Visit: https://docs.docker.com/compose/install/"
    exit 1
fi

# Check if Docker is running
echo -n "Checking if Docker is running... "
if docker info &> /dev/null; then
    echo -e "${GREEN}✓${NC}"
else
    echo -e "${RED}✗${NC}"
    echo "Docker is not running. Please start Docker first."
    exit 1
fi

# Check if port 3030 is available
echo -n "Checking if port 3030 is available... "
if lsof -Pi :3030 -sTCP:LISTEN -t &> /dev/null; then
    echo -e "${YELLOW}⚠${NC}"
    echo "Port 3030 is already in use. Please stop the service using it."
    echo "You can find the process with: lsof -i :3030"
    exit 1
else
    echo -e "${GREEN}✓${NC}"
fi

# Check if assets directory exists
echo -n "Checking assets directory... "
if [ -d "assets/grid_model_cgmes" ] && [ -d "assets/shacl_cgmes" ]; then
    echo -e "${GREEN}✓${NC}"
else
    echo -e "${YELLOW}⚠${NC}"
    echo "Assets directory structure incomplete."
    echo "Expected: assets/grid_model_cgmes/ and assets/shacl_cgmes/"
fi

echo ""
echo "═══════════════════════════════════════════════════════════"
echo "Starting workshop environment..."
echo "═══════════════════════════════════════════════════════════"
echo ""

# Start Docker Compose
echo "Building and starting containers..."
docker-compose up -d

echo ""
echo "Waiting for Fuseki to start..."
sleep 5

# Check Fuseki health
echo -n "Checking Fuseki health... "
MAX_RETRIES=30
RETRY_COUNT=0

while [ $RETRY_COUNT -lt $MAX_RETRIES ]; do
    if curl -s http://localhost:3030/$/ping &> /dev/null; then
        echo -e "${GREEN}✓${NC}"
        break
    fi
    RETRY_COUNT=$((RETRY_COUNT + 1))
    sleep 2
    echo -n "."
done

if [ $RETRY_COUNT -eq $MAX_RETRIES ]; then
    echo -e "${RED}✗${NC}"
    echo "Fuseki failed to start. Check logs with: docker-compose logs fuseki"
    exit 1
fi

# Create dataset in Fuseki
echo -n "Creating workshop dataset... "
if curl -s -X POST http://localhost:3030/$/datasets \
    -u admin:admin \
    -H "Content-Type: application/x-www-form-urlencoded" \
    -d "dbName=cim_workshop&dbType=tdb2" &> /dev/null; then
    echo -e "${GREEN}✓${NC}"
else
    echo -e "${YELLOW}⚠${NC} (Dataset may already exist)"
fi

# Verify dataset is accessible
echo -n "Verifying dataset accessibility... "
if curl -sf "http://localhost:3030/cim_workshop/query?query=ASK+%7B%3Fs+%3Fp+%3Fo%7D" &> /dev/null; then
    echo -e "${GREEN}✓${NC}"
else
    echo -e "${RED}✗${NC}"
    echo "Dataset verification failed. Check Fuseki logs: docker-compose logs fuseki"
fi

echo ""
echo "═══════════════════════════════════════════════════════════"
echo "Environment ready!"
echo "═══════════════════════════════════════════════════════════"
echo ""
echo "Next steps:"
echo ""
echo "1. Enter the workshop container:"
echo -e "   ${GREEN}docker-compose exec workshop-java bash${NC}"
echo ""
echo "2. Start with Task 1:"
echo -e "   ${GREEN}mvn test -Dtest=Task1Test${NC}"
echo ""
echo "3. Access Fuseki web UI:"
echo -e "   ${GREEN}http://localhost:3030${NC}"
echo "   Username: admin, Password: admin"
echo ""
echo "For help, see README.md"
echo ""
