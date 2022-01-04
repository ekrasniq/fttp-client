package de.ukhd.medic.mpi.client;

import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.llp.LLPException;
import ca.uhn.hl7v2.model.v25.message.QBP_Q21;
import ca.uhn.hl7v2.model.v25.message.RSP_K21;

import java.io.IOException;

public interface MpiClient
{
	RSP_K21 sendPatientDemographicsQuery(int port, QBP_Q21 request) throws LLPException, IOException, HL7Exception;

	RSP_K21 sendPatientIdentifierQuery(int port, QBP_Q21 request) throws LLPException, IOException, HL7Exception;
}
