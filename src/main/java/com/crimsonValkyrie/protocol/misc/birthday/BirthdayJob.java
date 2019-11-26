package com.crimsonValkyrie.protocol.misc.birthday;

import com.crimsonValkyrie.protocol.main.Bot;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;

import java.util.Calendar;
import java.util.Objects;

public class BirthdayJob implements Job
{
	public void execute(JobExecutionContext context)
	{
		try
		{
			JobDataMap dataMap = context.getJobDetail().getJobDataMap();

			long id = dataMap.getLong("id");
			int year;
			try
			{
				year = dataMap.getInt("year");
			}
			catch(ClassCastException e)
			{
				System.out.println("No year found");
				year = 0;
			}

			System.out.println("year is "+year);
			Objects.requireNonNull(Bot.getJDA().getTextChannelById(646885589026734101L))
					.sendMessage("It's <@" + id + ">'s Cake Day! "
							+ (year != 0 ? "They have turned " + (Calendar.getInstance().get(Calendar.YEAR) - year) + " years old today! " : "")
							+ "Wish them a happy Cake Day with a \uD83C\uDF70")
					.complete().addReaction("\uD83C\uDF70").queue();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
}
