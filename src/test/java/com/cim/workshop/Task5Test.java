package com.cim.workshop;

import com.cim.workshop.exercises.Task5_CustomShacl_Optional;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.riot.RDFDataMgr;
import org.apache.jena.shacl.ValidationReport;
import org.junit.jupiter.api.*;
import static org.assertj.core.api.Assertions.*;

import java.io.File;
import java.util.Map;

/**
 * Tests for Task 5: Create Custom SHACL Rules (Optional)
 *
 * Run with: mvn test -Dtest=Task5Test
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class Task5Test {

    private static final String TEST_FILE = "assets/grid_model_cgmes/PowerFlow/PowerFlow_EQ.xml";
    private static Task5_CustomShacl_Optional customShacl;
    private static Model testData;

    @BeforeAll
    static void setup() {
        customShacl = new Task5_CustomShacl_Optional();

        // Load test data
        testData = ModelFactory.createDefaultModel();
        RDFDataMgr.read(testData, TEST_FILE);

        System.out.println("Starting Custom SHACL Creation Tests");
        System.out.println("Test data loaded: " + testData.size() + " triples");
    }

    @Test
    @Order(1)
    @DisplayName("4.1: Create basic ACLineSegment shape")
    void testCreateACLineSegmentShape() {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("TEST 4.1: Create Basic SHACL Shape");
        System.out.println("=".repeat(60));

        try {
            Model shapesModel = customShacl.createACLineSegmentShape();

            assertThat(shapesModel)
                    .as("Shapes model should not be null")
                    .isNotNull();

            assertThat(shapesModel.size())
                    .as("Shapes model should contain triples")
                    .isGreaterThan(0);

            System.out.println("Created shape with " + shapesModel.size() + " triples");

            // Print the shape
            customShacl.printShapes(shapesModel);

            System.out.println("\n✓ Successfully created basic SHACL shape");
            System.out.println("\nNext step: Implement createConnectivityShape() method\n");

        } catch (UnsupportedOperationException e) {
            fail("Method not implemented yet. Complete the createACLineSegmentShape() method in Task5_CustomShacl_Optional.java");
        }
    }

    @Test
    @Order(2)
    @DisplayName("4.2: Create connectivity validation shape")
    void testCreateConnectivityShape() {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("TEST 4.2: Create Connectivity Shape");
        System.out.println("=".repeat(60));

        try {
            Model shapesModel = customShacl.createConnectivityShape();

            assertThat(shapesModel)
                    .as("Shapes model should not be null")
                    .isNotNull();

            assertThat(shapesModel.size())
                    .as("Shapes model should contain multiple constraints")
                    .isGreaterThan(5);

            System.out.println("Created connectivity shapes with " + shapesModel.size() + " triples");

            customShacl.printShapes(shapesModel);

            System.out.println("\n✓ Successfully created connectivity shapes");
            System.out.println("\nNext step: Implement createSparqlConstraintShape() method\n");

        } catch (UnsupportedOperationException e) {
            fail("Method not implemented yet. Complete the createConnectivityShape() method in Task5_CustomShacl_Optional.java");
        }
    }

    @Test
    @Order(3)
    @DisplayName("4.3: Create SHACL-SPARQL constraint")
    void testCreateSparqlConstraintShape() {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("TEST 4.3: Create SHACL-SPARQL Constraint");
        System.out.println("=".repeat(60));

        try {
            Model shapesModel = customShacl.createSparqlConstraintShape();

            assertThat(shapesModel)
                    .as("Shapes model should not be null")
                    .isNotNull();

            assertThat(shapesModel.size())
                    .as("Shapes model should contain SPARQL constraint")
                    .isGreaterThan(0);

            System.out.println("Created SPARQL constraint shape with " + shapesModel.size() + " triples");

            customShacl.printShapes(shapesModel);

            System.out.println("\n✓ Successfully created SHACL-SPARQL constraint");
            System.out.println("\nNext step: Implement saveShaclShapes() method\n");

        } catch (UnsupportedOperationException e) {
            fail("Method not implemented yet. Complete the createSparqlConstraintShape() method in Task5_CustomShacl_Optional.java");
        }
    }

    @Test
    @Order(4)
    @DisplayName("4.4: Save SHACL shapes to file")
    void testSaveShaclShapes() {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("TEST 4.4: Save SHACL Shapes");
        System.out.println("=".repeat(60));

        try {
            Model shapesModel = customShacl.createACLineSegmentShape();
            String outputPath = "output/my_custom_shapes.ttl";

            customShacl.saveShaclShapes(shapesModel, outputPath);

            File savedFile = new File(outputPath);
            assertThat(savedFile)
                    .as("Saved shapes file should exist")
                    .exists();

            // Load it back to verify
            Model loadedModel = ModelFactory.createDefaultModel();
            RDFDataMgr.read(loadedModel, outputPath);

            assertThat(loadedModel.size())
                    .as("Loaded model should have same size as original")
                    .isEqualTo(shapesModel.size());

            System.out.println("✓ Successfully saved and verified SHACL shapes");
            System.out.println("File saved to: " + outputPath);
            System.out.println("\nNext step: Implement validateWithCustomShapes() method\n");

        } catch (UnsupportedOperationException e) {
            fail("Method not implemented yet. Complete the saveShaclShapes() method in Task5_CustomShacl_Optional.java");
        }
    }

    @Test
    @Order(5)
    @DisplayName("4.5: Validate with custom shapes")
    void testValidateWithCustomShapes() {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("TEST 4.5: Validate with Custom Shapes");
        System.out.println("=".repeat(60));

        try {
            Model shapesModel = customShacl.createACLineSegmentShape();

            ValidationReport report = customShacl.validateWithCustomShapes(testData, shapesModel);

            assertThat(report)
                    .as("Validation report should not be null")
                    .isNotNull();

            System.out.println("Validation complete!");
            System.out.println("  Conforms: " + report.conforms());
            System.out.println("  Violations: " + report.getEntries().size());

            if (!report.conforms()) {
                System.out.println("\nFound violations using custom shapes:");
                report.getEntries().forEach(entry -> {
                    System.out.println("  - " + entry.message());
                });
            }

            System.out.println("\n✓ Successfully validated with custom shapes");
            System.out.println("\nNext step: Implement compareShaclAndSparql() method\n");

        } catch (UnsupportedOperationException e) {
            fail("Method not implemented yet. Complete the validateWithCustomShapes() method in Task5_CustomShacl_Optional.java");
        }
    }

    @Test
    @Order(6)
    @DisplayName("4.6: Compare SHACL and SPARQL approaches")
    void testCompareShaclAndSparql() {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("TEST 4.6: Compare SHACL and SPARQL");
        System.out.println("=".repeat(60));
        System.out.println("This demonstrates the connection between SHACL and SPARQL");
        System.out.println("=".repeat(60));

        try {
            Map<String, Integer> results = customShacl.compareShaclAndSparql(testData);

            assertThat(results)
                    .as("Results map should not be null")
                    .isNotNull()
                    .containsKeys("shacl", "sparql");

            int shaclCount = results.get("shacl");
            int sparqlCount = results.get("sparql");

            System.out.println("\nComparison Results:");
            System.out.println("  SHACL validation violations: " + shaclCount);
            System.out.println("  SPARQL query results: " + sparqlCount);

            System.out.println("\nAnalysis:");
            if (shaclCount == sparqlCount) {
                System.out.println("  ✓ Both approaches found the same violations!");
                System.out.println("  This shows that SHACL and SPARQL are complementary.");
            } else {
                System.out.println("  The counts differ, which may be expected depending on");
                System.out.println("  how the constraints are defined.");
            }

            System.out.println("\n✓ Successfully compared SHACL and SPARQL approaches");

            System.out.println("\n" + "=".repeat(60));
            System.out.println("TASK 5 COMPLETE!");
            System.out.println("=".repeat(60));
            System.out.println("\nYou have successfully:");
            System.out.println("  ✓ Created SHACL shapes programmatically");
            System.out.println("  ✓ Built connectivity validation rules");
            System.out.println("  ✓ Created SHACL-SPARQL constraints");
            System.out.println("  ✓ Saved shapes to Turtle files");
            System.out.println("  ✓ Validated data with custom shapes");
            System.out.println("  ✓ Compared SHACL and SPARQL approaches");
            System.out.println("\n" + "=".repeat(60));
            System.out.println("CONGRATULATIONS!");
            System.out.println("=".repeat(60));
            System.out.println("You have completed all workshop tasks!");
            System.out.println("\nYou now know how to:");
            System.out.println("  • Import CIM/CGMES files into graph databases");
            System.out.println("  • Query power grid data with SPARQL");
            System.out.println("  • Validate data quality with SHACL");
            System.out.println("  • Create custom validation rules");
            System.out.println("\nThank you for participating in the CIM Technical Workshop!\n");

        } catch (UnsupportedOperationException e) {
            fail("Method not implemented yet. Complete the compareShaclAndSparql() method in Task5_CustomShacl_Optional.java");
        }
    }
}
