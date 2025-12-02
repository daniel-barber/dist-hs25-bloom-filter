package ch.dani.bloomfilter;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

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

        // decide bloom filter size
        int n = words.size();  // expected number of elements
        double p = 0.01;       // desired false positive probability (1%)

        System.out.println("Creating Bloom filter with:");
        System.out.println("  n = " + n + " expected elements");
        System.out.println("  p = " + p + " target false positive probability");

        // bloom filter using formulas and murmur
        BloomFilter bloomFilter = new BloomFilter(n, p);
        System.out.println("  -> m = " + bloomFilter.getM() + " bits");
        System.out.println("  -> k = " + bloomFilter.getK() + " hash functions");

        // insert words into bloom filter
        for (String w : words) {
            if (!w.isBlank()) {
                bloomFilter.add(w);
            }
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
    }
}
