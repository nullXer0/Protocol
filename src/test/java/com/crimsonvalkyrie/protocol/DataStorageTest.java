package com.crimsonvalkyrie.protocol;

import java.io.*;
import java.util.HashMap;

public class DataStorageTest
{
	private static HashMap<String, Object> map;
	private static boolean write = false;

	//Data Type Tests
	private static boolean boolTest = true;
	private static String stringTest = "This is a test string";
	private static String[] stringArrayTest = new String[]{"This ", "is ", "an ", "array ", "string ", "test"};

	@SuppressWarnings("unchecked cast")
	public static void main(String[] args) throws IOException, ClassNotFoundException
	{
		if(write)
		{
			map = new HashMap<>();
			FileOutputStream fileOutputStream = new FileOutputStream("dataTest");
			ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);

			map.put("string", stringTest);
			map.put("array", stringArrayTest);

			objectOutputStream.writeBoolean(boolTest);
			objectOutputStream.writeObject(map);

			objectOutputStream.close();
			fileOutputStream.close();
		}
		else
		{
			FileInputStream fileInputStream = new FileInputStream("dataTest");
			ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);

			boolean tempBool = objectInputStream.readBoolean();
			map = (HashMap<String, Object>)objectInputStream.readObject();

			System.out.println(tempBool);
			System.out.println(map.get("string"));
			for(String t:(String[])map.get("array"))
			{
				System.out.print(t);
			}

			objectInputStream.close();
			fileInputStream.close();
		}
	}
}
