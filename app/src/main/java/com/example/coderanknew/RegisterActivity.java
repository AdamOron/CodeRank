package com.example.coderanknew;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.coderanknew.user.LoginManager;
import com.example.coderanknew.user.User;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener
{
	private EditText etFirstName, etLastName, etEmail, etUsername, etPassword;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);

		LoginManager.logOut();

		initVars();
	}

	private void initVars()
	{
		/* Init EditText fields */
		etFirstName = findViewById(R.id.etFirstName);
		etLastName = findViewById(R.id.etLastName);
		etEmail = findViewById(R.id.etEmail);
		etUsername = findViewById(R.id.etUsername);
		etPassword = findViewById(R.id.etPassword);

		/* Update fields with previous LoginActivity values.
		 * If a field was left empty in LoginActivity, an empty String was passed to the Intent,
		 * therefore an empty String will be put into the EditText, so there will be no change. */
		Bundle extras = getIntent().getExtras();
		etUsername.setText(extras.getString(User.KEY_USERNAME));
		etPassword.setText(extras.getString(User.KEY_PASSWORD));

		/* Listen to Register Button */
		View bRegister = findViewById(R.id.bRegister);
		bRegister.setOnClickListener(this);
	}

	@Override
	public void onClick(View view)
	{
		switch(view.getId())
		{
			case R.id.bRegister:
				attemptRegister();
				break;
		}
	}

	private void attemptRegister()
	{
		String firstName = etFirstName.getText().toString();
		String lastName = etLastName.getText().toString();
		String email = etEmail.getText().toString();
		String username = etUsername.getText().toString();
		String password = etPassword.getText().toString();

		if(LoginManager.attemptRegister(this, new User(firstName, lastName, email, username), password))
		{
			enterHome();
		}
		else
		{
			Toast.makeText(this, "Unable to register user", Toast.LENGTH_SHORT).show();
		}
	}

	private void enterHome()
	{
		startActivity(new Intent(this, HomeActivity.class));
	}
}