package com.example.coderank.post.submission;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import com.example.coderank.post.challenge.Challenge;
import com.example.coderank.user.LoginManager;
import com.example.coderank.R;
import com.example.coderank.sql.Database;
import java.util.Date;

public class CreateSubmissionActivity extends Activity implements AdapterView.OnItemClickListener, AdapterView.OnItemSelectedListener
{
	/* The request code of this Activity */
	public static final int REQUEST_CODE = 19;

	private EditText etContent;
	private String selectedLang;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_create_submission);

		initVars();
	}

	private void initVars()
	{
		/* Initialize the programming language Spinner */
		this.initSpinner();
		/* Initialize the selected language to the first one in the array */
		this.selectedLang = Submission.LANGUAGES[0];

		this.etContent = findViewById(R.id.etSubmissionContent);

		/* Listen to Submit button, when it's clicked attempt to submit Submission*/
		View bSubmit = findViewById(R.id.bSubmitSubmission);
		bSubmit.setOnClickListener($ -> submitSubmission());
	}

	/**
	 * Initialize the programming language Spinner.
	 */
	private void initSpinner()
	{
		Spinner spLanguage = findViewById(R.id.spLanguage);

		/* Create ArrayAdapter for the programming languages Array */
		ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.language_current_selection, Submission.LANGUAGES);
		/* Set the ArrayAdapter's view resource to the created item XML */
		adapter.setDropDownViewResource(R.layout.language_spinner_dropdown_item);

		/* Set the Spinner's Adapter to the created ArrayAdapter */
		spLanguage.setAdapter(adapter);
		/* Make this Activity handle any item selection (onItemSelected, onItemClick, onNothingSelected) */
		spLanguage.setOnItemSelectedListener(this);
	}

	/**
	 * Called whenever an item is selected in the language Spinner.
	 *
	 * @param adapterView used to get the selected item.
	 * @param view unused.
	 * @param i the index of the item within the Spinner.
	 * @param l unused.
	 */
	@Override
	public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l)
	{
		this.selectedLang = (String) adapterView.getItemAtPosition(i);
	}

	@Override
	public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)
	{
		// Unimplemented
	}

	@Override
	public void onNothingSelected(AdapterView<?> adapterView)
	{
		// Unimplemented
	}

	/**
	 * Submit the Submission into the Database.
	 */
	private void submitSubmission()
	{
		/* Extract Submission fields */
		long challengeId = getChallengeId();
		long authorId = LoginManager.getLoggedIn().id;
		String content = etContent.getText().toString();

		/* If fields aren't set properly, exit */
		if(!ensureFieldsSet(content))
			return;

		Submission sub = new Submission(challengeId, authorId, content, this.selectedLang, new Date());

		Database db = new Database(this);
		db.open();
		db.insertSubmission(sub);
		db.close();

		exit();
	}

	/**
	 * Ensures all required Submission fields are set.
	 * Display matching error message otherwise.
	 *
	 * @param content the content of the Challenge.
	 * @return whether the Submission's fields are properly set or not.
	 */
	private boolean ensureFieldsSet(String content)
	{
		/* If content is empty, there's a problem */
		if(content.isEmpty())
		{
			setError("Submission content can't be empty");
			return false;
		}

		/* No problem found */
		return true;
	}

	/**
	 * Makes error TextView visible, then sets its text to given error message.
	 *
	 * @param error the error's message.
	 */
	private void setError(String error)
	{
		TextView tvError = findViewById(R.id.tvError);
		tvError.setVisibility(View.VISIBLE);
		tvError.setText(error);
	}

	/**
	 * Properly exit this Activity after submitting Submission.
	 */
	private void exit()
	{
		setResult(RESULT_OK);
		finish();
	}

	/**
	 * @return the ID of the Challenge we are submitting to.
	 */
	private long getChallengeId()
	{
		return getIntent().getExtras().getLong(Submission.KEY_CHALLENGE_ID, Challenge.INVALID_ID);
	}
}