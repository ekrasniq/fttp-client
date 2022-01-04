package de.ukhd.medic.mpi.message;

import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.v25.datatype.*;
import ca.uhn.hl7v2.model.v25.group.RSP_K21_QUERY_RESPONSE;
import ca.uhn.hl7v2.model.v25.message.QBP_Q21;
import ca.uhn.hl7v2.model.v25.message.RSP_K21;
import ca.uhn.hl7v2.model.v25.segment.*;
import ca.uhn.hl7v2.util.Terser;
import de.ukhd.medic.mpi.client.Idat;
import de.ukhd.medic.mpi.client.IdatImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class MessageHelper
{
	private static final Logger logger = LoggerFactory.getLogger(MessageHelper.class);

	public QBP_Q21 createPatientDemographicsQuery(String senderApplication, String senderFacility,
												  String receiverApplication, String receiverFacility, List<QueryParameter> searchParameters) throws HL7Exception
	{
		QBP_Q21 qbp_q21 = new QBP_Q21();

		MSH msh = qbp_q21.getMSH();
		msh.getMsh1_FieldSeparator().setValue("");
		msh.getMsh2_EncodingCharacters().setValue("^~\\&");
		msh.getMsh3_SendingApplication().getNamespaceID().setValue(senderApplication);
		msh.getMsh4_SendingFacility().getNamespaceID().setValue(senderFacility);
		msh.getMsh5_ReceivingApplication().getNamespaceID().setValue(receiverApplication);
		msh.getMsh6_ReceivingFacility().getNamespaceID().setValue(receiverFacility);
		msh.getMsh7_DateTimeOfMessage().getTime().setValue(new Date());

		MSG messageType = msh.getMsh9_MessageType();
		messageType.getMsg1_MessageCode().setValue("QBP");
		messageType.getMsg2_TriggerEvent().setValue("Q22");
		messageType.getMsg3_MessageStructure().setValue("QBP_Q21");

		msh.getMsh10_MessageControlID().setValue(UUID.randomUUID().toString());

		msh.getMsh11_ProcessingID().getPt1_ProcessingID().setValue("P");
		msh.getMsh11_ProcessingID().getPt2_ProcessingMode().setValue("T");

		msh.getMsh12_VersionID().getVid1_VersionID().setValue("2.5");

		QPD qpd = qbp_q21.getQPD();
		qpd.getQpd1_MessageQueryName().getIdentifier().setValue("IHE PDQ Query");
		qpd.getQpd2_QueryTag().setValue(UUID.randomUUID().toString());

		RCP rcp = qbp_q21.getRCP();
		rcp.getRcp1_QueryPriority().setValue("I");

		Terser terser = new Terser(qbp_q21);

		for (int qpd3counter = 0; qpd3counter < searchParameters.size(); qpd3counter++)
		{
			QueryParameter searchParameter = searchParameters.get(qpd3counter);

			if (searchParameter.hasLocation())
			{
				terser.set(searchParameter.getLocation(), searchParameter.getValue());
			}
			else
			{
				terser.set("/QPD-3(" + qpd3counter + ")-1", searchParameter.getField());
				terser.set("/QPD-3(" + qpd3counter + ")-2", searchParameter.getValue());
			}
		}

		return qbp_q21;
	}

	public Idat extractPatientDemographics(RSP_K21 patientDemographicsQueryResult,
										   String pidAssigningAuthorityNamespaceId, String pidAssigningAuthorityUniversalId) throws Exception
	{
		ERR error = patientDemographicsQueryResult.getERR();

		if (error.getHL7ErrorCode().getIdentifier().getValue() != null)
		{
			String errorMessage = error.encode().replace("\r", "\n");
			logger.warn("Error in patient demographics query result, error: {}", errorMessage);
			throw new RuntimeException("Error in patient demographics query result, error: " + errorMessage);
		}

		List<RSP_K21_QUERY_RESPONSE> responses = patientDemographicsQueryResult.getQUERY_RESPONSEAll();
		String query = patientDemographicsQueryResult.getQPD().getUserParametersInsuccessivefields().encode();

		if (responses.size() == 0)
		{
			logger.warn("Result of query='{}' was empty", query);
			throw new RuntimeException("Result of query='" + query + "' was empty");
		}

		if (responses.size() > 1)
		{
			logger.warn("Found more than 1 demographic data result, using the first of query='{}'", query);
		}

		PID pid = responses.get(0).getPID();

		String patientId = null;
		for (CX identifier : pid.getPatientIdentifierList())
		{
			HD assigningAuthority = identifier.getAssigningAuthority();

			if (assigningAuthority.getNamespaceID().getValue() != null && assigningAuthority.getNamespaceID().getValue()
				.equals(pidAssigningAuthorityNamespaceId) && assigningAuthority.getUniversalID().getValue() != null
				&& assigningAuthority.getUniversalID().getValue().equals(pidAssigningAuthorityUniversalId))
			{
				patientId = identifier.getIDNumber().getValue();
			}
		}

		if (patientId == null)
		{
			logger.warn(
				"Could not find patient id with namespace-id='{}' and universal-id='{}' in PID segment of response of query='{}'",
				pidAssigningAuthorityNamespaceId, pidAssigningAuthorityUniversalId, query);
			throw new RuntimeException(
				"Could not find patient id with namespace-id='" + pidAssigningAuthorityNamespaceId
					+ "' and universal-id='" + pidAssigningAuthorityUniversalId
					+ "' in PID segment of response of query='" + query + "'");
		}

		XPN[] patientNames = pid.getPatientName();
		String firstname = "", lastname = "";

		if (patientNames.length > 0)
		{
			XPN patientName = patientNames[0];

			firstname = patientName.getGivenName().getValue();
			lastname = patientName.getFamilyName().getSurname().getValue();
		}

		DTM birthDtm = pid.getDateTimeOfBirth().getTime();
		String birthdate = birthDtm.getDay() + "." + birthDtm.getMonth() + "." + birthDtm.getYear();

		String sex = Optional.ofNullable(pid.getAdministrativeSex().getValue()).orElse("").toUpperCase();

		String insuranceNumber = pid.getSSNNumberPatient().getValue();

		return new IdatImpl(patientId, firstname, lastname, birthdate, sex);
	}
}
