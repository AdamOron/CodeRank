package com.example.coderanknew.comment;

import androidx.annotation.NonNull;

import java.util.Date;

public class Comment {
    public long id;
    public long authorId;
    public String content;
    public Date creationDate;
    public long parent;

    public Comment(long id, long authorId, String content, Date creationDate, long parent)
    {
        this.id = id;
        this.authorId = authorId;
        this.content = content;
        this.creationDate = creationDate;
        this.parent = parent;
    }

    public Comment(long authorId, String content, Date creationDate, long parent)
    {
        this.authorId = authorId;
        this.content = content;
        this.creationDate = creationDate;
        this.parent = parent;
    }

    @NonNull
    @Override
    public String toString()
    {
        return "Comment[" + id + "," + authorId + "," + content + "," + parent + "]";
    }
}
