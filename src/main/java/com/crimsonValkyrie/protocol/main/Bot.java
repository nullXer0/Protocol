package com.crimsonValkyrie.protocol.main;

import com.crimsonValkyrie.protocol.commands.GroupMove;
import com.crimsonValkyrie.protocol.commands.Shutdown;
import com.crimsonValkyrie.protocol.commands.birthday.BirthdayCommand;
import com.crimsonValkyrie.protocol.commands.birthday.ForceBirthday;
import com.crimsonValkyrie.protocol.commands.santa.SantaAddress;
import com.crimsonValkyrie.protocol.commands.santa.SantaDistribute;
import com.crimsonValkyrie.protocol.commands.santa.SantaNote;
import com.crimsonValkyrie.protocol.commands.santa.chat.ToSanta;
import com.crimsonValkyrie.protocol.commands.santa.chat.ToSantee;
import com.crimsonValkyrie.protocol.listeners.AFKListener;
import com.crimsonValkyrie.protocol.listeners.ReactListener;
import com.crimsonValkyrie.protocol.misc.AFKScheduler;
import com.crimsonValkyrie.protocol.misc.birthday.BirthdayScheduler;
import com.jagrosh.jdautilities.command.CommandClient;
import com.jagrosh.jdautilities.command.CommandClientBuilder;
import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.utils.Compression;
import org.quartz.SchedulerException;

import javax.security.auth.login.LoginException;
import java.io.IOException;

public class Bot
{
	private static JDA jda;

	Bot(String token, String ownerID, String prefix) throws LoginException, InterruptedException, SchedulerException, IOException, ClassNotFoundException
	{
		EventWaiter waiter = new EventWaiter();

		CommandClient commandClient = new CommandClientBuilder()
				.setOwnerId(ownerID)
				.setPrefix(prefix)
				.addCommands(
						// Admin Commands
						new Shutdown(),
						new GroupMove(),
						// Birthday Commands
						new ForceBirthday(),
						new BirthdayCommand(waiter),
						// Santa Commands
						new SantaAddress(),
						new SantaNote(),
						new SantaDistribute(),
						new ToSanta(),
						new ToSantee())
				.build();


		jda = new JDABuilder(token)
				.addEventListeners(commandClient, waiter, new ReactListener(), new AFKListener())
				.setCompression(Compression.NONE)
				.build()
				.awaitReady();

		BirthdayScheduler.initialize();
		AFKScheduler.initialize();
	}

	public static JDA getJDA()
	{
		return jda;
	}
}
