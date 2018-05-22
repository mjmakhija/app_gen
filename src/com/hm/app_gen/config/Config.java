package com.hm.app_gen.config;

import javax.xml.bind.annotation.XmlElement;

public class Config
{

	@XmlElement
	private Database database;
	@XmlElement
	private Truncate truncate;
	@XmlElement
	private Clear clear;
	@XmlElement(name = "output-file-name")
	private String outputFileName;

	public Database getDatabase()
	{
		return database;
	}

	public Truncate getTruncate()
	{
		return truncate;
	}

	public Clear getClear()
	{
		return clear;
	}

	public String getOutputFileName()
	{
		return outputFileName;
	}

}
