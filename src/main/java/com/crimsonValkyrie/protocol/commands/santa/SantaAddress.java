package com.crimsonValkyrie.protocol.commands.santa;

import com.crimsonValkyrie.protocol.commands.Categories;
import com.crimsonValkyrie.protocol.misc.santa.SantaUtils;
import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;

import java.io.IOException;

public class SantaAddress extends Command
{
	public SantaAddress()
	{
		name = "address";
		guildOnly = false;

		category = Categories.SANTA;
		arguments = "[address]";
		help = "Provide your address for your Santa to receive when they are chosen. leave empty to clear";
	}

	protected void execute(CommandEvent event)
	{
		try
		{
			SantaUtils.writeAddress(event.getArgs(), event.getAuthor().getId());
			if(event.getArgs().equals(""))
			{
				event.reply("Address successfully removed");
			}
			else
			{
				event.reply("Address successfully submitted");
			}
		}
		catch(IOException | ClassNotFoundException e)
		{
			event.reply("An error occured, please message @null_Zer0#2048 with the following message");
			event.reply(e.getMessage());
		}
	}
}
