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
        try {
            runProgram();
        } catch (IOException e) {
            System.out.println("Error while reading words.txt: " + e.getMessage());
        }
    }

    private static void runProgram() throws IOException {
        Path wordsPath = Path.of("data", "words.txt");
        List<String> words = Files.readAllLines(wordsPath);
        System.out.println("Loaded " + words.size() + " words from " + wordsPath);

        Set<String> dictionary = new HashSet<>();
        for (String word : words) {
            if (!word.isBlank()) {
                dictionary.add(word);
            }
        }

        // decide bloom filter size
        int n = dictionary.size();  // expected number of elements
        double p = 0.01;       // desired false positive probability (1%)

        System.out.println("Creating Bloom filter with:");
        System.out.println("  n = " + n + " expected elements");
        System.out.println("  p = " + p + " target false positive probability");

        // bloom filter using formulas and murmur
        BloomFilter bloomFilter = new BloomFilter(n, p);
        System.out.println("  -> m = " + bloomFilter.getM() + " bits");
        System.out.println("  -> k = " + bloomFilter.getK() + " hash functions");

        // insert words into bloom filter
        for (String word : words) {
            bloomFilter.add(word);
        }
        System.out.println("All words inserted into the Bloom filter.");

        if (!words.isEmpty()) {
            String wordIn = words.getFirst();
            String wordNotIn = "thiswordisnotinthedictionary123";

            System.out.println();
            System.out.println("Test 1: word from the list: " + wordIn);
            System.out.println("  mightContain = " + bloomFilter.mightContain(wordIn));

            System.out.println("Test 2: made-up word: " + wordNotIn);
            System.out.println("  mightContain = " + bloomFilter.mightContain(wordNotIn));
        }

        // run false positive experiment
        int testCount = 100_000; // random string count
        System.out.println();
        System.out.println("Running false-positive experiment with " + testCount + " random strings...");
        double experimentalRate = runFalsePositiveExperiment(bloomFilter, dictionary, testCount);

        System.out.println();
        System.out.println("===== RESULT =====");
        System.out.println("Target false positive probability p: " + p);
        System.out.println("Experimental false positive rate:   " + experimentalRate);
        System.out.println("n = " + n + ", m = " + bloomFilter.getM() + ", k = " + bloomFilter.getK());
        System.out.println("==================");
    }

    // false positive experiment
    private static double runFalsePositiveExperiment(BloomFilter bloomFilter, Set<String> dictionary, int testCount) {
        Random random = new Random();
        int falsePositives = 0;
        int tested = 0;

        while(tested < testCount) {
            String candidate = generateRandomString(random, 10); // 10 letters

            if(dictionary.contains(candidate)) {
                continue;
            }

            boolean maybe = bloomFilter.mightContain(candidate);

            if(maybe){
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
