package com.cim.workshop.utils;

import org.apache.jena.query.*;
import org.apache.jena.rdfconnection.RDFConnection;
import org.apache.jena.rdfconnection.RDFConnectionFactory;
import org.apache.jena.update.UpdateExecutionFactory;
import org.apache.jena.update.UpdateFactory;
import org.apache.jena.update.UpdateProcessor;
import org.apache.jena.update.UpdateRequest;

/**
 * Utility class for connecting to Apache Jena Fuseki
 * Provides helper methods for SPARQL queries and updates
 */
public class FusekiClient {

    private final String fusekiUrl;
    private final String datasetName;

    /**
     * Create a Fuseki client
     * @param fusekiUrl Base URL of Fuseki server (e.g., http://localhost:3030)
     * @param datasetName Name of the dataset to work with
     */
    public FusekiClient(String fusekiUrl, String datasetName) {
        this.fusekiUrl = fusekiUrl;
        this.datasetName = datasetName;
    }

    /**
     * Get the full query endpoint URL
     */
    public String getQueryEndpoint() {
        return fusekiUrl + "/" + datasetName + "/query";
    }

    /**
     * Get the full update endpoint URL
     */
    public String getUpdateEndpoint() {
        return fusekiUrl + "/" + datasetName + "/update";
    }

    /**
     * Get the full data endpoint URL (for HTTP POST)
     */
    public String getDataEndpoint() {
        return fusekiUrl + "/" + datasetName + "/data";
    }

    /**
     * Get the full GSP endpoint URL (Graph Store Protocol)
     */
    public String getGspEndpoint() {
        return fusekiUrl + "/" + datasetName + "/data";
    }

    /**
     * Execute a SPARQL SELECT query
     * @param sparqlQuery The SPARQL SELECT query string
     * @return ResultSet containing the query results
     */
    public ResultSet executeSelect(String sparqlQuery) {
        try (RDFConnection conn = RDFConnectionFactory.connect(fusekiUrl + "/" + datasetName)) {
            Query query = QueryFactory.create(sparqlQuery);
            QueryExecution qexec = conn.query(query);
            return ResultSetFactory.copyResults(qexec.execSelect());
        }
    }

    /**
     * Execute a SPARQL ASK query
     * @param sparqlQuery The SPARQL ASK query string
     * @return boolean result
     */
    public boolean executeAsk(String sparqlQuery) {
        try (RDFConnection conn = RDFConnectionFactory.connect(fusekiUrl + "/" + datasetName)) {
            Query query = QueryFactory.create(sparqlQuery);
            try (QueryExecution qexec = conn.query(query)) {
                return qexec.execAsk();
            }
        }
    }

    /**
     * Execute a SPARQL UPDATE query
     * @param sparqlUpdate The SPARQL UPDATE query string
     */
    public void executeUpdate(String sparqlUpdate) {
        UpdateRequest update = UpdateFactory.create(sparqlUpdate);
        UpdateProcessor processor = UpdateExecutionFactory.createRemote(
            update, getUpdateEndpoint()
        );
        processor.execute();
    }

    /**
     * Check if Fuseki server is accessible
     * @return true if server is accessible
     */
    public boolean isServerAccessible() {
        try {
            String testQuery = "ASK { ?s ?p ?o }";
            executeAsk(testQuery);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Get the count of triples in a named graph
     * @param graphUri The URI of the named graph
     * @return Number of triples
     */
    public long getTripleCount(String graphUri) {
        String query = """
            SELECT (COUNT(*) as ?count)
            WHERE {
                GRAPH <%s> {
                    ?s ?p ?o
                }
            }
            """.formatted(graphUri);

        ResultSet results = executeSelect(query);
        if (results.hasNext()) {
            QuerySolution solution = results.next();
            return solution.getLiteral("count").getLong();
        }
        return 0;
    }

    /**
     * List all named graphs in the dataset
     * @return ResultSet with graph URIs
     */
    public ResultSet listGraphs() {
        String query = """
            SELECT DISTINCT ?g
            WHERE {
                GRAPH ?g { ?s ?p ?o }
            }
            ORDER BY ?g
            """;
        return executeSelect(query);
    }
}
