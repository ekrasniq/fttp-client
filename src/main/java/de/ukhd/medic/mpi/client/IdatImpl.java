package de.ukhd.medic.mpi.client;

import java.util.HashMap;

public class IdatImpl implements Idat
{
	private final String medicId;
	private final String firstName;
	private final String lastName;
	private final String birthDate;
	private final String gender;

	public IdatImpl(String medicId, String firstName, String lastName, String birthDate, String gender)
	{
		this.medicId = medicId;
		this.firstName = firstName;
		this.lastName = lastName;
		this.birthDate = birthDate;
		this.gender = gender;
	}

	@Override
	public String getMedicId()
	{
		return medicId;
	}

	@Override
	public String getFirstName()
	{
		return firstName;
	}

	@Override
	public String getLastName()
	{
		return lastName;
	}

	@Override
	public String getBirthDate()
	{
		return birthDate;
	}

	@Override
	public String getGender()
	{
		return gender;
	}
	@Override
	public HashMap<String,String> getIdat()
	{
		HashMap<String,String> idat= new HashMap<String,String>();

		idat.put("medicId", medicId);
		idat.put("firstName", firstName);
		idat.put("lastName", lastName);
		idat.put("birthDate", birthDate);
		idat.put("gender", gender);
		return idat;
	}
}
