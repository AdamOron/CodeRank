package com.example.coderanknew.submission;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import com.example.coderanknew.user.LoginManager;
import com.example.coderanknew.R;
import com.example.coderanknew.sql.Database;

public class CreateSubmissionActivity extends Activity implements AdapterView.OnItemClickListener, AdapterView.OnItemSelectedListener
{
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
		this.initSpinner();

		this.selectedLang = Submission.LANGUAGES[0];

		this.etContent = findViewById(R.id.etSubmissionContent);

		View bSubmit = findViewById(R.id.bSubmitSubmission);
		bSubmit.setOnClickListener($ -> submitSubmission());
	}

	private void initSpinner()
	{
		Spinner spLanguage = findViewById(R.id.spLanguage);

		ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.language_current_selection, Submission.LANGUAGES);
		adapter.setDropDownViewResource(R.layout.language_spinner_dropdown_item);

		spLanguage.setAdapter(adapter);
		spLanguage.setOnItemSelectedListener(this);
	}

	/* Spinner Handling */

	@Override
	public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l)
	{
		this.selectedLang = (String) adapterView.getItemAtPosition(i);
	}

	@Override
	public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)
	{
	}

	@Override
	public void onNothingSelected(AdapterView<?> adapterView)
	{
	}

	private void submitSubmission()
	{
		long challengeId = getChallengeId();
		long authorId = LoginManager.getLoggedIn().id;
		String content = etContent.getText().toString();

		Submission sub = new Submission(challengeId, authorId, content, this.selectedLang);

		Database db = new Database(this);
		db.open();
		db.insertSubmission(sub);
		db.close();

		exit();
	}

	private void exit()
	{
		setResult(RESULT_OK);
		finish();
	}

	private long getChallengeId()
	{
		return getIntent().getExtras().getLong(Submission.KEY_CHALLENGE_ID, -1L);
	}
}