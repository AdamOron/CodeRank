package com.example.coderank.post.comment;

import androidx.annotation.NonNull;
import java.util.Date;

/**
 * Represents a Comment left under a Post.
 */
public class Comment
{
    public static final long INVALID_ID = -1;

    public long id;
    public long authorId;
    public String content;
    public Date creationDate;
    public long postId;

    public Comment(long id, long authorId, String content, Date creationDate, long postId)
    {
        this.id = id;
        this.authorId = authorId;
        this.content = content;
        this.creationDate = creationDate;
        this.postId = postId;
    }

    public Comment(long authorId, String content, Date creationDate, long postId)
    {
        this(INVALID_ID, authorId, content, creationDate, postId);
    }

    @NonNull
    @Override
    public String toString()
    {
        return "Comment[" + id + "," + authorId + "," + content + "," + postId + "]";
    }
}
