package com.example.coderank.post.challenge.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import com.example.coderank.R;
import com.example.coderank.post.challenge.fragments.listeners.OnChallengeStateChangeListener;
import com.example.coderank.post.challenge.fragments.listeners.OnSubmissionsChangeListener;
import com.example.coderank.post.submission.SubmissionViewActivity;
import com.example.coderank.post.challenge.Challenge;
import com.example.coderank.sql.Database;
import com.example.coderank.post.submission.CreateSubmissionActivity;
import com.example.coderank.post.submission.Submission;
import com.example.coderank.post.submission.SubmissionAdapter;
import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;
import java.util.List;

/**
 * SubmissionFragment is the Fragment responsible for displaying the Submissions for a Fragment.
 * Each SubmissionFragment handles a different Challenge type (NormalChallenge/LimitedChallenge).
 *
 * Implements OnChallengeStateChangeListener which allows to know whether the Challenge is receiving more Submissions or not.
 * If the Challenge isn't opened for Submissions, SubmissionFragment won't display the "Submit Submission" button.
 * @see OnChallengeStateChangeListener
 *
 * @param <C> the Challenge type we are handling.
 */
public abstract class SubmissionsFragment<C extends Challenge> extends Fragment implements OnChallengeStateChangeListener
{
	/* The Challenge we are handling */
	protected C challenge;
	/**
	 * This 'isOpened' boolean represents the state of the Challenge. If 'isOpened' is true, the Challenge accepts Submission, otherwise it doesn't.
	 * This boolean is required because it exists from the very moment a SubmissionFragment is created, whereas the View 'bSubmitSubmission' is only initialized
	 * whenever this SubmissionFragment is rendered (through 'onCreateView' function). For this exact reason we can't guarantee that 'bSubmitSubmission' isn't null.
	 * To solve this problem, instead of only using 'bSubmitSubmission' to keep track of the Challenge's state, we also use the boolean 'isOpened'.
	 *
	 * More specifically, if we view a LimitedChallenge and it expires before we open the SubmissionsFragment, 'bSubmitSubmission' will be null, as 'onCreateView'
	 * hasn't been called yet. This will result in an error, as when the LimitedChallenge expires its OverviewFragment will call 'onChallengeStateChange',
	 * which tries to set the null 'bSubmitSubmission' to GONE.
	 * Instead of trying to set 'bSubmitSubmission' to GONE, what we will do is set 'isOpened' to false, and when we open this SubmissionsFragment the 'onCreateView'
	 * function will automatically set 'bSubmitSubmission' to GONE if 'isOpened' is false.
	 */
	private boolean isOpened;
	/* The view of this SubmissionFragment. Initialized from the 'inflate' functions return value. This is used to call 'findViewById', e.t.c. */
	protected View view;
	/* The "Submit Submission" button. Initialized from 'onCreateView', only when this SubmissionFragment is rendered. */
	protected View bSubmitSubmission;
	/* The list of Submissions we are displaying. Initialized 'onCreateView', only when this SubmissionFragment is rendered. */
	private List<Submission> submissions;
	/* The ArrayAdapter connecting between our Submission list and the ListView of Submissions (creates View representing each Submission). */
	private SubmissionAdapter submissionAdapter;
	/* A listener for whenever our Submission list changes. This is implemented by OverviewFragment, as it needs to keep track of the amount of Submissions. */
	private OnSubmissionsChangeListener onSubmissionsChangeListener;

	public SubmissionsFragment(C challenge)
	{
		this.challenge = challenge;
		/* Initialize to true, as a Challenge is opened by default */
		this.isOpened = true;
	}

	/**
	 * Updates this SubmissionFragment's listener to the given listener.
	 * This is called once from FragmentAdapter, and is set to the OverviewFragment.
	 * @see ChallengeFragmentAdapter
	 *
	 * @param onSubmissionsChangeListener the listener we want to use.
	 */
	public void setOnSubmissionsChangeListener(OnSubmissionsChangeListener onSubmissionsChangeListener)
	{
		this.onSubmissionsChangeListener = onSubmissionsChangeListener;
	}

	@Override
	public void onChallengeStateChange(ChallengeState newState)
	{
		switch(newState)
		{
		case OPENED:
			isOpened = true;
			if(bSubmitSubmission != null)
				bSubmitSubmission.setVisibility(View.VISIBLE);
			break;

		case CLOSED:
			isOpened = false;
			if(bSubmitSubmission != null)
				bSubmitSubmission.setVisibility(View.GONE);
			break;
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		/* Inflate this Fragment's view using the implementation of 'inflate'. Set this Fragment's view to the return value of the 'inflate' function. */
		this.view = inflate(inflater, container);
		/* Initialize the variables of this Fragment */
		initVars();
		/* Call the implementation of the prepare function */
		prepare();

		return this.view;
	}

	/**
	 * This is used to create this Fragment's View.
	 * Each implementation of SubmissionsFragment uses a different Challenge type, and should therefore inflate a different Fragment
	 * matching its Challenge type's needs (Limited uses timer & Normal doesn't, e.t.c).
	 *
	 * @param inflater the inflater we use to inflate this Fragment.
	 * @param container the container of this Fragment.
	 * @return the view of this inflated Fragment.
	 */
	protected abstract View inflate(LayoutInflater inflater, ViewGroup container);

	/**
	 * This is basically a setup function, allowing any SubmissionFragment implementations to do their own preparation.
	 */
	protected abstract void prepare();

	private void enterCreateSubmission()
	{
		/* Intent from this Context to CreateSubmissionActivity */
		Intent intent = new Intent(getContext(), CreateSubmissionActivity.class);
		/* Pass the ID of this Fragment's Challenge to CreateSubmissionActivity */
		intent.putExtra(Submission.KEY_CHALLENGE_ID, challenge.id);
		/* Enter CreateSubmissionActivity and return to this Activity when it finishes */
		startActivityForResult(intent, CreateSubmissionActivity.REQUEST_CODE);
	}

	private void initVars()
	{
		/* Listen to Submit button click & on click enter CreateSubmissionActivity */
		bSubmitSubmission = view.findViewById(R.id.bSubmitSubmission);
		bSubmitSubmission.setOnClickListener($ -> enterCreateSubmission());
		/* If we learn that the submission is closed as we are initializing it, make the submission button invisible */
		if(!isOpened)
			bSubmitSubmission.setVisibility(View.GONE);

		/* Initialize the Submissions ListView using helper function */
		initSubmissionsListView();
		/* Get ListView of Submissions */
		ListView lvSubmissions = view.findViewById(R.id.lvSubmissions);
		/* The local SubmissionAdapter was initialized in 'initSubmissionsListView' */
		lvSubmissions.setAdapter(this.submissionAdapter);
		/* Each Submission view is clickable and opens a SubmissionViewActivity */
		lvSubmissions.setClickable(true);
		lvSubmissions.setOnItemClickListener((parent, view, position, id) ->
		{
			/* Intent from this Context to a SubmissionViewActivity */
			Intent intent = new Intent(getContext(), SubmissionViewActivity.class);
			/* Pass the clicked Submission's ID to the SubmissionViewActivity */
			intent.putExtra(SubmissionViewActivity.KEY_SUB_ID, submissions.get(position).id);
			/* Enter SubmissionViewActivity */
			startActivity(intent);
		});
	}

	/**
	 * Is called whenever we return to this Fragment from anywhere else (using the 'back' button).
	 * Updates the Submission list. This is required because after creating a Submission, we enter a SubmissionViewActivity, and then we return
	 * back to this SubmissionsFragment using the 'back' button. The SubmissionsFragment needs to be aware of the new Submission as well.
	 */
	@Override
	public void onResume()
	{
		super.onResume();
		/* Update Submission list */
		updateSubmissions();
	}

	/**
	 * Initializes Submission ListView & any of its components/helpers.
	 */
	private void initSubmissionsListView()
	{
		/* Initialize Submission list to be empty */
		submissions = new ArrayList<>();
		/* Initialize SubmissionAdapter with the local Submission list */
		submissionAdapter = new SubmissionAdapter(requireActivity(), submissions);
		/* Update Submission list (required on initialization as the Submission list is empty) */
		updateSubmissions();
	}

	/**
	 * Updates Submission list by re-reading all Submissions from Database.
	 */
	private void updateSubmissions()
	{
		Database database = new Database(getContext());
		database.open();

		/* Get all Submissions */
		List<Submission> newSubmissions = database.getAllSubmissionsByFilter(
				/* Get all Submissions whose Challenge ID match this Fragment's Challenge */
				Database.COL_SUB_CHALLENGE_ID + "=" + challenge.id,
				/* Sort them so their IDs descent - newer Submissions (with higher IDs) first. */
				Database.COL_SUB_ID + " DESC"
		);

		database.close();

		/*
		 * Empty 'submissions' & re-add all Submissions from new list.
		 * We do this instead of re-initializing 'submissions' (submissions = new ArrayList<>()) as 'submissionAdapter' uses the 'submissions'
		 * pointer, and re-initializing 'submissions' will change its pointer, as it uses a new object.
		 */
		submissions.clear();
		submissions.addAll(newSubmissions);
		/* Notify the SubmissionAdapter that we have changed the Submission list. This makes it change all Submission Views accordingly & add new ones if needed. */
		submissionAdapter.notifyDataSetChanged();
		/* Call 'onSubmissionsChangeListener' with the new Submission list. This will allow OverviewFragment to update its Submission counter. */
		onSubmissionsChangeListener.onChange(newSubmissions);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
	{
		super.onActivityResult(requestCode, resultCode, data);

		/* If the request code passed to the Activity matches CreateSubmissionActivity's request code */
		if(requestCode == CreateSubmissionActivity.REQUEST_CODE)
		{
			/* Update Submission list if we created a new Submission */
			if(resultCode == Activity.RESULT_OK)
				updateSubmissions();
		}
	}
}