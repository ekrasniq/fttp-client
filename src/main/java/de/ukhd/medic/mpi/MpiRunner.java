package de.ukhd.medic.mpi;

import ca.uhn.hl7v2.DefaultHapiContext;
import ca.uhn.hl7v2.model.v25.message.QBP_Q21;
import ca.uhn.hl7v2.model.v25.message.RSP_K21;
import de.ukhd.medic.mpi.client.Idat;
import de.ukhd.medic.mpi.client.MpiClient;
import de.ukhd.medic.mpi.client.MpiClientImpl;
import de.ukhd.medic.mpi.message.MessageHelper;
import de.ukhd.medic.mpi.message.QueryParameter;
import de.ukhd.medic.mpi.security.CustomSocketFactory;

import java.util.HashMap;
import java.util.List;

public class MpiRunner {
    private static final String HOST = "icwmpi99";
    private static final int ICW_PDQ_PORT = 3750;

    public static HashMap<String, String> getIdatFromMpi(String pidAssigningAuthorityUniversalId) throws Exception {
        List<QueryParameter> searchParameters = List.of(
                QueryParameter.createQueryParameterForQpd3("@PID.3.1", "0002038285"),
                QueryParameter.createQueryParameterForQpd3("@PID.3.4.1", "SAP-ISH"),
                QueryParameter.createQueryParameterForQpd3("@PID.3.4.2", "1.2.276.0.76.3.1.78.1.0.10.1.101.1"));

        //		List<QueryParameter> searchParameters = List.of(
        //			QueryParameter.createQueryParameterForNonQpd3("/QPD-8(0)-4", "ICWMPI"),
        //			QueryParameter.createQueryParameterForQpd3("@PID.5.1", "T*"));

        QBP_Q21 query = new MessageHelper().createPatientDemographicsQuery("MBE01", "MeDIC", "ICW-MPI", "ICW",
                searchParameters);

        CustomSocketFactory customSocketFactory = new CustomSocketFactory(  "src/test/resources/ukhd-chain-with-root.pem",
                "src/test/resources/star.medic.dev.krz.uni-heidelberg.de.pem",
                "src/test/resources/star.medic.dev.krz.uni-heidelberg.de.privatekey.pem", null);

        MpiClient client = new MpiClientImpl(HOST, new DefaultHapiContext(), customSocketFactory);
        RSP_K21 response = client.sendPatientDemographicsQuery(ICW_PDQ_PORT, query);

        Idat idat = new MessageHelper().extractPatientDemographics(response, "SAP-ISH",
                pidAssigningAuthorityUniversalId);

        return idat.getIdat();
    }
}
