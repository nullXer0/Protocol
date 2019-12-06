package com.crimsonValkyrie.protocol.commands;

import com.jagrosh.jdautilities.command.CommandEvent;
import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

public class CommandUtils
{
	private static EventWaiter waiter;

	public static void initiailize(EventWaiter waiter)
	{
		CommandUtils.waiter = waiter;
	}

	public static void waitForResponse(CommandEvent event, int seconds, Consumer<MessageReceivedEvent> consumer)
	{
		if(waiter != null)
		{
			waiter.waitForEvent(MessageReceivedEvent.class,
					e -> e.getAuthor().equals(event.getAuthor())
							&& e.getChannel().equals(event.getChannel())
							&& !e.getMessage().equals(event.getMessage()), consumer,
					seconds, TimeUnit.SECONDS, () -> event.reply("No response. cancelling"));
		}
	}
}
