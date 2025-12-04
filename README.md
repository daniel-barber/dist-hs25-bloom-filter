# Bloom Filter Project

In our module we were tasked with implementing a Bloom filter in Java. The goal was to construct a Bloom filter for a large word list, compute its optimal parameters, and experimentally determine its false-positive rate.

The program performs:
- Reading all words from `words.txt`
- Computing the optimal filter size m and number of hash functions k based on
  the expected number of elements n and the desired false-positive probability p 
- Building the Bloom filter using Murmur3 128-bit hash functions with different seeds 
- Inserting all words into the filter 
- Running a large experiment with random non-words to measure the false-positive rate 
- Printing all relevant parameters (n, p, m, k) and the measured error rate

- Example output (using p = 0.01):
```
Loaded 58109 words from data/words.txt

Creating Bloom filter with:
  n = 58109 elements
  p = 0.01 target false positive probability
  m = 556979 bits
  k = 7 hash functions
All words inserted into the Bloom filter.

Manual check:
  word from list: aardvark -> true
  made-up word: thiswordisnotinthedictionary123 -> false

Running false-positive experiment with 100000 random strings

===== RESULT =====
Target false positive probability p: 0.01
Experimental false positive rate: 0.01009
Parameters: n = 58109, m = 556979, k = 7
==================
```

## How to run
1. Place the word list `words.txt` inside the `./data/` directory
2. Run the project using Gradle: `./gradlew run`
3. To specify a custom false-positive probability, provide p as argument: `./gradlew run --args="0.02"`


## Worked on by
- Daniel Barber
- Tamira Leber

