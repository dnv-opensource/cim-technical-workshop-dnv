package com.cim.workshop.exercises;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.ResIterator;
import org.apache.jena.rdf.model.StmtIterator;
import org.apache.jena.riot.RDFDataMgr;
import org.apache.jena.riot.RDFFormat;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.File;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * TASK 1: Import CIM Files into Graph Database (15 minutes)
 *
 * Learn how to import CIM/CGMES RDF files into Apache Jena Fuseki
 * and store them as named graphs using their Model IDs (mRIDs).
 *
 * BACKGROUND:
 * - CIM files are RDF/XML documents containing power grid data
 * - Each file has a unique Model ID (mRID) that identifies it
 * - Named graphs store multiple datasets separately in one database
 * - Fuseki uses Graph Store Protocol (GSP) for uploading data
 *
 * YOUR TASK:
 * Implement 3 methods:
 * 1. loadCimFile() - Load RDF/XML into Jena Model
 * 2. uploadToFuseki() - POST model to Fuseki GSP endpoint
 * 3. importDirectory() - Process all XML files in a folder
 *
 * Note: extractModelId() is provided - study it to understand mRID extraction
 *
 * Run: mvn test -Dtest=Task1Test
 */
public class Task1_DataImport {

    private final String fusekiUrl;
    private final String datasetName;

    public Task1_DataImport(String fusekiUrl, String datasetName) {
        this.fusekiUrl = fusekiUrl;
        this.datasetName = datasetName;
    }

    /**
     * PROVIDED: Extract the Model ID (mRID) from a CIM file
     *
     * Study this implementation to understand how CIM files are structured.
     * The mRID is found in md:FullModel resources.
     */
    public String extractModelId(String cimFilePath) {
        Model model = loadCimFile(cimFilePath);
        String mdNamespace = "http://iec.ch/TC57/61970-552/ModelDescription/1#";
        Property fullModelType = model.createProperty(mdNamespace + "FullModel");

        ResIterator iterator = model.listSubjectsWithProperty(
                model.createProperty("http://www.w3.org/1999/02/22-rdf-syntax-ns#type"),
                fullModelType);

        if (iterator.hasNext()) {
            return iterator.next().getURI();
        }

        StmtIterator stmts = model.listStatements();
        while (stmts.hasNext()) {
            Resource subject = stmts.next().getSubject();
            String uri = subject.getURI();
            if (uri != null && uri.startsWith("urn:uuid:")) {
                if (subject.hasProperty(model.createProperty(mdNamespace + "Model.created"))) {
                    return uri;
                }
            }
        }

        throw new RuntimeException("Could not find Model ID in file: " + cimFilePath);
    }

    /**
     * Load a CIM file into a Jena Model
     *
     * @param cimFilePath Path to the CIM/CGMES XML file
     * @return Jena Model containing the RDF data
     */
    public Model loadCimFile(String cimFilePath) {
        // TODO: Create a default model
        Model model = null; // FIX THIS

        // TODO: Load the RDF/XML file into the model
        // HINT: RDFDataMgr.read(model, cimFilePath)

        return model;
    }

    /**
     * Upload a model to Fuseki as a named graph
     *
     * Uses Graph Store Protocol (GSP) to POST RDF data to Fuseki.
     */
    public void uploadToFuseki(Model model, String graphUri) {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            StringWriter writer = new StringWriter();

            // TODO: Write model to writer as RDF/XML
            // HINT: RDFDataMgr.write(writer, model, RDFFormat.RDFXML)

            String endpoint = fusekiUrl + "/" + datasetName + "/data?graph=" + graphUri;

            // TODO: Create HTTP POST request with endpoint
            HttpPost request = null; // FIX THIS

            // TODO: Set Content-Type header to "application/rdf+xml"

            // TODO: Set request entity with RDF data
            // HINT: new StringEntity(writer.toString(), "UTF-8")

            try (CloseableHttpResponse response = httpClient.execute(request)) {
                int statusCode = response.getStatusLine().getStatusCode();
                if (statusCode < 200 || statusCode >= 300) {
                    throw new RuntimeException("Failed to upload. Status: " + statusCode);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Error uploading to Fuseki: " + e.getMessage(), e);
        }
    }

    /**
     * Import all CIM files from a directory
     *
     * Process all XML files: extract mRID, load model, upload to Fuseki.
     */
    public List<String> importDirectory(String directoryPath) {
        List<String> importedGraphs = new ArrayList<>();
        File directory = new File(directoryPath);

        if (!directory.exists() || !directory.isDirectory()) {
            throw new IllegalArgumentException("Invalid directory: " + directoryPath);
        }

        // TODO: Get all .xml files from directory
        // HINT: directory.listFiles((dir, name) -> name.toLowerCase().endsWith(".xml"))
        File[] xmlFiles = null; // FIX THIS

        if (xmlFiles != null) {
            for (File file : xmlFiles) {
                try {
                    System.out.println("Importing: " + file.getName());

                    // TODO: Extract mRID using the provided method
                    String mrid = null; // FIX THIS

                    // TODO: Load the CIM file into a model
                    Model model = null; // FIX THIS

                    // TODO: Upload model to Fuseki

                    // TODO: Add mrid to importedGraphs list

                } catch (Exception e) {
                    System.err.println("Error importing " + file.getName() + ": " + e.getMessage());
                }
            }
        }

        return importedGraphs;
    }

    /**
     * Helper method to get the GSP endpoint URL
     */
    private String getGspEndpoint(String graphUri) {
        return fusekiUrl + "/" + datasetName + "/data?graph=" + graphUri;
    }
}
