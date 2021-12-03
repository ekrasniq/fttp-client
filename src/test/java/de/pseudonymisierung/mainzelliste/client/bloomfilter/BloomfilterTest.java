package de.pseudonymisierung.mainzelliste.client.bloomfilter;

import de.pseudoymisierung.mainzelliste.client.bloomfilter.BloomFilter;

public class BloomfilterTest {
    public static void main(String[] args) {
        try {
            BloomFilter bloomFilter = new BloomFilter("pid");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
