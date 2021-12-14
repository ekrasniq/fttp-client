package de.pseudonymization.fttp;

import de.netzwerk_universitaetsmedizin.codex.processes.data_transfer.client.FttpClientImpl;
import de.pseudoymisierung.mainzelliste.client.bloomfilter.BloomFilter;

import java.security.KeyStore;

public class FttpRunnerTest {

    public static void main(String[] args) {
        KeyStore trustStore = null;
        KeyStore keyStore = null;
        char[] keyStorePassword = null;
        int connectTimeout = 100;
        int socketTimeout = 100;
        int connectionRequestTimeout = 100;
        String fttpBasicAuthUsername = null;
        String fttpBasicAuthPassword = null;
        String fttpServerBase = null;
        String fttpApiKey = null;
        String fttpStudy = null;
        String fttpTarget = null;
        String proxyUrl = null;
        String proxyUsername = null;
        String proxyPassword = null;
        boolean hapiClientVerbose = true;

        FttpClientImpl fttpClient = new FttpClientImpl(trustStore, keyStore, keyStorePassword, connectTimeout, socketTimeout, connectionRequestTimeout, fttpBasicAuthUsername, fttpBasicAuthPassword, fttpServerBase, fttpApiKey, fttpStudy, fttpTarget, proxyUrl, proxyUsername, proxyPassword, hapiClientVerbose);

        String pid = null;
        String pidAssigningAuthorityUniversalId = null;

        BloomFilter bloomFilter = null;
        try {
            bloomFilter = new BloomFilter(pid, pidAssigningAuthorityUniversalId);
        } catch (Exception e) {
            e.printStackTrace();
        }

        String dicPsn = fttpClient.getDicPseudonym(bloomFilter.getBloomFilter()).get();
    }
}
