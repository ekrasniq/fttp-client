package de.pseudonymisierung.fttp;

import de.netzwerk_universitaetsmedizin.codex.processes.data_transfer.client.FttpClientImpl;
import de.pseudoymisierung.mainzelliste.client.bloomfilter.BloomFilter;

import java.security.KeyStore;

public class FttpRunnerTest {

    public static void main(String[] args) {
        KeyStore trustStore = null;
        KeyStore keyStore = null;
        char[] keyStorePassword = {'a', 'b', 'c'};
        int connectTimeout = 100;
        int socketTimeout = 100;
        int connectionRequestTimeout = 100;
        String fttpBasicAuthUsername = "fttpBasicAuthUsername";
        String fttpBasicAuthPassword = "fttpBasicAuthPassword";
        String fttpServerBase = "fttpServerBase";
        String fttpApiKey = "fttpApiKey";
        String fttpStudy = "fttpStudy";
        String fttpTarget = "fttpTarget";
        String proxyUrl = "proxyUrl";
        String proxyUsername = "proxyUsername";
        String proxyPassword = "proxyPassword";
        boolean hapiClientVerbose = true;
        FttpClientImpl fttpClient = new FttpClientImpl(trustStore, keyStore, keyStorePassword, connectTimeout, socketTimeout, connectionRequestTimeout, fttpBasicAuthUsername, fttpBasicAuthPassword, fttpServerBase, fttpApiKey, fttpStudy, fttpTarget, proxyUrl, proxyUsername, proxyPassword, hapiClientVerbose);
        BloomFilter bloomFilter = null;

        try {
            bloomFilter = new BloomFilter("pid");
        } catch (Exception e) {
            e.printStackTrace();
        }

        String dicPsn = fttpClient.getDicPseudonym(bloomFilter.getBloomFilter()).get();
    }
}
