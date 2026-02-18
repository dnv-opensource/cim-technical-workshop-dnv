package com.cim.workshop.exercises;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.riot.RDFDataMgr;
import org.apache.jena.riot.RDFFormat;
import org.apache.jena.query.*;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

/**
 * TASK 2: CIM Data Export from Graph Database (25 minutes)
 * ═════════════════════════════════════════════════════════════
 *
 * OBJECTIVE:
 * Learn how to export CIM/CGMES RDF data from Apache Jena Fuseki.
 *
 * BACKGROUND:
 * - CGMES uses multiple profile types: EQ (Equipment), TP (Topology),
 * SSH (Steady State Hypothesis), SV (State Variables), etc.
 * - Profile information is stored in md:FullModel metadata
 * - Different export strategies needed for different profiles
 * - Some profiles depend on others (SSH needs EQ, SV needs SSH+TP)
 *
 * WORKSHOP FOCUS (25 minutes):
 * Implement these 2 methods:
 * 1. listAllGraphs() - Query Fuseki for all named graphs
 * 2. downloadFromFuseki() - Download a specific graph
 *
 * BONUS (Optional):
 * - exportGraphToFile(), exportAllGraphs()
 * - extractProfileType(), groupGraphsByProfile()
 * - exportByProfile(), generateExportReport()
 *
 * TESTING:
 * Run: mvn test -Dtest=Task2Test
 *
 * CIM/CGMES PROFILE TYPES:
 * - EQ: Equipment profile (substations, lines, transformers)
 * - TP: Topology profile (connectivity nodes, terminals)
 * - SSH: Steady State Hypothesis (switch positions, setpoints)
 * - SV: State Variables (voltages, power flows)
 * - DL: Diagram Layout (visual representation)
 * - GL: Geographical Location
 * - DY: Dynamics
 * - EQ_BD: Equipment Boundary
 * - TP_BD: Topology Boundary
 *
 * HINTS:
 * - Use HTTP GET to download from Fuseki's GSP endpoint
 * - Query md:FullModel to extract profile information
 * - Profile type is in md:Model.profile property
 * - Use SPARQL to filter graphs by profile type
 */
public class Task2_DataExport {

    private final String fusekiUrl;
    private final String datasetName;

    // CIM Namespaces
    private static final String MD_NS = "http://iec.ch/TC57/61970-552/ModelDescription/1#";
    private static final String CIM_NS = "http://iec.ch/TC57/CIM100#";

    public Task2_DataExport(String fusekiUrl, String datasetName) {
        this.fusekiUrl = fusekiUrl;
        this.datasetName = datasetName;
    }

    /**
     * List all named graphs in the Fuseki database
     *
     * Use SPARQL to query for all graph URIs.
     */
    public List<String> listAllGraphs() {
        List<String> graphs = new ArrayList<>();
        
        // TODO: Define SPARQL query to list all graphs
        // HINT: SELECT DISTINCT ?g WHERE { GRAPH ?g { ?s ?p ?o } }
        String sparqlQuery = """        
                // YOUR QUERY HERE
                """;
        
        String endpoint = fusekiUrl + "/" + datasetName + "/query";
        
        // TODO: Create QueryExecution and execute the query
        // HINT: QueryExecutionFactory.sparqlService(endpoint, sparqlQuery)
        try (QueryExecution qexec = null /* FIX THIS */) {
            // TODO: Execute SELECT query
            // HINT: qexec.execSelect()
            ResultSet results = null; // FIX THIS
            
            // TODO: Iterate through results and extract graph URIs
            // HINT: while (results.hasNext())
            // HINT: QuerySolution solution = results.next()
            // HINT: solution.getResource("g").getURI()
            
        }
        
        return graphs;
    }

    /**
     * Download a named graph from Fuseki as a Jena Model
     *
     * Use HTTP GET to retrieve graph data via Graph Store Protocol.
     */
    public Model downloadFromFuseki(String graphUri) {
        Model model = ModelFactory.createDefaultModel();
        String endpoint = fusekiUrl + "/" + datasetName + "/data?graph=" + graphUri;
        
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            // TODO: Create HTTP GET request
            // HINT: new HttpGet(endpoint)
            HttpGet request = null; // FIX THIS
            
            try (CloseableHttpResponse response = httpClient.execute(request)) {
                // TODO: Get response entity as string
                // HINT: EntityUtils.toString(response.getEntity())
                String rdfContent = null; // FIX THIS
                
                // TODO: Read RDF content into model
                // HINT: RDFDataMgr.read(model, new StringReader(rdfContent), null, Lang.RDFXML)
                
            }
        } catch (Exception e) {
            throw new RuntimeException("Error downloading from Fuseki: " + e.getMessage(), e);
        }
        
        return model;
    }

    /**
     * [Optional] Export a named graph to a file
     *
     * This method downloads a graph from Fuseki and saves it as an RDF/XML file.
     *
     * Steps:
     * 1. Download the graph using downloadFromFuseki()
     * 2. Create the output file
     * 3. Write the model to the file using RDFDataMgr
     * 4. Use RDF/XML format for compatibility
     *
     * @param graphUri       The URI of the named graph to export
     * @param outputFilePath Path where the file should be saved
     */
    public void exportGraphToFile(String graphUri, String outputFilePath) {
        // TODO: Implement this method
        // HINT: Use downloadFromFuseki() to get the model
        // HINT: Use RDFDataMgr.write() with FileOutputStream
        // HINT: Format: RDFFormat.RDFXML

        throw new UnsupportedOperationException("TODO: Implement exportGraphToFile method");
    }

    /**
     * [Optional] Export all graphs from Fuseki to separate files
     *
     * This method should:
     * 1. List all graphs in the database
     * 2. For each graph, generate a filename from its URI
     * 3. Export each graph to a separate file
     * 4. Return the list of created filenames
     *
     * Filename generation:
     * - Extract the UUID from URIs like
     * "urn:uuid:1c2ba832-cd6f-42a2-9a68-ced167d09ee3"
     * - Create filename like "graph_1c2ba832-cd6f-42a2-9a68-ced167d09ee3.xml"
     *
     * @param outputDirectory Directory where files should be saved
     * @return List of created file paths
     */
    public List<String> exportAllGraphs(String outputDirectory) {
        List<String> exportedFiles = new ArrayList<>();

        // TODO: Implement this method
        // HINT: Use listAllGraphs() to get all graph URIs
        // HINT: Create output directory if it doesn't exist
        // HINT: Extract filename from graph URI (use substring or split)
        // HINT: Use exportGraphToFile() for each graph

        throw new UnsupportedOperationException("TODO: Implement exportAllGraphs method");
    }

    /**
     * [BONUS - Optional] Extract profile type from a CIM model
     *
     * CIM models contain metadata about their profile type in md:FullModel.
     * The profile is specified using md:Model.profile property.
     *
     * Profile URIs look like:
     * - http://entsoe.eu/CIM/EquipmentCore/3/1 (EQ)
     * - http://entsoe.eu/CIM/Topology/4/1 (TP)
     * - http://entsoe.eu/CIM/SteadyStateHypothesis/1/1 (SSH)
     * - http://entsoe.eu/CIM/StateVariables/4/1 (SV)
     *
     * Extract the short profile name (EQ, TP, SSH, SV, etc.)
     *
     * Steps:
     * 1. Download the graph model
     * 2. Find the md:FullModel resource
     * 3. Get the md:Model.profile property value
     * 4. Extract the profile type from the URI
     *
     * @param graphUri The URI of the named graph
     * @return Profile type (e.g., "EQ", "TP", "SSH", "SV") or "UNKNOWN"
     */
    public String extractProfileType(String graphUri) {
        // TODO: Implement this method
        // HINT: Download the model first
        // HINT: Find md:FullModel resources in the model
        // HINT: Get property
        // http://iec.ch/TC57/61970-552/ModelDescription/1#Model.profile
        // HINT: Parse the profile URI to extract the type (look for keywords like
        // "Equipment", "Topology", etc.)
        // HINT: Map full names to short codes: Equipment→EQ, Topology→TP,
        // SteadyStateHypothesis→SSH, StateVariables→SV

        throw new UnsupportedOperationException("TODO: Implement extractProfileType method");
    }

    /**
     * [BONUS - Optional] Group all graphs by their profile type
     *
     * This method analyzes all graphs in the database and groups them
     * by their CGMES profile type (EQ, TP, SSH, SV, etc.).
     *
     * Returns a map where:
     * - Key: Profile type (e.g., "EQ", "TP", "SSH")
     * - Value: List of graph URIs with that profile type
     *
     * @return Map of profile type to list of graph URIs
     */
    public java.util.Map<String, List<String>> groupGraphsByProfile() {
        java.util.Map<String, List<String>> profileGroups = new java.util.HashMap<>();

        // TODO: Implement this method
        // HINT: Use listAllGraphs() to get all graphs
        // HINT: For each graph, use extractProfileType()
        // HINT: Build a map grouping graphs by profile
        // HINT: Use computeIfAbsent to handle new profile types

        throw new UnsupportedOperationException("TODO: Implement groupGraphsByProfile method");
    }

    /**
     * [BONUS - Optional] Export all graphs of a specific profile type
     *
     * This method exports only graphs matching a specific CGMES profile type.
     * For example, export all EQ (Equipment) files or all SSH files.
     *
     * The exported files should use profile-aware naming:
     * - "EQ_<uuid>.xml"
     * - "TP_<uuid>.xml"
     * - "SSH_<uuid>.xml"
     *
     * @param profileType     The profile type to export (e.g., "EQ", "TP", "SSH")
     * @param outputDirectory Directory where files should be saved
     * @return List of created file paths
     */
    public List<String> exportByProfile(String profileType, String outputDirectory) {
        List<String> exportedFiles = new ArrayList<>();

        // TODO: Implement this method
        // HINT: Use groupGraphsByProfile() to get all profiles
        // HINT: Get the list of graphs for the requested profile type
        // HINT: Export each graph with profile-aware filename
        // HINT: Create output directory if needed

        throw new UnsupportedOperationException("TODO: Implement exportByProfile method");
    }

    /**
     * [BONUS - Optional] Generate an export report
     *
     * Create a summary report of what's in the database:
     * - Total number of graphs
     * - Breakdown by profile type
     * - List of graph URIs for each profile
     *
     * This helps understand what data is available before exporting.
     *
     * @return Multi-line string report
     */
    public String generateExportReport() {
        // TODO: Implement this method
        // HINT: Use groupGraphsByProfile()
        // HINT: Build a formatted string with statistics
        // HINT: Show count for each profile type
        // HINT: List graph URIs under each profile

        throw new UnsupportedOperationException("TODO: Implement generateExportReport method");
    }

    /**
     * Helper method to get the GSP endpoint URL for downloading
     */
    private String getGspEndpoint(String graphUri) {
        return fusekiUrl + "/" + datasetName + "/data?graph=" + graphUri;
    }

    /**
     * Helper method to generate a filename from a graph URI
     */
    private String generateFilename(String graphUri) {
        // Extract UUID from URIs like "urn:uuid:xxxxx"
        if (graphUri.startsWith("urn:uuid:")) {
            String uuid = graphUri.substring("urn:uuid:".length());
            return "graph_" + uuid + ".xml";
        }
        // Fallback: use last part of URI
        String[] parts = graphUri.split("[/#]");
        return "graph_" + parts[parts.length - 1] + ".xml";
    }

    /**
     * Helper method to generate a profile-aware filename
     */
    private String generateProfileFilename(String graphUri, String profileType) {
        // Extract UUID from URIs like "urn:uuid:xxxxx"
        if (graphUri.startsWith("urn:uuid:")) {
            String uuid = graphUri.substring("urn:uuid:".length());
            return profileType + "_" + uuid + ".xml";
        }
        // Fallback
        String[] parts = graphUri.split("[/#]");
        return profileType + "_" + parts[parts.length - 1] + ".xml";
    }

    /**
     * Helper method to map profile URI to short code
     */
    private String mapProfileUriToCode(String profileUri) {
        if (profileUri == null)
            return "UNKNOWN";

        String uri = profileUri.toLowerCase();
        if (uri.contains("equipment"))
            return "EQ";
        if (uri.contains("topology"))
            return "TP";
        if (uri.contains("steadystatehypothesis"))
            return "SSH";
        if (uri.contains("statevariables"))
            return "SV";
        if (uri.contains("diagram"))
            return "DL";
        if (uri.contains("geographical"))
            return "GL";
        if (uri.contains("dynamics"))
            return "DY";
        if (uri.contains("boundary")) {
            if (uri.contains("equipment"))
                return "EQ_BD";
            if (uri.contains("topology"))
                return "TP_BD";
            return "BOUNDARY";
        }

        return "UNKNOWN";
    }
}
