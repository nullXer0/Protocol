package com.crimsonValkyrie.protocol.commands.santa.chat;

import com.crimsonValkyrie.protocol.commands.Categories;
import com.crimsonValkyrie.protocol.main.Bot;
import com.crimsonValkyrie.protocol.misc.santa.SantaUtils;
import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;

import java.io.IOException;
import java.util.Objects;

public class ToSanta extends Command
{
	public ToSanta()
	{
		name = "tosanta";
		guildOnly = false;

		category = Categories.SANTA;
		arguments = "<message>";
		help = "Send a message to your Santa";
	}

	protected void execute(CommandEvent event)
	{
		try
		{
			String santaID = SantaUtils.getSanta(event.getAuthor().getId());
			Objects.requireNonNull(Bot.getJDA().getUserById(santaID)).openPrivateChannel().complete()
					.sendMessage("___***You have received a message from your Santa***___\n"+event.getArgs()).queue();
			event.reply("Your message has been sent");
		}
		catch(IOException | ClassNotFoundException e)
		{
			e.printStackTrace();
		}
		catch(SantaUtils.SantaNotFoundException e)
		{
			event.reply("Santa was unable to be found, have you been given a santa yet?");
		}
	}
}
