package com.example.coderank.entry.home;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import com.example.coderank.post.challenge.activities.ChallengeViewActivity;
import com.example.coderank.R;
import com.example.coderank.post.challenge.Challenge;
import com.example.coderank.post.challenge.ChallengePreviewAdapter;
import java.util.List;

/**
 * Fragment for a Challenge ListView. Creates ChallengePreviews for all Challenges.
 * Used in the Home screen.
 */
public class ChallengeListFragment extends Fragment
{
	/* The View of this Fragment, used to getting sub-Views, e.t.c */
	private View view;

	private List<Challenge> challenges;

	public ChallengeListFragment(List<Challenge> challenges)
	{
		this.challenges = challenges;
	}

	public void setChallenges(List<Challenge> challenges)
	{
		this.challenges = challenges;
		initChallengeList();
	}

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		this.view = inflater.inflate(R.layout.fragment_challenge_list, container, false);
		initChallengeList();
		return this.view;
	}

	private void initChallengeList()
	{
		Activity context = requireActivity();

		/* Initialize ChallengePreview Adapter */
		ChallengePreviewAdapter challengePreviewAdapter = new ChallengePreviewAdapter(context, challenges);
		/* This is done to initially draw the Previews */
		challengePreviewAdapter.notifyDataSetChanged();

		/* Create the ListView */
		ListView lvTrendingChallenges = view.findViewById(R.id.lvChallenges);
		lvTrendingChallenges.setAdapter(challengePreviewAdapter);
		lvTrendingChallenges.setClickable(true);
		/* When a ChallengePreview is clicked, enter its Challenge's View */
		lvTrendingChallenges.setOnItemClickListener((parent, view, position, id) -> enterChallengeView(challenges.get(position).id));
	}

	private void enterChallengeView(long challengeId)
	{
		Intent intent = new Intent(getContext(), ChallengeViewActivity.class);
		intent.putExtra(Challenge.KEY_ID, challengeId);
		startActivity(intent);
	}
}