package com.crimsonValkyrie.protocol.listeners;

import com.crimsonValkyrie.protocol.main.Bot;
import net.dv8tion.jda.api.entities.MessageReaction;
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.message.GenericMessageEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionRemoveEvent;
import net.dv8tion.jda.api.hooks.EventListener;

import javax.annotation.Nonnull;
import java.util.Objects;

public class ReactListener implements EventListener
{
	public void onEvent(@Nonnull GenericEvent event)
	{
		if(event instanceof GenericMessageEvent)
		{
			onMessageEvent((GenericMessageEvent) event);
		}
	}

	private void onMessageEvent(@Nonnull GenericMessageEvent event)
	{
		// Secret Santa
		long message = 645689570322808832L;
		long role = 645596062954029061L;
		String emote = "🎁";

		if(event.getMessageIdLong() == message)
		{
			if(event instanceof MessageReactionAddEvent)
			{
				MessageReaction.ReactionEmote reactionEmote = ((MessageReactionAddEvent) event).getReactionEmote();
				if(reactionEmote.isEmoji() && reactionEmote.getEmoji().equals(emote))
				{
					event.getGuild().addRoleToMember(Objects.requireNonNull(((MessageReactionAddEvent) event).getMember()), Objects.requireNonNull(event.getGuild().getRoleById(role))).queue();
					Objects.requireNonNull(Objects.requireNonNull(Bot.getJDA().getGuildById(188929540968415233L)).getTextChannelById(645596398904934420L))
							.sendMessage(((MessageReactionAddEvent) event).getUser().getAsMention() + " has joined the group, don't forget to read the pinned message").queue();
				}
			}
			else if(event instanceof MessageReactionRemoveEvent)
			{
				MessageReaction.ReactionEmote reactionEmote = ((MessageReactionRemoveEvent) event).getReactionEmote();
				if(reactionEmote.isEmoji() && reactionEmote.getEmoji().equals(emote))
				{
					event.getGuild().removeRoleFromMember(Objects.requireNonNull(((MessageReactionRemoveEvent) event).getMember()), Objects.requireNonNull(event.getGuild().getRoleById(role))).queue();
					Objects.requireNonNull(Objects.requireNonNull(Bot.getJDA().getGuildById(188929540968415233L)).getTextChannelById(645596398904934420L))
							.sendMessage(((MessageReactionRemoveEvent) event).getUser().getAsMention() + " has left the group").queue();
				}
			}
		}
	}
}
