package com.example.coderank.entry;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.example.coderank.R;
import com.example.coderank.entry.home.HomeActivity;
import com.example.coderank.user.LoginManager;
import com.example.coderank.user.User;

/**
 * Allows already registered users to log in.
 * Allows unregistered users to register, by forwarding them to RegisterActivity.
 * Eventually leads to HomeActivity.
 */
public class LoginActivity extends AppCompatActivity
{
	/* EditText fields for the User's username & password */
	private EditText etUsername, etPassword;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

		/* If we enter LoginActivity, ensure that there is no other User logged in */
		LoginManager.logOut();
		/* Initialize the Activity's variables */
		initVars();
	}

	/**
	 * Called whenever an item is pressed in the menu.
	 *
	 * @param item the clicked item.
	 * @return super call value.
	 */
	@Override
	public boolean onOptionsItemSelected(@NonNull MenuItem item)
	{
		/* If the item is the Information item (only option) */
		if(item.getItemId() == R.id.iInfo)
			/* Display Information Dialog */
			displayInfoDialog();

		return super.onOptionsItemSelected(item);
	}

	/**
	 * Creates and shows a dialog containing a short explanation of the application.
	 */
	private void displayInfoDialog()
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("Information");
		builder.setMessage("This app is a playground for anyone who's passionate about programming and wants to enjoy their passion. I hope that you manage to enjoy it and further develop your skills. Have fun!");
		builder.show();
	}

	/**
	 * Called whenever the options menu is opened. Inflates the menu's view.
	 *
	 * @param menu the menu object.
	 * @return always true.
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		getMenuInflater().inflate(R.menu.info_menu, menu);
		return true;
	}

	private void initVars()
	{
		/* Init EditText fields */
		etUsername = findViewById(R.id.etUsername);
		etPassword = findViewById(R.id.etPassword);

		/* Listen to Login button, attempt to log in if clicked */
		View bLogin = findViewById(R.id.bLogin);
		bLogin.setOnClickListener($ -> attemptLogin());
		/* Listen to Register button, enter RegisterActivity if clicked */
		View bRegister = findViewById(R.id.bEnterRegister);
		bRegister.setOnClickListener($ -> enterRegistration());
	}

	private void closeKeyboard()
	{
		/* Close Keyboard */
		InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
		View currentFocus = getCurrentFocus();
		if(currentFocus != null) imm.hideSoftInputFromWindow(currentFocus.getWindowToken(), 0);
	}

	/**
	 * Attempt to log in using the entered credentials.
	 * Extracts said credentials from relevant EditTexts & passes them to LoginManager.
	 * If login is successful, enter HomeActivity, otherwise display an error message.
	 */
	private void attemptLogin()
	{
		closeKeyboard();

		/* Extract username & password fields from their EditTexts */
		String username = etUsername.getText().toString();
		String password = etPassword.getText().toString();

		/* Attempt to log in using LoginManager - if login successful, enter HomeActivity, otherwise display error message. */
		if(LoginManager.attemptLogin(this, username, password))
			enterHome();
		else
			setError("Wrong username or password.");
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
	 * Enters RegisterActivity.
	 * Uses existing username & password fields in the RegisterActivity's username & password fields.
	 */
	private void enterRegistration()
	{
		/* Intent from this Context to RegisterActivity */
		Intent intent = new Intent(this, RegisterActivity.class);
		/* Pass present username & password to Intent, so RegisterActivity can use them. */
		intent.putExtra(User.KEY_USERNAME, etUsername.getText().toString());
		intent.putExtra(User.KEY_PASSWORD, etPassword.getText().toString());
		/* Enter RegisterActivity */
		startActivity(intent);
	}

	/**
	 * Enters HomeActivity.
	 */
	private void enterHome()
	{
		startActivity(new Intent(this, HomeActivity.class));
	}
}