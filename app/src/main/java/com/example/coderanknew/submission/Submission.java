package com.example.coderanknew.submission;

public class Submission
{
	public static final class Language
	{
		public final String name;

		public Language(String name)
		{
			this.name = name;
		}
	}

	public static final String KEY_CHALLENGE_ID = "key_chId";

	public long id;
	public long challengeId;
	public long authorId;
	public String content;
	//public Language lang;
	public String lang;

	public Submission(long id, long challengeId, long authorId, String content, String lang)
	{
		this.id = id;
		this.challengeId = challengeId;
		this.authorId = authorId;
		this.content = content;
		this.lang = lang;
	}

	public Submission(long challengeId, long authorId, String content, String lang)
	{
		this(0, challengeId, authorId, content, lang);
	}
}
