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
            // If something goes wrong with reading the file, we see the error.
            System.out.println("Error while reading words.txt: " + e.getMessage());
        }
    }

    private static void runProgram() throws IOException {
        Path wordsPath = Path.of("data", "words.txt");
        List<String> words = Files.readAllLines(wordsPath);
        System.out.println("Loaded " + words.size() + " words from " + wordsPath);

        System.out.println("First 5 words:");
        for (int i = 0; i < 5 && i < words.size(); i++) {
            System.out.println("  " + words.get(i));
        }

        if (!words.isEmpty()) {
            String last = words.getLast();
            System.out.println("Last word: " + last);


        }
    }
}
