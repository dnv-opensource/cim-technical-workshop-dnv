package com.cim.workshop;

import com.cim.workshop.exercises.Task3_SparqlQueries;
import com.cim.workshop.exercises.Task1_DataImport;
import com.cim.workshop.utils.FusekiClient;
import org.junit.jupiter.api.*;
import static org.assertj.core.api.Assertions.*;

import java.util.List;
import java.util.Map;

/**
 * Tests for Task 3: SPARQL Queries (40 minutes)
 *
 * All 4 queries are hands-on exercises:
 * [REQUIRED] Test 3.1: Query 1 (simple pattern)
 * [REQUIRED] Test 3.2: Query 2 (joins)
 * [REQUIRED] Test 3.3: Query 3 (FILTER NOT EXISTS)
 * [REQUIRED] Test 3.4: Query 4 (complex connectivity)
 *
 * Run with: mvn test -Dtest=Task3Test
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class Task3Test {

    private static final String FUSEKI_URL = System.getenv().getOrDefault("FUSEKI_URL", "http://localhost:3030");
    private static final String DATASET_NAME = "cim_workshop";

    private static Task3_SparqlQueries sparqlQueries;
    private static FusekiClient fusekiClient;
    private static boolean dataImported = false;

    @BeforeAll
    static void setup() {
        fusekiClient = new FusekiClient(FUSEKI_URL, DATASET_NAME);
        sparqlQueries = new Task3_SparqlQueries(FUSEKI_URL, DATASET_NAME);

        // Ensure data is imported
        if (!dataImported) {
            System.out.println("Importing test data...");
            Task1_DataImport dataImport = new Task1_DataImport(FUSEKI_URL, DATASET_NAME);

            try {
                dataImport.importDirectory("assets/grid_model_cgmes/PowerFlow");
                dataImported = true;
                System.out.println("✓ Test data imported");
            } catch (Exception e) {
                System.err.println("Warning: Could not import data. Tests may fail.");
                System.err.println("Make sure Task 1 is completed first!");
            }
        }
    }

    @Test
    @Order(1)
    @DisplayName("2.1: Query all ACLineSegments")
    void testGetAllACLineSegments() {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("TEST 2.1: Query All ACLineSegments");
        System.out.println("=".repeat(60));

        try {
            String query = sparqlQueries.getAllACLineSegmentsQuery();
            System.out.println("Executing query:\n" + query);

            List<Map<String, String>> results = sparqlQueries.executeQueryAsMaps(query);

            System.out.println("\nResults (" + results.size() + " ACLineSegments found):");
            System.out.println("-".repeat(60));

            int displayCount = Math.min(10, results.size());
            for (int i = 0; i < displayCount; i++) {
                Map<String, String> row = results.get(i);
                String name = row.get("name");
                System.out.println("  " + (i + 1) + ". " + name);
            }

            if (results.size() > 10) {
                System.out.println("  ... and " + (results.size() - 10) + " more");
            }

            assertThat(results)
                    .as("Query should return ACLineSegments")
                    .isNotEmpty();

            assertThat(results.get(0))
                    .as("Results should contain 'line' and 'name' variables")
                    .containsKeys("line", "name");

            System.out.println("\n✓ Successfully queried ACLineSegments");
            System.out.println("\nNext step: Complete getACLineSegmentTerminalsQuery()\n");

        } catch (UnsupportedOperationException e) {
            fail("Query not implemented yet. Complete getAllACLineSegmentsQuery() in Task3_SparqlQueries.java");
        }
    }

    @Test
    @Order(2)
    @DisplayName("2.2: Query ACLineSegment terminals")
    void testGetACLineSegmentTerminals() {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("TEST 2.2: Query ACLineSegment Terminals");
        System.out.println("=".repeat(60));

        try {
            String query = sparqlQueries.getACLineSegmentTerminalsQuery();
            System.out.println("Executing query:\n" + query);

            List<Map<String, String>> results = sparqlQueries.executeQueryAsMaps(query);

            System.out.println("\nResults (" + results.size() + " line-terminal connections found):");
            System.out.println("-".repeat(60));

            // Group by line
            Map<String, Integer> terminalCountByLine = new java.util.HashMap<>();
            for (Map<String, String> row : results) {
                String lineName = row.get("lineName");
                terminalCountByLine.put(lineName, terminalCountByLine.getOrDefault(lineName, 0) + 1);
            }

            int displayCount = Math.min(5, terminalCountByLine.size());
            int count = 0;
            for (Map.Entry<String, Integer> entry : terminalCountByLine.entrySet()) {
                if (count++ < displayCount) {
                    System.out.println("  " + entry.getKey() + ": " + entry.getValue() + " terminals");
                }
            }

            if (terminalCountByLine.size() > 5) {
                System.out.println("  ... and " + (terminalCountByLine.size() - 5) + " more lines");
            }

            assertThat(results)
                    .as("Query should return line-terminal connections")
                    .isNotEmpty();

            assertThat(results.get(0))
                    .as("Results should contain 'line', 'lineName', and 'terminal' variables")
                    .containsKeys("line", "lineName", "terminal");

            System.out.println("\n✓ Successfully queried terminals");
            System.out.println("\nNext step: Complete getACLineSegmentsWithoutTerminalsQuery()\n");

        } catch (UnsupportedOperationException e) {
            fail("Query not implemented yet. Complete getACLineSegmentTerminalsQuery() in Task3_SparqlQueries.java");
        }
    }

    @Test
    @Order(3)
    @DisplayName("2.3: Find ACLineSegments without terminals (ERRORS)")
    void testGetACLineSegmentsWithoutTerminals() {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("TEST 2.3: Find ACLineSegments WITHOUT Terminals");
        System.out.println("=".repeat(60));
        System.out.println("This query identifies ERRORS in the grid model!");
        System.out.println("=".repeat(60));

        try {
            String query = sparqlQueries.getACLineSegmentsWithoutTerminalsQuery();
            System.out.println("Executing query:\n" + query);

            List<Map<String, String>> results = sparqlQueries.executeQueryAsMaps(query);

            if (results.isEmpty()) {
                System.out.println("\n✓ Good news! No ACLineSegments without terminals found.");
            } else {
                System.out.println("\n⚠ Found " + results.size() + " ACLineSegments WITHOUT terminals:");
                System.out.println("-".repeat(60));

                for (int i = 0; i < results.size(); i++) {
                    Map<String, String> row = results.get(i);
                    String name = row.get("name");
                    System.out.println("  " + (i + 1) + ". " + name + " [ERROR: No terminals!]");
                }

                System.out.println("\nThese are configuration errors that should be fixed!");
            }

            // The query should work regardless of whether errors are found
            assertThat(results)
                    .as("Query should execute successfully (may return empty if no errors)")
                    .isNotNull();

            System.out.println("\n✓ Successfully identified lines without terminals");
            System.out.println("\nNext step: Complete getConnectedSwitchesQuery()\n");

        } catch (UnsupportedOperationException e) {
            fail("Query not implemented yet. Complete getACLineSegmentsWithoutTerminalsQuery() in Task3_SparqlQueries.java");
        }
    }

    @Test
    @Order(4)
    @DisplayName("2.4: Find connected switches")
    void testGetConnectedSwitches() {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("TEST 2.4: Find Switches Connected to ACLineSegments");
        System.out.println("=".repeat(60));

        try {
            String query = sparqlQueries.getConnectedSwitchesQuery();
            System.out.println("Executing query:\n" + query);

            List<Map<String, String>> results = sparqlQueries.executeQueryAsMaps(query);

            System.out.println("\nResults (" + results.size() + " line-switch connections found):");
            System.out.println("-".repeat(60));

            // Group by line
            Map<String, java.util.List<String>> switchesByLine = new java.util.HashMap<>();
            for (Map<String, String> row : results) {
                String lineName = row.get("lineName");
                String switchName = row.get("switchName");
                switchesByLine.computeIfAbsent(lineName, k -> new java.util.ArrayList<>()).add(switchName);
            }

            int displayCount = Math.min(5, switchesByLine.size());
            int count = 0;
            for (Map.Entry<String, java.util.List<String>> entry : switchesByLine.entrySet()) {
                if (count++ < displayCount) {
                    System.out.println("  " + entry.getKey() + ":");
                    for (String switchName : entry.getValue()) {
                        System.out.println("    → " + switchName);
                    }
                }
            }

            if (switchesByLine.size() > 5) {
                System.out.println("  ... and " + (switchesByLine.size() - 5) + " more lines");
            }

            if (results.isEmpty()) {
                System.out.println("\nNo line-switch connections found in this dataset.");
            }

            assertThat(results)
                    .as("Query should execute successfully")
                    .isNotNull();

            System.out.println("\n✓ Successfully completed Task 2!");
            System.out.println("\n" + "=".repeat(60));
            System.out.println("TASK 2 COMPLETE!");
            System.out.println("=".repeat(60));
            System.out.println("\nYou have successfully:");
            System.out.println("  ✓ Queried all ACLineSegments");
            System.out.println("  ✓ Found terminals for ACLineSegments");
            System.out.println("  ✓ Identified ACLineSegments without terminals");
            System.out.println("  ✓ Found switches connected to lines");
            System.out.println("\nNext: Move on to Task 3 - SHACL Validation");
            System.out.println("Run: mvn test -Dtest=Task3Test\n");

        } catch (UnsupportedOperationException e) {
            fail("Query not implemented yet. Complete getConnectedSwitchesQuery() in Task3_SparqlQueries.java");
        }
    }
}
