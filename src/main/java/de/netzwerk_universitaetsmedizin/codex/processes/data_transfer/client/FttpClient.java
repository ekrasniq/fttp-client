package de.netzwerk_universitaetsmedizin.codex.processes.data_transfer.client;

import java.util.Optional;

public interface FttpClient {
    /**
     * @param dicSourceAndPseudonym not <code>null</code>
     * @return
     */
    Optional<String> getCrrPseudonym(String dicSourceAndPseudonym);

    /**
     * @param bloomFilter not <code>null</code>
     * @return
     */
    Optional<String> getDicPseudonym(String bloomFilter);


    void testConnection();
}