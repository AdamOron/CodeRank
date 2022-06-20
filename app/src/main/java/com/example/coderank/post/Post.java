package com.example.coderank.post;

import android.content.Context;
import com.example.coderank.post.rating.Rating;
import com.example.coderank.sql.Database;

/**
 * Both Challenge and Submission have Comments and Ratings.
 * Instead of having duplicate logic, have a common abstract Post class.
 */
public abstract class Post
{
	/* The correct tables for getting the Post's Comments & Ratings */
	public final String TBL_COMMENTS;
	public final String TBL_RATINGS;

	/* The ID of the Post */
	public long id;

	public Post(String tblComments, String tblRatings, long id)
	{
		this.TBL_COMMENTS = tblComments;
		this.TBL_RATINGS = tblRatings;

		this.id = id;
	}

	/**
	 * Returns the Rating left by a certain User.
	 *
	 * @param context the Context from which we are calling.
	 * @param userId the ID of the User who rated.
	 * @return Rating left by the given User.
	 */
	public Rating getRatingByUser(Context context, long userId)
	{
		Database database = new Database(context);
		database.open();
		Rating rating = database.getRating(userId, this.id, TBL_RATINGS);
		database.close();
		return rating;
	}

	/**
	 * @param context the Context from which we are calling.
	 * @return the average rating of this post.
	 */
	public float getAvgRating(Context context)
	{
		Database database = new Database(context);
		database.open();
		float avg = database.getAvgPostRating(this);
		database.close();
		return avg;
	}
}
