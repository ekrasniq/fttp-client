package de.pseudonymization.fttp;

import de.netzwerk_universitaetsmedizin.codex.processes.data_transfer.client.FttpClientImpl;
import de.pseudoymisierung.mainzelliste.client.bloomfilter.BloomFilterGenerator;
import de.ukhd.medic.mpi.MpiRunner;

import java.security.KeyStore;
import java.util.HashMap;

public class DicPseudonymTest {

    public static void main(String[] args) {
        // set the parameters for the MpiRunner to get the idat
        String pid = "";
        String pidAssigningAuthorityUniversalId = "";
        String pathToTrustCertificateFile = "src/test/resources/ukhd-chain-with-root.pem";
        String pathToClientCertificateFile = "src/test/resources/star.medic.dev.krz.uni-heidelberg.de.pem";
        String pathToClientCertificatePrivateKeyFile = "src/test/resources/star.medic.dev.krz.uni-heidelberg.de.privatekey.pem";
        char[] clientCertificatePrivateKeyPassword = null;
        String bf = "";

        try {
            // get idat from MPI
            HashMap<String, String> idat = MpiRunner.getIdatFromMpi(pid, pidAssigningAuthorityUniversalId, pathToTrustCertificateFile, pathToClientCertificateFile,
                    pathToClientCertificatePrivateKeyFile, clientCertificatePrivateKeyPassword);
            bf = BloomFilterGenerator.generateBloomFilter(idat);
            System.out.println("Bloomfilter: " + bf);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // set parameters for the connection of the fttp client to fttp in Greifswald
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

        String dicPsn = fttpClient.getDicPseudonym(bf).get();
        System.out.println("DIC PSN: " + dicPsn);
    }
}
