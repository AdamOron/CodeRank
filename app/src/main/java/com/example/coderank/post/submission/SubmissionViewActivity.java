package com.example.coderank.post.submission;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;
import com.example.coderank.R;
import com.example.coderank.post.comment.CommentManager;
import com.example.coderank.post.rating.Rating;
import com.example.coderank.sql.Database;
import com.example.coderank.syntax.SyntaxHighlighter;
import com.example.coderank.user.LoginManager;
import com.example.coderank.user.UserPreview;

/**
 * Activity responsible for viewing a Submission.
 */
public class SubmissionViewActivity extends Activity
{
	/* String key for the viewed Submission's ID, used for passing the Submission's ID to the Intent */
	public static final String KEY_SUB_ID = "keySubId";

	/* The Submission we are viewing */
	private Submission submission;
	/* CommentManager view & logic */
	private CommentManager commentManager;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_submission_view);

		initVars();
	}

	private void initVars()
	{
		/* Read the Submission into 'submission' variable */
		readSubmission();

		/* Initialize CommentManager */
		this.commentManager = findViewById(R.id.commentManager);
		this.commentManager.setPost(submission);

		/* Set UserPreview within Submission to the Submission author's ID */
		UserPreview upSubAuthor = findViewById(R.id.upSubAuthor);
		upSubAuthor.setUser(submission.authorId);
		/* Set Submission's programming language */
		TextView tvSubLang = findViewById(R.id.tvSubLang);
		tvSubLang.setText(submission.lang);

		/* Create SyntaxHighlighter for Submission's content */
		SyntaxHighlighter highlighter = new SyntaxHighlighter(submission.content);
		/* Apply SyntaxHighlighting to content, and use it in TextView */
		TextView tvSubContent = findViewById(R.id.tvSubContent);
		tvSubContent.setText(highlighter.run());

		/* Initialize RatingBar for the Submission */
		initRatingBar();
	}

	/**
	 * Initialize RatingBar for the Submission.
	 */
	private void initRatingBar()
	{
		/* Get View */
		RatingBar rbSubmissionRating = findViewById(R.id.rbSubmissionRating);

		/* If the logged in User is the Submission's author, don't let him rate it */
		if(LoginManager.getLoggedIn().id == submission.authorId)
		{
			/* Make RatingBar disappear & exit */
			rbSubmissionRating.setVisibility(View.GONE);
			return;
		}

		/* Listen to changes in RatingBar, forward to onRatingChanged method */
		rbSubmissionRating.setOnRatingBarChangeListener(this::onRatingChanged);

		/* Get our previous rating of the Submission */
		Rating prev = this.submission.getRatingByUser(this, LoginManager.getLoggedIn().id);
		/* If we already rating the Submission */
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
		Database database = new Database(this);
		database.open();
		database.insertSubmissionRating(new Rating(LoginManager.getLoggedIn().id, submission.id, rating));
		database.close();

		/* Make the RatingBar an indicator, as we already rated and can't change our rating */
		ratingBar.setIsIndicator(true);

		/* After rating a Submission, our rating should appear next to our comments under that Submission. Refresh CommentManager to ensure that. */
		this.commentManager.refresh();
	}

	/**
	 * Read the Submission into 'submission' using Submission ID that's passed to Intent.
	 */
	private void readSubmission()
	{
		long id = getIntent().getLongExtra(KEY_SUB_ID, Submission.INVALID_ID);
		Database database = new Database(this);
		database.open();
		this.submission = database.getSubmissionById(id);
		database.close();

		if(this.submission == null)
			throw new IllegalStateException("Invalid Submission ID passed to SubmissionViewActivity.");
	}
}