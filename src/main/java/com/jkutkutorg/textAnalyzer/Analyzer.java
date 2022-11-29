package com.jkutkutorg.textAnalyzer;

import java.io.*;
import java.text.Normalizer;
import java.util.Locale;
import java.util.function.Predicate;

public class Analyzer {
    private static final int SUCCESS = 0;
    private static final int FAILURE = 1;

    public static final String VOWELS = "-v";
    public static final String CONSONANTS = "-c";
    public static final String LETTERS = "-l";
    public static final String NUMBERS = "-n";

    private static final String[] OPTIONS = {VOWELS, CONSONANTS, LETTERS, NUMBERS};

    private static final int MODE_ARG = 0;
    private static final int FILE_IN_ARG = 1;
    private static final int FILE_OUT_ARG = 2;

    private static final Predicate<Character>[] FTS = new Predicate[] {
            c -> inArray((Character) c, "aeiouy"),
            c -> inArray((Character) c, "bcdfghjklmnpqrstvwxz"),
            c -> Character.isLetter((Character) c),
            c -> Character.isDigit((Character) c)
    };

    private static boolean inArray(Character c, String dict) {
        for (Character character : dict.toCharArray())
            if (c == character)
                return true;
        return false;
    }

    public static void main(String[] args) {
        for (String s : args)
            System.out.println(s);
        if (args.length != 3) {
            System.err.println("Usage: java Analyzer <mode> <file_in> <file_out>");
            System.exit(FAILURE);
        }
        if (!isValidMode(args[MODE_ARG])) {
            System.err.println("Mode must any of: " + String.join(", ", OPTIONS) + "; not " + args[MODE_ARG]);
            System.exit(FAILURE);
        }
        Predicate<Character> mode = getMode(args[MODE_ARG]);

        String fileName = args[FILE_IN_ARG];
        try (
            BufferedReader br = new BufferedReader(new FileReader(fileName));
            BufferedWriter bw = new BufferedWriter(new FileWriter(args[FILE_OUT_ARG]))
        ) {
            int count = analyze(br, mode);
            bw.write(String.valueOf(count));
        } catch (FileNotFoundException e) {
            System.err.println("File not found: " + fileName);
            System.exit(FAILURE);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.exit(SUCCESS);
    }

    private static Predicate<Character> getMode(String arg) {
        for (int i = 0; i < OPTIONS.length; i++) {
            if (OPTIONS[i].equals(arg)) {
                return FTS[i];
            }
        }
        return null;
    }

    private static boolean isValidMode(String mode) {
        for (String option : OPTIONS)
            if (option.equals(mode))
                return true;
        return false;
    }

    private static int analyze(BufferedReader fr, Predicate<Character> mode) throws IOException {
        int count = 0;
        String line;
        while ((line = fr.readLine()) != null) {
            line = normalizeText(line);
            for (char c : line.toCharArray()) {
                if (mode.test(c))
                    count++;
                if (mode.test(c)) {
                    System.out.print("|" + c + "|");
                }
                else {
                    System.out.print(" " + c + " ");
                }
            }
        }
        return count;
    }

    // TOOLS
    private static String normalizeText(String txt) {
        txt = Normalizer.normalize(txt, Normalizer.Form.NFD);
        return txt.replaceAll("\\p{InCombiningDiacriticalMarks}", "").toLowerCase(Locale.ROOT);
    }
}
