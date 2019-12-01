package com.crimsonValkyrie.protocol.listeners;

import com.crimsonValkyrie.protocol.misc.AFKScheduler;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceSelfDeafenEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.quartz.SchedulerException;

import javax.annotation.Nonnull;

public class AFKListener extends ListenerAdapter
{
	public void onGuildVoiceSelfDeafen(@Nonnull GuildVoiceSelfDeafenEvent event)
	{
		long id = event.getMember().getIdLong();

		super.onGuildVoiceSelfDeafen(event);

		if(event.isSelfDeafened())
		{
			AFKScheduler.createAndScheduleJob(id);
			System.out.println(event.getMember().getUser().getAsTag() + " has been marked as afk");
		}
		else
		{
			try
			{
				AFKScheduler.cancelJob(id);
				System.out.println(event.getMember().getUser().getAsTag() + " is no longer afk");
			}
			catch(SchedulerException e)
			{
				e.printStackTrace();
			}
		}
	}
}
