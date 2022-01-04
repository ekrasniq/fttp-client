package de.ukhd.medic.mpi;

import ca.uhn.hl7v2.DefaultHapiContext;
import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.llp.LLPException;
import ca.uhn.hl7v2.model.v25.message.QBP_Q21;
import ca.uhn.hl7v2.model.v25.message.RSP_K21;
import de.ukhd.medic.mpi.client.Idat;
import de.ukhd.medic.mpi.client.MpiClient;
import de.ukhd.medic.mpi.client.MpiClientImpl;
import de.ukhd.medic.mpi.message.MessageHelper;
import de.ukhd.medic.mpi.message.QueryParameter;
import de.ukhd.medic.mpi.security.CustomSocketFactory;

import java.io.IOException;
import java.util.List;

public class MpiRunnerTest {

    public static void main(String[] args) {
        String HOST = "icwmpi99";
        int ICW_PDQ_PORT = 3750;

        List<QueryParameter> searchParameters = List.of(
                QueryParameter.createQueryParameterForQpd3("@PID.3.1", "0002038285"),
                QueryParameter.createQueryParameterForQpd3("@PID.3.4.1", "SAP-ISH"),
                QueryParameter.createQueryParameterForQpd3("@PID.3.4.2", "1.2.276.0.76.3.1.78.1.0.10.1.101.1"));

        //		List<QueryParameter> searchParameters = List.of(
        //			QueryParameter.createQueryParameterForNonQpd3("/QPD-8(0)-4", "ICWMPI"),
        //			QueryParameter.createQueryParameterForQpd3("@PID.5.1", "T*"));

        QBP_Q21 query = null;
        try {
            query = new MessageHelper().createPatientDemographicsQuery("MBE01", "MeDIC", "ICW-MPI", "ICW",
                    searchParameters);
        } catch (HL7Exception e) {
            e.printStackTrace();
        }

        CustomSocketFactory customSocketFactory = new CustomSocketFactory(  "src/test/resources/ukhd-chain-with-root.pem",
                "src/test/resources/star.medic.dev.krz.uni-heidelberg.de.pem",
                "src/test/resources/star.medic.dev.krz.uni-heidelberg.de.privatekey.pem", null);

        MpiClient client = new MpiClientImpl(HOST, new DefaultHapiContext(), customSocketFactory);
        RSP_K21 response = null;
        try {
            response = client.sendPatientDemographicsQuery(ICW_PDQ_PORT, query);
        } catch (LLPException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (HL7Exception e) {
            e.printStackTrace();
        }

        //System.out.println(response.encode().replace("\r", "\n"));

        try {
            Idat idat = new MessageHelper().extractPatientDemographics(response, "SAP-ISH",
                    "PID");
            for (String key: idat.getIdat().keySet()) {
                System.out.println(key + ": " + idat.getIdat().get(key));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
