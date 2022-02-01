package com.example.coderanknew.challenge;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;
import com.example.coderanknew.user.LoginManager;
import com.example.coderanknew.R;
import com.example.coderanknew.sql.Database;
import java.util.Date;

public class ChallengeCreateActivity extends Activity implements View.OnClickListener
{
	private EditText etTitle, etContent;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_challenge_create);

		initVars();
	}

	private void initVars()
	{
		if(!LoginManager.isLoggedIn())
		{
			throw new IllegalStateException("Can't enter ChallengeCreateActivity without being logged in");
		}

		etTitle = findViewById(R.id.etTopicTitle);
		etContent = findViewById(R.id.etTopicContent);

		View bSubmit = findViewById(R.id.bSubmit);
		bSubmit.setOnClickListener(this);
	}

	@Override
	public void onClick(View view)
	{
		if(view.getId() == R.id.bSubmit)
		{
			createChallenge();
			return;
		}
	}

	private void createChallenge()
	{
		long authorId = LoginManager.getLoggedIn().id;
		String title = etTitle.getText().toString();
		String content = etContent.getText().toString();

		NormalChallenge newChallenge = new NormalChallenge(authorId, title, content, new Date());

		Database challengeDatabase = new Database(this);
		challengeDatabase.open();
		challengeDatabase.insertChallenge(newChallenge);
		enterCreatedChallenge(newChallenge.id);
	}

	private void enterCreatedChallenge(long createdId)
	{
		Intent intent = new Intent(this, ChallengeViewActivity.class);
		intent.putExtra(Challenge.KEY_ID, createdId);
		startActivity(intent);
	}
}