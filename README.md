# CIM/CGMES Technical Workshop

An interactive, hands-on workshop for learning how to work with CIM (Common Information Model) and CGMES (Common Grid Model Exchange Standard) files using Apache Jena, SPARQL, and SHACL.

## Workshop Overview

This workshop teaches power system developers how to:

- Import and manage CIM/CGMES files in graph databases
- Query power grid data using SPARQL
- Validate data quality using SHACL
- Create custom validation rules

## Workshop Structure

**Total Duration:** 2 hours 30 minutes

- Introduction: 8 minutes
- Task 1: Data Import (15 min)
- Task 2: Data Export (30 min)
- Break: 5 minutes
- Task 3: SPARQL Queries (45 min)
- Break: 5 minutes
- Task 4: SHACL Validation (30 min)
- Wrap-up: 7 minutes
- Buffer: 15 minutes

### Task 1: Import CIM Files into Graph Database (15 min)

**Scaffolded hands-on** - Implement 3 methods with provided structure and hints. One complete method provided as reference.

### Task 2: Export CIM Data (30 min)

**Scaffolded hands-on** - Implement 2 core methods for listing graphs and downloading from Fuseki. Optional bonus methods available.

### Task 3: Query with SPARQL (45 min)

**Scaffolded hands-on** - Write 4 SPARQL queries with progressive difficulty. Queries scaffolded from simple pattern filling to complex multi-hop paths.

### Task 4: Validate with SHACL (30 min)

**Scaffolded hands-on** - Implement 4 core validation methods with structured guidance. Focus on loading shapes and parsing validation reports.

### Task 5: Create Custom SHACL Rules (Optional)

**BONUS MATERIAL** - Advanced content for self-study after the workshop. Not included in the 2h 30min timeline.

---

## Prerequisites

### Required Software

- **Docker** and **Docker Compose** (to run Fuseki and Java environment)
- **Git** (to clone the repository)

### Recommended Knowledge

- Basic understanding of:
  - Power system concepts (optional but helpful)
  - RDF/XML format basics
  - Command line usage

**Note:** No Java installation needed on your machine - everything runs in Docker!

---

## Quick Start

### 1. Clone the Repository

```bash
git clone <repository-url>
cd cim-technical-workshop
```

### 2. Start the Workshop Environment

```bash
docker-compose up -d
```

This will start:

- **Apache Jena Fuseki** (graph database) on `http://localhost:3030`
- **Java workshop container** with all dependencies

### 3. Enter the Workshop Container

```bash
docker-compose exec workshop-java bash
```

You're now in the workshop environment!

### 4. Start with Task 1

```bash
# View the workshop menu
mvn exec:java -Dexec.mainClass="com.cim.workshop.WorkshopRunner"

# Start Task 1
mvn test -Dtest=Task1Test
```

---

## Workshop Tasks

### Task 1: Import CIM Files into Graph Database (15 min)

**Scaffolded Exercise:**

- One complete method provided: `extractModelId()` - study this to learn Jena API
- Implement 3 methods with structure and hints provided

**File to complete:** `src/main/java/com/cim/workshop/exercises/Task1_DataImport.java`

**What you'll learn:**

- How CIM files are structured (RDF/XML)
- Extracting Model IDs (mRIDs) from CIM files
- Loading files into Apache Jena models
- Uploading to Fuseki as named graphs

**Run tests:**

```bash
mvn test -Dtest=Task1Test
```

**Expected output:**

- ✓ Extract Model ID from CIM file
- ✓ Load CIM file into Jena Model
- ✓ Upload model to Fuseki
- ✓ Import entire directory

---

### Task 2: Export CIM Data (30 min)

**Scaffolded Exercise:**

- Implement 2 core methods with query and HTTP structures provided
- 6 optional bonus methods available for extra practice

**File to complete:** `src/main/java/com/cim/workshop/exercises/Task2_DataExport.java`

**What you'll learn:**

- Listing named graphs with SPARQL
- Downloading graphs via HTTP GET
- Graph Store Protocol for data export
- Working with RDF/XML format

**Run tests:**

```bash
mvn test -Dtest=Task2Test
```

**Expected output:**

- ✓ List all named graphs
- ✓ Download graph from Fuseki
- Optional bonus methods for advanced export features

---

### Task 3: Query Datasets with SPARQL (45 min)

**Scaffolded Exercise:**

- Write 4 SPARQL queries with progressive difficulty
- Query 1: Fill in 2 triple patterns
- Query 2: Write complete WHERE clause with hints
- Query 3: Apply FILTER NOT EXISTS pattern
- Query 4: Complex multi-hop connectivity path

**File to complete:** `src/main/java/com/cim/workshop/exercises/Task3_SparqlQueries.java`

**What you'll learn:**

- SPARQL query language basics
- Finding resources by type
- Following relationships in the graph
- Identifying data errors with queries

**Key Concepts:**

- `SELECT` queries to retrieve data
- Triple patterns for graph navigation
- `FILTER NOT EXISTS` to find missing data
- Multi-hop paths through connectivity nodes

**Run tests:**

```bash
mvn test -Dtest=Task3Test
```

**Expected output:**

- ✓ Query all ACLineSegments
- ✓ Find terminals for ACLineSegments
- ✓ Identify ACLineSegments without terminals
- ✓ Find connected switches

---

### Task 4: Validate with SHACL (30 min)

**Scaffolded Exercise:**

- Implement 4 core validation methods with structured guidance
- Load SHACL shapes, run validation, parse results
- Optional bonus methods for advanced validation features

**File to complete:** `src/main/java/com/cim/workshop/exercises/Task4_ShaclValidation.java`

**What you'll learn:**

- SHACL (Shapes Constraint Language) basics
- Loading SHACL validation rules
- Running validation against CIM data
- Analyzing validation reports

**Key Concepts:**

- SHACL shapes define constraints
- Validation produces reports with violations
- ENTSO-E provides official CGMES SHACL rules
- Violations indicate data quality issues

**Run tests:**

```bash
mvn test -Dtest=Task4Test
```

**Expected output:**

- ✓ Load SHACL shapes
- ✓ Validate CIM data
- ✓ Extract violation details
- ✓ Parse validation reports

---

### Task 5: Create Custom SHACL Rules (Optional)

**BONUS MATERIAL** - Self-study content, not included in 2h 30min workshop

**File to complete:** `src/main/java/com/cim/workshop/exercises/Task5_CustomShacl.java`

**What you'll learn:**

- Creating SHACL shapes programmatically
- Building property constraints
- SHACL-SPARQL constraints
- Connection between SHACL and SPARQL

**Key Concepts:**

- `sh:NodeShape` - defines a shape
- `sh:targetClass` - specifies target class
- `sh:property` - defines property constraints
- `sh:sparql` - SPARQL-based validation

**Run tests:**

```bash
mvn test -Dtest=Task5Test
```

**Expected output:**

- ✓ Create basic SHACL shape
- ✓ Create connectivity validation
- ✓ Create SHACL-SPARQL constraint
- ✓ Compare SHACL and SPARQL

---

## Workshop Data

### CIM Files

Located in `assets/grid_model_cgmes/`:

- **PowerFlow/** - Small dataset for testing
- **FullGrid/** - Complete grid model

### SHACL Rules

Located in `assets/shacl_cgmes/`:

- ENTSO-E official CGMES validation rules
- Multiple profiles (Equipment, Topology, StateVariables, etc.)

---

## Useful Commands

### Run All Tests

```bash
mvn test
```

### Run Specific Task

```bash
mvn test -Dtest=Task1Test
mvn test -Dtest=Task2Test
mvn test -Dtest=Task3Test
mvn test -Dtest=Task4Test
```

### Compile Project

```bash
mvn compile
```

### Clean Build

```bash
mvn clean compile
```

### Access Fuseki Web UI

Open browser: `http://localhost:3030`

- Username: `admin`
- Password: `admin`

---

## Troubleshooting

### Fuseki Connection Error

```bash
# Restart Fuseki
docker-compose restart fuseki

# Check Fuseki logs
docker-compose logs fuseki
```

### Maven Build Issues

```bash
# Clean and rebuild
mvn clean install

# Update dependencies
mvn dependency:resolve
```

### Container Issues

```bash
# Restart all services
docker-compose down
docker-compose up -d

# View logs
docker-compose logs
```

---

## Architecture

```
┌─────────────────────────────────────────────────────────┐
│                   Workshop Container                     │
│  ┌────────────────────────────────────────────────┐    │
│  │           Java Application (Maven)              │    │
│  │  - Exercise files (Task1-4)                    │    │
│  │  - Test suites                                  │    │
│  │  - Apache Jena libraries                       │    │
│  └────────────────────────────────────────────────┘    │
│                         │                               │
│                         ↓                               │
│  ┌────────────────────────────────────────────────┐    │
│  │        Apache Jena Fuseki (Graph DB)           │    │
│  │  - Stores RDF graphs                           │    │
│  │  - SPARQL endpoint                             │    │
│  │  - Web UI (port 3030)                          │    │
│  └────────────────────────────────────────────────┘    │
└─────────────────────────────────────────────────────────┘
                         │
                         ↓
              ┌──────────────────────┐
              │    CIM/CGMES Files    │
              │    SHACL Rules        │
              │  (assets directory)   │
              └──────────────────────┘
```

---

## How Tests Work

Each task has a test file that:

1. **Checks if you've implemented methods** - throws error if method not implemented
2. **Validates your implementation** - tests correctness with assertions
3. **Provides feedback** - clear success/failure messages
4. **Guides to next step** - tells you what to do next

### Test-Driven Learning Flow

```
1. Run test → Method not implemented error
2. Complete method in exercises/ file
3. Run test → Passes with ✓
4. Move to next method
```

---

## Solutions

Solution files are provided in the `src/main/java/com/cim/workshop/solutions/` directory. Try to complete tasks yourself first!

To view a solution:

```bash
cat src/main/java/com/cim/workshop/solutions/Task1_DataImport.java
```

---

## Understanding the Code Structure

```
cim-technical-workshop/
├── src/
│   ├── main/java/com/cim/workshop/
│   │   ├── exercises/              # YOUR WORK HERE
│   │   │   ├── Task1_DataImport.java
│   │   │   ├── Task2_SparqlQueries.java
│   │   │   ├── Task3_ShaclValidation.java
│   │   │   └── Task4_CustomShacl.java
│   │   │
│   │   ├── solutions/              # Reference solutions
│   │   │   └── (same files as exercises/)
│   │   │
│   │   ├── WorkshopRunner.java     # Main menu
│   │   └── utils/
│   │       └── FusekiClient.java   # Fuseki helper
│   │
│   └── test/java/com/cim/workshop/
│       ├── Task1Test.java          # Test suites
│       ├── Task2Test.java
│       ├── Task3Test.java
│       └── Task4Test.java
│
├── assets/                 # Data files
│   ├── grid_model_cgmes/  # CIM files
│   └── shacl_cgmes/       # SHACL rules
│
├── docker-compose.yml     # Docker setup
├── Dockerfile
└── pom.xml               # Maven config
```

---

## SPARQL Quick Reference

### Basic Query Pattern

```sparql
PREFIX cim: <http://iec.ch/TC57/CIM100#>
PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>

SELECT ?variable
WHERE {
    ?variable rdf:type cim:ClassName .
}
```

### Find All ACLineSegments

```sparql
SELECT ?line ?name
WHERE {
    ?line rdf:type cim:ACLineSegment .
    ?line cim:IdentifiedObject.name ?name .
}
```

### Follow Relationships

```sparql
SELECT ?line ?terminal ?node
WHERE {
    ?line rdf:type cim:ACLineSegment .
    ?terminal cim:Terminal.ConductingEquipment ?line .
    ?terminal cim:Terminal.ConnectivityNode ?node .
}
```

### Find Missing Data

```sparql
SELECT ?line ?name
WHERE {
    ?line rdf:type cim:ACLineSegment .
    ?line cim:IdentifiedObject.name ?name .

    FILTER NOT EXISTS {
        ?terminal cim:Terminal.ConductingEquipment ?line .
    }
}
```

---

## SHACL Quick Reference

### Basic Shape Structure

```turtle
@prefix sh: <http://www.w3.org/ns/shacl#> .
@prefix cim: <http://iec.ch/TC57/CIM100#> .

shapes:ACLineSegmentShape
    a sh:NodeShape ;
    sh:targetClass cim:ACLineSegment ;
    sh:property [
        sh:path cim:Equipment.Terminal ;
        sh:minCount 2 ;
        sh:message "ACLineSegment must have at least 2 terminals" ;
    ] .
```

### Common SHACL Properties

- `sh:minCount` - Minimum number of values
- `sh:maxCount` - Maximum number of values
- `sh:class` - Expected type
- `sh:datatype` - Expected datatype
- `sh:pattern` - Regex pattern
- `sh:sparql` - SPARQL constraint

---

## Key CIM Concepts

### Equipment

- `cim:ACLineSegment` - Power line segment
- `cim:Switch` - Switching device
- `cim:PowerTransformer` - Transformer

### Connectivity

- `cim:Terminal` - Connection point of equipment
- `cim:ConnectivityNode` - Node where terminals meet
- `cim:TopologicalNode` - Simplified connectivity

### Relationships

- `Terminal.ConductingEquipment` - Terminal → Equipment
- `Terminal.ConnectivityNode` - Terminal → Node
- `Equipment.EquipmentContainer` - Equipment → Container

---

## Additional Resources

### CIM/CGMES Standards

- [IEC 61970 (CIM)](https://en.wikipedia.org/wiki/IEC_61970)
- [ENTSO-E CGMES](https://www.entsoe.eu/digital/common-information-model/)

### Apache Jena

- [Jena Documentation](https://jena.apache.org/documentation/)
- [SPARQL Tutorial](https://jena.apache.org/tutorials/sparql.html)
- [SHACL in Jena](https://jena.apache.org/documentation/shacl/)

### SPARQL & SHACL

- [SPARQL W3C Spec](https://www.w3.org/TR/sparql11-query/)
- [SHACL W3C Spec](https://www.w3.org/TR/shacl/)

---

## Tips for Success

1. **Read the comments** - Exercise files have detailed hints
2. **Run tests frequently** - Get immediate feedback
3. **Use the solutions** - Learn from examples when stuck
4. **Explore Fuseki UI** - Visualize your data at http://localhost:3030
5. **Ask questions** - Discuss with instructors and peers

---

## Workshop Completion

After completing all tasks, you will be able to:

- ✅ Import and manage CIM files in graph databases
- ✅ Write SPARQL queries to analyze power grid data
- ✅ Validate data quality with SHACL
- ✅ Create custom validation rules
- ✅ Understand the relationship between SHACL and SPARQL

---

## Cleanup

When finished with the workshop:

```bash
# Stop containers
docker-compose down

# Remove volumes (deletes database data)
docker-compose down -v

# Remove images
docker-compose down --rmi all
```

---

## Support

If you encounter issues:

1. Check the Troubleshooting section
2. Review test output carefully
3. Consult solution files
4. Ask workshop instructors

---

## License

This workshop is created for educational purposes.

CIM/CGMES test data is provided by ENTSO-E.

---

**Ready to start?**

```bash
docker-compose up -d
docker-compose exec workshop-java bash
mvn test -Dtest=Task1Test
```

Good luck and enjoy the workshop! 🚀
