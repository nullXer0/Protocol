package com.crimsonValkyrie.protocol.commands.santa;

import com.crimsonValkyrie.protocol.commands.Categories;
import com.crimsonValkyrie.protocol.commands.CommandUtils;
import com.crimsonValkyrie.protocol.main.Bot;
import com.crimsonValkyrie.protocol.misc.santa.SantaUtils;
import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;

import java.io.IOException;
import java.util.*;

public class SantaAdmin extends Command
{
	private static Random random = new Random();

	public SantaAdmin()
	{
		name = "santaAdmin";
		ownerCommand = true;
		guildOnly = false;

		category = Categories.SANTA;
		help = "Admin command for managing Santas/Santees";
	}

	protected void execute(CommandEvent event)
	{
		event.reply("What would you like to do?\n**distribute**, **edit**, **clear**");
		CommandUtils.waitForResponse(event, 30, receivedEvent ->
		{
			String firstResponse = receivedEvent.getMessage().getContentRaw().toLowerCase();
			switch(firstResponse)
			{
				case "distribute":
					event.reply("Do you want to **resend** the current Santas? or distribute **new** Santas?");
					CommandUtils.waitForResponse(event, 15, receivedEvent1 ->
					{
						Guild guild = Bot.getJDA().getGuildById(188929540968415233L);
						List<Member> santeeList = Objects.requireNonNull(guild).getMembersWithRoles(guild.getRoleById(645596062954029061L));
						List<Member> santaList = new ArrayList<>(santeeList);

						String secondResponse = receivedEvent1.getMessage().getContentRaw().toLowerCase();
						if(secondResponse.equals("new"))
						{
							event.reply("___**WARNING:**___ This action will clear all current Santas/Santees and is **irreverisble**, do you want to proceed?");
							CommandUtils.waitForResponse(event, 15, receivedEvent2 ->
							{
								String thirdResponse = receivedEvent2.getMessage().getContentRaw().toLowerCase();
								switch(thirdResponse)
								{
									case "yes":
										distributeNewSantas(santaList, santeeList);
										sendNewSanteeMessage(event, santaList, false);
										break;
									case "no":
										event.reply("Cancelling");
										break;
									default:
										event.reply("Invalid respose. Cancelling");
										break;
								}
							});
						}
						else if(secondResponse.equals("resend"))
						{
							sendNewSanteeMessage(event, santaList, true);
						}
						else
						{
							event.reply("Invalid response. Cancelling");
						}
					});
					break;
				case "edit":
					event.reply("This has not been implemented yet");
					break;
				case "clear":
					event.reply("___**WARNING:**___ This action will clear all current Santas/Santees and is **irreverisble**, do you want to proceed?");
					CommandUtils.waitForResponse(event, 15, receivedEvent1 ->
					{
						String secondResponse = receivedEvent1.getMessage().getContentRaw().toLowerCase();
						switch(secondResponse)
						{
							case "yes":
								clearSantas();
								event.reply("Successfully cleared all Santas/Santees");

								break;
							case "no":
								event.reply("Cancelling");
								break;
							default:
								event.reply("Invalid response. Cancelling");
								break;
						}
					});
					break;
				default:
					event.reply("Invalid response. Cancelling");
					break;
			}
		});
	}

	private static void distributeNewSantas(List<Member> santaList, List<Member> santeeList)
	{
		for(Member santa : santaList)
		{
			int santee = random.nextInt(santeeList.size());
			while(santeeList.get(santee) == santa)
			{
				santee = random.nextInt(santeeList.size());
			}

			try
			{
				SantaUtils.writeSanta(santeeList.get(santee).getId(), santa.getId());
				SantaUtils.writeSantee(santa.getId(), santeeList.get(santee).getId());
			}
			catch(IOException | ClassNotFoundException exception)
			{
				exception.printStackTrace();
			}

			santeeList.remove(santee);
		}
	}

	private static void sendNewSanteeMessage(CommandEvent event, List<Member> santaList, boolean isResend)
	{
		for(Member santa : santaList)
		{
			try
			{
				santa.getUser().openPrivateChannel().complete().sendMessage((isResend ? "___**This is a resend of the previous distribution. Your Santa and Santee have not been changed**___\n" : "") + getNewSanteeMessage(santa.getId())).queue();
			}
			catch(IOException | ClassNotFoundException e)
			{
				event.reply("Exception encountered when messaging santa " + santa.getUser().getAsTag() + e.getMessage());
				e.printStackTrace();
			}
			catch(SantaUtils.SanteeNotFoundException e)
			{
				event.reply("Could not find Santee for " + santa.getUser().getAsTag());
			}
		}
	}

	private static String getNewSanteeMessage(String santaID) throws IOException, ClassNotFoundException, SantaUtils.SanteeNotFoundException
	{
		String santeeID = SantaUtils.getSantee(Objects.requireNonNull(Bot.getJDA().getUserById(santaID)).getId());
		HashMap<SantaUtils.SantaDataType, String> santeeMap = SantaUtils.getUserMap(santeeID);

		return "You are the santa for " + Objects.requireNonNull(Bot.getJDA().getUserById(santeeID)).getAsTag()
				+ (santeeMap.containsKey(SantaUtils.SantaDataType.ADDRESS) && !santeeMap.get(SantaUtils.SantaDataType.ADDRESS).equals("")
				? "\nThey are eligible to receive digital and physical gifts, their address is: " + santeeMap.get(SantaUtils.SantaDataType.ADDRESS)
				: "\nThey are only eligible for digital gifts")
				+ (santeeMap.containsKey(SantaUtils.SantaDataType.NOTE) && !santeeMap.get(SantaUtils.SantaDataType.NOTE).equals("")
				? "\nThey have left a note: " + santeeMap.get(SantaUtils.SantaDataType.NOTE)
				: "");
	}

	private static void clearSantas()
	{
		Guild guild = Bot.getJDA().getGuildById(188929540968415233L);
		List<Member> santaList = Objects.requireNonNull(guild).getMembersWithRoles(guild.getRoleById(645596062954029061L));
		for(Member santa : santaList)
		{
			try
			{
				SantaUtils.clearSanta(santa.getId());
				SantaUtils.clearSantee(santa.getId());
			}
			catch(IOException | ClassNotFoundException e)
			{
				e.printStackTrace();
			}
		}
	}
}
