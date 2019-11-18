package com.crimsonValkyrie.protocol.commands;

import com.crimsonValkyrie.protocol.main.Bot;
import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;

public class Shutdown extends Command
{
	public Shutdown()
	{
		name = "shutdown";
		ownerCommand = true;

		help = "Shuts the bot down";
	}

	protected void execute(CommandEvent commandEvent)
	{
		Bot.getJDA().shutdown();
	}
}
