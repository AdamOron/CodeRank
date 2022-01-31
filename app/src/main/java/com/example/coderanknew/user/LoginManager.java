package com.example.coderanknew.user;

import android.content.Context;
import android.util.Log;

import com.example.coderanknew.sql.Database;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class LoginManager
{
	private static final String HASH_FUNC = "SHA-256";

	private static User loggedIn;

	public static User getLoggedIn()
	{
		return loggedIn;
	}

	public static boolean isLoggedIn()
	{
		return loggedIn != null;
	}

	public static boolean logOut()
	{
		boolean wasLoggedIn = LoginManager.isLoggedIn();
		LoginManager.loggedIn = null;
		return wasLoggedIn;
	}

	public static boolean attemptLogin(Context context, String username, String passwordPlain)
	{
		String passwordHash = hash(passwordPlain);

		Log.d("hash", passwordHash);

		Database userDatabase = new Database(context);
		userDatabase.open();

		String selection = usernameSelection(username) + " AND " + passwordHashSelection(passwordHash);
		LoginManager.loggedIn = userDatabase.getUserByFilter(selection);

		userDatabase.close();

		return isLoggedIn();
	}

	public static boolean attemptRegister(Context context, User user, String passwordPlain)
	{
		LoginManager.logOut();

		String passwordHash = hash(passwordPlain);

		Database userDatabase = new Database(context);
		userDatabase.open();

		String usernameOrEmailSelection = usernameSelection(user.username) + " OR " + emailSelection(user.email);
		boolean usernameOrEmailExist = userDatabase.getUserByFilter(usernameOrEmailSelection) != null;

		if(!usernameOrEmailExist)
		{
			LoginManager.loggedIn = userDatabase.insertUser(user, passwordHash);
		}

		userDatabase.close();

		return LoginManager.isLoggedIn();
	}

	private static String usernameSelection(String username)
	{
		return Database.COL_U_USERNAME + " = \"" + username + "\"";
	}

	private static String emailSelection(String email)
	{
		return Database.COL_U_EMAIL + " = \"" + email + "\"";
	}

	private static String passwordHashSelection(String passwordHash)
	{
		return Database.COL_U_PASSWORD_HASH + " = \"" + passwordHash + "\"";
	}

	private static String hash(String string)
	{
		MessageDigest digest = null;

		try
		{
			digest = MessageDigest.getInstance(HASH_FUNC);
		}
		catch (NoSuchAlgorithmException e)
		{
			e.printStackTrace();
			throw new NullPointerException("Apparently " + HASH_FUNC + " doesn't exist.");
		}

		byte[] hash = digest.digest(string.getBytes(StandardCharsets.UTF_8));

		return new String(Base64.getEncoder().encode(hash));
	}
}
