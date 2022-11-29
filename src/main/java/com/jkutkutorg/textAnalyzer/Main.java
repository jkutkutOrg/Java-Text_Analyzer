package com.jkutkutorg.textAnalyzer;

import jkutkut.SuperScanner;

import java.io.*;
import java.util.Scanner;

public class Main {
    // TODO autoconfig
//    public static String jaimeEjec = "/snap/eclipse/61/plugins/org.eclipse.justj.openjdk.hotspot.jre.full.linux.x86_64_17.0.3.v20220515-1416/jre/lib/jexec";
//    public static String jaimeEjec = "~/.jdks/corretto-18.0.2/lib/jexec";
//    public static String jaimeEjec = "/home/jkutkut/.jdks/corretto-18.0.2/lib/jexec";

    private static SuperScanner userInput;
    public static void main(String[] args) {
        try { // TODO reduce try-catch scope
            userInput = new SuperScanner.Es(System.in);

            // TODO Result of all analysis
            System.out.println("Introduce los datos para el analyzer:");
            String modo = userInput.getStringIn("Introduce el modo: ", Analyzer.OPTIONS);
            String archivoLectura = userInput.getFileName("Archivo a analizar: ");
            String archivoSalida = userInput.getString("Archivo de salida: ", 1, Integer.MAX_VALUE);

//            ProcessBuilder pb = new ProcessBuilder(jaimeEjec, "/home/jaime/Documentos/WorkSpacePSP/Java-Text_Analyzer/jars/hijo.jar",modo,archivoLectura,archivoSalida);
            ProcessBuilder pb = new ProcessBuilder(
                    "/home/jkutkut/.jdks/corretto-18.0.2/bin/java",
                    "-cp", "jars/hijo.jar",
                    "com.jkutkutorg.textAnalyzer.Analyzer",
                    modo, archivoLectura, archivoSalida
            );

            // Arreglo de linux para subprocesos (visto y hablado en clase).
            pb.inheritIO()
                .redirectInput(ProcessBuilder.Redirect.PIPE)
                .redirectError(ProcessBuilder.Redirect.PIPE)
                .redirectOutput(ProcessBuilder.Redirect.PIPE);

            Process hijo = pb.start();

            Scanner output = new Scanner(hijo.getInputStream());
            Scanner error = new Scanner(hijo.getErrorStream());

            if (error.hasNextLine()) {
                System.out.println("Error in child element");
                while (error.hasNextLine()) {
                    System.err.println(error.nextLine());
                }
            }
            else {
                System.out.println("Child element executed correctly");
                while (output.hasNextLine()) {
                    System.out.println(output.nextLine());
                }
            }

            userInput.close();
            output.close();
            error.close();
        } catch (IOException e) {
            System.err.println("IOException");
            e.printStackTrace();
        }
    }
}
