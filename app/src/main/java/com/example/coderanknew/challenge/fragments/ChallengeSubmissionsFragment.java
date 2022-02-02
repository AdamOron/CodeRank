package com.example.coderanknew.challenge.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import com.example.coderanknew.R;
import com.example.coderanknew.challenge.Challenge;
import com.example.coderanknew.challenge.NormalChallenge;
import com.example.coderanknew.sql.Database;
import com.example.coderanknew.submission.CreateSubmissionActivity;
import com.example.coderanknew.submission.Submission;
import com.example.coderanknew.submission.SubmissionAdapter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ChallengeSubmissionsFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemClickListener
{
	private View view;

	private Challenge challenge;

	private SubmissionAdapter submissionAdapter;
	private List<Submission> submissions;

	public interface OnSubmissionsChangeListener
	{
		void onChange(List<Submission> newSubmissions);
	}

	private OnSubmissionsChangeListener onSubmissionsChangeListener;

	public ChallengeSubmissionsFragment(Challenge challenge)
	{
		this.challenge = challenge;
	}

	public void setOnSubmissionsChangeListener(OnSubmissionsChangeListener onSubmissionsChangeListener)
	{
		this.onSubmissionsChangeListener = onSubmissionsChangeListener;
	}

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		this.view = inflater.inflate(R.layout.fragment_challenge_submissions, container, false);
		initVars();
		return this.view;
	}

	private void initVars()
	{
		getChallenge();

		/* Listen to Submit Button */
		View bSubmitSubmission = view.findViewById(R.id.bSubmitSubmission);
		bSubmitSubmission.setOnClickListener(this);

		initSubmissionListView();

		ListView lvSubmissions = view.findViewById(R.id.lvSubmissions);
		lvSubmissions.setAdapter(this.submissionAdapter);
		lvSubmissions.setClickable(true);
		lvSubmissions.setOnItemClickListener(this);
	}

	private void initSubmissionListView()
	{
		submissions = new ArrayList<>();
		submissionAdapter = new SubmissionAdapter(requireActivity(), submissions);

		updateSubmissions();
	}

	private void updateSubmissions()
	{
		Database database = new Database(getContext());
		database.open();

		List<Submission> newSubmissions = database.getAllSubmissionsByFilter(
				Database.COL_SUB_CHALLENGE_ID + "=" + challenge.id
				, Database.COL_SUB_ID + " DESC");

		database.close();

		submissions.clear();
		submissions.addAll(newSubmissions);

		submissionAdapter.notifyDataSetChanged();

		onSubmissionsChangeListener.onChange(newSubmissions);
	}

	@Override
	public void onClick(View v)
	{
		switch(v.getId())
		{
			case R.id.bSubmitSubmission:
				enterCreateSubmission();
				break;
		}
	}

	private void enterCreateSubmission()
	{
		Intent intent = new Intent(getActivity(), CreateSubmissionActivity.class);
		intent.putExtra(Submission.KEY_CHALLENGE_ID, challenge.id);
		startActivityForResult(intent, CreateSubmissionActivity.REQUEST_CODE);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
	{
		super.onActivityResult(requestCode, resultCode, data);

		if(requestCode == CreateSubmissionActivity.REQUEST_CODE)
		{
			if(resultCode != Activity.RESULT_OK) return;

			updateSubmissions();
		}
	}

	private void getChallenge()
	{
		final int INVALID_VALUE = -1;

		Bundle intentExtras = getActivity().getIntent().getExtras();
		long challengeId = intentExtras.getLong(NormalChallenge.KEY_ID, INVALID_VALUE);

		if(challengeId == INVALID_VALUE)
		{
			throw new IllegalArgumentException("No Challenge ID was passed to ChallengeViewActivity.");
		}

		Database challengeDatabase = new Database(getActivity());
		challengeDatabase.open();

		this.challenge = challengeDatabase.getChallengeById(challengeId);

		if(this.challenge == null)
		{
			throw new IllegalArgumentException("Challenge ID passed to ChallengeViewActivity doesn't exist in ChallengeDatabase.");
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id)
	{

	}
}