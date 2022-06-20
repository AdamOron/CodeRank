package com.example.coderank.post.rating;

import org.jetbrains.annotations.NotNull;

/**
 * Represents a Rating made for a Post.
 */
public class Rating
{
	public long authorId;
	public long postId;
	public float rating;

	public Rating(long authorId, long postId, float rating)
	{
		this.authorId = authorId;
		this.postId = postId;
		this.rating = rating;
	}

	@Override
	public @NotNull String toString()
	{
		return "<" + authorId + ", " + postId + ", " + rating + ">";
	}
}
