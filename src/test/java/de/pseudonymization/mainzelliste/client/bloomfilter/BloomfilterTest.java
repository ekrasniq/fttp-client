package de.pseudonymization.mainzelliste.client.bloomfilter;

import de.pseudoymisierung.mainzelliste.client.bloomfilter.BloomFilterGenerator;
import de.ukhd.medic.mpi.MpiRunner;

import java.util.HashMap;

public class BloomfilterTest {
    public static void main(String[] args) {

        // set the parameters for the MpiRunner to get the idat
        String pid = "";
        String pidAssigningAuthorityUniversalId = "";
        String pathToTrustCertificateFile = "src/test/resources/ukhd-chain-with-root.pem";
        String pathToClientCertificateFile = "src/test/resources/star.medic.dev.krz.uni-heidelberg.de.pem";
        String pathToClientCertificatePrivateKeyFile = "src/test/resources/star.medic.dev.krz.uni-heidelberg.de.privatekey.pem";
        char[] clientCertificatePrivateKeyPassword = null;

        try {
            // get idat from MPI
            HashMap<String, String> idat = MpiRunner.getIdatFromMpi(pid, pidAssigningAuthorityUniversalId, pathToTrustCertificateFile, pathToClientCertificateFile,
                    pathToClientCertificatePrivateKeyFile, clientCertificatePrivateKeyPassword);

            String bf = BloomFilterGenerator.generateBloomFilter(idat);
            System.out.println("Bloomfilter: " + bf);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}