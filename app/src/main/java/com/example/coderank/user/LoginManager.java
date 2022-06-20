package com.example.coderank.user;

import android.content.Context;
import com.example.coderank.sql.Database;
import com.example.coderank.utils.Debug;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

/**
 * Responsible for all Login & Register related logic.
 * Keeps track of logged in User's instance.
 */
public class LoginManager
{
	/* The name of the hash function we are using to hash the passwords */
	private static final String HASH_FUNC = "SHA-256";
	/* The instance of the logged in User (null if no one is logged in) */
	private static User loggedIn;

	/**
	 * @return instance of logged in User.
	 */
	public static User getLoggedIn()
	{
		return loggedIn;
	}

	/**
	 * @return whether there is a logged in User or not.
	 */
	public static boolean isLoggedIn()
	{
		return loggedIn != null;
	}

	/**
	 * Logs out the currently logged in User (if there is one).
	 *
	 * @return whether there was a User logged in or not.
	 */
	public static boolean logOut()
	{
		/* Save whether there was a logged in User */
		boolean wasLoggedIn = LoginManager.isLoggedIn();
		/* Set logged in User to null, the same as logging out */
		LoginManager.loggedIn = null;
		/* Return whether someone was logged in or not */
		return wasLoggedIn;
	}

	/**
	 * Attempt to log in with the given User credentials passed.
	 * Hashes the given password.
	 *
	 * @param context the Context from which we are trying to log in.
	 * @param username the username of the User that is trying to log in.
	 * @param passwordPlain the password of the User that is trying to log in, in plaintext.
	 * @return whether the login was successful or not.
	 */
	public static boolean attemptLogin(Context context, String username, String passwordPlain)
	{
		/* First, log out the currently logged in User */
		LoginManager.logOut();

		/* Hash the password using the helper 'hash' function */
		String passwordHash = hash(passwordPlain);
		/* Debug the hashed password */
		Debug.print("Hash: " + passwordHash);

		/* Get database instance */
		Database userDatabase = new Database(context);
		userDatabase.open();

		/* Generate the selection query for the User who matches given credentials */
		String selection = usernameSelection(username) + " AND " + passwordHashSelection(passwordHash);
		/* Set logged in instance to the user who matches the generated selection. If no User matches, it will be null. */
		LoginManager.loggedIn = userDatabase.getUserByFilter(selection);

		userDatabase.close();

		/* Return whether login was successful or not */
		return LoginManager.isLoggedIn();
	}

	/**
	 * Attempt to register given User, associate it with given password.
	 * If registration is successful, it will also log in to the registered User.
	 * The User object and its password are separated because a User object should not be aware of its password.
	 *
	 * @param context the Context from which we are trying to register.
	 * @param user the User that we are trying to register.
	 * @param passwordPlain the password of the User we are trying to register, in plaintext.
	 * @return whether registration was successful or not.
	 */
	public static boolean attemptRegister(Context context, User user, String passwordPlain)
	{
		/* First, log out the currently logged in User */
		LoginManager.logOut();

		/* Hash the password using the helper 'hash' function */
		String passwordHash = hash(passwordPlain);

		/* Get database instance */
		Database userDatabase = new Database(context);
		userDatabase.open();

		/* Selection query for either the given User's username or its email */
		String usernameOrEmailSelection = usernameSelection(user.username) + " OR " + emailSelection(user.email);
		/*
		 * Check whether a User with the same username or email exists.
		 * If such User exists, we can't register the new User - there can't be two Users with the same username or email.
		 */
		boolean usernameOrEmailExist = userDatabase.getUserByFilter(usernameOrEmailSelection) != null;
		/* If there's no collision, insert new User into database & set logged in instance to the inserted User */
		if(!usernameOrEmailExist)
			LoginManager.loggedIn = userDatabase.insertUser(user, passwordHash);

		userDatabase.close();

		/* Return whether registration was successful or not */
		return LoginManager.isLoggedIn();
	}

	/**
	 * @param username the username we want to search for.
	 * @return selection query for User that matches given username.
	 */
	private static String usernameSelection(String username)
	{
		return Database.COL_U_USERNAME + " = \"" + username + "\"";
	}

	/**
	 * @param email the email we want to search for.
	 * @return selection query for User that matches given email.
	 */
	private static String emailSelection(String email)
	{
		return Database.COL_U_EMAIL + " = \"" + email + "\"";
	}

	/**
	 * @param passwordHash the password hash we want to search for.
	 * @return selection query for User that matches given password hash.
	 */
	private static String passwordHashSelection(String passwordHash)
	{
		return Database.COL_U_PASSWORD_HASH + " = \"" + passwordHash + "\"";
	}

	/**
	 * Hashes given string and returns the result.
	 * Uses hash function specified in static final 'HASH_FUNC' property.
	 *
	 * @param string the string we want to hash.
	 * @return the hash of the string.
	 */
	private static String hash(String string)
	{
		MessageDigest digest = null;

		try
		{
			/* Get the hash function that matches 'HASH_FUNC' */
			digest = MessageDigest.getInstance(HASH_FUNC);
		}
		catch (NoSuchAlgorithmException e)
		{
			/* This should & will never happen, as we are certain that our hash function exists */
			throw new IllegalArgumentException("Apparently " + HASH_FUNC + " doesn't exist.");
		}

		/* Hash the UTF_8 encoded bytes of our original String */
		byte[] hash = digest.digest(string.getBytes(StandardCharsets.UTF_8));
		/* Convert the hashed bytes to a Base64 String */
		return new String(Base64.getEncoder().encode(hash));
	}
}
