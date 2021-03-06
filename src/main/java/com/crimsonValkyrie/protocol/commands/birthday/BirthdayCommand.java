package com.crimsonValkyrie.protocol.commands.birthday;

import com.crimsonValkyrie.protocol.commands.Categories;
import com.crimsonValkyrie.protocol.commands.CommandUtils;
import com.crimsonValkyrie.protocol.misc.birthday.BirthdayScheduler;
import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import org.quartz.SchedulerException;

import java.io.IOException;

public class BirthdayCommand extends Command
{
	public BirthdayCommand()
	{
		name = "birthday";
		guildOnly = false;

		category = Categories.BIRTHDAY;
		help = "Used to tell the bot when your birthday it is so it can announce it in the birthdays channel";
	}

	protected void execute(CommandEvent event)
	{
		if(BirthdayScheduler.isInitialized())
		{
			final long id;

			if(event.isOwner() && !event.getArgs().equals(""))
			{
				id = Long.parseLong(event.getArgs());
			}
			else
			{
				id = event.getAuthor().getIdLong();
			}

			if(BirthdayScheduler.birthdayExists(id))
			{
				event.reply("Do you want to 'edit' or 'remove' your birthday?");
				CommandUtils.waitForResponse(event, 30, e ->
				{
					String message = e.getMessage().getContentRaw().toLowerCase();
					if(message.equals("edit"))
					{
						try
						{
							if(BirthdayScheduler.removeBirthday(id))
							{
								addBirthday(event, id);
							}
							else
							{
								event.reply("Unable to remove existing birthday, this is a problem");
							}
						}
						catch(SchedulerException | IOException ex)
						{
							ex.printStackTrace();
						}
					}
					else if(message.equals("remove"))
					{
						try
						{
							if(BirthdayScheduler.removeBirthday(id))
							{
								event.reply("Successfully removed birthday");
							}
							else
							{
								event.reply("Unable to remove birthday, this is a problem");
							}
						}
						catch(SchedulerException | IOException ex)
						{
							ex.printStackTrace();
						}
					}
					else
					{
						event.reply("Invalid response, cancelling");
					}
				});
			}
			else
			{
				addBirthday(event, id);
			}
		}
		else
		{
			event.reply("Scheduler has not started yet, please wait");
		}
	}

	private void addBirthday(CommandEvent event, long id)
	{
		event.reply("What day were you born on? (ex. 22)");
		CommandUtils.waitForResponse(event, 30, e ->
		{
			int day;
			try
			{
				day = Integer.parseInt(e.getMessage().getContentRaw());
				if(day >= 1 && day <= 31)
				{
					event.reply("What month were you born in? (ex. 7)");
					CommandUtils.waitForResponse(event, 30, e1 ->
					{
						int month;
						try
						{
							month = Integer.parseInt(e1.getMessage().getContentRaw());
							if(month >= 1 && month <= 12)
							{
								event.reply("What year were you born in? (ex. 1998) 'skip' to skip");
								CommandUtils.waitForResponse(event, 30, e2 ->
								{
									int year;
									String userInput;
									userInput = e2.getMessage().getContentRaw().toLowerCase();
									if(userInput.equals("skip"))
									{
										try
										{
											BirthdayScheduler.addBirthday(id, day, month, 0);
										}
										catch(IOException ex)
										{
											ex.printStackTrace();
										}
										event.reply("Birthday added");
									}
									else
									{
										try
										{
											year = Integer.parseInt(e2.getMessage().getContentRaw());
											if(year >= 1900 && year <= 2100)
											{
												BirthdayScheduler.addBirthday(id, day, month, year);
												event.reply("Birthday added");
											}
											else
											{
												BirthdayScheduler.addBirthday(id, day, month, 0);
												event.reply("Invalid response. Birthday added without year");
											}
										}
										catch(NumberFormatException | IOException ex)
										{
											ex.printStackTrace();
											try
											{
												BirthdayScheduler.addBirthday(id, day, month, 0);
											}
											catch(IOException exc)
											{
												exc.printStackTrace();
											}
											event.reply("Invalid response. Birthday added without year");
										}
									}
								});
							}
							else
							{
								event.reply("Invalid response. cancelling");
							}
						}
						catch(NumberFormatException ex)
						{
							event.reply("Invalid response. cancelling");
						}
					});
				}
				else
				{
					event.reply("Invalid response, cancelling");
				}
			}
			catch(NumberFormatException ex)
			{
				event.reply("Invalid response, cancelling");
			}
		});
	}
}
