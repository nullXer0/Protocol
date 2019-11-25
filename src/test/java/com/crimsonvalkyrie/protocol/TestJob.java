package com.crimsonvalkyrie.protocol;

import org.quartz.Job;
import org.quartz.JobExecutionContext;

public class TestJob implements Job
{
	public void execute(JobExecutionContext context)
	{
		try
		{
		String test = context.getJobDetail().getJobDataMap().getString("testData");
		System.out.println("ping");
		int test2 = context.getJobDetail().getJobDataMap().getInt("testData2");
		System.out.println("testData: "+test);
		System.out.println("testData2: "+test2);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
}
