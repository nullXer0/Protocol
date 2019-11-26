package com.crimsonValkyrie.protocol.commands.birthday;

import com.crimsonValkyrie.protocol.commands.Categories;
import com.crimsonValkyrie.protocol.misc.birthday.BirthdayScheduler;
import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import org.quartz.SchedulerException;

public class ForceBirthday extends Command
{
	public ForceBirthday()
	{
		name = "forceBirthday";
		ownerCommand = true;
		guildOnly = false;

		category = Categories.ADMIN;
		arguments = "<id>";
		help = "Forces the bot the announce the birthday of the given id";
	}

	protected void execute(CommandEvent event)
	{
		if(BirthdayScheduler.isInitialized())
		{
			try
			{
				BirthdayScheduler.forceTrigger("job." + event.getArgs());
			}
			catch(SchedulerException e)
			{
				event.reply("Failed to trigger job");
				e.printStackTrace();
			}
		}
		else
		{
			event.reply("Scheduler has not started yet, please wait");
		}
	}
}
