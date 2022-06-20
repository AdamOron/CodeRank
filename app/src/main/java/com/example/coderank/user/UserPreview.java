package com.example.coderank.user;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.Nullable;
import com.example.coderank.R;
import com.example.coderank.sql.Database;
import java.security.InvalidParameterException;

/**
 * View representing a User.
 * This is used for previewing Users within the app, for example when we are showing a User's comments, posts, e.t.c.
 */
public class UserPreview extends LinearLayout implements View.OnClickListener
{
	/* The displayed User's fields */
	private TextView tvUsername;
	private ImageView ivProfilePicture;
	/* The User we are previewing */
	private User user;

	/**
	 * Initializes the UserPreview.
	 * On initialization, a UserPreview isn't aware of its User. The User is later set using the 'setUser' function.
	 *
	 * @param context the Context from which we are previewing a User.
	 * @param attrs the attributes of the View (we aren't using this).
	 */
	public UserPreview(Context context, @Nullable AttributeSet attrs)
	{
		super(context, attrs);

		/* Inflate the Layout */
		inflate(context, R.layout.layout_user_preview, this);
		/* Initialize the View's variables */
		initVars();
	}

	/**
	 * Called externally to provide a User to this UserPreview.
	 *
	 * @param userId the ID of the User we want to preview.
	 */
	public void setUser(long userId)
	{
		/* Read the User object that matches the given ID */
		readUser(userId);
		/* Set the username TextView */
		tvUsername.setText(user.username);
		/* Set the profile picture ImageView */
		ivProfilePicture.setImageBitmap(user.picture);
	}

	/**
	 * Sets this UserPreview's User to the User that matches the given userId.
	 *
	 * @param userId the ID of the User we want to preview.
	 */
	private void readUser(long userId)
	{
		Database database = new Database(getContext());
		database.open();
		/* Set this UserPreview's User to User from Database that matches given ID */
		user = database.getUserById(userId);
		/* Throw error if unable to fetch User matching given ID. This should never happen. */
		if(user == null)
			throw new InvalidParameterException("No User matches given ID " + userId + ".");
		database.close();
	}

	private void initVars()
	{
		/* Listen to Username clicks */
		tvUsername = findViewById(R.id.tvUsername);
		tvUsername.setOnClickListener(this);

		/* Listen to Profile Picture clicks */
		ivProfilePicture = findViewById(R.id.ivProfilePicture);
		ivProfilePicture.setOnClickListener(this);
	}

	@Override
	public void onClick(View v)
	{
		// Go into user profile view? Unimplemented for now
	}
}
