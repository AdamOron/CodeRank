package com.example.coderank.post.challenge.normal.fragments;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.example.coderank.R;
import com.example.coderank.post.challenge.fragments.OverviewFragment;
import com.example.coderank.post.challenge.normal.NormalChallenge;
import com.example.coderank.user.UserPreview;

/**
 * OverviewFragment for a NormalChallenge.
 * This implementation has nothing special about it.
 */
public class NormalOverview extends OverviewFragment<NormalChallenge>
{
	public NormalOverview(NormalChallenge challenge)
	{
		super(challenge);
	}

	@Override
	protected View inflate(LayoutInflater inflater, ViewGroup container)
	{
		/* Inflate & return the view for 'fragment_normal_challenge_overview' */
		return inflater.inflate(R.layout.fragment_normal_challenge_overview, container, false);
	}

	@Override
	protected void prepare()
	{
		UserPreview upChallengeAuthor = view.findViewById(R.id.upChallengeAuthor);
		upChallengeAuthor.setUser(challenge.authorId);

		TextView tvTitle = view.findViewById(R.id.tvChallengeTitle);
		tvTitle.setText(challenge.title);

		TextView tvContent = view.findViewById(R.id.tvTopicContent);
		tvContent.setText(challenge.content);
	}
}
