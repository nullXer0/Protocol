package com.crimsonValkyrie.protocol.main;

import javax.security.auth.login.LoginException;
import java.io.*;
import java.util.Properties;

public class Main
{
	private static Properties properties = new Properties();

	private Main() throws IOException, LoginException, InterruptedException
	{
		loadDefaultProperties();
		loadPropertiesFromFile(new File(".properties"));
		new Bot(properties.getProperty("token"), properties.getProperty("ownerID"), properties.getProperty("prefix"));
	}

	private static void loadPropertiesFromFile(File file) throws IOException
	{
		if(file.createNewFile())
		{
			Writer fileWriter = new FileWriter(file);
			properties.store(fileWriter, null);
			fileWriter.close();

			System.out.println("Properties file has been created, please provide token");
			System.exit(0);
		}
		else
		{
			Reader reader = new FileReader(new File(".properties"));
			properties.load(reader);
			reader.close();
		}
	}

	private static void loadDefaultProperties()
	{
		properties.setProperty("token", "");
		properties.setProperty("ownerID", "");
		properties.setProperty("prefix", "!");
	}

	public static void main(String[] args) throws IOException, LoginException, InterruptedException
	{
		new Main();
	}
}
