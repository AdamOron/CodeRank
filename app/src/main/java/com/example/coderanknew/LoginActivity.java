package com.example.coderanknew;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.coderanknew.user.LoginManager;
import com.example.coderanknew.user.User;

public class LoginActivity extends Activity implements View.OnClickListener
{
	private EditText etUsername, etPassword;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

		LoginManager.logOut();

		initVars();
	}

	private void initVars()
	{
		/* Init EditText fields */
		etUsername = findViewById(R.id.etUsername);
		etPassword = findViewById(R.id.etPassword);

		/* Listen to Login Button */
		View bLogin = findViewById(R.id.bLogin);
		bLogin.setOnClickListener(this);
		/* Listen to Register Button */
		View bRegister = findViewById(R.id.bEnterRegister);
		bRegister.setOnClickListener(this);
	}

	@Override
	public void onClick(View view)
	{
		switch(view.getId())
		{
			case R.id.bLogin:
				attemptLogin();
				break;

			case R.id.bEnterRegister:
				enterRegistration();
				break;
		}
	}

	private void attemptLogin()
	{
		String username = etUsername.getText().toString();
		String password = etPassword.getText().toString();

		if(LoginManager.attemptLogin(this, username, password))
		{
			enterHome();
		}
		else
		{
			Toast.makeText(this, "Unable to log in.", Toast.LENGTH_SHORT).show();
		}
	}

	private void enterRegistration()
	{
		/* Pass present Username & Password to Intent, so RegisterActivity can use them. */
		Intent intent = new Intent(this, RegisterActivity.class);
		intent.putExtra(User.KEY_USERNAME, etUsername.getText().toString());
		intent.putExtra(User.KEY_PASSWORD, etPassword.getText().toString());
		startActivity(intent);
	}

	private void enterHome()
	{
		startActivity(new Intent(this, HomeActivity.class));
	}
}