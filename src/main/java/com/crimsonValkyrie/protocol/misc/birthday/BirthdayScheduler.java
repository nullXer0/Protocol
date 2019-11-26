package com.crimsonValkyrie.protocol.misc.birthday;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;

public class BirthdayScheduler
{
	private static Path birthdayFile = Paths.get("birthdays");

	private static Scheduler scheduler;
	private static boolean initialized;

	private static HashMap<Long, Birthday> birthdayMap;

	public static void initialize() throws SchedulerException, IOException, ClassNotFoundException
	{
		if(!initialized)
		{
			scheduler = new StdSchedulerFactory().getScheduler();
			scheduler.start();
			loadBirthdaysToFile();
			System.out.println("BirthdayScheduler started");
			initialized = true;
		}
		else
		{
			System.err.println("BirthdayScheduler is already initialized");
		}
	}

	public static void addBirthday(Long id, int day, int month, int year) throws IOException
	{
		Birthday birthday = new Birthday(id, day, month, year);
		createAndScheduleJob(birthday);
		birthdayMap.put(id, birthday);
		saveBirthdaysToFile();
	}

	public static boolean removeBirthday(Long id) throws SchedulerException, IOException
	{
		if(scheduler.deleteJob(JobKey.jobKey("job." + id)))
		{
			birthdayMap.remove(id);
			saveBirthdaysToFile();
			return true;
		}
		return false;
	}

	public static boolean birthdayExists(Long id)
	{
		return birthdayMap.containsKey(id);
	}

	@SuppressWarnings("unchecked cast")
	private static void loadBirthdaysToFile() throws IOException, ClassNotFoundException
	{
		if(Files.exists(birthdayFile))
		{
			FileInputStream fileInputStream = new FileInputStream(birthdayFile.toFile());
			ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);

			birthdayMap = (HashMap<Long, Birthday>) objectInputStream.readObject();

			System.out.println("Found " + birthdayMap.size() + " birthdays in file");

			if(birthdayMap.size() > 0)
			{
				for(Birthday birthday : birthdayMap.values())
				{
					createAndScheduleJob(birthday);
				}
				System.out.println("Successfully scheduled birthdays");
			}

			objectInputStream.close();
			fileInputStream.close();
		}
		else
		{
			System.out.println("No birthday file found, creating empty map");
			birthdayMap = new HashMap<>();
		}
	}

	private static void saveBirthdaysToFile() throws IOException
	{
		FileOutputStream fileOutputStream = new FileOutputStream(birthdayFile.toFile());
		ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);

		objectOutputStream.writeObject(birthdayMap);

		objectOutputStream.close();
		fileOutputStream.close();

		System.out.println("Successfully saved birthdays to file");
	}

	private static void createAndScheduleJob(Birthday birthday)
	{
		long id = birthday.getId();
		int day = birthday.getDay();
		int month = birthday.getMonth();
		int year = birthday.getYear();

		try
		{
			JobBuilder jobBuilder = JobBuilder.newJob(BirthdayJob.class)
					.withIdentity("job." + id)
					.usingJobData("id", id);
			if(year != 0)
			{
				jobBuilder = jobBuilder.usingJobData("year", year);
			}
			JobDetail jobDetail = jobBuilder.build();

			Trigger trigger = TriggerBuilder.newTrigger()
					.withIdentity("trigger." + id)
					.forJob(jobDetail)
					.withSchedule(CronScheduleBuilder.cronSchedule("0 0 12 " + day + " " + month + " ?").withMisfireHandlingInstructionFireAndProceed()).startNow()
					.build();

			scheduler.scheduleJob(jobDetail, trigger);
			System.out.println("Scheduled job: " + jobDetail.getKey().getName());
		}
		catch(SchedulerException e)
		{
			e.printStackTrace();
		}
	}

	public static void forceTrigger(String id) throws SchedulerException
	{
		scheduler.triggerJob(JobKey.jobKey(id));
	}

	public static boolean isInitialized()
	{
		return initialized;
	}

	public static void shutdown() throws SchedulerException
	{
		scheduler.shutdown();
	}
}
