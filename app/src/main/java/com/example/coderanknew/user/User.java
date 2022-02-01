package com.example.coderanknew.user;

import android.graphics.Bitmap;

public class User
{
	public static final String KEY_USERNAME = "KEY_USERNAME";
	public static final String KEY_PASSWORD = "KEY_PASSWORD";

	public long id;
	public String firstName, lastName, email, username;
	public Bitmap picture;

	public User(long id, String firstName, String lastName, String email, String username, Bitmap picture)
	{
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.username = username;
		this.picture = picture;
	}

	public User(String firstName, String lastName, String email, String username, Bitmap picture)
	{
		this(0L, firstName, lastName, email, username, picture);
	}
}
