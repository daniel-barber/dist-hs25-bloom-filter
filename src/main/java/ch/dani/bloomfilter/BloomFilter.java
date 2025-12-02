package ch.dani.bloomfilter;

import java.util.BitSet;

public class BloomFilter {

    // version 1: fixed size and simple hash function

    private final BitSet bits; // stores bits 0/1
    private final int m; // number of bits
    private final int k; // number of hash functions

    public BloomFilter(int m, int k) {
        this.m = m;
        this.k = k;
        this.bits = new BitSet(m);
    }

    // add word to bloom filter
    public void add(String value) {
        for (int i = 0; i < k; i++) {
            int index = hashToIndex(value, i);
            bits.set(index);  // set bit to 1
        }
    }

    // check if word might be in the set
    public boolean mightContain(String value) {
        for (int i = 0; i < k; i++) {
            int index = hashToIndex(value, i);
            if (!bits.get(index)) {
                // if one of the bits is 0, the word was never added
                return false;
            }
        }
        return true;
    }

    // simple hash function
    private int hashToIndex(String value, int i) {
        int baseHash = value.hashCode();
        long combined = baseHash * 31L + i * 17L;
        if (combined < 0) {
            combined = -combined;
        }
        return (int) (combined % m); // map into [0, m)
    }

}
