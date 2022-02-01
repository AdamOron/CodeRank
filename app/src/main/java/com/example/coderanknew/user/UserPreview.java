package com.example.coderanknew.user;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.Nullable;
import com.example.coderanknew.R;
import com.example.coderanknew.sql.Database;

public class  UserPreview extends LinearLayout implements View.OnClickListener
{
	private TextView tvUsername;
	private ImageView ivProfilePicture;

	private User user;

	public UserPreview(Context context, @Nullable AttributeSet attrs)
	{
		super(context, attrs);

		inflate(getContext(), R.layout.layout_user_preview, this);

		initVars();
	}

	public void setUser(long userId)
	{
		readUser(userId);

		tvUsername.setText(user.username);

		ivProfilePicture.setImageBitmap(user.picture);
	}

	private void readUser(long userId)
	{
		Database database = new Database(getContext());
		database.open();
		user = database.getUserById(userId);
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
		// Go into user profile view
	}
}
