package com.cim.workshop.exercises;

import org.apache.jena.rdf.model.*;
import org.apache.jena.riot.RDFDataMgr;
import org.apache.jena.riot.RDFFormat;
import org.apache.jena.shacl.ShaclValidator;
import org.apache.jena.shacl.Shapes;
import org.apache.jena.shacl.ValidationReport;
import org.apache.jena.vocabulary.RDF;

import java.io.FileOutputStream;
import java.io.IOException;

/**
 * TASK 5: Create Custom SHACL Validation Rules
 * ═════════════════════════════════════════════════════════════
 *
 * OBJECTIVE:
 * Learn how to create custom SHACL validation rules programmatically
 * and understand the connection between SHACL and SPARQL.
 *
 * BACKGROUND:
 * SHACL rules can be created in two ways:
 * 1. Manually writing Turtle (.ttl) files
 * 2. Programmatically using Jena API
 *
 * SHACL and SPARQL Connection:
 * - SHACL shapes can include SPARQL constraints (sh:sparql)
 * - This allows complex validation logic using SPARQL queries
 * - Example: Check if ACLineSegments have exactly 2 terminals
 *
 * YOUR TASK:
 * 1. Create a SHACL shape programmatically to validate ACLineSegments
 * 2. Add constraints to check for:
 * - Minimum 2 terminals per ACLineSegment
 * - Each terminal must have a ConnectivityNode
 * 3. Write a custom SHACL rule using SPARQL constraints
 * 4. Save the shapes to a Turtle file
 *
 * TESTING:
 * Run: mvn test -Dtest=Task5Test
 *
 * SHACL COMPONENTS REFERENCE:
 * ---------------------------
 * sh:NodeShape - Defines a shape for validating nodes
 * sh:targetClass - Specifies which class this shape applies to
 * sh:property - Defines a property constraint
 * sh:path - The property path to validate
 * sh:minCount - Minimum number of values required
 * sh:maxCount - Maximum number of values allowed
 * sh:class - The expected class of values
 * sh:message - Error message when constraint violated
 * sh:severity - Severity level (sh:Violation, sh:Warning, sh:Info)
 * sh:sparql - SPARQL-based constraint
 */
public class Task5_CustomShacl_Optional {

    // Namespaces
    private static final String SHACL_NS = "http://www.w3.org/ns/shacl#";
    private static final String CIM_NS = "http://iec.ch/TC57/CIM100#";
    private static final String CUSTOM_SHAPES_NS = "http://workshop.cim.com/shapes#";

    /**
     * TODO: Create a basic SHACL shape for ACLineSegment validation
     *
     * Create a shape that validates ACLineSegments to ensure they have
     * at least 2 terminals.
     *
     * The shape should:
     * 1. Target cim:ACLineSegment class
     * 2. Define a property constraint on terminals
     * 3. Require minimum 2 terminals
     *
     * Steps:
     * 1. Create a model for the shapes
     * 2. Create a NodeShape resource
     * 3. Set sh:targetClass to cim:ACLineSegment
     * 4. Add sh:property with constraint on terminal count
     *
     * @return Model containing the SHACL shape
     */
    public Model createACLineSegmentShape() {
        Model shapesModel = ModelFactory.createDefaultModel();

        // Define namespaces
        shapesModel.setNsPrefix("sh", SHACL_NS);
        shapesModel.setNsPrefix("cim", CIM_NS);
        shapesModel.setNsPrefix("shapes", CUSTOM_SHAPES_NS);

        // TODO: Create the shape
        // HINT: Use shapesModel.createResource() to create the shape
        // HINT: Use RDF.type to set type to sh:NodeShape
        // HINT: Use sh:targetClass to specify cim:ACLineSegment
        // HINT: Create a blank node for sh:property
        // HINT: In the property, set sh:path, sh:minCount, sh:message

        throw new UnsupportedOperationException("TODO: Implement createACLineSegmentShape method");
    }

    /**
     * TODO: Create a shape with connectivity validation
     *
     * Create a more complex shape that validates:
     * 1. ACLineSegments must have at least 2 terminals
     * 2. Each terminal must be connected to a ConnectivityNode
     *
     * This requires multiple property constraints.
     *
     * @return Model containing the SHACL shapes
     */
    public Model createConnectivityShape() {
        Model shapesModel = ModelFactory.createDefaultModel();

        shapesModel.setNsPrefix("sh", SHACL_NS);
        shapesModel.setNsPrefix("cim", CIM_NS);
        shapesModel.setNsPrefix("shapes", CUSTOM_SHAPES_NS);

        // TODO: Create shape for ACLineSegment with terminal count constraint
        // TODO: Create shape for Terminal with ConnectivityNode constraint

        throw new UnsupportedOperationException("TODO: Implement createConnectivityShape method");
    }

    /**
     * TODO: Create a SHACL-SPARQL constraint
     *
     * SHACL allows embedding SPARQL queries for complex validation.
     * Create a shape that uses SPARQL to validate that ACLineSegments
     * have exactly 2 terminals (not more, not less).
     *
     * The SPARQL constraint format:
     * sh:sparql [
     * sh:message "Error message" ;
     * sh:select """
     * SELECT $this
     * WHERE {
     * # SPARQL query that finds violations
     * # $this refers to the focus node being validated
     * }
     * """ ;
     * ]
     *
     * @return Model containing SHACL-SPARQL shapes
     */
    public Model createSparqlConstraintShape() {
        Model shapesModel = ModelFactory.createDefaultModel();

        shapesModel.setNsPrefix("sh", SHACL_NS);
        shapesModel.setNsPrefix("cim", CIM_NS);
        shapesModel.setNsPrefix("shapes", CUSTOM_SHAPES_NS);

        // TODO: Create a NodeShape with SPARQL constraint
        // HINT: Use sh:sparql property
        // HINT: Create a blank node for the SPARQL constraint
        // HINT: Set sh:select with SPARQL query
        // HINT: The query should find ACLineSegments without exactly 2 terminals

        String sparqlQuery = """
                PREFIX cim: <http://iec.ch/TC57/CIM100#>
                PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>

                SELECT $this
                WHERE {
                    # TODO: Write SPARQL query to find violations
                    # HINT: Count terminals for $this
                    # HINT: Filter where count != 2
                }
                """;

        throw new UnsupportedOperationException("TODO: Implement createSparqlConstraintShape method");
    }

    /**
     * TODO: Save SHACL shapes to a Turtle file
     *
     * Write the shapes model to a .ttl file so it can be reused.
     *
     * @param shapesModel The model containing SHACL shapes
     * @param outputPath  Path to save the Turtle file
     */
    public void saveShaclShapes(Model shapesModel, String outputPath) {
        // TODO: Implement this method
        // HINT: Use RDFDataMgr.write()
        // HINT: Use RDFFormat.TURTLE
        // HINT: Use try-with-resources for FileOutputStream

        throw new UnsupportedOperationException("TODO: Implement saveShaclShapes method");
    }

    /**
     * TODO: Validate using custom shapes
     *
     * Apply your custom SHACL shapes to validate CIM data.
     *
     * @param dataModel         The CIM data to validate
     * @param customShapesModel Your custom SHACL shapes
     * @return ValidationReport
     */
    public ValidationReport validateWithCustomShapes(Model dataModel, Model customShapesModel) {
        // TODO: Implement this method
        // HINT: Similar to Task 4's validate method
        // HINT: Use Shapes.parse() and ShaclValidator.get().validate()

        throw new UnsupportedOperationException("TODO: Implement validateWithCustomShapes method");
    }

    /**
     * TODO: Compare SHACL and SPARQL approaches
     *
     * This method demonstrates the connection between SHACL and SPARQL.
     * Implement the same validation using both approaches and compare results.
     *
     * Validation rule: Find ACLineSegments without terminals
     *
     * @param dataModel The CIM data
     * @return Map with "shacl" and "sparql" result counts
     */
    public java.util.Map<String, Integer> compareShaclAndSparql(Model dataModel) {
        java.util.Map<String, Integer> results = new java.util.HashMap<>();

        // TODO: Approach 1 - Use SHACL validation
        // Create a shape that finds ACLineSegments without terminals
        // Count violations

        // TODO: Approach 2 - Use direct SPARQL query
        // Write SPARQL query to find same violations
        // Count results

        // Compare and return both counts

        throw new UnsupportedOperationException("TODO: Implement compareShaclAndSparql method");
    }

    /**
     * Helper: Create property for shape
     */
    private Resource createPropertyConstraint(
            Model model,
            String path,
            int minCount,
            String message) {
        Property shPath = model.createProperty(SHACL_NS + "path");
        Property shMinCount = model.createProperty(SHACL_NS + "minCount");
        Property shMessage = model.createProperty(SHACL_NS + "message");

        Resource property = model.createResource();
        property.addProperty(shPath, model.createResource(path));
        property.addProperty(shMinCount, model.createTypedLiteral(minCount));
        property.addProperty(shMessage, message);

        return property;
    }

    /**
     * Helper: Print shape model in Turtle format
     */
    public void printShapes(Model shapesModel) {
        System.out.println("\nSHACL Shapes (Turtle format):");
        System.out.println("=".repeat(60));
        RDFDataMgr.write(System.out, shapesModel, RDFFormat.TURTLE_PRETTY);
        System.out.println("=".repeat(60));
    }
}
