package com.example.coderank.entry;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Patterns;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.example.coderank.R;
import com.example.coderank.entry.home.HomeActivity;
import com.example.coderank.user.LoginManager;
import com.example.coderank.user.User;
import java.io.FileNotFoundException;
import java.io.InputStream;

/**
 * Responsible for registering new Users.
 * Enters HomeActivity if registration is successful.
 */
public class RegisterActivity extends AppCompatActivity
{
	/* Request codes for a profile picture grabbing Intent. Just two random ints. */
	public static final int CAMERA_REQUEST_CODE = 951;
	public static final int GALLERY_REQUEST_CODE = 952;
	/* The key of the profile picture's data within an Intent's extras */
	public static final String KEY_PROFILE_PICTURE = "data";

	/* EditText fields for the User's first name, last name, email, username and password */
	private EditText etFirstName, etLastName, etEmail, etUsername, etPassword;
	/* ImageView for the User's profile picture */
	private ImageView ivProfilePicture;
	/**
	 * Boolean describing whether there is currently a profile picture set or not.
	 * This is used to ensure that a profile picture is provided.
	 */
	private boolean isProfilePictureSet;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);

		/* If we enter RegisterActivity, ensure there is no User logged in */
		LoginManager.logOut();
		/* Initialize Activity's variables */
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
		this.etFirstName = findViewById(R.id.etFirstName);
		this.etLastName = findViewById(R.id.etLastName);
		this.etEmail = findViewById(R.id.etEmail);
		this.etUsername = findViewById(R.id.etUsername);
		this.etPassword = findViewById(R.id.etPassword);

		/* Update fields with previous LoginActivity values.
		 * If a field was left empty in LoginActivity, an empty String was passed to the Intent,
		 * therefore an empty String will be put into the EditText, so there will be no change.
		 */
		Bundle extras = getIntent().getExtras();
		this.etUsername.setText(extras.getString(User.KEY_USERNAME));
		this.etPassword.setText(extras.getString(User.KEY_PASSWORD));

		/* Listen to Register button, attempt to register the User if it's clicked */
		View bRegister = findViewById(R.id.bRegister);
		bRegister.setClickable(true);
		bRegister.setOnClickListener($ -> attemptRegister());

		/* Listen to profile picture ImageView, upload new profile picture if it's clicked */
		this.ivProfilePicture = findViewById(R.id.ivProfilePicture);
		this.ivProfilePicture.setClickable(true);
		this.ivProfilePicture.setOnClickListener($ -> uploadProfilePicture());
		/* On creation, there is no profile picture uploaded */
		this.isProfilePictureSet = false;
	}

	/**
	 * Starts process for uploading new profile picture.
	 * Opens ImagePickerDialog, which allows getting image from camera/gallery.
	 * The Dialog then enters the relevant Activity and eventually returns to this Activity.
	 * Returned profile picture is handled in 'onActivityResult'
	 */
	private void uploadProfilePicture()
	{
		ImagePickerDialog dialog = new ImagePickerDialog(this);
		dialog.show();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		super.onActivityResult(requestCode, resultCode, data);

		/* If result isn't OK, we failed to get an image (the User cancelled halfway through, perhaps). Exit the function. */
		if(resultCode != Activity.RESULT_OK)
			return;

		switch(requestCode)
		{
		/* If the request code is for a profile picture grabbed from camera */
		case CAMERA_REQUEST_CODE:
			/* Extract the Bitmap returned from the camera & call helper function 'setProfilePicture' */
			setProfilePicture(extractImageFromCamera(data));
			break;

		/* If the request code is for a profile picture grabbed from gallery  */
		case GALLERY_REQUEST_CODE:
			/* Extract the bitmap returned from the gallery & call helper function 'setProfilePicture' */
			setProfilePicture(extractImageFromGallery(data));
			break;
		}
	}

	/**
	 * Extracts the Bitmap of an image that was taken from the camera.
	 *
	 * @param data the Intent returned from the camera.
	 * @return the Bitmap of the image the camera took.
	 */
	private Bitmap extractImageFromCamera(Intent data)
	{
		/* Extract profile picture's Bitmap from Intent. It's key is "data", which is saved to KEY_PROFILE_PICTURE as a constant. */
		return (Bitmap) data.getExtras().get(KEY_PROFILE_PICTURE);
	}

	/**
	 * Extract the Bitmap of an image that was picked from the gallery.
	 *
	 * @param data the Intent returned from the gallery.
	 * @return the Bitmap of the image that was picked from the gallery.
	 */
	private Bitmap extractImageFromGallery(Intent data)
	{
		/* Get the picked image's Uri (identifier) */
		Uri imageUri = data.getData();
		/* Declare InputStream for the image's Bitmap */
		InputStream imageStream = null;

		try
		{
			/* Get the InputStream for the picked image using its Uri */
			imageStream = getContentResolver().openInputStream(imageUri);
		}
		catch(FileNotFoundException e)
		{
			/* An exception is thrown if given Uri doesn't exist. This will never happen as we grabbed the Uri directly from the gallery. */
			//throw new InvalidParameterException("Unable to locate Image.");
			setError("Failed to load image. Please try again.");

		}

		/* Decode the InputStream into a Bitmap using BitmapFactory */
		return BitmapFactory.decodeStream(imageStream);
	}

	/**
	 * @param profilePicture the profile picture's Bitmap
	 */
	private void setProfilePicture(Bitmap profilePicture)
	{
		/* Set ImageView to display given profile picture Bitmap */
		this.ivProfilePicture.setImageBitmap(profilePicture);
		/* Update 'isProfilePictureSet' to true */
		this.isProfilePictureSet = true;
	}

	private void closeKeyboard()
	{
		/* Close Keyboard */
		InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
		View currentFocus = getCurrentFocus();
		if(currentFocus != null) imm.hideSoftInputFromWindow(currentFocus.getWindowToken(), 0);
	}

	/**
	 * Attempt to register a new User.
	 * Enters HomeActivity if registration is successful.
	 */
	private void attemptRegister()
	{
		closeKeyboard();

		/* Extract all relevant fields */
		String firstName = etFirstName.getText().toString();
		String lastName = etLastName.getText().toString();
		String email = etEmail.getText().toString();
		String username = etUsername.getText().toString();
		String password = etPassword.getText().toString();
		Bitmap picture = ((BitmapDrawable) ivProfilePicture.getDrawable()).getBitmap();

		/* Ensure that all relevant fields are set, if they aren't exit the function */
		if(!ensureFieldsSet(firstName, lastName, email, username, password))
			return;

		/* Attempt to register new User with given fields. If registration successful, enter HomeActivity */
		if(LoginManager.attemptRegister(this, new User(firstName, lastName, email, username, picture), password))
			enterHome();
		/* If registration fails, the username/email already exist. Display vague message. */
		else
			setError("Unable to register user.");
	}

	/**
	 * Checks whether all of the new User's fields are set (non-empty) and valid.
	 * If that's not the case, it displays an error message accordingly.
	 *
	 * @param firstName the first name extracted from the EditText
	 * @param lastName the last name extracted from the EditText
	 * @param email the email extracted from the EditText
	 * @param username the username extracted from the EditText
	 * @param password the password extracted from the EditText
	 * @return whether all of the User's fields are set & valid
	 */
	private boolean ensureFieldsSet(String firstName, String lastName, String email, String username, String password)
	{
		/* Ensure profile picture is set */
		if(!isProfilePictureSet)
		{
			setError("Profile picture can't be empty.");
			return false;
		}

		/* Ensure first name isn't empty */
		if(firstName.isEmpty())
		{
			setError("First name can't be empty.");
			return false;
		}

		/* Ensure last name isn't empty */
		if(lastName.isEmpty())
		{
			setError("Last name can't be empty.");
			return false;
		}

		/* Ensure email isn't empty */
		if(email.isEmpty())
		{
			setError("Email can't be empty.");
			return false;
		}

		/* Ensure username's length is within 4 and 10 characters */
		if(username.length() < 4 || username.length() > 10)
		{
			setError("Username must be between 4 and 10 characters long.");
			return false;
		}

		/* Ensure password's length is within 4 and 20 characters */
		if (password.length() < 4 || password.length() > 20)
		{
			setError("Password must be between 4 and 20 characters long.");
			return false;
		}

		/*
		 * Ensure that given email address is valid.
		 * This works by getting Java's EMAIL Pattern class and matching it to the given email.
		 */
		if(!Patterns.EMAIL_ADDRESS.matcher(email).matches())
		{
			setError("Invalid email.");
			return false;
		}

		/* Return true if no mismatch found */
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
	 * Enter HomeActivity.
	 */
	private void enterHome()
	{
		startActivity(new Intent(this, HomeActivity.class));
	}
}