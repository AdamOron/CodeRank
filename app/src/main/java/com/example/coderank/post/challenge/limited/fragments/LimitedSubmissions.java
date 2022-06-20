package com.example.coderank.post.challenge.limited.fragments;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.coderank.R;
import com.example.coderank.post.challenge.fragments.SubmissionsFragment;
import com.example.coderank.post.challenge.limited.LimitedChallenge;

/**
 * SubmissionsFragment for LimitedChallenge.
 * This implementation has nothing special about it.
 */
public class LimitedSubmissions extends SubmissionsFragment<LimitedChallenge>
{
	public LimitedSubmissions(LimitedChallenge challenge)
	{
		super(challenge);
	}

	@Override
	protected View inflate(LayoutInflater inflater, ViewGroup container)
	{
		/* Inflate & return the view for 'fragment_normal_challenge_submissions' (SubmissionsFragment for both Limited & Normal Challenges are identical). */
		return inflater.inflate(R.layout.fragment_normal_challenge_submissions, container, false);
	}

	@Override
	protected void prepare()
	{
		/* LimitedChallenge has no special fields - no need to prepare anything else */
	}
}
