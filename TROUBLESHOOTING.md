# Troubleshooting Guide

## Issue: Tests Can't Connect to Fuseki

### Symptoms
```
java.lang.AssertionError: Could not connect to Fuseki.
Make sure it's running with: docker-compose up -d
```

### Root Cause
The `cim_workshop` dataset doesn't exist in Fuseki, causing connection tests to fail.

### Solution
The dataset is now automatically created on startup via `init-fuseki.sh`. If you still encounter this issue:

#### Option 1: Restart Containers (Recommended)
```bash
docker-compose down
docker-compose up -d
```

The init script will automatically create the dataset.

#### Option 2: Manual Creation
```bash
# Create the dataset manually
curl -X POST "http://localhost:3030/$/datasets" \
    -u admin:admin \
    -H "Content-Type: application/x-www-form-urlencoded" \
    -d "dbName=cim_workshop&dbType=tdb2"

# Verify it was created
curl -s "http://localhost:3030/cim_workshop/query?query=ASK+%7B%3Fs+%3Fp+%3Fo%7D"
```

#### Option 3: Use Setup Script
```bash
./setup.sh
```

---

## Issue: "Connection refused" to Fuseki

### Symptoms
```
Connection refused: http://fuseki:3030
```

### Diagnosis
```bash
# Check if containers are running
docker ps

# Check Fuseki logs
docker-compose logs fuseki

# Test connectivity from workshop container
docker exec cim-workshop curl http://fuseki:3030/$/ping
```

### Possible Causes & Solutions

#### 1. Fuseki Container Not Running
```bash
docker-compose up -d fuseki
```

#### 2. Wrong FUSEKI_URL
Check environment variable:
```bash
docker exec cim-workshop env | grep FUSEKI_URL
```

Should be: `FUSEKI_URL=http://fuseki:3030` (not `localhost`)

#### 3. Network Issues
```bash
# Check network exists
docker network ls | grep cim-network

# Recreate network
docker-compose down
docker-compose up -d
```

---

## Issue: Dataset Already Exists Error

### Symptoms
```
Dataset 'cim_workshop' already exists
```

### Solution
This is usually harmless. The dataset persists in the `fuseki-data` volume.

To start fresh:
```bash
# Remove volumes
docker-compose down -v

# Restart
docker-compose up -d
```

---

## Issue: 404 Not Found for Dataset

### Symptoms
```
HTTP ERROR 404 Not Found
URI: http://fuseki:3030/cim_workshop/query
```

### Solution
The dataset doesn't exist. Create it:

```bash
# From host
curl -X POST "http://localhost:3030/$/datasets" \
    -u admin:admin \
    -H "Content-Type: application/x-www-form-urlencoded" \
    -d "dbName=cim_workshop&dbType=tdb2"

# Or restart containers (automatic creation)
docker-compose restart workshop-java
```

---

## Issue: Permission Denied

### Symptoms
```
Permission denied: /workspace/output
```

### Solution
```bash
# Create output directory with correct permissions
mkdir -p output
chmod 777 output

# Or restart containers
docker-compose down
docker-compose up -d
```

---

## Issue: Maven Can't Find Dependencies

### Symptoms
```
Could not resolve dependencies
```

### Solution
```bash
# Clear Maven cache
docker-compose down
docker volume rm cim-technical-workshop_maven-cache
docker-compose up -d

# Or inside container
docker exec cim-workshop mvn clean install -U
```

---

## Issue: Tests Timeout

### Symptoms
```
Tests timed out after 20 seconds
```

### Possible Causes

#### 1. Fuseki Not Ready
Wait longer for Fuseki to fully start:
```bash
# Check if Fuseki is ready
curl http://localhost:3030/$/ping
```

#### 2. Large Dataset Import
First imports may take time. This is normal.

#### 3. Network Latency
Check Docker network performance:
```bash
docker exec cim-workshop ping fuseki
```

---

## Issue: Files Not Visible in Container

### Symptoms
```
FileNotFoundException: assets/grid_model_cgmes/...
```

### Solution
Check volume mounts:
```bash
# Check if files are mounted
docker exec cim-workshop ls -la /workspace/assets

# If empty, check docker-compose.yml volumes section
# Should have: - ./assets:/workspace/assets:ro
```

---

## Debugging Commands

### Check Container Status
```bash
docker ps
docker-compose ps
```

### View Logs
```bash
# All services
docker-compose logs

# Specific service
docker-compose logs fuseki
docker-compose logs workshop-java

# Follow logs
docker-compose logs -f
```

### Enter Container
```bash
# Workshop container
docker exec -it cim-workshop bash

# Fuseki container
docker exec -it cim-fuseki bash
```

### Test Connectivity
```bash
# From host to Fuseki
curl http://localhost:3030/$/ping

# From workshop container to Fuseki
docker exec cim-workshop curl http://fuseki:3030/$/ping

# Check dataset
curl http://localhost:3030/$/server | grep cim_workshop
```

### Check Environment Variables
```bash
docker exec cim-workshop env | grep FUSEKI
```

### Verify Files
```bash
# List source files
docker exec cim-workshop ls -la /workspace/src

# List assets
docker exec cim-workshop ls -la /workspace/assets

# Check if Maven can find pom.xml
docker exec cim-workshop cat /workspace/pom.xml | head -10
```

---

## Complete Reset

If all else fails, completely reset the environment:

```bash
# Stop and remove everything
docker-compose down -v

# Remove images
docker-compose down --rmi all

# Clean Docker system (optional, affects other projects too)
# docker system prune -a --volumes

# Rebuild from scratch
docker-compose build --no-cache
docker-compose up -d

# Wait for services to start
sleep 10

# Verify
./setup.sh
```

---

## Getting Help

If issues persist:

1. **Check logs**:
   ```bash
   docker-compose logs > logs.txt
   ```

2. **Verify setup**:
   ```bash
   docker --version
   docker-compose --version
   docker ps
   docker network ls
   ```

3. **Test manually**:
   ```bash
   # Enter container
   docker exec -it cim-workshop bash

   # Inside container
   cd /workspace
   mvn clean compile
   curl http://fuseki:3030/$/ping
   ```

4. **Check documentation**:
   - README.md - Main workshop guide
   - DOCKER_FIXES.md - Docker configuration details
   - QUICK_REFERENCE.md - Command reference

---

## Common Gotchas

### 1. localhost vs service name
❌ `http://localhost:3030` - Only works from host
✅ `http://fuseki:3030` - Works from other containers

### 2. Dataset type
Use `tdb2` for persistence, not `mem`:
```bash
dbType=tdb2  # ✅ Recommended
dbType=mem   # ⚠️ Lost on restart
```

### 3. Authentication
Fuseki admin operations require authentication:
```bash
curl -u admin:admin ...  # ✅ Correct
curl ...                  # ❌ 401 Unauthorized
```

### 4. Volume paths
Must be absolute or relative to docker-compose.yml:
```yaml
- ./src:/workspace/src        # ✅ Correct
- src:/workspace/src          # ❌ May fail
```

---

## Prevention

To avoid these issues in the future:

1. **Always use `./setup.sh`** for initial setup
2. **Use `docker-compose down -v`** to clean slate
3. **Check logs** if something doesn't work
4. **Wait for services** to fully start before testing
5. **Use the init script** (runs automatically on startup)

---

## Quick Checks

Before asking for help, verify:

- [ ] Docker and Docker Compose installed
- [ ] Containers are running (`docker ps`)
- [ ] Fuseki accessible from host (`curl http://localhost:3030/$/ping`)
- [ ] Dataset exists (`curl http://localhost:3030/$/server`)
- [ ] Environment variable set (`docker exec cim-workshop env | grep FUSEKI`)
- [ ] Files mounted (`docker exec cim-workshop ls /workspace/src`)
- [ ] Tests compile (`docker exec cim-workshop mvn compile`)

If all checks pass but tests still fail, check the specific test output for clues.
