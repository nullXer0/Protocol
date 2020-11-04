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
	private static final long MESSAGE = 645689570322808832L;
	private static final long ROLE = 645596062954029061L;
	private static final long CHANNEL = 773649859856826413L;
	private static final String EMOTE = "🎁";

	public void onMessageReactionAdd(@Nonnull MessageReactionAddEvent event)
	{
		if(event.getMessageIdLong() == MESSAGE)
		{
			MessageReaction.ReactionEmote reactionEmote = event.getReactionEmote();
			if(reactionEmote.isEmoji() && reactionEmote.getEmoji().equals(EMOTE))
			{
				event.getGuild().addRoleToMember(Objects.requireNonNull(event.getMember()), Objects.requireNonNull(event.getGuild().getRoleById(ROLE))).queue();
				Objects.requireNonNull(Objects.requireNonNull(Bot.getJDA().getGuildById(188929540968415233L)).getTextChannelById(CHANNEL))
						.sendMessage(event.getUser().getAsMention() + " has joined the group, don't forget to read the pinned message").queue();
			}
		}
	}

	public void onMessageReactionRemove(@Nonnull MessageReactionRemoveEvent event)
	{
		if(event.getMessageIdLong() == MESSAGE)
		{
			MessageReaction.ReactionEmote reactionEmote = event.getReactionEmote();
			if(reactionEmote.isEmoji() && reactionEmote.getEmoji().equals(EMOTE))
			{
				event.getGuild().removeRoleFromMember(Objects.requireNonNull(event.getMember()), Objects.requireNonNull(event.getGuild().getRoleById(ROLE))).queue();
				Objects.requireNonNull(Objects.requireNonNull(Bot.getJDA().getGuildById(188929540968415233L)).getTextChannelById(CHANNEL))
						.sendMessage(event.getUser().getAsMention() + " has left the group").queue();
			}
		}
	}
}
