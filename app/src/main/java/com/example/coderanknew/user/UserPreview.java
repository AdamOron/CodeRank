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

		 ivProfilePicture.setImageBitmap();

		tvUsername.setText(user.username);
	}

	private void readUser(long userId)
	{
		Database database = new Database(getContext());
		database.open();
		this.user = database.getUserById(userId);
		database.close();
	}

	private void initVars()
	{
		/* Listen to Profile Picture clicks */
		ivProfilePicture = findViewById(R.id.ivProfilePicture);
		ivProfilePicture.setOnClickListener(this);

		/* Listen to Username clicks */
		this.tvUsername = findViewById(R.id.tvUsername);
		tvUsername.setOnClickListener(this);
	}

	@Override
	public void onClick(View v)
	{
		// Go into user profile view
	}
}
