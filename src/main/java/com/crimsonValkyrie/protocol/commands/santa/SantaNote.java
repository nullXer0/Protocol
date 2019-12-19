package com.crimsonValkyrie.protocol.commands.santa;

import com.crimsonValkyrie.protocol.commands.Categories;
import com.crimsonValkyrie.protocol.commands.CommandUtils;
import com.crimsonValkyrie.protocol.main.Bot;
import com.crimsonValkyrie.protocol.misc.santa.SantaUtils;
import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;

import java.io.IOException;
import java.util.Objects;

public class SantaNote extends Command
{
	public SantaNote()
	{
		name = "note";
		guildOnly = false;

		category = Categories.SANTA;
		arguments = "[note]";
		help = "Provide a note for your Santa to receive when they are chosen. leave empty to clear";
	}

	protected void execute(CommandEvent event)
	{
		if(event.getArgs().length() > 1600)
		{
			event.reply("Note is larger than 1600 character limit. Please try to shorten your note");
		}
		else
		{
			try
			{
				SantaUtils.writeNote(event.getArgs(), event.getAuthor().getId());
				if(event.getArgs().equals(""))
				{
					event.reply("Note successfully removed");
				}
				else
				{
					event.reply("Note successfully submitted");
					try
					{
						String santa = SantaUtils.getSanta(event.getAuthor().getId());
						event.reply("Would you like to send your updated note to your current Santa?");
						CommandUtils.waitForResponse(event, 15, receivedEvent ->
						{
							String messageRaw = receivedEvent.getMessage().getContentRaw().toLowerCase();
							switch(messageRaw)
							{
								case "y":
								case "ye":
								case "yes":
									Objects.requireNonNull(Bot.getJDA().getUserById(santa)).openPrivateChannel().complete()
											.sendMessage("___**Your Santee has updated their note**___\n" + event.getArgs()).queue();
									break;
								case "n":
								case "no":
									break;
								default:
									event.reply("Invalid response. Not sending");
									break;
							}
						});
					}
					catch(SantaUtils.SantaNotFoundException ignored)
					{
					}
				}
			}
			catch(IOException | ClassNotFoundException e)
			{
				event.reply("An error occured, please message @null_Zer0#2048 with the following message");
				event.reply(e.getMessage());
				e.printStackTrace();
			}
		}
	}
}
