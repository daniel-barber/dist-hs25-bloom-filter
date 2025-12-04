package ch.dani.bloomfilter;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class Main {
    public static void main(String[] args) {
        // Fehlerwahrscheinlichkeit p
        double p = 0.01;
        if (args.length > 0) {
            try {
                p = Double.parseDouble(args[0]);
                if (p <= 0.0 || p >= 1.0) {
                    System.out.println("p must be between 0 and 1. Using default p = 0.01.");
                    p = 0.01;
                }
            } catch (NumberFormatException e) {
                System.out.println("Could not parse p. Using default p = 0.01.");
            }
        }

        try {
            runProgram(p);
        } catch (IOException e) {
            System.out.println("Error while reading words.txt: " + e.getMessage());
        }
    }

    private static void runProgram(double p) throws IOException {
        Path wordsPath = Path.of("data", "words.txt");
        List<String> words = Files.readAllLines(wordsPath);
        System.out.println("Loaded " + words.size() + " words from " + wordsPath);

        Set<String> dictionary = new HashSet<>();
        for (String word : words) {
            if (!word.isBlank()) {
                dictionary.add(word);
            }
        }


        int n = dictionary.size();  // erwartete anzahl elemente

        System.out.println();
        System.out.println("Creating Bloom filter with:");
        System.out.println("  n = " + n + " elements");
        System.out.println("  p = " + p + " target false positive probability");

        // bloom filter using formulas and murmur
        BloomFilter bloomFilter = new BloomFilter(n, p);
        System.out.println("  m = " + bloomFilter.getM() + " bits");
        System.out.println("  k = " + bloomFilter.getK() + " hash functions");

        // insert words into bloom filter
        for (String word : dictionary) {
            bloomFilter.add(word);
        }
        System.out.println("All words inserted into the Bloom filter.");

        if (!words.isEmpty()) {
            String wordIn = words.getFirst();
            String wordNotIn = "thiswordisnotinthedictionary123";

            System.out.println();
            System.out.println("Manual check:");
            System.out.println("  word from list: " + wordIn +
                    " -> " + bloomFilter.mightContain(wordIn));
            System.out.println("  made-up word: " + wordNotIn +
                    " -> " + bloomFilter.mightContain(wordNotIn));
        }

        // Experiment zur Fehlerrate
        int testCount = 100_000; // random string count
        System.out.println();
        System.out.println("Running false-positive experiment with " + testCount + " random strings");
        double experimentalRate = runFalsePositiveExperiment(bloomFilter, dictionary, testCount);

        System.out.println();
        System.out.println("===== RESULT =====");
        System.out.println("Target false positive probability p: " + p);
        System.out.println("Experimental false positive rate: " + experimentalRate);
        System.out.println("Parameters: n = " + n
                + ", m = " + bloomFilter.getM()
                + ", k = " + bloomFilter.getK());
        System.out.println("==================");
    }

    // Experiment: viele zufällige strings testen, die nicht im dictionary sind
    private static double runFalsePositiveExperiment(BloomFilter bloomFilter, Set<String> dictionary, int testCount) {
        Random random = new Random();
        int falsePositives = 0;
        int tested = 0;

        while(tested < testCount) {
            String candidate = generateRandomString(random, 10); // 10 letters

            if(dictionary.contains(candidate)) {
                continue;
            }

            if (bloomFilter.mightContain(candidate)) {
                falsePositives++;
            }
            tested++;
        }
        return falsePositives / (double) testCount;
    }

    // generate random string
    private static String generateRandomString(Random random, int length) {
        String letters = "abcdefghijklmnopqrstuvwxyz";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append(letters.charAt(random.nextInt(letters.length())));
        }
        return sb.toString();
    }
}
