package com.jkutkutorg.textAnalyzer;

import java.io.*;
import java.text.Normalizer;
import java.util.Locale;
import java.util.function.Predicate;

/**
 * Class with the logic needed to analyze a text file, counting the required characters
 * in the characters set defined by the mode.
 */
public class Analyzer {
    private static final int SUCCESS = 0;
    private static final int FAILURE = 1;

    public static final String VOWELS = "-v";
    public static final String CONSONANTS = "-c";
    public static final String LETTERS = "-l";
    public static final String NUMBERS = "-n";

    public static final String[] OPTIONS = {VOWELS, CONSONANTS, LETTERS, NUMBERS};
    public static final String[] OPTIONS_NAMES = {"Vocales", "Consonantes", "Letras", "Números"};

    private static final int MODE_ARG = 0;
    private static final int FILE_IN_ARG = 1;
    private static final int FILE_OUT_ARG = 2;

    public static final String STD_OUTPUT = "STD_OUT";

    private static final Predicate<Character>[] FTS = new Predicate[] {
            c -> inArray((Character) c, "aeiouy"),
            c -> inArray((Character) c, "bcdfghjklmnpqrstvwxz"),
            c -> Character.isLetter((Character) c),
            c -> Character.isDigit((Character) c)
    };

    /**
     * Usage:
     * java -jar textAnalyzer.jar -[v|c|l|n] &lt;file_in> &lt;file_out|STD_OUT>
     * @param args Arguments passed to the program
     */
    public static void main(String[] args) {
        if (args.length != 3) {
            System.err.println("Usage: analyzer.jar <mode> <file_in> <file_out" + STD_OUTPUT + ">");
            System.exit(FAILURE);
        }
        if (!isValidMode(args[MODE_ARG])) {
            System.err.printf(
                "Mode must any of: %s; not %s\n",
                String.join(", ", OPTIONS),
                args[MODE_ARG]
            );
            System.exit(FAILURE);
        }
        Predicate<Character> mode = getMode(args[MODE_ARG]);
        String fileName = args[FILE_IN_ARG];
        try (
            BufferedReader br = new BufferedReader(new FileReader(fileName));
        ) {
            int count = analyze(br, mode);

            if (args[FILE_OUT_ARG].equals(STD_OUTPUT))
                System.out.println(count);
            else {
                BufferedWriter bw = new BufferedWriter(new FileWriter(args[FILE_OUT_ARG]));
                bw.write(String.valueOf(count));
                bw.close();
            }
        } catch (FileNotFoundException e) {
            System.err.println("File not found: " + fileName);
            System.exit(FAILURE);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.exit(SUCCESS);
    }

    /**
     * Obtains the mode from the given string.
     * @param mode The selected mode.
     * @return The mode as a predicate.
     */
    private static Predicate<Character> getMode(String mode) {
        for (int i = 0; i < OPTIONS.length; i++)
            if (OPTIONS[i].equals(mode))
                return FTS[i];
        return null;
    }

    /**
     * Checks if the given mode is valid.
     * @param mode The mode to check.
     * @return The result of the verification.
     */
    private static boolean isValidMode(String mode) {
        for (String option : OPTIONS)
            if (option.equals(mode))
                return true;
        return false;
    }

    /**
     * Analyzes the given file, counting the characters that match the given mode.
     * @param fr File reader of the input file.
     * @param mode Predicate that defines the mode.
     * @return The number of characters that match the given mode.
     * @throws IOException If an I/O error occurs.
     */
    private static int analyze(BufferedReader fr, Predicate<Character> mode) throws IOException {
        int count = 0;
        String line;
        while ((line = fr.readLine()) != null) {
            line = normalizeText(line);
            for (char c : line.toCharArray()) {
                if (mode.test(c))
                    count++;
            }
        }
        return count;
    }

    // TOOLS

    /**
     * Normalizes the given text, removing accents and converting to lower case.
     * @param txt The text to normalize.
     * @return The normalized text.
     */
    private static String normalizeText(String txt) {
        txt = Normalizer.normalize(txt, Normalizer.Form.NFD);
        return txt.replaceAll("\\p{InCombiningDiacriticalMarks}", "").toLowerCase(Locale.ROOT);
    }

    /**
     * Checks if the given character is in the given array.
     * @param c The character to check.
     * @param dict The array to check.
     * @return The result of the verification.
     */
    private static boolean inArray(Character c, String dict) {
        for (Character character : dict.toCharArray())
            if (c == character)
                return true;
        return false;
    }
}
