package com.example.coderank.post.challenge.fragments.listeners;

/**
 * Basic interface for listening to a change in a Challenge's state (opened/closed).
 * This interface is implemented in SubmissionsOverview, and OverviewFragment notifies it whenever a LimitedChallenge is over so
 * the SubmissionsFragment knows to stop accepting Submissions.
 */
public interface OnChallengeStateChangeListener
{
	/**
	 * The state of the Challenge.
	 */
	enum ChallengeState
	{
		OPENED, // Now accepting Submissions
		CLOSED, // Now not accepting Submissions
	}

	/**
	 * The listener function.
	 * Called by OverviewFragment, implemented in SubmissionsFragment.
	 *
	 * @param newState the new ChallengeState of the Challenge.
	 */
	void onChallengeStateChange(ChallengeState newState);
}
