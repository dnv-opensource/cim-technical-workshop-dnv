package com.cim.workshop.exercises;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.riot.Lang;
import org.apache.jena.riot.RDFDataMgr;
import org.apache.jena.shacl.ShaclValidator;
import org.apache.jena.shacl.Shapes;
import org.apache.jena.shacl.ValidationReport;
import org.apache.jena.shacl.lib.ShLib;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * TASK 4: Validate Instance Files with SHACL
 * ═════════════════════════════════════════════════════════════
 *
 * OBJECTIVE:
 * Learn how to validate CIM/CGMES data using SHACL (Shapes Constraint Language)
 * to ensure data quality and identify errors.
 *
 * BACKGROUND:
 * - SHACL is a W3C standard for validating RDF data
 * - SHACL defines "shapes" that describe constraints on data
 * - Validation produces a report showing violations
 * - ENTSO-E provides official SHACL rules for CGMES validation
 *
 * SHACL Basics:
 * -------------
 * A SHACL shape defines constraints on resources of a certain type.
 * Example:
 * :ACLineSegmentShape a sh:NodeShape ;
 * sh:targetClass cim:ACLineSegment ;
 * sh:property [
 * sh:path cim:Equipment.Terminal ;
 * sh:minCount 2 ;
 * sh:message "ACLineSegment must have at least 2 terminals" ;
 * ] .
 *
 * YOUR TASK:
 * Complete the methods below to:
 * 1. Load SHACL shapes from files
 * 2. Validate CIM data against SHACL rules
 * 3. Parse and analyze validation reports
 * 4. Identify specific violations (e.g., missing terminals)
 *
 * TESTING:
 * Run: mvn test -Dtest=Task4Test
 */
public class Task4_ShaclValidation {

    /**
     * Load a single SHACL shapes file
     */
    public Model loadShaclShapes(String filePath) {
        Model model = ModelFactory.createDefaultModel();
        RDFDataMgr.read(model, filePath, Lang.TURTLE);
        return model;
    }

    /**
     * Load all SHACL shape files from a directory
     *
     * SHACL files define validation rules (shapes and constraints).
     */
    public Model loadAllShaclShapes(String directoryPath) {
        Model mergedModel = ModelFactory.createDefaultModel();
        File directory = new File(directoryPath);

        if (!directory.exists() || !directory.isDirectory()) {
            throw new IllegalArgumentException("Invalid directory: " + directoryPath);
        }

        // TODO: Get all .ttl files from directory
        // HINT: directory.listFiles((dir, name) -> name.toLowerCase().endsWith(".ttl"))
        File[] ttlFiles = null; // FIX THIS

        if (ttlFiles != null) {
            for (File file : ttlFiles) {
                try {
                    // TODO: Load each file using loadShaclShapes() method
                    // HINT: Model shapeModel = loadShaclShapes(file.getAbsolutePath());
                    // TODO: Add the loaded model to mergedModel
                    // HINT: mergedModel.add(shapeModel);
                } catch (Exception e) {
                    System.err.println("Error loading SHACL file: " + file.getAbsolutePath());
                    e.printStackTrace();
                }
            }
        }
        return mergedModel;
    }

    /**
     * Validate data against SHACL shapes
     *
     * Returns a report containing all constraint violations.
     */
    public ValidationReport validate(Model dataModel, Model shapesModel) {
        // TODO: Parse shapes from model
        // HINT: Shapes.parse(shapesModel.getGraph())
        Shapes shapes = null; // FIX THIS

        // TODO: Run validation and return report
        // HINT: ShaclValidator.get().validate(shapes.getGraph(), dataModel.getGraph())
        return null; // FIX THIS
    }

    /**
     * Check if validation passed
     */
    public boolean isValid(ValidationReport report) {
        // TODO: Return validation result
        // HINT: report.conforms()
        return false; // FIX THIS
    }

    /**
     * Get count of violations
     */
    public int getViolationCount(ValidationReport report) {
        // TODO: Return number of violations
        // HINT: report.getEntries().size()
        return 0; // FIX THIS
    }

    /**
     * Extract violation details from report
     *
     * Parse each violation and extract key information.
     */
    public List<Map<String, String>> getViolationDetails(ValidationReport report) {
        List<Map<String, String>> violations = new ArrayList<>();

        // TODO: Iterate through report entries
        // HINT: for (ReportEntry entry : report.getEntries())
        // For each entry, create a HashMap with:
        // - "focusNode": entry.focusNode()
        // - "message": entry.message()
        // - "path": entry.resultPath() (may be null, convert to string)
        // - "value": entry.value() (may be null, convert to string)
        // Add the map to violations list

        return violations;
    }

    /**
     * TODO: Find violations for a specific constraint
     *
     * Filter violations to find those related to a specific issue.
     * For example, find all ACLineSegments missing terminals.
     *
     * @param report         The validation report
     * @param messagePattern A pattern to match in violation messages
     * @return List of violations matching the pattern
     */
    public List<Map<String, String>> findViolationsByMessage(
            ValidationReport report,
            String messagePattern) {
        // TODO: Implement this method
        // HINT: Get all violations using getViolationDetails()
        // HINT: Filter violations where message contains messagePattern
        // HINT: Use String.contains() or String.matches()

        throw new UnsupportedOperationException("TODO: Implement findViolationsByMessage method");
    }

    /**
     * TODO: Generate a human-readable validation report
     *
     * Create a formatted string summarizing the validation results.
     *
     * The report should include:
     * - Whether validation passed or failed
     * - Total number of violations
     * - Details of each violation (limited to first 20)
     *
     * @param report The validation report
     * @return Formatted report string
     */
    public String generateReport(ValidationReport report) {
        // TODO: Implement this method
        // HINT: Use StringBuilder to build the report
        // HINT: Check if validation passed
        // HINT: Include violation count
        // HINT: List violation details (use getViolationDetails())
        // HINT: Format nicely with separators and indentation

        throw new UnsupportedOperationException("TODO: Implement generateReport method");
    }

    /**
     * Helper: Print validation report to console
     */
    public void printReport(ValidationReport report) {
        System.out.println(generateReport(report));
    }

    /**
     * Helper: Validate a CIM file against SHACL rules
     */
    public ValidationReport validateFile(String cimFilePath, String shaclDirectory) {
        Model dataModel = ModelFactory.createDefaultModel();
        RDFDataMgr.read(dataModel, cimFilePath);

        Model shapesModel = loadAllShaclShapes(shaclDirectory);

        return validate(dataModel, shapesModel);
    }
}
