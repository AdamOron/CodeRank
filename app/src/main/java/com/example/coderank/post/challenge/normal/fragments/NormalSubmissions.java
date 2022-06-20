package com.example.coderank.post.challenge.normal.fragments;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.coderank.R;
import com.example.coderank.post.challenge.fragments.SubmissionsFragment;
import com.example.coderank.post.challenge.normal.NormalChallenge;

/**
 * SubmissionsFragment for a NormalChallenge.
 * This implementation has nothing special about it.
 */
public class NormalSubmissions extends SubmissionsFragment<NormalChallenge>
{
	public NormalSubmissions(NormalChallenge challenge)
	{
		super(challenge);
	}

	@Override
	protected View inflate(LayoutInflater inflater, ViewGroup container)
	{
		/* Inflate & return the view for 'fragment_normal_challenge_submissions' */
		return inflater.inflate(R.layout.fragment_normal_challenge_submissions, container, false);
	}

	@Override
	protected void prepare()
	{
		/* NormalChallenge has no special fields - no need to prepare anything else */
	}
}
