# Workshop Instructor Guide

This guide provides additional information for the workshop.

## Pre-Workshop Setup

### 1. Test the Environment

```bash
# Build and start
docker-compose up -d

# Verify Fuseki is running
curl http://localhost:3030/$/ping

# Enter container and run tests
docker-compose exec workshop-java bash
mvn test
```

### 2. Prepare Materials

- [ ] Ensure all CIM files are in `assets/grid_model_cgmes/`
- [ ] Verify SHACL rules are in `assets/shacl_cgmes/`
- [ ] Test all 4 tasks with solution files
- [ ] Prepare presentation slides (optional)

### 3. Environment Check

- [ ] Docker and Docker Compose installed
- [ ] Ports 3030 available
- [ ] Sufficient disk space (2GB+)

---

## Workshop Timeline (2h 30min - Balanced Approach)

**Total: 2 hours 30 minutes** (with built-in buffer time)

### Introduction (10 minutes)

- Quick workshop overview
- CIM/CGMES essentials
- Apache Jena introduction
- Setup verification

### Task 1: Data Import (10 minutes)

- **Approach:** This is a **working example** - quick code walkthrough.

- **Teaching Points (briefly cover):**
  - RDF/XML structure & named graphs concept
  - Graph Store Protocol & Model IDs (mRIDs)
  - How the import process works

- **Common Issues:**
  - Fuseki not accessible → Check Docker
  - File path issues → Use absolute paths

### Task 2: Data Export (25 minutes)

- **Implement ONLY 2 methods:**
  - `listAllGraphs()` - SPARQL basics (10 min)
  - `downloadFromFuseki()` - HTTP GET + RDF parsing (15 min)

- **Teaching Points:**
  - Graph Store Protocol for downloading
  - SPARQL syntax basics
  - Building on Task 1 concepts

- **Common Issues:**
  - HTTP connection errors
  - Query syntax
  - Variable naming in SPARQL

- **What to skip:** `exportGraphToFile()`, `exportAllGraphs()`, `extractProfileType()`, and all profile methods → Instructor demos OR homework

- **Note:** Tests are written to work with just these 2 methods implemented

### Short Break (5 minutes)

### Task 3: SPARQL Queries (40 minutes - 4 Queries Hands-On)

- **All 4 queries are hands-on:**
  - Query 1: Simple pattern matching (8 min)
  - Query 2: Joins between types (10 min)
  - Query 3: FILTER NOT EXISTS (10 min)
  - Query 4: Complex connectivity path (12 min)

- **Teaching Points:**
  - Named graphs concept
  - Graph Store Protocol
  - Model IDs (mRIDs)

- **Common Issues:**
  - Fuseki not accessible → Check Docker
  - File path issues → Use absolute paths
  - HTTP errors → Check Fuseki logs

- **Tip:** This task is mostly a working example. Let participants study the code.

### Task 2: Data Export (30 minutes)

- **Teaching Points:**
  - Graph Store Protocol for downloading
  - CGMES profile types (EQ, TP, SSH, SV)
  - Profile metadata extraction
  - Organizing exported data

- **Common Issues:**
  - HTTP connection errors
  - Profile type extraction
  - File naming conventions

### Short Break (5 minutes)

### Task 3: SPARQL Queries (45 minutes)

- **Teaching Points:**
  - SPARQL syntax basics
  - Triple patterns
  - FILTER and OPTIONAL
  - Finding errors with queries

- **Common Issues:**
  - Namespace prefixes
  - Query syntax errors
  - Understanding graph patterns

- **Demo Queries:**

  ```sparql
  # Show all classes
  SELECT DISTINCT ?type
  WHERE { ?s rdf:type ?type }
  LIMIT 20
  ```

- **Pacing:**

### Task 4: SHACL Validation (45 minutes)

- **Teaching Points:**
  - SHACL basics
  - Shapes and constraints
  - Validation reports
  - ENTSO-E rules

- **Common Issues:**
  - Loading multiple files
  - Understanding violations
  - Interpreting reports

- **Discussion Points:**
  - Why validation matters
  - Data quality in power systems
  - Real-world error examples

### Task 5: Custom SHACL - OPTIONAL (30 minutes)

**BONUS MATERIAL**

- **Teaching Points:**
  - Creating shapes programmatically
  - SHACL-SPARQL connection
  - When to use SHACL vs SPARQL
  - Best practices

- **Common Issues:**
  - Jena API usage
  - Property paths
  - SPARQL in SHACL syntax

- **Note:** This is advanced material. Participants can complete it after the workshop.

### Wrap-up (10 minutes)

- Review key concepts (Tasks 1-4)
- Q&A session
- Mention Task 5 as self-study option
- Next steps
- Feedback collection

---

## Troubleshooting for Instructors

### Fuseki Issues

```bash
# Check if running
docker ps | grep fuseki

# View logs
docker-compose logs fuseki -f

# Restart
docker-compose restart fuseki
```

### Java/Maven Issues

```bash
# Rebuild
docker-compose build workshop-java

# Clear Maven cache
docker-compose exec workshop-java mvn clean

# Check Java version
docker-compose exec workshop-java java -version
```

### Data Issues

```bash
# Check data volume
docker volume ls
docker volume inspect cim-technical-workshop_fuseki-data

# Clear all data
docker-compose down -v
docker-compose up -d
```

---

## Extensions & Advanced Topics

### For Fast Learners

1. **Advanced SPARQL:**
   - Aggregation (COUNT, SUM, AVG)
   - CONSTRUCT queries
   - Federated queries

2. **Performance:**
   - Query optimization
   - Indexing strategies
   - TDB2 configuration

3. **Integration:**
   - REST API design
   - Batch processing
   - Real-time validation

### Additional Exercises

1. Create a custom SHACL profile
2. Build a validation dashboard
3. Implement incremental updates
4. Design a CIM data pipeline

---

## Resources for Instructors

### CIM/CGMES

- ENTSO-E CGMES documentation
- IEC 61970 standards
- CIM UML models

### Apache Jena

- Jena Fuseki documentation
- SPARQL best practices
- SHACL implementation guide

### Teaching Materials

- Sample presentations
- Exercise solutions
- Real-world examples

---

**Good luck with the workshop!**

Remember: The goal is learning, not perfection. Encourage experimentation and questions!
