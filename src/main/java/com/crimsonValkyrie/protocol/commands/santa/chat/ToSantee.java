package com.crimsonValkyrie.protocol.commands.santa.chat;

import com.crimsonValkyrie.protocol.main.Bot;
import com.crimsonValkyrie.protocol.santa.SantaUtils;
import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;

import java.io.IOException;
import java.util.Objects;

public class ToSantee extends Command
{
	public ToSantee()
	{
		name = "tosantee";
		guildOnly = false;

		arguments = "<message>";
		help = "Send a message to your Santee";
	}

	protected void execute(CommandEvent event)
	{
		try
		{
			String santeeID = SantaUtils.getSantee(event.getAuthor().getId());
			Objects.requireNonNull(Bot.getJDA().getUserById(santeeID)).openPrivateChannel().complete()
					.sendMessage("___***You have received a message from your Santee***___\n"+event.getArgs()).queue();
			event.reply("Your message has been sent");
		}
		catch(IOException | ClassNotFoundException e)
		{
			e.printStackTrace();
		}
		catch(SantaUtils.SanteeNotFoundException e)
		{
			event.reply("Santee was unable to be found, have you been given a Santee yet?");
		}
	}
}
