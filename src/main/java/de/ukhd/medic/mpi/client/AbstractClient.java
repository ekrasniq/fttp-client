package de.ukhd.medic.mpi.client;

import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.HapiContext;
import ca.uhn.hl7v2.app.Connection;
import ca.uhn.hl7v2.llp.LLPException;
import ca.uhn.hl7v2.model.Message;
import ca.uhn.hl7v2.util.SocketFactory;
import ca.uhn.hl7v2.util.Terser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public abstract class AbstractClient
{
	private static final Logger logger = LoggerFactory.getLogger(AbstractClient.class);

	private final String host;
	private final boolean useTLS;

	private final HapiContext context;

	protected AbstractClient(String host, HapiContext context, SocketFactory socketFactory)
	{
		this.host = host;
		this.context = context;

		if (socketFactory != null)
		{
			logger.debug("HL7v2 client configured using TLS");

			this.useTLS = true;
			context.setSocketFactory(socketFactory);
		}
		else
		{
			logger.debug("HL7v2 client configured without TLS");

			this.useTLS = false;
		}
	}

	protected Message send(int port, Message request) throws HL7Exception, LLPException, IOException
	{
		Terser terser = new Terser(request);
		String messageType = terser.get("/MSH-9-1") + "^" + terser.get("/MSH-9-2");
		String destination = host + ":" + port;

		logger.debug("Sending message of type {} to {}", messageType, destination);

		Connection connection = context.newClient(host, port, useTLS);
		return connection.getInitiator().sendAndReceive(request);
	}
}