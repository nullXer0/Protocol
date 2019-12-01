package com.crimsonValkyrie.protocol.commands;

import com.crimsonValkyrie.protocol.main.Bot;
import com.crimsonValkyrie.protocol.misc.AFKScheduler;
import com.crimsonValkyrie.protocol.misc.birthday.BirthdayScheduler;
import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import org.quartz.SchedulerException;

public class Shutdown extends Command
{
	public Shutdown()
	{
		name = "shutdown";
		ownerCommand = true;

		category = Categories.ADMIN;
		help = "Shuts the bot down";
	}

	protected void execute(CommandEvent event)
	{
		try
		{
			BirthdayScheduler.shutdown();
			AFKScheduler.shutdown();
			Bot.getJDA().shutdown();
		}
		catch(SchedulerException e)
		{
			event.reply("Exception occured when trying to shut down the scheduler, unable to shut down bot");
			e.printStackTrace();
		}
	}
}
