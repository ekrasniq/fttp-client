package de.pseudoymisierung.mainzelliste.client.bloomfilter;

public class BloomFilter {
    private String bloomFilter;

    public BloomFilter(String pid, String pidAssigningAuthorityUniversalId) throws Exception {
        bloomFilter = GenerateBloomFilter.generateBloomFilter(pid, pidAssigningAuthorityUniversalId);
    }
    public String getBloomFilter() {
        return bloomFilter;
    }
}
