package de.pseudoymisierung.mainzelliste.client.bloomfilter;

public class BloomFilter {
    private String bloomFilter;

    public BloomFilter(String pidAssigningAuthorityUniversalId) throws Exception {
        bloomFilter = GenerateBloomFilter.generateBloomFilter(pidAssigningAuthorityUniversalId);
    }
    public String getBloomFilter() {
        return bloomFilter;
    }
}
