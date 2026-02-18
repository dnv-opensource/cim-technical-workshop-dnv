# Quick Reference Card

## Essential Commands

### Starting Workshop

```bash
# Setup (first time only)
./setup.sh

# Or manually
docker-compose up -d
docker-compose exec workshop-java bash
```

### Running Tests

```bash
# All tasks
mvn test

# Specific task
mvn test -Dtest=Task1Test
mvn test -Dtest=Task2Test
mvn test -Dtest=Task3Test
mvn test -Dtest=Task4Test
mvn test -Dtest=Task5Test

# Watch mode (rerun on changes)
mvn test -Dtest=Task1Test -DfailIfNoTests=false
```

### Workshop Menu

```bash
mvn exec:java -Dexec.mainClass="com.cim.workshop.WorkshopRunner"
```

---

## SPARQL Cheat Sheet

### Basic Query

```sparql
PREFIX cim: <http://iec.ch/TC57/CIM100#>
PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>

SELECT ?var
WHERE {
    ?var rdf:type cim:ClassName .
}
```

### With Properties

```sparql
SELECT ?resource ?name
WHERE {
    ?resource rdf:type cim:ACLineSegment .
    ?resource cim:IdentifiedObject.name ?name .
}
```

### Following Relationships

```sparql
SELECT ?line ?terminal ?node
WHERE {
    ?line rdf:type cim:ACLineSegment .
    ?terminal cim:Terminal.ConductingEquipment ?line .
    ?terminal cim:Terminal.ConnectivityNode ?node .
}
```

### Finding Missing Data

```sparql
SELECT ?line
WHERE {
    ?line rdf:type cim:ACLineSegment .
    FILTER NOT EXISTS {
        ?terminal cim:Terminal.ConductingEquipment ?line .
    }
}
```

### Optional Values

```sparql
SELECT ?line ?name ?description
WHERE {
    ?line rdf:type cim:ACLineSegment .
    ?line cim:IdentifiedObject.name ?name .
    OPTIONAL {
        ?line cim:IdentifiedObject.description ?description .
    }
}
```

### Aggregation

```sparql
SELECT ?line (COUNT(?terminal) as ?count)
WHERE {
    ?line rdf:type cim:ACLineSegment .
    ?terminal cim:Terminal.ConductingEquipment ?line .
}
GROUP BY ?line
```

---

## SHACL Cheat Sheet

### Basic Shape

```turtle
@prefix sh: <http://www.w3.org/ns/shacl#> .
@prefix cim: <http://iec.ch/TC57/CIM100#> .

shapes:MyShape
    a sh:NodeShape ;
    sh:targetClass cim:ACLineSegment ;
    sh:property [
        sh:path cim:Equipment.Terminal ;
        sh:minCount 2 ;
        sh:message "Must have at least 2 terminals" ;
    ] .
```

### Property Constraints

```turtle
sh:property [
    sh:path cim:IdentifiedObject.name ;
    sh:minCount 1 ;           # Required
    sh:maxCount 1 ;           # Single value
    sh:datatype xsd:string ;  # String type
    sh:minLength 1 ;          # Not empty
] .
```

### Class Constraint

```turtle
sh:property [
    sh:path cim:Terminal.ConnectivityNode ;
    sh:class cim:ConnectivityNode ;  # Must be this type
    sh:minCount 1 ;
] .
```

### SPARQL Constraint

```turtle
sh:sparql [
    sh:message "Must have exactly 2 terminals" ;
    sh:select """
        PREFIX cim: <http://iec.ch/TC57/CIM100#>
        SELECT $this
        WHERE {
            { SELECT $this (COUNT(?t) as ?count)
              WHERE { ?t cim:Terminal.ConductingEquipment $this }
              GROUP BY $this }
            FILTER(?count != 2)
        }
    """ ;
] .
```

---

## Jena API Quick Reference

### Loading RDF

```java
// From file
Model model = RDFDataMgr.loadModel("file.xml");

// From string
Model model = ModelFactory.createDefaultModel();
RDFDataMgr.read(model, new StringReader(rdfString), null, RDFFormat.RDFXML);
```

### Creating Resources

```java
// Create resource with URI
Resource resource = model.createResource("http://example.com/resource");

// Create property
Property prop = model.createProperty("http://example.com/property");

// Add statement
resource.addProperty(prop, "value");
```

### Querying Model

```java
// List subjects with property
ResIterator iter = model.listSubjectsWithProperty(property);

// List statements
StmtIterator stmts = model.listStatements();
```

### SPARQL Query

```java
String queryString = "SELECT ?s WHERE { ?s ?p ?o }";
Query query = QueryFactory.create(queryString);

try (QueryExecution qexec = QueryExecutionFactory.create(query, model)) {
    ResultSet results = qexec.execSelect();
    while (results.hasNext()) {
        QuerySolution soln = results.nextSolution();
        RDFNode s = soln.get("s");
        System.out.println(s);
    }
}
```

### SHACL Validation

```java
// Load shapes
Model shapesModel = RDFDataMgr.loadModel("shapes.ttl");
Shapes shapes = Shapes.parse(shapesModel);

// Validate
ValidationReport report = ShaclValidator.get().validate(
    shapes.getGraph(),
    dataModel.getGraph()
);

// Check results
boolean conforms = report.conforms();
List<ReportEntry> violations = report.getEntries();
```

---

## CIM Namespaces

```java
// Common namespaces
String CIM_NS = "http://iec.ch/TC57/CIM100#";
String RDF_NS = "http://www.w3.org/1999/02/22-rdf-syntax-ns#";
String MD_NS = "http://iec.ch/TC57/61970-552/ModelDescription/1#";
String SHACL_NS = "http://www.w3.org/ns/shacl#";
```

## Common CIM Classes

- `cim:ACLineSegment` - Power line
- `cim:Terminal` - Connection point
- `cim:ConnectivityNode` - Electrical node
- `cim:Switch` - Switching device
- `cim:PowerTransformer` - Transformer
- `cim:EnergyConsumer` - Load
- `cim:GeneratingUnit` - Generator

## Common CIM Properties

- `cim:IdentifiedObject.name` - Name
- `cim:IdentifiedObject.mRID` - Unique ID
- `cim:Terminal.ConductingEquipment` - Terminal → Equipment
- `cim:Terminal.ConnectivityNode` - Terminal → Node
- `cim:Equipment.EquipmentContainer` - Equipment → Container

---

## File Locations

### Exercise Files (Your Work)

- `src/main/java/com/cim/workshop/exercises/Task1_DataImport.java`
- `src/main/java/com/cim/workshop/exercises/Task2_DataExport.java`
- `src/main/java/com/cim/workshop/exercises/Task3_SparqlQueries.java`
- `src/main/java/com/cim/workshop/exercises/Task4_ShaclValidation.java`
- `src/main/java/com/cim/workshop/exercises/Task5_CustomShacl_Optional.java`

### Test Files

- `src/test/java/com/cim/workshop/Task1Test.java` (etc.)

### Data Files

- `assets/grid_model_cgmes/` - CIM files
- `assets/shacl_cgmes/` - SHACL rules

### Solutions

- `src/main/java/com/cim/workshop/solutions/` - Reference implementations

---

## Useful URLs

- **Fuseki UI:** http://localhost:3030
  - Username: `admin`
  - Password: `admin`

- **SPARQL Endpoint:** http://localhost:3030/cim_workshop/query
- **Update Endpoint:** http://localhost:3030/cim_workshop/update
- **GSP Endpoint:** http://localhost:3030/cim_workshop/data

---

## Docker Commands

```bash
# View running containers
docker ps

# View logs
docker-compose logs -f fuseki
docker-compose logs -f workshop-java

# Restart service
docker-compose restart fuseki

# Stop all
docker-compose down

# Stop and remove volumes
docker-compose down -v

# Rebuild
docker-compose build
docker-compose up -d
```

---

## Debugging Tips

### Test Fails with "Method not implemented"

→ You need to complete the method in the exercise file

### Test Fails with Assertion Error

→ Your implementation is incorrect, check the error message

### Cannot Connect to Fuseki

→ Check if Fuseki is running: `docker ps`
→ Check logs: `docker-compose logs fuseki`

### SPARQL Query Returns Empty

→ Check if data is imported
→ Verify namespaces in query
→ Test query in Fuseki UI

### SHACL Validation Not Working

→ Check shapes are loaded correctly
→ Verify target class matches data
→ Print shapes to see structure

---

## Testing Your Work

### Task 1: Data Import

✓ Extract mRID from file
✓ Load file into model
✓ Upload to Fuseki
✓ Import directory

### Task 2: Data Export

✓ List all named graphs
✓ Download graph from Fuseki
✓ (Optional bonus methods)

### Task 3: SPARQL Queries

✓ Query all ACLineSegments
✓ Find terminals
✓ Find lines without terminals
✓ Find connected switches

### Task 4: SHACL Validation

✓ Load SHACL shapes
✓ Run validation
✓ Extract violations
✓ Parse violation details

### Task 5: Custom SHACL (Optional)

✓ Create basic shape
✓ Create connectivity shape
✓ Create SPARQL constraint
✓ Save and validate

---

## Common Errors

**Error:** `UnsupportedOperationException`
**Fix:** Method not implemented yet

**Error:** `Connection refused`
**Fix:** Start Fuseki with `docker-compose up -d`

**Error:** `QueryParseException`
**Fix:** Check SPARQL syntax, verify prefixes

**Error:** `FileNotFoundException`
**Fix:** Use absolute paths or check file exists

---

## Getting Help

1. Read error messages carefully
2. Check this reference card
3. Review README.md
4. Look at solution files
5. Ask instructors

---

**Happy coding! 🚀**
