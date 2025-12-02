package ch.dani.bloomfilter;

import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;

import java.util.BitSet;

public class BloomFilter {

    private final BitSet bits; // bit array
    private final int m; // anzahl bits
    private final int k; // anzahl hashfunktionen
    private final HashFunction[] hashFunctions; // k verschiedene murmur3 hashfunktionen mit verschiedenen seeds


    public BloomFilter(int n, double p) {
        if (n <= 0) {
            throw new IllegalArgumentException("n must be > 0");
        }
        if (p <= 0.0 || p >= 1.0) {
            throw new IllegalArgumentException("p must be between 0 and 1");
        }

        // m - (n * ln p) / (ln 2)^2
        double mDouble = - (n * Math.log(p)) / (Math.log(2) * Math.log(2));
        this.m = (int) Math.ceil(mDouble);

        // k = (m / n) * ln 2
        double kDouble = (m / (double) n) * Math.log(2);
        this.k = (int) Math.ceil(kDouble);

        this.bits = new BitSet(m);
        this.hashFunctions = new HashFunction[k];

        // create k murmur hash functions with different seeds
        for (int i = 0; i < k; i++) {
            hashFunctions[i] = Hashing.murmur3_128(i);
        }
    }

    // getters
    public int getM() {
        return m;
    }

    public int getK() {
        return k;
    }

    // add word to bloom filter
    public void add(String value) {
        for (int i = 0; i < k; i++) {
            int index = hashToIndex(value, i);
            bits.set(index);
        }
    }

    // check if word might be in the set
    public boolean mightContain(String value) {
        for (int i = 0; i < k; i++) {
            int index = hashToIndex(value, i);
            if (!bits.get(index)) {
                return false;
            }
        }
        return true;
    }

    // use i-th hash function and map hash to a bit index in [0, m)
    private int hashToIndex(String value, int functionIndex) {
       long hash64 = hashFunctions[functionIndex].hashUnencodedChars(value).asLong();
        long positive = hash64 & Long.MAX_VALUE; // makes sure non-negative
        return (int) (positive % m);
    }
}
