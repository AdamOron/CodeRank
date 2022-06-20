package com.example.coderank.utils;

import android.util.Log;

/**
 * THIS IS NOT A PART OF THE PROJECT, SO I AM NOT DOCUMENTING IT.
 * JUST A FEATURE I IMPLEMENTED FOR MY OWN CONVENIENCE.
 */
public class Debug
{
	private static boolean isActive = false;

	public static void setStatus(boolean isActive)
	{
		Debug.isActive = isActive;
	}

	public static void print(String message)
	{
		if(!isActive)
			return;

		Log.d("DEBUG", message);
	}
}
