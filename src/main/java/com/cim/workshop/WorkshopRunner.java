package com.cim.workshop;

import java.util.Scanner;

/**
 * Main entry point for the CIM Technical Workshop
 * This class helps navigate through different workshop tasks
 */
public class WorkshopRunner {

    private static final String BANNER = """
            ╔═══════════════════════════════════════════════════════════╗
            ║                                                           ║
            ║         CIM/CGMES Technical Workshop                      ║
            ║         Working with Power Grid Models                    ║
            ║                                                           ║
            ╚═══════════════════════════════════════════════════════════╝
            """;

    private static final String MENU = """

            Workshop Tasks:
            ───────────────────────────────────────────────────────────
            1. Import CIM files into Graph Database
            2. Query datasets with SPARQL
            3. Validate instance files with SHACL
            4. Create custom SHACL validation rules

            0. Exit

            Enter your choice: """;

    public static void main(String[] args) {
        System.out.println(BANNER);
        System.out.println("Welcome to the CIM Technical Workshop!");
        System.out.println("This workshop will teach you how to work with CIM/CGMES files.\n");

        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        while (running) {
            System.out.print(MENU);

            try {
                int choice = scanner.nextInt();
                scanner.nextLine(); // consume newline

                switch (choice) {
                    case 1 -> showTask1Info();
                    case 2 -> showTask2Info();
                    case 3 -> showTask3Info();
                    case 4 -> showTask4Info();
                    case 0 -> {
                        System.out.println("\nThank you for participating in the workshop!");
                        running = false;
                    }
                    default -> System.out.println("\nInvalid choice. Please try again.");
                }
            } catch (Exception e) {
                System.out.println("\nInvalid input. Please enter a number.");
                scanner.nextLine(); // clear buffer
            }
        }

        scanner.close();
    }

    private static void showTask1Info() {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("TASK 1: Import CIM Files into Graph Database");
        System.out.println("=".repeat(60));
        System.out.println("""
                Duration: 30 minutes

                In this task, you will:
                • Learn about Apache Jena and RDF graphs
                • Import CIM/CGMES files into Fuseki graph database
                • Store each dataset with its file mRID as a named graph
                • View and analyze the data in the database

                Files to complete:
                • exercises/Task1_DataImport.java

                Run tests with:
                mvn test -Dtest=Task1Test

                """);
    }

    private static void showTask2Info() {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("TASK 2: Query Datasets with SPARQL");
        System.out.println("=".repeat(60));
        System.out.println("""
                Duration: 1 hour

                In this task, you will:
                • Learn SPARQL basics and theory
                • Write queries to extract specific information
                • Find connected switches for each AC line
                • Identify faulty ACLineSegments without connections

                Files to complete:
                • exercises/Task2_SparqlQueries.java

                Run tests with:
                mvn test -Dtest=Task2Test

                """);
    }

    private static void showTask3Info() {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("TASK 3: Validate with SHACL");
        System.out.println("=".repeat(60));
        System.out.println("""
                Duration: 1 hour

                In this task, you will:
                • Learn SHACL basics and theory
                • Use ENTSO-E SHACL files to validate CIM data
                • Identify ACLineSegments without terminals or nodes
                • Analyze validation reports

                Files to complete:
                • exercises/Task3_ShaclValidation.java

                Run tests with:
                mvn test -Dtest=Task3Test

                """);
    }

    private static void showTask4Info() {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("TASK 4: Create Custom SHACL Rules");
        System.out.println("=".repeat(60));
        System.out.println("""
                Duration: 1 hour

                In this task, you will:
                • Learn how to create SHACL rules manually
                • Create rules to identify remaining errors
                • Understand the connection between SHACL and SPARQL
                • Apply custom validation rules

                Files to complete:
                • src/main/java/com/cim/workshop/exercises/Task4_CustomShacl.java
                • output/custom_validation_rules.ttl (generated)

                Run tests with:
                mvn test -Dtest=Task4Test

                """);
    }
}
