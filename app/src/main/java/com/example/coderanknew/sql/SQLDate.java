package com.example.coderanknew.sql;

import java.util.Date;

public class SQLDate
{
	public static String toString(Date date)
	{
		return date.getDay() + "." + date.getMonth() + "." + date.getYear();
	}

	public static Date fromString(String dateString)
	{
		String[] split = dateString.split("\\.");
		return new Date(Integer.parseInt(split[0]), Integer.parseInt(split[1]), Integer.parseInt(split[2]));
	}
}
