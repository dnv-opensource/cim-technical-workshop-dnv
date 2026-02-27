package com.cim.workshop.exercises;

import com.cim.workshop.utils.FusekiClient;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;

import java.util.*;

/**
 * TASK 3: Query Datasets with SPARQL
 * ═════════════════════════════════════════════════════════════
 *
 * OBJECTIVE:
 * Learn SPARQL query language to extract specific information from
 * CIM/CGMES data stored in the graph database.
 *
 * BACKGROUND:
 * - SPARQL is the query language for RDF data (like SQL for databases)
 * - Basic pattern: SELECT ?variable WHERE { ?subject ?predicate ?object }
 * - CIM namespace: http://iec.ch/TC57/CIM100#
 * - Common patterns:
 * - Find all instances: ?x rdf:type cim:ClassName
 * - Follow relationships: ?x cim:propertyName ?y
 * - Filter results: FILTER(?value > 100)
 *
 * YOUR TASK:
 * Write SPARQL queries to:
 * 1. List all ACLineSegments
 * 2. Find switches connected to each ACLineSegment
 * 3. Identify ACLineSegments without proper connections (errors)
 *
 * TESTING:
 * Run: mvn test -Dtest=Task3Test
 *
 * SPARQL CHEAT SHEET:
 * -------------------
 * Namespaces:
 * cim: http://iec.ch/TC57/CIM100#
 * rdf: http://www.w3.org/1999/02/22-rdf-syntax-ns#
 *
 * Basic patterns:
 * SELECT ?var WHERE { GRAPH ?g { ?var rdf:type cim:ACLineSegment } }
 * SELECT ?name WHERE { GRAPH ?g { ?line cim:IdentifiedObject.name ?name } }
 *
 * Joining data:
 * ?line cim:Equipment.EquipmentContainer ?container .
 * ?container cim:IdentifiedObject.name ?containerName .
 *
 * Optional patterns:
 * OPTIONAL { ?line cim:propertyName ?value }
 *
 * Filtering:
 * FILTER NOT EXISTS { ?line cim:propertyName ?something }
 */
public class Task3_SparqlQueries {

    private final FusekiClient fusekiClient;

    // CIM Namespace
    private static final String CIM_NS = "http://iec.ch/TC57/CIM100#";

    public Task3_SparqlQueries(String fusekiUrl, String datasetName) {
        this.fusekiClient = new FusekiClient(fusekiUrl, datasetName);
    }

    /**
     * TODO: Query 1 - List all ACLineSegments with their names
     *
     * Write a SPARQL query that returns:
     * - ?line: The URI of each ACLineSegment
     * - ?name: The name of the ACLineSegment
     *
     * The query should:
     * 1. Find all resources of type cim:ACLineSegment
     * 2. Get their names using cim:IdentifiedObject.name
     *
     * Example result:
     * line | name
     * ----------------------------------------|------------------
     * urn:uuid:abc123... | LINE_1
     * urn:uuid:def456... | LINE_2
     *
     * @return SPARQL query string
     */
    public String getAllACLineSegmentsQuery() {
        // TODO: Write your SPARQL query here
        String query = """
                PREFIX cim: <http://iec.ch/TC57/CIM100#>
                PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>

                SELECT ?line ?name
                WHERE {
                    GRAPH ?g {
                        # TODO: Add your query patterns here
                        # HINT: Use rdf:type to find instances of cim:ACLineSegment
                        # HINT: Use cim:IdentifiedObject.name to get the name property
                    }
                }
                """;

        throw new UnsupportedOperationException("TODO: Complete the SPARQL query");
    }

    /**
     * Query 2: Find terminals for each ACLineSegment
     *
     * Join ACLineSegments with their Terminals.
     * Relationship: Terminal --cim:Terminal.ConductingEquipment--> ACLineSegment
     */
    public String getACLineSegmentTerminalsQuery() {
        // TODO: Write complete query
        // Structure:
        // - SELECT ?line ?lineName ?terminal
        // - Find ACLineSegments and their names (from Query 1, but use ?lineName)
        // - Add pattern: ?terminal cim:Terminal.ConductingEquipment ?line .

        String query = """
                PREFIX cim: <http://iec.ch/TC57/CIM100#>
                PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>

                SELECT ?line ?lineName ?terminal
                WHERE {
                    GRAPH ?g {
                        # TODO: Add patterns from Query 1 (ACLineSegment + name as ?lineName)
                        # TODO: Add pattern: ?terminal cim:Terminal.ConductingEquipment ?line .
                    }
                }
                """;

        throw new UnsupportedOperationException("Complete the query");
    }

    /**
     * Query 3: Find ACLineSegments WITHOUT terminals (data errors)
     *
     * Use FILTER NOT EXISTS to find missing relationships.
     * The FILTER NOT EXISTS should be inside the GRAPH block, after finding
     * ACLineSegments.
     */
    public String getACLineSegmentsWithoutTerminalsQuery() {
        // TODO: Write query using FILTER NOT EXISTS
        // Structure:
        // - Find all ACLineSegments with names (from Query 1)
        // - Add: FILTER NOT EXISTS { ?terminal cim:Terminal.ConductingEquipment ?line }
        // - This filters OUT lines that have terminals, leaving only those without

        String query = """
                PREFIX cim: <http://iec.ch/TC57/CIM100#>
                PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>

                SELECT ?line ?name
                WHERE {
                    GRAPH ?g {
                        # TODO: Find all ACLineSegments with names (from Query 1)
                        # TODO: Add FILTER NOT EXISTS { ?terminal cim:Terminal.ConductingEquipment ?line }
                    }
                }
                """;

        throw new UnsupportedOperationException("Complete the query");
    }

    /**
     * Query 4: Find switches connected to ACLineSegments
     *
     * Multi-hop path through connectivity:
     * ACLineSegment -> Terminal -> ConnectivityNode <- Terminal <- Switch
     *
     * Step-by-step hints:
     * 1. Find line: ?line rdf:type cim:ACLineSegment
     * 2. Get line's terminal: ?lineTerminal cim:Terminal.ConductingEquipment ?line
     * 3. Get connectivity node: ?lineTerminal cim:Terminal.ConnectivityNode ?node
     * 4. Find other terminals at same node: ?switchTerminal
     * cim:Terminal.ConnectivityNode ?node
     * 5. Get equipment from terminal: ?switchTerminal
     * cim:Terminal.ConductingEquipment ?switch
     * 6. Filter to switches: ?switch rdf:type cim:Switch
     * 7. Get names: Use cim:IdentifiedObject.name for both ?line and ?switch
     */
    public String getConnectedSwitchesQuery() {
        // TODO: Build complete query with all patterns above

        String query = """
                PREFIX cim: <http://iec.ch/TC57/CIM100#>
                PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>

                SELECT ?line ?lineName ?switch ?switchName
                WHERE {
                    GRAPH ?g {
                        # TODO: Follow the 7-step pattern from method comments above
                        # Start with finding ACLineSegments (step 1)
                        # Then trace through terminals and connectivity nodes (steps 2-6)
                        # Don't forget to get names for both line and switch (step 7)!
                    }
                }
                """;

        throw new UnsupportedOperationException("Complete the query");
    }

    /**
     * Execute a query and return results
     */
    public ResultSet executeQuery(String sparqlQuery) {
        return fusekiClient.executeSelect(sparqlQuery);
    }

    /**
     * Execute a query and format results as a list of maps
     */
    public List<Map<String, String>> executeQueryAsMaps(String sparqlQuery) {
        ResultSet results = executeQuery(sparqlQuery);
        List<Map<String, String>> resultList = new ArrayList<>();

        while (results.hasNext()) {
            QuerySolution solution = results.next();
            Map<String, String> row = new HashMap<>();

            solution.varNames().forEachRemaining(varName -> {
                if (solution.get(varName) != null) {
                    row.put(varName, solution.get(varName).toString());
                }
            });

            resultList.add(row);
        }

        return resultList;
    }

    /**
     * Count results from a query
     */
    public int countResults(String sparqlQuery) {
        ResultSet results = executeQuery(sparqlQuery);
        int count = 0;
        while (results.hasNext()) {
            results.next();
            count++;
        }
        return count;
    }
}
