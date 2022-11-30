package com.jkutkutorg.textAnalyzer;

import jkutkut.SuperScanner;

import java.io.*;
import java.util.Scanner;

/**
 * Note: Enter the java executable file with an absolute path. It should be here:
 * ~/.jdks/corretto-18.0.2/bin/java
 */
public class Main {
    private static final String ANALYZER_JAR = "jars/hijo.jar";
    private static final String ANALYZER_JAVA = "com.jkutkutorg.textAnalyzer.Analyzer";

    public static void main(String[] args) {
        if (args.length != 1) {
            System.err.println("Please enter the java binary file to execute the JAR child processes.");
            System.exit(1);
        }
        final String JAVA = args[0];

        // Obtain data from user
        SuperScanner userInput = new SuperScanner.Es(System.in);
        System.out.println("Please, fill the data:");
        String archivoLectura = userInput.getFileName("- File to analyze: ");
        System.out.println();
        userInput.close();

        Integer result;
        for (int i = 0; i < Analyzer.OPTIONS.length; i++) {
            result = analyze(Analyzer.OPTIONS[i], archivoLectura, JAVA);
            if (result == null) {
                System.err.println("Child process failed to analyze the file.");
                break;
            }
            System.out.println(Analyzer.OPTIONS_NAMES[i] + ": " + result);
        }
    }

    private static Integer analyze(String mode, String file, String java) {
        ProcessBuilder pb = new ProcessBuilder(
                java,
                "-cp", ANALYZER_JAR,
                ANALYZER_JAVA,
                mode, file, Analyzer.STD_OUTPUT
        );

        // Arreglo de linux para subprocesos (visto y hablado en clase).
        pb.inheritIO()
            .redirectInput(ProcessBuilder.Redirect.PIPE)
            .redirectError(ProcessBuilder.Redirect.PIPE)
            .redirectOutput(ProcessBuilder.Redirect.PIPE);

        Integer result = null;

        Scanner output = null;
        Scanner error = null;
        try {
            Process hijo = pb.start();
            output = new Scanner(hijo.getInputStream());
            error = new Scanner(hijo.getErrorStream());

            if (error.hasNextLine()) {
                System.err.println("Error in child:");
                while (error.hasNextLine()) {
                    System.err.println(error.nextLine());
                }
            }
            else {
                result = output.nextInt();
            }

        } catch (IOException e) {
            System.err.println("Error in child process:");
            System.err.println("Are you sure you have the correct java binary file?");
            System.err.println("Error: " + e.getMessage());
        }
        finally {
            if (output != null)
                output.close();
            if (error != null)
                error.close();
        }
        return result;
    }
}
