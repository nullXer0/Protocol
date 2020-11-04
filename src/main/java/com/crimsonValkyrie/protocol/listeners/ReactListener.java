package com.crimsonValkyrie.protocol.listeners;

import com.crimsonValkyrie.protocol.main.Bot;
import net.dv8tion.jda.api.entities.MessageReaction;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionRemoveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import javax.annotation.Nonnull;
import java.util.Objects;

public class ReactListener extends ListenerAdapter
{
	private static long message = 645689570322808832L;
	private static long role = 645596062954029061L;
	private static String emote = "🎁";

	public void onMessageReactionAdd(@Nonnull MessageReactionAddEvent event)
	{
		if(event.getMessageIdLong() == message)
		{
			MessageReaction.ReactionEmote reactionEmote = event.getReactionEmote();
			if(reactionEmote.isEmoji() && reactionEmote.getEmoji().equals(emote))
			{
				event.getGuild().addRoleToMember(Objects.requireNonNull(event.getMember()), Objects.requireNonNull(event.getGuild().getRoleById(role))).queue();
				Objects.requireNonNull(Objects.requireNonNull(Bot.getJDA().getGuildById(188929540968415233L)).getTextChannelById(645596398904934420L))
						.sendMessage(event.getUser().getAsMention() + " has joined the group, don't forget to read the pinned message").queue();
			}
		}
	}

	public void onMessageReactionRemove(@Nonnull MessageReactionRemoveEvent event)
	{
		if(event.getMessageIdLong() == message)
		{
			MessageReaction.ReactionEmote reactionEmote = event.getReactionEmote();
			if(reactionEmote.isEmoji() && reactionEmote.getEmoji().equals(emote))
			{
				event.getGuild().removeRoleFromMember(Objects.requireNonNull(event.getMember()), Objects.requireNonNull(event.getGuild().getRoleById(role))).queue();
				Objects.requireNonNull(Objects.requireNonNull(Bot.getJDA().getGuildById(188929540968415233L)).getTextChannelById(645596398904934420L))
						.sendMessage(event.getUser().getAsMention() + " has left the group").queue();
			}
		}
	}
}
