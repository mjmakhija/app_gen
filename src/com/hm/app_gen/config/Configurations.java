package com.hm.app_gen.config;

import com.hm.app_gen.LoggerWrapper;
import java.io.File;
import java.util.List;
import java.util.logging.Level;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "configurations")
public class Configurations
{

	@XmlElement(name = "mysql-bin-path")
	private String mysqlBinPath;

	@XmlElement(name = "config")
	private List<Config> configs;

	public List<Config> getConfigs()
	{
		return configs;
	}

	public String getMysqlBinPath()
	{
		return mysqlBinPath;
	}

	private static Configurations configurations;

	public static Configurations getInstance(String filePath)
	{
		if (configurations == null)
		{
			try
			{
				filePath = filePath == null || filePath.isEmpty() ? "config.xml" : filePath;
				File file = new File("config.xml");
				JAXBContext jaxbContext = JAXBContext.newInstance(Configurations.class);

				Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
				configurations = (Configurations) jaxbUnmarshaller.unmarshal(file);

			}
			catch (JAXBException e)
			{
				LoggerWrapper.getInstance().log(Level.SEVERE, Configurations.class.getName(), e);
			}
			catch (Exception e)
			{
				LoggerWrapper.getInstance().log(Level.SEVERE, Configurations.class.getName(), e);
			}
		}

		return configurations;
	}

}
