package com.example.coderank.post.challenge.limited;

import android.content.ContentValues;
import com.example.coderank.post.challenge.Challenge;
import com.example.coderank.post.challenge.limited.fragments.LimitedOverview;
import com.example.coderank.post.challenge.limited.fragments.LimitedSubmissions;
import com.example.coderank.post.challenge.normal.NormalChallenge;
import com.example.coderank.post.challenge.fragments.OverviewFragment;
import com.example.coderank.post.challenge.fragments.SubmissionsFragment;
import com.example.coderank.sql.Database;
import com.example.coderank.sql.DataConverter;
import com.example.coderank.utils.MidnightCalendar;
import java.util.Date;

/**
 * Adds a time limit to the NormalChallenge.
 * Each LimitedChallenge is created with a final submission date, and when it passes there are no more submissions allowed.
 */
public class LimitedChallenge extends NormalChallenge
{
    /**
     * The final date for submissions.
     * @see MidnightCalendar
     */
    public MidnightCalendar finalDate;

    public LimitedChallenge(long id, long authorId, Date creationDate, String title, String content, MidnightCalendar finalDate)
    {
        super(id, authorId, creationDate, title, content);

        this.finalDate = finalDate;
    }

    public LimitedChallenge(long authorId, Date creationDate, String title, String content, MidnightCalendar finalDate)
    {
        this(INVALID_ID, authorId, creationDate, title, content, finalDate);
    }

    @Override
    public int getType()
    {
        return Challenge.TYPE_LIMITED;
    }

    public void write(ContentValues values)
    {
        /* Use NormalChallenge's write function, as we are extending it */
        super.write(values);
        /* Add the final date */
        values.put(Database.COL_CH_END_DATE, DataConverter.midnightCalendarToLong(finalDate));
    }

    @Override
    public OverviewFragment<?> createOverviewFragment()
    {
        return new LimitedOverview(this);
    }

    @Override
    public SubmissionsFragment<?> createSubmissionsFragment()
    {
        return new LimitedSubmissions(this);
    }
}
