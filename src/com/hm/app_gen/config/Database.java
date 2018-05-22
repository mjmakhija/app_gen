package com.hm.app_gen.config;

import javax.xml.bind.annotation.XmlElement;

public class Database
{

	@XmlElement
	private String url;
	@XmlElement
	private String name;
	@XmlElement
	private String username;
	@XmlElement
	private String password;

	public String getUrl()
	{
		return url;
	}

	public String getName()
	{
		return name;
	}

	public String getUsername()
	{
		return username;
	}

	public String getPassword()
	{
		return password;
	}

}
