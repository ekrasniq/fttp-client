package de.ukhd.medic.mpi.client;

import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.HapiContext;
import ca.uhn.hl7v2.llp.LLPException;
import ca.uhn.hl7v2.model.v25.message.QBP_Q21;
import ca.uhn.hl7v2.model.v25.message.RSP_K21;
import ca.uhn.hl7v2.util.SocketFactory;

import java.io.IOException;

public class MpiClientImpl extends AbstractClient implements MpiClient
{
	public MpiClientImpl(String host, HapiContext context)
	{
		this(host, context, null);
	}

	public MpiClientImpl(String host, HapiContext context, SocketFactory socketFactory)
	{
		super(host, context, socketFactory);
	}

	@Override
	public RSP_K21 sendPatientDemographicsQuery(int port, QBP_Q21 request)
		throws LLPException, IOException, HL7Exception
	{
		return (RSP_K21) send(port, request);
	}

	@Override
	public RSP_K21 sendPatientIdentifierQuery(int port, QBP_Q21 request)
		throws LLPException, IOException, HL7Exception
	{
		return (RSP_K21) send(port, request);
	}
}
