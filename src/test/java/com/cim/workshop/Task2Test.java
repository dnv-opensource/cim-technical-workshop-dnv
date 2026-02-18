package com.cim.workshop;

import com.cim.workshop.exercises.Task2_DataExport;
import com.cim.workshop.exercises.Task1_DataImport;
import com.cim.workshop.utils.FusekiClient;
import org.apache.jena.rdf.model.Model;
import org.junit.jupiter.api.*;
import static org.assertj.core.api.Assertions.*;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

/**
 * Tests for Task 2: Data Export (25 minutes)
 *
 * WORKSHOP FOCUS - These tests validate the 2 required methods:
 * [REQUIRED] Test 2.1: listAllGraphs()
 * [REQUIRED] Test 2.2: downloadFromFuseki()
 *
 * BONUS TESTS - These test optional/homework methods:
 * [BONUS] Tests 2.3-2.8: Additional export functionality
 *
 * Run with: mvn test -Dtest=Task2Test
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class Task2Test {

    private static final String FUSEKI_URL = System.getenv().getOrDefault("FUSEKI_URL", "http://localhost:3030");
    private static final String DATASET_NAME = "cim_workshop";
    private static final String TEST_DIRECTORY = "assets/grid_model_cgmes/PowerFlow";
    private static final String OUTPUT_DIR = "output/exported_graphs";

    private static Task2_DataExport dataExport;
    private static Task1_DataImport dataImport;
    private static FusekiClient fusekiClient;

    @BeforeAll
    static void setup() {
        dataExport = new Task2_DataExport(FUSEKI_URL, DATASET_NAME);
        dataImport = new Task1_DataImport(FUSEKI_URL, DATASET_NAME);
        fusekiClient = new FusekiClient(FUSEKI_URL, DATASET_NAME);

        System.out.println("Starting Data Export Tests");

        int attempts = 0;
        while (attempts < 10) {
            try {
                if (fusekiClient.isServerAccessible()) {
                    System.out.println("✓ Connected to Fuseki successfully");
                    break;
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

        if (attempts >= 10) {
            fail("Could not connect to Fuseki. Make sure it's running with: docker-compose up -d");
        }

        // Import test data - multiple CGMES profiles
        try {
            System.out.println("\nPreparing test data (multiple CGMES profiles)...");
            List<String> imported = dataImport.importDirectory(TEST_DIRECTORY);
            System.out.println("✓ Imported " + imported.size() + " test graphs");
        } catch (Exception e) {
            System.err.println("Warning: Could not prepare test data: " + e.getMessage());
        }
    }

    @Test
    @Order(1)
    @DisplayName("[REQUIRED] 2.1: List all named graphs")
    void testListAllGraphs() {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("[REQUIRED] TEST 2.1: List All Graphs");
        System.out.println("=".repeat(60));

        try {
            List<String> graphs = dataExport.listAllGraphs();

            System.out.println("Found " + graphs.size() + " graphs:");
            for (String graphUri : graphs) {
                System.out.println("  - " + graphUri);
            }

            assertThat(graphs)
                    .as("Should return a list of graphs")
                    .isNotNull();

            assertThat(graphs)
                    .as("Should find at least one graph")
                    .isNotEmpty();

            // Check that our test graph is in the list
            assertThat(graphs.size())
                    .as("Should have imported multiple graphs from PowerFlow directory")
                    .isGreaterThan(1);

            System.out.println("✓ Successfully listed all graphs");
            System.out.println("\nNext step: Implement downloadFromFuseki() method\n");

        } catch (UnsupportedOperationException e) {
            fail("Method not implemented yet. Complete the listAllGraphs() method in Task2_DataExport.java");
        }
    }

    @Test
    @Order(2)
    @DisplayName("[REQUIRED] 2.2: Download graph from Fuseki")
    void testDownloadFromFuseki() {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("[REQUIRED] TEST 2.2: Download From Fuseki");
        System.out.println("=".repeat(60));

        try {
            List<String> graphs = dataExport.listAllGraphs();
            assertThat(graphs).isNotEmpty();

            String testGraphUri = graphs.get(0);
            System.out.println("Downloading graph: " + testGraphUri);

            Model downloadedModel = dataExport.downloadFromFuseki(testGraphUri);

            assertThat(downloadedModel)
                    .as("Downloaded model should not be null")
                    .isNotNull();

            long tripleCount = downloadedModel.size();
            System.out.println("Downloaded model with " + tripleCount + " triples");

            assertThat(tripleCount)
                    .as("Downloaded model should contain triples")
                    .isGreaterThan(0);

            System.out.println("✓ Successfully downloaded graph from Fuseki");
            System.out.println("\nNext step: Implement exportGraphToFile() method\n");

        } catch (UnsupportedOperationException e) {
            fail("Method not implemented yet. Complete the downloadFromFuseki() method in Task2_DataExport.java");
        }
    }

    @Test
    @Order(3)
    @DisplayName("[BONUS] 2.3: Export graph to file")
    void testExportGraphToFile() {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("[BONUS] TEST 2.3: Export Graph To File");
        System.out.println("=".repeat(60));

        try {
            // Create output directory
            Path outputPath = Paths.get(OUTPUT_DIR);
            Files.createDirectories(outputPath);

            List<String> graphs = dataExport.listAllGraphs();
            assertThat(graphs).isNotEmpty();

            String testGraphUri = graphs.get(0);
            String outputFile = OUTPUT_DIR + "/test_export.xml";

            System.out.println("Exporting to: " + outputFile);
            dataExport.exportGraphToFile(testGraphUri, outputFile);

            // Verify file was created
            File file = new File(outputFile);
            assertThat(file)
                    .as("Exported file should exist")
                    .exists();

            assertThat(file.length())
                    .as("Exported file should not be empty")
                    .isGreaterThan(0);

            System.out.println("File size: " + file.length() + " bytes");

            // Verify we can read it back
            Model exportedModel = dataImport.loadCimFile(outputFile);
            assertThat(exportedModel.size())
                    .as("Exported file should contain the same data")
                    .isGreaterThan(0);

            System.out.println("✓ Successfully exported graph to file");
            System.out.println("\nNext step: Implement extractProfileType() method\n");

        } catch (UnsupportedOperationException e) {
            fail("Method not implemented yet. Complete the exportGraphToFile() method in Task2_DataExport.java");
        } catch (Exception e) {
            fail("Test failed: " + e.getMessage());
        }
    }

    @Test
    @Order(4)
    @DisplayName("[BONUS] 2.4: Extract profile type from model")
    void testExtractProfileType() {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("[BONUS] TEST 2.4: Extract Profile Type");
        System.out.println("=".repeat(60));

        try {
            List<String> graphs = dataExport.listAllGraphs();
            assertThat(graphs).isNotEmpty();

            System.out.println("Analyzing profile types:");
            for (String graphUri : graphs) {
                String profileType = dataExport.extractProfileType(graphUri);
                System.out.println("  " + profileType + " : " + graphUri);

                assertThat(profileType)
                        .as("Profile type should not be null")
                        .isNotNull();
            }

            System.out.println("\n✓ Successfully extracted profile types");
            System.out.println("\nNext step: Implement groupGraphsByProfile() method\n");

        } catch (UnsupportedOperationException e) {
            fail("Method not implemented yet. Complete the extractProfileType() method in Task2_DataExport.java");
        }
    }

    @Test
    @Order(5)
    @DisplayName("5.5: Group graphs by profile type")
    void testGroupGraphsByProfile() {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("TEST 5.5: Group Graphs by Profile (CIM-Aware)");
        System.out.println("=".repeat(60));

        try {
            Map<String, List<String>> profileGroups = dataExport.groupGraphsByProfile();

            assertThat(profileGroups)
                    .as("Profile groups should not be null")
                    .isNotNull();

            assertThat(profileGroups)
                    .as("Should have at least one profile type")
                    .isNotEmpty();

            System.out.println("\nProfile breakdown:");
            for (Map.Entry<String, List<String>> entry : profileGroups.entrySet()) {
                System.out.println("  " + entry.getKey() + ": " + entry.getValue().size() + " graph(s)");
            }

            // Verify typical CGMES profiles exist
            boolean hasKnownProfile = profileGroups.keySet().stream()
                    .anyMatch(p -> p.matches("EQ|TP|SSH|SV|DL|GL"));

            assertThat(hasKnownProfile)
                    .as("Should detect at least one known CGMES profile (EQ, TP, SSH, SV, etc.)")
                    .isTrue();

            System.out.println("\n✓ Successfully grouped graphs by profile");
            System.out.println("\nNext step: Implement exportByProfile() method\n");

        } catch (UnsupportedOperationException e) {
            fail("Method not implemented yet. Complete the groupGraphsByProfile() method in Task2_DataExport.java");
        }
    }

    @Test
    @Order(6)
    @DisplayName("5.6: Export graphs by profile type")
    void testExportByProfile() {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("TEST 5.6: Export by Profile (CIM-Aware)");
        System.out.println("=".repeat(60));

        try {
            String exportDir = OUTPUT_DIR + "/by_profile";

            // Get available profiles
            Map<String, List<String>> profileGroups = dataExport.groupGraphsByProfile();
            assertThat(profileGroups).isNotEmpty();

            // Pick first available profile
            String testProfile = profileGroups.keySet().iterator().next();

            System.out.println("Exporting profile type: " + testProfile);
            List<String> exportedFiles = dataExport.exportByProfile(testProfile, exportDir);

            assertThat(exportedFiles)
                    .as("Should return list of exported files")
                    .isNotNull();

            if (!exportedFiles.isEmpty()) {
                System.out.println("\nExported " + exportedFiles.size() + " file(s):");
                for (String filePath : exportedFiles) {
                    System.out.println("  - " + filePath);

                    File file = new File(filePath);
                    assertThat(file)
                            .as("File should exist: " + filePath)
                            .exists();

                    assertThat(file.getName())
                            .as("Filename should start with profile type")
                            .startsWith(testProfile);
                }
            }

            System.out.println("\n✓ Successfully exported graphs by profile");
            System.out.println("\nNext step: Implement generateExportReport() method\n");

        } catch (UnsupportedOperationException e) {
            fail("Method not implemented yet. Complete the exportByProfile() method in Task2_DataExport.java");
        }
    }

    @Test
    @Order(7)
    @DisplayName("5.7: Generate export report")
    void testGenerateExportReport() {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("TEST 5.7: Generate Export Report (CIM-Aware)");
        System.out.println("=".repeat(60));

        try {
            String report = dataExport.generateExportReport();

            assertThat(report)
                    .as("Report should not be null or empty")
                    .isNotNull()
                    .isNotEmpty();

            System.out.println(report);

            assertThat(report)
                    .as("Report should contain profile information")
                    .containsAnyOf("EQ", "TP", "SSH", "SV", "Profile", "Total");

            System.out.println("✓ Successfully generated export report");
            System.out.println("\nNext step: Implement exportAllGraphs() method\n");

        } catch (UnsupportedOperationException e) {
            fail("Method not implemented yet. Complete the generateExportReport() method in Task2_DataExport.java");
        }
    }

    @Test
    @Order(8)
    @DisplayName("5.8: Export all graphs to directory")
    void testExportAllGraphs() {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("TEST 5.8: Export All Graphs");
        System.out.println("=".repeat(60));

        try {
            String exportDir = OUTPUT_DIR + "/all_graphs";

            System.out.println("Exporting all graphs to: " + exportDir);
            List<String> exportedFiles = dataExport.exportAllGraphs(exportDir);

            System.out.println("\nExported " + exportedFiles.size() + " files:");
            for (String filePath : exportedFiles) {
                System.out.println("  - " + filePath);
            }

            assertThat(exportedFiles)
                    .as("Should return list of exported files")
                    .isNotNull()
                    .isNotEmpty();

            // Verify all files exist
            for (String filePath : exportedFiles) {
                File file = new File(filePath);
                assertThat(file)
                        .as("File should exist: " + filePath)
                        .exists();

                assertThat(file.length())
                        .as("File should not be empty: " + filePath)
                        .isGreaterThan(0);
            }

            System.out.println("\n✓ Successfully exported all graphs");
            System.out.println("\n" + "=".repeat(60));
            System.out.println("TASK 5 COMPLETE! 🎉");
            System.out.println("=".repeat(60));
            System.out.println("\nYou've successfully implemented CIM-aware data export:");
            System.out.println("  • List all graphs in Fuseki");
            System.out.println("  • Download individual graphs");
            System.out.println("  • Extract CGMES profile types");
            System.out.println("  • Group graphs by profile");
            System.out.println("  • Export graphs with profile-aware naming");
            System.out.println("  • Generate comprehensive export reports");
            System.out.println("\nThis is real-world CGMES data management!");
            System.out.println("Check the exported files in: " + exportDir);
            System.out.println("=".repeat(60) + "\n");

        } catch (UnsupportedOperationException e) {
            fail("Method not implemented yet. Complete the exportAllGraphs() method in Task2_DataExport.java");
        }
    }

    @AfterAll
    static void cleanup() {
        System.out.println("\nTest cleanup complete");
    }
}
