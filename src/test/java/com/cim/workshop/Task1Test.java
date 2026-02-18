package com.cim.workshop;

import com.cim.workshop.exercises.Task1_DataImport;
import com.cim.workshop.utils.FusekiClient;
import org.apache.jena.rdf.model.Model;
import org.junit.jupiter.api.*;
import static org.assertj.core.api.Assertions.*;

import java.io.File;
import java.util.List;

/**
 * Tests for Task 1: Data Import
 *
 * Run with: mvn test -Dtest=Task1Test
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class Task1Test {

    private static final String FUSEKI_URL = System.getenv().getOrDefault("FUSEKI_URL", "http://localhost:3030");
    private static final String DATASET_NAME = "cim_workshop";
    private static final String TEST_FILE = "assets/grid_model_cgmes/PowerFlow/PowerFlow_EQ.xml";

    private static Task1_DataImport dataImport;
    private static FusekiClient fusekiClient;

    @BeforeAll
    static void setup() {
        dataImport = new Task1_DataImport(FUSEKI_URL, DATASET_NAME);
        fusekiClient = new FusekiClient(FUSEKI_URL, DATASET_NAME);

        // Check if Fuseki is accessible
        System.out.println("Testing connection to Fuseki at: " + FUSEKI_URL);
        int attempts = 0;
        while (attempts < 10) {
            try {
                if (fusekiClient.isServerAccessible()) {
                    System.out.println("✓ Connected to Fuseki successfully");
                    return;
                }
            } catch (Exception e) {
                System.out.println("Waiting for Fuseki... (attempt " + (attempts + 1) + "/10)");
            }
            attempts++;
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        fail("Could not connect to Fuseki. Make sure it's running with: docker-compose up -d");
    }

    @Test
    @Order(1)
    @DisplayName("1.1: Extract Model ID from CIM file")
    void testExtractModelId() {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("TEST 1.1: Extract Model ID");
        System.out.println("=".repeat(60));

        try {
            String mrid = dataImport.extractModelId(TEST_FILE);

            System.out.println("Extracted mRID: " + mrid);

            assertThat(mrid)
                .as("mRID should not be null")
                .isNotNull();

            assertThat(mrid)
                .as("mRID should start with 'urn:uuid:'")
                .startsWith("urn:uuid:");

            System.out.println("✓ Successfully extracted Model ID");
            System.out.println("\nNext step: Implement loadCimFile() method\n");

        } catch (UnsupportedOperationException e) {
            fail("Method not implemented yet. Complete the extractModelId() method in Task1_DataImport.java");
        }
    }

    @Test
    @Order(2)
    @DisplayName("1.2: Load CIM file into Jena Model")
    void testLoadCimFile() {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("TEST 1.2: Load CIM File");
        System.out.println("=".repeat(60));

        try {
            Model model = dataImport.loadCimFile(TEST_FILE);

            assertThat(model)
                .as("Model should not be null")
                .isNotNull();

            long tripleCount = model.size();
            System.out.println("Loaded model with " + tripleCount + " triples");

            assertThat(tripleCount)
                .as("Model should contain triples")
                .isGreaterThan(0);

            System.out.println("✓ Successfully loaded CIM file into model");
            System.out.println("\nNext step: Implement uploadToFuseki() method\n");

        } catch (UnsupportedOperationException e) {
            fail("Method not implemented yet. Complete the loadCimFile() method in Task1_DataImport.java");
        }
    }

    @Test
    @Order(3)
    @DisplayName("1.3: Upload model to Fuseki")
    void testUploadToFuseki() {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("TEST 1.3: Upload to Fuseki");
        System.out.println("=".repeat(60));

        try {
            String mrid = dataImport.extractModelId(TEST_FILE);
            Model model = dataImport.loadCimFile(TEST_FILE);

            System.out.println("Uploading model to Fuseki...");
            dataImport.uploadToFuseki(model, mrid);

            // Verify upload
            long tripleCount = fusekiClient.getTripleCount(mrid);
            System.out.println("Triple count in Fuseki: " + tripleCount);

            assertThat(tripleCount)
                .as("Fuseki should contain the uploaded triples")
                .isEqualTo(model.size());

            System.out.println("✓ Successfully uploaded model to Fuseki");
            System.out.println("\nNext step: Implement importDirectory() method\n");

        } catch (UnsupportedOperationException e) {
            fail("Method not implemented yet. Complete the uploadToFuseki() method in Task1_DataImport.java");
        }
    }

    @Test
    @Order(4)
    @DisplayName("1.4: Import entire directory")
    void testImportDirectory() {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("TEST 1.4: Import Directory");
        System.out.println("=".repeat(60));

        try {
            String directory = "assets/grid_model_cgmes/PowerFlow";
            File dir = new File(directory);

            assertThat(dir)
                .as("Test directory should exist")
                .exists()
                .isDirectory();

            System.out.println("Importing all files from: " + directory);
            List<String> importedGraphs = dataImport.importDirectory(directory);

            System.out.println("Imported " + importedGraphs.size() + " graphs:");
            importedGraphs.forEach(graph -> System.out.println("  - " + graph));

            assertThat(importedGraphs)
                .as("Should have imported multiple graphs")
                .isNotEmpty();

            // Verify each graph in Fuseki
            for (String graphUri : importedGraphs) {
                long count = fusekiClient.getTripleCount(graphUri);
                assertThat(count)
                    .as("Graph " + graphUri + " should contain triples")
                    .isGreaterThan(0);
            }

            System.out.println("\n✓ Successfully completed Task 1!");
            System.out.println("\n" + "=".repeat(60));
            System.out.println("TASK 1 COMPLETE!");
            System.out.println("=".repeat(60));
            System.out.println("\nYou have successfully:");
            System.out.println("  ✓ Extracted Model IDs from CIM files");
            System.out.println("  ✓ Loaded CIM files into Jena models");
            System.out.println("  ✓ Uploaded models to Fuseki graph database");
            System.out.println("  ✓ Imported multiple files as named graphs");
            System.out.println("\nNext: Move on to Task 2 - SPARQL Queries");
            System.out.println("Run: mvn test -Dtest=Task2Test\n");

        } catch (UnsupportedOperationException e) {
            fail("Method not implemented yet. Complete the importDirectory() method in Task1_DataImport.java");
        }
    }
}
