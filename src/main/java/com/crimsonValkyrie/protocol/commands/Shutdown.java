package com.crimsonValkyrie.protocol.commands;

import com.crimsonValkyrie.protocol.main.Bot;
import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import org.quartz.SchedulerException;

public class Shutdown extends Command
{
	public Shutdown()
	{
		name = "shutdown";
		ownerCommand = true;
		guildOnly = false;

		category = Categories.ADMIN;
		help = "Shuts the bot down";
	}

	protected void execute(CommandEvent event)
	{
		try
		{
			Bot.shutdown();
		}
		catch(SchedulerException e)
		{
			event.reply("Bot failed to shutdown. This is a major problem");
			e.printStackTrace();
		}
	}
}
