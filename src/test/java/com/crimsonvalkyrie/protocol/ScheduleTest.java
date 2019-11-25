package com.crimsonvalkyrie.protocol;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

import java.text.ParseException;
import java.util.HashMap;
import java.util.Scanner;

public class ScheduleTest
{
	private static Scanner scanner = new Scanner(System.in);

	private static SchedulerFactory factory;
	private static Scheduler scheduler;

	private static HashMap<Long, TestBirthdaySchedule> birthdaySchedules = new HashMap<>();

	public static void main(String[] args) throws SchedulerException, ParseException
	{
		factory = new StdSchedulerFactory();
		scheduler = factory.getScheduler();

		JobDetail jobDetail = JobBuilder.newJob(TestJob.class).withIdentity("testJob").usingJobData("testData", "this is a test").build();
		Trigger trigger = TriggerBuilder.newTrigger().forJob("testJob").withIdentity("testTrigger").withSchedule(CronScheduleBuilder.cronSchedule(new CronExpression("5 * * * * ?"))).build();
		scheduler.scheduleJob(jobDetail,trigger);
		scheduler.start();

		/*
		String userInput;
		while(true)
		{
			userInput = null;
			Long id;
			System.out.print("Enter your id: ");
			id = scanner.nextLong();
			if(birthdaySchedules.containsKey(id))
			{
				System.out.print("Do you want to edit or remove your birthday? ");
				userInput = scanner.nextLine().toLowerCase();
				if(userInput.equals("edit"))
				{
					removeBirthday(id);
				}
				else if(userInput.equals("remove"))
				{
					addBirthday(id);
				}
			}
			else
			{
				addBirthday(id);
			}
		}*/
	}

	private static void addBirthday(Long id) throws ParseException
	{
		int day, month, year;
		System.out.println("reply with \"cancel\" at anytime to cancel");
		System.out.print("What day were you born on? (ex. 22) ");
		day = scanner.nextInt();
		System.out.print("What month were you born in? ");
		month = scanner.nextInt();
		System.out.print("What year were you born in? (type \"skip\" to skip) ");

		new TestBirthdaySchedule(id, day, month);
	}

	private static void removeBirthday(Long id)
	{
		birthdaySchedules.get(id);
	}
}
