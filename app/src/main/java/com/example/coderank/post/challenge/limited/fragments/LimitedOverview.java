package com.example.coderank.post.challenge.limited.fragments;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.example.coderank.R;
import com.example.coderank.post.challenge.fragments.listeners.OnChallengeStateChangeListener;
import com.example.coderank.post.challenge.fragments.OverviewFragment;
import com.example.coderank.post.challenge.limited.LimitedChallenge;
import com.example.coderank.user.LoginManager;
import com.example.coderank.user.UserPreview;
import com.example.coderank.utils.Debug;
import java.util.Locale;

/**
 * OverviewFragment for a LimitedChallenge.
 * Displays countdown for the time left to submit a Submission.
 */
public class LimitedOverview extends OverviewFragment<LimitedChallenge>
{
	public LimitedOverview(LimitedChallenge challenge)
	{
		super(challenge);
	}

	@Override
	protected View inflate(LayoutInflater inflater, ViewGroup container)
	{
		return inflater.inflate(R.layout.fragment_limited_challenge_overview, container, false);
	}

	@Override
	protected void prepare()
	{
		UserPreview upChallengeAuthor = view.findViewById(R.id.upChallengeAuthor);
		upChallengeAuthor.setUser(challenge.authorId);

		TextView tvTitle = view.findViewById(R.id.tvChallengeTitle);
		tvTitle.setText(challenge.title);

		TextView tvContent = view.findViewById(R.id.tvTopicContent);
		tvContent.setText(challenge.content);

		/* Initialize the timer component */
		initTimer();
	}

	/**
	 * Initializes timer component & behavior.
	 */
	private void initTimer()
	{
		TextView tvTimeLeft = view.findViewById(R.id.tvTimeLeft);

		/* Grab ending time from LimitedChallenge object */
		long endTime = challenge.finalDate.getCalendar().getTimeInMillis();
		/* Calculate the amount of time left */
		long timeLeft = endTime - System.currentTimeMillis();
		/* Debug amount of time left */
		Debug.print("Time left: " + timeLeft);

		/* If there is still time left - the Challenge isn't over. Plan a reminder notification. */
		if(timeLeft > 0)
			planNotification();

		/* Initialize countdown timer */
		new CountDownTimer(timeLeft, 1000)
		{
			@Override
			public void onTick(long millisUntilFinished)
			{
				/* Calculate amount of hours, minutes & seconds left */
				long hours = millisUntilFinished / 3600000;
				millisUntilFinished -= hours * 3600000;
				long minutes = millisUntilFinished / 60000;
				millisUntilFinished -= minutes * 60000;
				long seconds = millisUntilFinished / 1000;
				/* Display amount of hours, minutes & seconds left in tvTimeLeft */
				tvTimeLeft.setText(String.format(Locale.US, "Time Left: %dh %dm %ds", hours, minutes, seconds));
			}

			/**
			 * Called when timer is finished, which is when the Challenge is over.
			 * If Challenge was over when we entered the OverviewFragment, this is instantly called.
			 */
			@Override
			public void onFinish()
			{
				tvTimeLeft.setText("Challenge Over");

				/* The state of the Challenge has changed - it is no longer accepting Submissions. Notify the Listener, if there is one. */
				if(onChallengeStateChangeListener != null)
					onChallengeStateChangeListener.onChallengeStateChange(OnChallengeStateChangeListener.ChallengeState.CLOSED);
			}
		}
		/* Start the initialized timer */
		.start();
	}

	/**
	 * Plans a reminder notification to be sent 1 hour before the Challenge is over.
	 */
	private void planNotification()
	{
		/* Save end time */
		long endTime = challenge.finalDate.getCalendar().getTimeInMillis();
		Debug.print("End time: " + endTime);

		/* Get current Context */
		Context context = requireContext();

		/* Create Intent a SendNotificationReceiver. Pass this Challenge & logged in User */
		Intent intent = new Intent(context, SendNotificationReceiver.class);
		intent.putExtra(SendNotificationReceiver.CHALLENGE_ID, this.challenge.id);
		intent.putExtra(SendNotificationReceiver.USER_ID, LoginManager.getLoggedIn().id);
		/* Create PendingIntent to the Intent */
		PendingIntent pendingIntent = PendingIntent.getBroadcast(context, SendNotificationReceiver.REQUEST_CODE, intent, 0);

		/* Get AlarmManager Service */
		AlarmManager alarm = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
		/* Enters SendNotificationReceiver at the given timestamp. SendNotificationReceiver is responsible for dispatching the reminder notification. */
		long targetMillis = endTime - 60 * 60 * 1000; // 1 hour before end time
		alarm.set(AlarmManager.RTC_WAKEUP, targetMillis, pendingIntent);
	}
}
