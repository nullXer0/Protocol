package com.crimsonValkyrie.protocol.commands.santa;

import com.crimsonValkyrie.protocol.commands.Categories;
import com.crimsonValkyrie.protocol.main.Bot;
import com.crimsonValkyrie.protocol.misc.santa.SantaUtils;
import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;

import java.io.IOException;
import java.util.*;

public class SantaDistribute extends Command
{
	private Random random = new Random();

	public SantaDistribute()
	{
		name = "santa";
		ownerCommand = true;

		category = Categories.SANTA;
		help = "Send everyone their Santas";
	}

	protected void execute(CommandEvent commandEvent)
	{
		Guild guild = Bot.getJDA().getGuildById(188929540968415233L);
		if(guild != null)
		{
			List<Member> santeeList = guild.getMembersWithRoles(guild.getRoleById(645596062954029061L));
			List<Member> santaList = new ArrayList<>(santeeList);
			for(Member santa : santaList)
			{
				int santee;
				santee = random.nextInt(santeeList.size());
				while(santeeList.get(santee) == santa)
				{
					santee = random.nextInt(santeeList.size());
				}

				HashMap<SantaUtils.SantaDataType, String> santeeMap = new HashMap<>();
				try
				{
					santeeMap = SantaUtils.getUserMap(santaList.get(santee).getId());
				}
				catch(IOException | ClassNotFoundException e)
				{
					Objects.requireNonNull(Bot.getJDA().getUserById(89622569740697600L)).openPrivateChannel().complete()
							.sendMessage("An error occured getting the data for " + santeeList.get(santee).getUser().getAsTag()).queue();
					e.printStackTrace();
				}

				santa.getUser().openPrivateChannel().complete().sendMessage("You are the santa for " + santeeList.get(santee).getUser().getAsTag()
						+ (santeeMap.containsKey(SantaUtils.SantaDataType.ADDRESS) && !santeeMap.get(SantaUtils.SantaDataType.ADDRESS).equals("")
						? "\nThey are eligible to receive digital and physical gifts, their address is: " + santeeMap.get(SantaUtils.SantaDataType.ADDRESS)
						: "\nThey are only eligible for digital gifts")
						+ (santeeMap.containsKey(SantaUtils.SantaDataType.NOTE) && !santeeMap.get(SantaUtils.SantaDataType.NOTE).equals("")
						? "\nThey have left a note: " + santeeMap.get(SantaUtils.SantaDataType.NOTE)
						: "")).queue();

				try
				{
					SantaUtils.writeSanta(santeeList.get(santee).getId(), santa.getId());
					SantaUtils.writeSantee(santa.getId(), santeeList.get(santee).getId());
				}
				catch(IOException | ClassNotFoundException e)
				{
					e.printStackTrace();
				}

				santeeList.remove(santee);
			}
		}
	}
}
