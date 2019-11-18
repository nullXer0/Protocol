package com.crimsonValkyrie.protocol.main;

import com.crimsonValkyrie.protocol.commands.Shutdown;
import com.crimsonValkyrie.protocol.commands.santa.SantaAddress;
import com.crimsonValkyrie.protocol.commands.santa.SantaDistribute;
import com.crimsonValkyrie.protocol.commands.santa.SantaNote;
import com.crimsonValkyrie.protocol.commands.santa.chat.ToSanta;
import com.crimsonValkyrie.protocol.commands.santa.chat.ToSantee;
import com.crimsonValkyrie.protocol.listeners.ReactListener;
import com.jagrosh.jdautilities.command.CommandClient;
import com.jagrosh.jdautilities.command.CommandClientBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.utils.Compression;

import javax.security.auth.login.LoginException;

public class Bot
{
	private static JDA jda;

	Bot(String token, String ownerID, String prefix) throws LoginException, InterruptedException
	{
		CommandClient commandClient = new CommandClientBuilder()
				.setOwnerId(ownerID)
				.setPrefix(prefix)
				.addCommands(new Shutdown(),
						new SantaAddress(),
						new SantaNote(),
						new SantaDistribute(),
						new ToSanta(),
						new ToSantee())
				.build();


		jda = new JDABuilder(token)
				.addEventListeners(commandClient, new ReactListener())
				.setCompression(Compression.NONE)
				.build()
				.awaitReady();
	}

	public static JDA getJDA()
	{
		return jda;
	}
}
