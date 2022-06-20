package com.example.coderank.user;

import android.graphics.Bitmap;

/**
 * Describes a User.
 *
 * This class does not keep track of a User's password, as a User object should not be aware of its password.
 * If each User object kept track of its User's password, it would be exposed whenever we read a Users from the Database.
 * Passwords should only be retrieved for validation of a User's identity (logging in/registering).
 * Simply, in one sentence - a User object should only contain the public/insensitive properties of a User.
 */
public class User
{
	/* Keys for a User's username & password, in case we need to pass them to Intents or stuff */
	public static final String KEY_USERNAME = "KEY_USERNAME";
	public static final String KEY_PASSWORD = "KEY_PASSWORD";
	/* Represents an invalid User ID. This is used when we don't know what the User's ID is. */
	public static final int INVALID_ID = -1;

	/* All properties properties of a User, except for its password. */
	public long id;
	public String firstName, lastName, email, username;
	public Bitmap picture;

	/**
	 * Constructs a User object.
	 * This is used for Users whose IDs are already known.
	 *
	 * @param id the id of the User.
	 * @param firstName the first name of the User.
	 * @param lastName the last name of the User.
	 * @param email the email of the User.
	 * @param username the username of the User.
	 * @param picture the profile picture of the User.
	 */
	public User(long id, String firstName, String lastName, String email, String username, Bitmap picture)
	{
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.username = username;
		this.picture = picture;
	}

	/**
	 * Constructs a User object.
	 * This is used for Users whose IDs are unknown, as it initializes their IDs to 'INVALID_ID'.
	 *
	 * @param firstName the first name of the User.
	 * @param lastName the last name of the User.
	 * @param email the email of the User.
	 * @param username the username of the User.
	 * @param picture the profile picture of the User.
	 */
	public User(String firstName, String lastName, String email, String username, Bitmap picture)
	{
		this(INVALID_ID, firstName, lastName, email, username, picture);
	}
}
