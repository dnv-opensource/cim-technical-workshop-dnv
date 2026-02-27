package com.cim.workshop;

import com.cim.workshop.exercises.Task4_ShaclValidation;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.riot.RDFDataMgr;
import org.apache.jena.shacl.ValidationReport;
import org.junit.jupiter.api.*;
import static org.assertj.core.api.Assertions.*;

import java.io.File;
import java.util.List;
import java.util.Map;

/**
 * Tests for Task 4: SHACL Validation (30 minutes)
 *
 * WORKSHOP FOCUS - Core validation methods:
 * [REQUIRED] Test 4.1: loadAllShaclShapes()
 * [REQUIRED] Test 4.2: validate()
 * [REQUIRED] Test 4.3: isValid() & getViolationCount()
 * [REQUIRED] Test 4.4: getViolationDetails()
 *
 * BONUS TESTS - Advanced filtering (optional):
 * [BONUS] Tests 4.5-4.6: Additional methods
 *
 * Run with: mvn test -Dtest=Task4Test
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class Task4Test {

    private static final String SHACL_DIR = "assets/shacl_cgmes";
    private static final String TEST_FILE = "assets/grid_model_cgmes/PowerFlow/PowerFlow_EQ.xml";

    private static Task4_ShaclValidation shaclValidator;

    @BeforeAll
    static void setUp() {
        shaclValidator = new Task4_ShaclValidation();
        System.out.println("Starting SHACL Validation Tests");
    }

    @Test
    @Order(1)
    @DisplayName("4.1: Load SHACL shapes from file")
    void testLoadShaclShapes() {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("TEST 4.1: Load SHACL Shapes");
        System.out.println("=".repeat(60));

        try {
            String shaclFile = SHACL_DIR + "/61970-301_Equipment-AP-Con-Complex-SHACL_v3-0-0.ttl";
            File file = new File(shaclFile);

            assertThat(file)
                    .as("Test SHACL file should exist")
                    .exists();

            Model shapesModel = shaclValidator.loadShaclShapes(shaclFile);

            assertThat(shapesModel)
                    .as("Shapes model should not be null")
                    .isNotNull();

            long tripleCount = shapesModel.size();
            System.out.println("Loaded SHACL shapes with " + tripleCount + " triples");

            assertThat(tripleCount)
                    .as("Shapes model should contain triples")
                    .isGreaterThan(0);

            System.out.println("✓ Successfully loaded SHACL shapes");
            System.out.println("\nNext step: Implement loadAllShaclShapes() method\n");

        } catch (UnsupportedOperationException e) {
            fail("Method not implemented yet. Complete the loadShaclShapes() method in Task4_ShaclValidation.java");
        }
    }

    @Test
    @Order(2)
    @DisplayName("4.2: Load and merge multiple SHACL files")
    void testLoadAllShaclShapes() {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("TEST 4.2: Load All SHACL Shapes");
        System.out.println("=".repeat(60));

        try {
            Model mergedShapes = shaclValidator.loadAllShaclShapes(SHACL_DIR);

            assertThat(mergedShapes)
                    .as("Merged shapes model should not be null")
                    .isNotNull();

            long tripleCount = mergedShapes.size();
            System.out.println("Merged model contains " + tripleCount + " triples");

            assertThat(tripleCount)
                    .as("Merged model should contain many triples from all files")
                    .isGreaterThan(1000);

            System.out.println("✓ Successfully loaded and merged SHACL shapes");
            System.out.println("\nNext step: Implement validate() method\n");

        } catch (UnsupportedOperationException e) {
            fail("Method not implemented yet. Complete the loadAllShaclShapes() method in Task4_ShaclValidation.java");
        }
    }

    @Test
    @Order(3)
    @DisplayName("4.3: Validate CIM data against SHACL shapes")
    void testValidate() {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("TEST 4.3: Validate CIM Data");
        System.out.println("=".repeat(60));

        try {
            // Load data
            Model dataModel = ModelFactory.createDefaultModel();
            RDFDataMgr.read(dataModel, TEST_FILE);
            System.out.println("Loaded data model with " + dataModel.size() + " triples");

            // Load shapes (use subset for faster testing)
            String shaclFile = SHACL_DIR + "/61970-301_Equipment-AP-Con-Complex-SHACL_v3-0-0.ttl";
            Model shapesModel = shaclValidator.loadShaclShapes(shaclFile);
            System.out.println("Loaded SHACL shapes");

            // Validate
            System.out.println("Running validation...");
            ValidationReport report = shaclValidator.validate(dataModel, shapesModel);

            assertThat(report)
                    .as("Validation report should not be null")
                    .isNotNull();

            boolean isValid = shaclValidator.isValid(report);
            int violationCount = shaclValidator.getViolationCount(report);

            System.out.println("Validation complete!");
            System.out.println("  Conforms: " + isValid);
            System.out.println("  Violations: " + violationCount);

            System.out.println("\n✓ Successfully validated data against SHACL shapes");
            System.out.println("\nNext step: Implement getViolationDetails() method\n");

        } catch (UnsupportedOperationException e) {
            fail("Method not implemented yet. Complete the validate() method in Task4_ShaclValidation.java");
        }
    }

    @Test
    @Order(4)
    @DisplayName("4.4: Extract violation details")
    void testGetViolationDetails() {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("TEST 4.4: Extract Violation Details");
        System.out.println("=".repeat(60));

        try {
            // Load and validate
            Model dataModel = ModelFactory.createDefaultModel();
            RDFDataMgr.read(dataModel, TEST_FILE);

            String shaclFile = SHACL_DIR + "/61970-301_Equipment-AP-Con-Complex-SHACL_v3-0-0.ttl";
            Model shapesModel = shaclValidator.loadShaclShapes(shaclFile);

            ValidationReport report = shaclValidator.validate(dataModel, shapesModel);

            // Extract violations
            List<Map<String, String>> violations = shaclValidator.getViolationDetails(report);

            assertThat(violations)
                    .as("Violations list should not be null")
                    .isNotNull();

            System.out.println("Found " + violations.size() + " violations");

            if (!violations.isEmpty()) {
                System.out.println("\nSample violations:");
                int maxDisplay = Math.min(3, violations.size());
                for (int i = 0; i < maxDisplay; i++) {
                    Map<String, String> violation = violations.get(i);
                    System.out.println("\n  Violation " + (i + 1) + ":");
                    violation.forEach((key, value) -> {
                        System.out.println("    " + key + ": " + value);
                    });
                }
            } else {
                System.out.println("No violations found - data conforms to shapes!");
            }

            System.out.println("\n✓ Successfully extracted violation details");
            System.out.println("\nNext step: Implement findViolationsByMessage() method\n");

        } catch (UnsupportedOperationException e) {
            fail("Method not implemented yet. Complete the getViolationDetails() method in Task4_ShaclValidation.java");
        }
    }

    @Test
    @Order(5)
    @DisplayName("4.5: Find specific violations")
    void testFindViolationsByMessage() {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("TEST 4.5: Find Specific Violations");
        System.out.println("=".repeat(60));

        try {
            // Load and validate
            Model dataModel = ModelFactory.createDefaultModel();
            RDFDataMgr.read(dataModel, TEST_FILE);

            Model shapesModel = shaclValidator.loadAllShaclShapes(SHACL_DIR);
            ValidationReport report = shaclValidator.validate(dataModel, shapesModel);

            // Find violations related to terminals
            System.out.println("Searching for violations related to 'Terminal'...");
            List<Map<String, String>> terminalViolations = shaclValidator.findViolationsByMessage(
                    report, "Terminal");

            System.out.println("Found " + terminalViolations.size() + " violations mentioning 'Terminal'");

            if (!terminalViolations.isEmpty()) {
                System.out.println("\nSample terminal-related violations:");
                int maxDisplay = Math.min(3, terminalViolations.size());
                for (int i = 0; i < maxDisplay; i++) {
                    Map<String, String> violation = terminalViolations.get(i);
                    System.out.println("\n  " + (i + 1) + ". " + violation.get("message"));
                }
            }

            System.out.println("\n✓ Successfully filtered violations");
            System.out.println("\nNext step: Implement generateReport() method\n");

        } catch (UnsupportedOperationException e) {
            fail("Method not implemented yet. Complete the findViolationsByMessage() method in Task4_ShaclValidation.java");
        }
    }

    @Test
    @Order(6)
    @DisplayName("4.6: Generate human-readable report")
    void testGenerateReport() {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("TEST 4.6: Generate Validation Report");
        System.out.println("=".repeat(60));

        try {
            // Validate a file
            ValidationReport report = shaclValidator.validateFile(TEST_FILE, SHACL_DIR);

            // Generate report
            String reportText = shaclValidator.generateReport(report);

            assertThat(reportText)
                    .as("Report text should not be null or empty")
                    .isNotNull()
                    .isNotEmpty();

            System.out.println("\nGenerated Report:");
            System.out.println(reportText);

            assertThat(reportText)
                    .as("Report should contain validation status")
                    .containsAnyOf("PASSED", "FAILED");

            System.out.println("✓ Successfully generated validation report");
            System.out.println("\n" + "=".repeat(60));
            System.out.println("TASK 4 COMPLETE!");
            System.out.println("=".repeat(60));
            System.out.println("\nYou have successfully:");
            System.out.println("  ✓ Loaded SHACL shapes from files");
            System.out.println("  ✓ Validated CIM data against SHACL rules");
            System.out.println("  ✓ Extracted and analyzed violations");
            System.out.println("  ✓ Generated validation reports");
            System.out.println("\nNext: Move on to Task 5 - Create Custom SHACL Rules");
            System.out.println("Run: mvn test -Dtest=Task5Test\n");

        } catch (UnsupportedOperationException e) {
            fail("Method not implemented yet. Complete the generateReport() method in Task4_ShaclValidation.java");
        }
    }
}
