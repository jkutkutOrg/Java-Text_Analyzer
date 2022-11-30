package com.jkutkutorg.textAnalyzer;

import jkutkut.SuperScanner;

import java.io.*;
import java.util.Scanner;

public class Main {
    // TODO autoconfig

    private static final String JAVA = "/home/jkutkut/.jdks/corretto-18.0.2/bin/java";
    private static final String ANALYZER_JAR = "jars/hijo.jar";
    private static final String ANALYZER_JAVA = "com.jkutkutorg.textAnalyzer.Analyzer";

    public static void main(String[] args) {
        SuperScanner userInput = new SuperScanner.Es(System.in);

        System.out.println("Introduce los datos para el analyzer:");
        String archivoLectura = userInput.getFileName("Archivo a analizar: ");
        userInput.close();

        Integer result;
        for (int i = 0; i < Analyzer.OPTIONS.length; i++) {
            result = analyze(Analyzer.OPTIONS[i], archivoLectura);
            if (result == null) {
                System.err.println("Hijo no ha podido analizar el archivo");
                break;
            }
            System.out.println(Analyzer.OPTIONS_NAMES[i] + ": " + result);
        }
    }

    private static Integer analyze(String mode, String file) {
        ProcessBuilder pb = new ProcessBuilder(
                JAVA,
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
                System.err.println("Error en el hijo:");
                while (error.hasNextLine()) {
                    System.err.println(error.nextLine());
                }
            }
            else {
                result = output.nextInt();
            }

        } catch (IOException e) {
            System.err.println("IOException");
            e.printStackTrace();
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
