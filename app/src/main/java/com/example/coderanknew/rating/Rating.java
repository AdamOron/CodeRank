package com.example.coderanknew.rating;

import com.example.coderanknew.sql.Database;

public class Rating
{
	public enum Context
	{
		CHALLENGE(Database.TBL_CH_RATINGS),
		SUBMISSION(Database.TBL_SUB_RATINGS);

		public final String table;

		Context(String table)
		{
			this.table = table;
		}
	}

	public long authorId;
	public long contextId;
	public float rating;

	public Rating(long authorId, long threadId, float rating)
	{
		this.authorId = authorId;
		this.contextId = threadId;
		this.rating = rating;
	}
}
