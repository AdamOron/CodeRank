package com.example.coderank.post.challenge.fragments.listeners;

import com.example.coderank.post.submission.Submission;
import java.util.List;

/**
 * Basic interface for listening to a change in a Challenge's Submissions.
 * This interface is implemented in OverviewFragment, and SubmissionsFragment notifies it whenever a new Submission is added.
 * This is done so the OverviewFragment knows to update its Submission counter.
 */
public interface OnSubmissionsChangeListener
{
	/**
	 * The listener function.
	 * Called by SubmissionFragment, implemented in OverviewFragment.
	 *
	 * @param newSubmissions the new list of the Challenge's Submissions.
	 */
	void onChange(List<Submission> newSubmissions);
}
