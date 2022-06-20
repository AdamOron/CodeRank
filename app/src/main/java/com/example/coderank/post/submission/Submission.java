package com.example.coderank.post.submission;

import com.example.coderank.post.Post;
import com.example.coderank.sql.Database;

import java.util.Date;

/**
 * Represents a Submission made to a Challenge.
 * Extends Post, as a Submission is a type of Post. This allows for easy management the Comments & Ratings for this Challenge.
 */
public class Submission extends Post
{
	/* String ID for the Submission's Challenge's ID */
	public static final String KEY_CHALLENGE_ID = "key_chId";
	/* Represents an invalid ID for a Submission that hasn't been inserted into the Database yet */
	public static final int INVALID_ID = -1;

	/* String array containing each accepted programming language */
	public static final String[] LANGUAGES = {"Java", "Python", "C++"};

	/* Submission's fields */
	public long challengeId;
	public long authorId;
	public String content;
	public String lang;
	public Date date;

	/**
	 * Constructs Submission that's already in the Database with an ID.
	 *
	 * @param id the id of the Submission.
	 * @param challengeId the ID of the Challenge of the Submission.
	 * @param authorId the ID of the author of the of the Submission.
	 * @param content the content of the Submission.
	 * @param lang the programming language of the Submission.
	 * @param date the submission date of the Submission.
	 */
	public Submission(long id, long challengeId, long authorId, String content, String lang, Date date)
	{
		/* Call Post's constructor, using the tables for a Submission's comments & ratings */
		super(Database.TBL_SUB_COMMENTS, Database.TBL_SUB_RATINGS, id);

		this.challengeId = challengeId;
		this.authorId = authorId;
		this.content = content;
		this.lang = lang;
		this.date = date;
	}

	/**
	 * Constructs Submission that's hasn't been inserted into the Database.
	 *
	 * @param challengeId the ID of the Challenge of the Submission.
	 * @param authorId the ID of the author of the of the Submission.
	 * @param content the content of the Submission.
	 * @param lang the programming language of the Submission.
	 * @param date the submission date of the Submission.
	 */
	public Submission(long challengeId, long authorId, String content, String lang, Date date)
	{
		this(INVALID_ID, challengeId, authorId, content, lang, date);
	}
}
