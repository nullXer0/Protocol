package com.crimsonValkyrie.protocol.commands.santa.chat;

import com.crimsonValkyrie.protocol.commands.Categories;
import com.crimsonValkyrie.protocol.commands.CommandUtils;
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
		event.reply("You are about to send this message to your ***Santa***. You do not know your Santa, they are the one sending you a gift\nAre you sure you want to send the message?");
		CommandUtils.waitForResponse(event, 15, e ->
		{
			String messageRaw = e.getMessage().getContentRaw().toLowerCase();
			if(messageRaw.equals("y") || messageRaw.equals("ye") || messageRaw.equals("yes"))
			{
				try
				{
					String santaID = SantaUtils.getSanta(event.getAuthor().getId());
					Objects.requireNonNull(Bot.getJDA().getUserById(santaID)).openPrivateChannel().complete()
							.sendMessage("___***You have received a message from your Santee***___\n" + event.getArgs()).queue();
					event.reply("Your message has been sent");
				}
				catch(IOException | ClassNotFoundException exception)
				{
					exception.printStackTrace();
				}
				catch(SantaUtils.SantaNotFoundException exception)
				{
					event.reply("Santa was unable to be found, have you been given a santa yet?");
				}
			}
			else if(messageRaw.equals("n") || messageRaw.equals("no"))
			{
				event.reply("Cancelling message");
			}
			else
			{
				event.reply("Invalid response. Cancelling message");
			}
		});
	}
}
