package com.example.coderank.post.challenge.fragments;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;
import com.example.coderank.post.challenge.Challenge;
import com.example.coderank.post.challenge.fragments.listeners.OnChallengeStateChangeListener;
import com.example.coderank.post.challenge.fragments.listeners.OnSubmissionsChangeListener;
import com.example.coderank.post.comment.CommentManager;
import com.example.coderank.post.rating.Rating;
import com.example.coderank.post.submission.Submission;
import com.example.coderank.user.LoginManager;
import com.example.coderank.R;
import com.example.coderank.sql.Database;
import org.jetbrains.annotations.NotNull;
import java.util.List;

/**
 * Display Overview for given Challenge.
 * Implements OnSubmissionsChangeListener, as we want to know when new Submissions are added, e.t.c.
 *
 * @param <C> the type of Challenge we're Viewing.
 */
public abstract class OverviewFragment<C extends Challenge> extends Fragment implements OnSubmissionsChangeListener
{
	/* The Challenge we're Viewing */
	protected C challenge;
	/* This Fragment's View, used for getting its sub-Views */
	protected View view;
	/* Listens for any change in the Challenge's state. We call it whenever a change is made, to notify SubmissionsFragment. */
	protected OnChallengeStateChangeListener onChallengeStateChangeListener;
	/* Manages the comments of the Challenge */
	private CommentManager commentManager;

	private TextView tvSubCount;

	public OverviewFragment(C challenge)
	{
		this.challenge = challenge;
	}

	/**
	 * Called from FragmentAdapter to set the OnChallengeListener to SubmissionsFragment, which implements the interface.
	 *
	 * @param onChallengeStateChangeListener the listener we use (we know this is SubmissionsFragment).
	 */
	public void setOnChallengeStateChangeListener(OnChallengeStateChangeListener onChallengeStateChangeListener)
	{
		this.onChallengeStateChangeListener = onChallengeStateChangeListener;
	}

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		view = inflate(inflater, container);

		initVars();

		prepare();

		return view;
	}

	/**
	 * Inflates the OverviewFragment Layout. Abstract to allow different Layouts inflated by different Challenge types.
	 *
	 * @param inflater the inflater.
	 * @param container the container.
	 * @return inflated View.
	 */
	protected abstract View inflate(LayoutInflater inflater, ViewGroup container);

	protected abstract void prepare();

	/**
	 * Listens to any change in the Submissions.
	 *
	 * @param newSubmissions the new Submissions, after they were changed.
	 */
	@Override
	public void onChange(List<Submission> newSubmissions)
	{
		/* Update the Submission count */
		String subCount = newSubmissions.size() + "";
		tvSubCount.setText(subCount);
	}

	private void initVars()
	{
		/* Init CommentManager and use the Challenge, assumes commentManager exists */
		this.commentManager = view.findViewById(R.id.commentManager);
		this.commentManager.setPost(challenge);

		/* Initialize the Submission count TextView */
		initSubCount();

		initRatingBar();
	}

	private void initSubCount()
	{
		/* Assumes that a tvSubCount exists */
		this.tvSubCount = view.findViewById(R.id.tvSubCount);

		Database database = new Database(getContext());
		database.open();
		int initialCount = database.getSubmissionCountForChallenge(this.challenge.id);
		database.close();

		this.tvSubCount.setText(Integer.toString(initialCount));
	}

	/**
	 * Initialize RatingBar for the Challenge.
	 */
	private void initRatingBar()
	{
		/* Assumes that a rbChallengeRating exists within the XML */
		RatingBar rbSubmissionRating = view.findViewById(R.id.rbChallengeRating);

		/* If the logged in User is the Challenge's author, don't let him rate it */
		if(LoginManager.getLoggedIn().id == this.challenge.authorId)
		{
			/* Make RatingBar disappear & exit */
			rbSubmissionRating.setVisibility(View.GONE);
			return;
		}

		/* Listen to changes in RatingBar, forward to onRatingChanged method */
		rbSubmissionRating.setOnRatingBarChangeListener(this::onRatingChanged);

		/* Get our previous rating of the Challenge */
		Rating prev = this.challenge.getRatingByUser(getContext(), LoginManager.getLoggedIn().id);
		/* If we already rating the Challenge */
		if(prev != null)
		{
			/* Display previous rating and set RatingBar to indicator, so we can't rate again */
			rbSubmissionRating.setRating(prev.rating);
			rbSubmissionRating.setIsIndicator(true);
		}
	}

	/**
	 * Listen to changes in RatingBar.
	 *
	 * @param ratingBar the RatingBar.
	 * @param rating the new rating.
	 * @param fromUser was the change made by a User.
	 */
	public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser)
	{
		/* If RatingBar wasn't changed by a User (but rather by the app, e.t.c), exit */
		if(!fromUser)
			return;

		/* Insert the new rating to the Database */
		Database database = new Database(getContext());
		database.open();
		database.insertChallengeRating(new Rating(LoginManager.getLoggedIn().id, this.challenge.id, rating));
		database.close();

		/* Make the RatingBar an indicator, as we already rated and can't change our rating */
		ratingBar.setIsIndicator(true);

		/* After rating a Submission, our rating should appear next to our comments under that Submission. Refresh CommentManager to ensure that. */
		this.commentManager.refresh();
	}
}