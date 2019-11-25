package com.crimsonvalkyrie.protocol;

import org.quartz.*;

import java.text.ParseException;

public class TestBirthdaySchedule
{
	private int day;
	private int month;
	private int year;

	private JobDetail jobDetail;
	private Trigger trigger;

	TestBirthdaySchedule(Long id, int day, int month) throws ParseException
	{
		this.day = day;
		this.month = month;

		jobDetail = JobBuilder.newJob()
				.withIdentity(id.toString())
				.build();

		trigger = TriggerBuilder.newTrigger()
				.forJob(id.toString())
				.withSchedule(CronScheduleBuilder.cronSchedule(new CronExpression("* * * "+day+" "+month)))
				.build();
	}

	TestBirthdaySchedule(long id, int day, int month, int year) throws ParseException
	{
		this(id, day, month);
		this.year = year;
	}

	public JobDetail getJob()
	{
		return jobDetail;
	}

	public Trigger getTrigger()
	{
		return trigger;
	}
}
