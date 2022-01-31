package com.example.coderanknew.challenge;

import java.util.Date;

public class NormalChallenge extends Challenge
{
	public String title, content;

	public NormalChallenge(long id, long authorId, Date creationDate, String title, String content)
	{
		super(id, authorId, creationDate);

		this.title = title;
		this.content = content;
	}

	public NormalChallenge(long authorId, String title, String content, Date creationDate)
	{
		this(0L, authorId, creationDate, title, content);
	}
}
