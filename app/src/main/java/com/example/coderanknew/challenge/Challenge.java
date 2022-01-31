package com.example.coderanknew.challenge;

import com.example.coderanknew.challenge.fragments.ChallengeOverviewFragment;

import java.util.Date;

public abstract class Challenge
{
    public final static String KEY_ID = "challengeKey";

    public long id, authorId;
    public Date creationDate;

    public Challenge(long id, long authorId, Date creationDate)
    {
        this.id = id;
        this.authorId = authorId;
        this.creationDate = creationDate;
    }

    public abstract ChallengeOverviewFragment createOverviewFragment();
}
