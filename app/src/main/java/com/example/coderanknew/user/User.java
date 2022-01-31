package com.example.coderanknew.user;

public class User
{
	public static final String KEY_USERNAME = "KEY_USERNAME";
	public static final String KEY_PASSWORD = "KEY_PASSWORD";

	public long id;
	public String firstName, lastName, email, username;

	public User(long id, String firstName, String lastName, String email, String username)
	{
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.username = username;
	}

	public User(String firstName, String lastName, String email, String username)
	{
		this.id = 0L;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.username = username;
	}
}
