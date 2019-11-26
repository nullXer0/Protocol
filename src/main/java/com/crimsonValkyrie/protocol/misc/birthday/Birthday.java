package com.crimsonValkyrie.protocol.misc.birthday;

import java.io.Serializable;

class Birthday implements Serializable
{
	private long id;

	private int day;
	private int month;
	private int year;

	Birthday(Long id, int day, int month, int year)
	{
		this.id = id;
		this.day = day;
		this.month = month;
		this.year = year;
	}

	long getId()
	{
		return id;
	}

	int getDay()
	{
		return day;
	}

	int getMonth()
	{
		return month;
	}

	int getYear()
	{
		return year;
	}
}
