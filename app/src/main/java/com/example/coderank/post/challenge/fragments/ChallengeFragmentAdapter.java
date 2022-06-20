package com.example.coderank.post.challenge.fragments;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import com.example.coderank.post.challenge.Challenge;
import java.security.InvalidParameterException;

/**
 * Adapter used to create & bridge between the two Challenge Fragments - OverviewFragment & SubmissionsFragment.
 * Used by ChallengeViewActivity.
 */
public class ChallengeFragmentAdapter extends FragmentStateAdapter
{
	/* The OverviewFragment of the Challenge */
	private final OverviewFragment<?> overviewFragment;
	/* The SubmissionsFragment of the Challenge */
	private final SubmissionsFragment<?> submissionsFragment;

	public ChallengeFragmentAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle, Challenge challenge)
	{
		super(fragmentManager, lifecycle);

		/* Get OverviewFragment from given Challenge's abstract 'createOverviewFragment' method */
		this.overviewFragment = challenge.createOverviewFragment();
		/* Get SubmissionsFragment from given Challenge's abstract 'createSubmissionsFragment' method */
		this.submissionsFragment = challenge.createSubmissionsFragment();
		/*
		 * Set the OnSubmissionChangeListener of the SubmissionsFragment to the OverviewFragment.
		 * SubmissionsFragment notifies OverviewFragment of any changes to the Submissions list. OverviewFragment updates the Submission
		 * counter accordingly.
		 */
		this.submissionsFragment.setOnSubmissionsChangeListener(this.overviewFragment);
		/*
		 * Set the OnChallengeStateChangeListener of the OverviewFragment to the SubmissionsFragment.
		 * OverviewFragment notifies SubmissionsFragment of any changes to the Challenge's state (whether it accepts new Submissions or not).
		 * SubmissionsFragment then uses that information to hide/show the "Submit Submissions" button.
		 */
		this.overviewFragment.setOnChallengeStateChangeListener(this.submissionsFragment);
	}

	@NonNull
	@Override
	public Fragment createFragment(int position)
	{
		switch(position)
		{
			case 0:
				return overviewFragment;

			case 1:
				return submissionsFragment;
		}

		throw new InvalidParameterException(position + " is an invalid Challenge Fragment position.");
	}

	@Override
	public int getItemCount()
	{
		/* Only Overview & Submissions Fragments */
		return 2;
	}
}
