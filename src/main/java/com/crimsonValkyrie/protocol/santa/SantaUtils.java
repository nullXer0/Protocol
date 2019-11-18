package com.crimsonValkyrie.protocol.santa;

import java.io.*;
import java.util.HashMap;

public class SantaUtils
{
	private static HashMap<String, String> santaMap = new HashMap<>();
	private static HashMap<String, String> santeeMap = new HashMap<>();

	public static HashMap<SantaDataType, String> getUserMap(String userID) throws IOException, ClassNotFoundException
	{
		return loadUserMap(userID);
	}

	public static String getSanta(String userID) throws IOException, ClassNotFoundException, SantaNotFoundException
	{
		if(santaMap.containsKey(userID))
		{
			return santaMap.get(userID);
		}
		else
		{
			HashMap<SantaDataType, String> userMap = loadUserMap(userID);
			if(userMap.containsKey(SantaDataType.SANTA))
			{
				santaMap.put(userID, loadUserMap(userID).get(SantaDataType.SANTA));
				return getSanta(userID);
			}
			else
			{
				throw new SantaNotFoundException();
			}
		}
	}

	public static String getSantee(String userID) throws IOException, ClassNotFoundException, SanteeNotFoundException
	{
		if(santeeMap.containsKey(userID))
		{
			return santeeMap.get(userID);
		}
		else
		{
			HashMap<SantaDataType, String> userMap = loadUserMap(userID);
			if(userMap.containsKey(SantaDataType.SANTEE))
			{
				santeeMap.put(userID, loadUserMap(userID).get(SantaDataType.SANTEE));
				return getSantee(userID);
			}
			else
			{
				throw new SanteeNotFoundException();
			}
		}
	}

	public static String getAddress(String userID) throws IOException, ClassNotFoundException
	{
		return loadUserMap(userID).get(SantaDataType.ADDRESS);
	}

	public static String getNote(String userID) throws IOException, ClassNotFoundException
	{
		return loadUserMap(userID).get(SantaDataType.NOTE);
	}

	public static void writeSanta(String userID, String santa) throws IOException, ClassNotFoundException
	{
		HashMap<SantaDataType, String> userMap = loadUserMap(userID);
		userMap.put(SantaDataType.SANTA, santa);
		santaMap.put(userID, santa);
		saveUserMap(userMap, userID);
	}

	public static void writeSantee(String userID, String santee) throws IOException, ClassNotFoundException
	{
		HashMap<SantaDataType, String> userMap = loadUserMap(userID);
		userMap.put(SantaDataType.SANTEE, santee);
		santeeMap.put(userID, santee);
		saveUserMap(userMap, userID);
	}

	public static void writeAddress(String address, String userID) throws IOException, ClassNotFoundException
	{
		HashMap<SantaDataType, String> userMap = loadUserMap(userID);
		userMap.put(SantaDataType.ADDRESS, address);
		saveUserMap(userMap, userID);
	}

	public static void writeNote(String note, String userID) throws IOException, ClassNotFoundException
	{
		HashMap<SantaDataType, String> userMap = loadUserMap(userID);
		userMap.put(SantaDataType.NOTE, note);
		saveUserMap(userMap, userID);
	}

	@SuppressWarnings("unchecked cast")
	private static HashMap<SantaDataType, String> loadUserMap(String userID) throws IOException, ClassNotFoundException
	{
		if(new File("santa/" + userID).exists())
		{
			FileInputStream fileInputStream = new FileInputStream("santa/" + userID);
			ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);

			HashMap<SantaDataType, String> userMap = (HashMap<SantaDataType, String>) objectInputStream.readObject();

			objectInputStream.close();
			fileInputStream.close();

			return userMap;
		}
		return new HashMap<>();
	}

	private static void saveUserMap(HashMap<SantaDataType, String> userMap, String userID) throws IOException
	{
		FileOutputStream fileOutputStream = new FileOutputStream("santa/" + userID);
		ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);

		objectOutputStream.writeObject(userMap);

		objectOutputStream.close();
		fileOutputStream.close();
	}

	public enum SantaDataType
	{
		ADDRESS,
		NOTE,
		SANTA,
		SANTEE
	}

	public static class SantaNotFoundException extends Exception
	{
		SantaNotFoundException()
		{
			super();
		}
	}

	public static class SanteeNotFoundException extends Exception
	{
		SanteeNotFoundException()
		{
			super();
		}
	}
}
