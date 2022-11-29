package com.jkutkutorg.textAnalyzer;

import java.io.*;
import java.util.Scanner;

public class Main {
    public static String jaimeEjec = "/snap/eclipse/61/plugins/org.eclipse.justj.openjdk.hotspot.jre.full.linux.x86_64_17.0.3.v20220515-1416/jre/lib/jexec";
    static Scanner sc;
    public static void main(String[] args) {
        try {
            sc = new Scanner(System.in);

            System.out.println("Introduce el modo");
            String modo = sc.nextLine();

            System.out.println("Introduce el nombre del archivo del que se toman los datos");
            String archivoLectura = sc.nextLine();
            System.out.println("Introduce como quieres que se llame el archivo de salida");
            String archivoSalida = sc.nextLine();


            ProcessBuilder pb = new ProcessBuilder(jaimeEjec, "/home/jaime/Documentos/WorkSpacePSP/Java-Text_Analyzer/jars/hijo.jar",modo,archivoLectura,archivoSalida);
            pb.inheritIO().redirectInput(ProcessBuilder.Redirect.PIPE).redirectOutput(ProcessBuilder.Redirect.PIPE);

            Process hijo = pb.start();

            BufferedReader br = new BufferedReader(new InputStreamReader(hijo.getInputStream()));


            String error = (hijo.getErrorStream()).toString();

            if (error != null) {


                PrintStream ps = new PrintStream(hijo.getOutputStream());
                ps.flush();
                System.out.println(br.readLine());
            }
            else{
                System.out.println(error);
            }

            System.out.println("**FIN**");

            sc.close();

        } catch (IOException e) {
            System.out.println("IOException");
        }


    }
}
