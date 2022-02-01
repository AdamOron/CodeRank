package com.example.coderanknew;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.coderanknew.challenge.ChallengeCreateActivity;
import com.example.coderanknew.user.LoginManager;

public class HomeActivity extends Activity implements View.OnClickListener
{
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);

		initVars();
	}

	private void initVars()
	{
		if(!LoginManager.isLoggedIn())
		{
			throw new IllegalArgumentException("Can't enter HomeActivity without being logged in");
		}

		View bCreateChallenge = findViewById(R.id.bCreateChallenge);
		bCreateChallenge.setOnClickListener(this);
	}

	@Override
	public void onClick(View view)
	{
		switch(view.getId())
		{
			case R.id.bCreateChallenge:
				enterCreateChallenge();
				break;
		}
	}

	private void enterCreateChallenge()
	{
		Intent intent = new Intent(this, ChallengeCreateActivity.class);
		startActivity(intent);
	}
}