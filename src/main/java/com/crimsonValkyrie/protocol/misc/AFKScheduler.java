package com.crimsonValkyrie.protocol.misc;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

public class AFKScheduler
{
	private static Scheduler scheduler;
	private static boolean initialized;

	public static void initialize() throws SchedulerException
	{
		if(!initialized)
		{
			scheduler = new StdSchedulerFactory().getScheduler();
			scheduler.start();
			System.out.println("AFKScheduler started");
			initialized = true;
		}
		else
		{
			System.err.println("BirthdayScheduler is already initialized");
		}
	}

	public static void createAndScheduleJob(Long id)
	{
		try
		{
			JobBuilder jobBuilder = JobBuilder.newJob(AFKJob.class)
					.withIdentity("AFKJob." + id)
					.usingJobData("id", id);
			JobDetail jobDetail = jobBuilder.build();

			Trigger trigger = TriggerBuilder.newTrigger()
					.withIdentity("AFKTrigger." + id)
					.forJob(jobDetail)
					.startAt(DateBuilder.futureDate(10, DateBuilder.IntervalUnit.MINUTE))
					.build();

			scheduler.scheduleJob(jobDetail, trigger);
			System.out.println("Scheduled job: " + jobDetail.getKey().getName());
		}
		catch(SchedulerException e)
		{
			e.printStackTrace();
		}
	}

	public static void cancelJob(Long id) throws SchedulerException
	{
		scheduler.deleteJob(JobKey.jobKey("AFKJob." + id));
	}

	public static void forceTrigger(String id) throws SchedulerException
	{
		scheduler.triggerJob(JobKey.jobKey("AFKJob." + id));
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
