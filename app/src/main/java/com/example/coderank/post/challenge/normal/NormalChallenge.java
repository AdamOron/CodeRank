package com.example.coderank.post.challenge.normal;

import android.content.ContentValues;
import com.example.coderank.post.challenge.Challenge;
import com.example.coderank.post.challenge.fragments.OverviewFragment;
import com.example.coderank.post.challenge.fragments.SubmissionsFragment;
import com.example.coderank.post.challenge.normal.fragments.NormalOverview;
import com.example.coderank.post.challenge.normal.fragments.NormalSubmissions;
import com.example.coderank.sql.Database;
import java.util.Date;

/**
 * Adds a 'content' field to the abstract Challenge.
 */
public class NormalChallenge extends Challenge
{
	/* The Challenge's content */
	public String content;

	public NormalChallenge(long id, long authorId, Date creationDate, String title, String content)
	{
		super(id, authorId, creationDate, title);

		this.content = content;
	}

	public NormalChallenge(long authorId, Date creationDate, String title, String content)
	{
		this(INVALID_ID, authorId, creationDate, title, content);
	}

	@Override
	public int getType()
	{
		return Challenge.TYPE_NORMAL;
	}

	public void write(ContentValues values)
	{
		/* Use Challenge's implementation first, as we extend him */
		super.write(values);
		/* Add the content field */
		values.put(Database.COL_CH_CONTENT, content);
	}

	@Override
	public OverviewFragment<?> createOverviewFragment()
	{
		return new NormalOverview(this);
	}

	@Override
	public SubmissionsFragment<?> createSubmissionsFragment()
	{
		return new NormalSubmissions(this);
	}
}
