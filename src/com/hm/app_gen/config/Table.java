package com.hm.app_gen.config;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

public class Table
{

	@XmlAttribute
	private String name;

	@XmlAttribute
	private String column;

	@XmlElement(name = "id")
	private int[] ids;

	public String getName()
	{
		return name;
	}

	public String getColumn()
	{
		return column;
	}

	public int[] getIds()
	{
		return ids;
	}

}
