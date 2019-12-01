package com.crimsonValkyrie.protocol.misc;

import com.crimsonValkyrie.protocol.main.Bot;
import net.dv8tion.jda.api.entities.Guild;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;

import java.util.Objects;

public class AFKJob implements Job
{
	public void execute(JobExecutionContext context)
	{
		JobDataMap dataMap = context.getJobDetail().getJobDataMap();
		long id = dataMap.getLong("id");
		Guild guild = Objects.requireNonNull(Bot.getJDA().getGuildById(188929540968415233L));

		guild.moveVoiceMember(Objects.requireNonNull(guild.getMemberById(id)), guild.getVoiceChannelById(485312780241469440L)).queue();
		Objects.requireNonNull(Bot.getJDA().getUserById(id)).openPrivateChannel().complete()
				.sendMessage("You have been moved to AFK because you were deafened for more than 5 minutes").queue();
	}
}
