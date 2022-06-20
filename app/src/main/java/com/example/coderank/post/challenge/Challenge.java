package com.example.coderank.post.challenge;

import android.content.ContentValues;
import androidx.annotation.NonNull;
import com.example.coderank.post.Post;
import com.example.coderank.post.challenge.fragments.OverviewFragment;
import com.example.coderank.post.challenge.fragments.SubmissionsFragment;
import com.example.coderank.sql.Database;
import com.example.coderank.sql.DataConverter;
import java.util.Date;

/**
 * Represents an abstract Challenge within the app.
 * Extends Post, as a Challenge is a type of Post. This allows for easy management the Comments & Ratings for this Challenge.
 * @see Post
 *
 * Each Challenge is responsible for creating the two Fragments displaying it - OverviewFragment & SubmissionsFragment.
 * Each Challenge Type is responsible for implementing the different behaviors of its Fragments.
 */
public abstract class Challenge extends Post
{
    /**
     * The ints representing each unique Challenge Type.
     * Each int is unique and identifies the different types.
     * This is used later on in the Database to identify the different Challenge types.
     */
    public static final int TYPE_NORMAL = 1;
    public static final int TYPE_LIMITED = 2;

    /* This is used for when we pass a Challenge's ID through an Intent, e.t.c */
    public final static String KEY_ID = "challengeId";
    /* Represents an invalid ID, for when we don't know the Challenge's ID */
    public final static long INVALID_ID = -1;

    /* All properties of a Challenge */
    public long authorId;
    public Date creationDate;
    public String title;

    /**
     * Constructs a Challenge with an already known ID.
     *
     * @param id the ID of the Challenge.
     * @param authorId the ID of the author of this Challenge.
     * @param creationDate the creation date of this Challenge.
     * @param title the title of this Challenge.
     */
    public Challenge(long id, long authorId, Date creationDate, String title)
    {
        /* Call Post's constructor, using the tables for a Challenge's comments & ratings */
        super(Database.TBL_CH_COMMENTS, Database.TBL_CH_RATINGS, id);

        this.authorId = authorId;
        this.creationDate = creationDate;
        this.title = title;
    }

    /**
     * Constructs a Challenge with an unknown ID. Passes 'INVALID_ID' to the ID field.
     *
     * @param authorId the ID of the author of this Challenge.
     * @param creationDate the creation date of this Challenge.
     * @param title the title of this Challenge.
     */
    public Challenge(long authorId, Date creationDate, String title)
    {
        this(INVALID_ID, authorId, creationDate, title);
    }

    /**
     * This is used to identify the different Challenge Types.
     * Forces implementation by other Challenge Types.
     *
     * @return the unique int identifier of this Challenge' Type.
     */
    public abstract int getType();

    /**
     * Responsible for writing this Challenge's fields into a ContentValues.
     * Used for writing Challenge's into Database.
     *
     * @param values the ContentValues we write into.
     */
    public void write(ContentValues values)
    {
        values.put(Database.COL_CH_TYPE, getType());
        values.put(Database.COL_CH_AUTHOR_ID, authorId);
        values.put(Database.COL_CH_CREATE_DATE, DataConverter.dateToLong(creationDate));
        values.put(Database.COL_CH_TITLE, title);
    }

    /**
     * Each Challenge object is responsible for creating the OverviewFragment that displays its general overview.
     * This is abstract to allow other Challenge Types to implement different Fragment behaviors.
     *
     * @return this Challenge's OverviewFragment.
     */
    public abstract OverviewFragment<?> createOverviewFragment();
    /**
     * Each Challenge object is responsible for creating the SubmissionsFragment that displays its submissions.
     * This is abstract to allow other Challenge Types to implement different Fragment behaviors.
     *
     * @return this Challenge's SubmissionsFragment.
     */
    public abstract SubmissionsFragment<?> createSubmissionsFragment();

    @NonNull
    @Override
    public String toString()
    {
        return "<" + id + ", " + authorId + ", " + creationDate.toString() + ">";
    }
}
