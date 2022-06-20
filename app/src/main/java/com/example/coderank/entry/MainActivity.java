package com.example.coderank.entry;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import com.example.coderank.R;
import com.example.coderank.utils.Debug;

/**
 * The entry-point of this app.
 * Displays drop-down splash screen animation, then enters LoginActivity.
 */
public class MainActivity extends Activity
{
	/* The duration of the drop-down animation */
	private static final long ANIM_DURATION = 3000;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		/* Activate debugging */
		Debug.setStatus(true);

		/* Play drop-down animation */
		animate();
		/* Wait for the duration of the animation, only then enter LoginActivity */
		new Handler().postDelayed(this::enterLogin, ANIM_DURATION);
	}

	/**
	 * Plays the drop-down animation.
	 * The animation is happening asynchronously - it doesn't halt any other behavior, simply runs in the background.
	 */
	private void animate()
	{
		/* Load the animation from the XML file */
		Animation dropDown = AnimationUtils.loadAnimation(this, R.anim.drop_down_animation);
		/* Apply the drop-down animation to the "<CodeRank/>" TextView */
		TextView tvCodeRank = findViewById(R.id.tvCodeRank);
		tvCodeRank.setAnimation(dropDown);
	}

	/**
	 * Enter LoginActivity.
	 */
	private void enterLogin()
	{
		startActivity(new Intent(this, LoginActivity.class));
	}
}
