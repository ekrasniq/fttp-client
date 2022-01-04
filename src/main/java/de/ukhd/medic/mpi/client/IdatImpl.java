package de.ukhd.medic.mpi.client;

import java.util.HashMap;

public class IdatImpl implements Idat
{
	private final String medicId;
	private final String firstName;
	private final String lastName;
	private final String birthday;
	private final String sex;

	public IdatImpl(String medicId, String firstName, String lastName, String birthday, String sex)
	{
		this.medicId = medicId;
		this.firstName = firstName;
		this.lastName = lastName;
		this.birthday = birthday;
		this.sex = sex;
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
	public String getBirthday()
	{
		return birthday;
	}

	@Override
	public String getSex()
	{
		return sex;
	}
	@Override
	public HashMap<String,String> getIdat()
	{
		HashMap<String,String> idat= new HashMap<String,String>();

		idat.put("medicId", medicId);
		idat.put("firstName", firstName);
		idat.put("lastName", lastName);
		idat.put("birthDate", birthday);
		idat.put("sex", sex);
		return idat;
	}
}
