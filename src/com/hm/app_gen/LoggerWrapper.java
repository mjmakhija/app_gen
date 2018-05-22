package com.hm.app_gen;

import java.io.IOException;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class LoggerWrapper
{

	private static Logger logger = null;
	private static Handler fileHandler = null;

	public LoggerWrapper()
	{
		logger = Logger.getLogger(GV.APP_NAME);

		Handler consoleHandler = null;
		SimpleFormatter simpleFormatter = null;

		try
		{

			// Creating FileHandler
			consoleHandler = new ConsoleHandler();
			fileHandler = new FileHandler(GV.INSTALLATION_DIR + "/" + "log", true);

			//Assigning handlers to logger object
			logger.addHandler(consoleHandler);
			logger.addHandler(fileHandler);

			//Setting levels to handlers and logger
			consoleHandler.setLevel(Level.ALL);
			fileHandler.setLevel(Level.ALL);
			logger.setLevel(Level.ALL);

			// Creating SimpleFormatter
			simpleFormatter = new SimpleFormatter();
			// Setting formatter to the handler
			fileHandler.setFormatter(simpleFormatter);

			// Logging message of Level info (this should be publish in the default format i.e. XMLFormat)
		}
		catch (IOException exception)
		{
			logger.log(Level.SEVERE, "Error occur in FileHandler.", exception);
		}
	}

	public static Logger getInstance()
	{
		if (logger == null)
		{
			new LoggerWrapper();
		}
		return logger;
	}

	public static void close()
	{
		if (logger == null)
		{
			return;
		}

		try
		{
			fileHandler.close();
			logger.removeHandler(fileHandler);
		}
		catch (Exception e)
		{
			logger.log(Level.SEVERE, "Problem closing and removing logger file handler", e);
		}
	}

}
