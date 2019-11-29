package com.crimsonValkyrie.protocol.commands;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;

import java.util.Objects;

public class GroupMove extends Command
{
	public GroupMove()
	{
		name = "groupMove";
		aliases = new String[]{"gMove"};

		arguments = "<channel> <channel>";
		help = "Moves all the members from the 1st given voice channel to the 2nd given voice channel";

		ownerCommand = true;
	}

	protected void execute(CommandEvent event)
	{
		String[] args = event.getArgs().split(" ");
		if(args.length >= 2)
		{
			Guild guild = event.getGuild();
			for(Member member : Objects.requireNonNull(guild.getVoiceChannelById(args[0])).getMembers())
			{
				guild.moveVoiceMember(member, guild.getVoiceChannelById(args[1])).queue();
			}
		}
	}
}
