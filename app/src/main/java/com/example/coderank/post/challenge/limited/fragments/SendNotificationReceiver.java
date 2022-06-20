package com.example.coderank.post.challenge.limited.fragments;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import com.example.coderank.R;
import com.example.coderank.post.challenge.Challenge;
import com.example.coderank.post.challenge.limited.LimitedChallenge;
import com.example.coderank.sql.Database;
import com.example.coderank.post.submission.Submission;
import com.example.coderank.user.User;
import com.example.coderank.utils.Debug;

/**
 * BroadcastReceiver that's called 1 hour before a LimitedChallenge is completed.
 * Will dispatch a reminder notification.
 */
public class SendNotificationReceiver extends BroadcastReceiver
{
	/* String IDs for User and Challenge that are passed to this BroadcastReceiver through the Intent */
	public static final String USER_ID = "uid";
	public static final String CHALLENGE_ID = "cid";
	/* The request code for this BroadcastReceiver */
	public static final int REQUEST_CODE = 456;

	/* The name of the NotificationChannel of our notification */
	private static final String NOTIFICATION_CHANNEL = "ChallengeExpiring";

	/* The Challenge & User that were passed */
	private LimitedChallenge challenge;
	private User user;

	/**
	 * Checks whether passed User has already made a Submission to passed Challenge.
	 * If he did, there's no reason to notify him.
	 *
	 * @param context the Context from which this is called.
	 * @param intent the Intent containing the Challenge & User.
	 * @return whether we should notify the given User or not.
	 */
	private boolean shouldNotify(Context context, Intent intent)
	{
		/* Get the ID of the User that was logged in when the notification was planned */
		long userId = intent.getLongExtra(USER_ID, User.INVALID_ID);

		if(userId == User.INVALID_ID)
			throw new IllegalArgumentException("No User ID passed to SendNotificationReceiver's Intent");

		/* Get the ID of the Challenge that was viewed when the notification was planned */
		long challengeId = intent.getLongExtra(CHALLENGE_ID, Challenge.INVALID_ID);

		if(challengeId == Challenge.INVALID_ID)
			throw new IllegalArgumentException("No Challenge ID passed to SendNotificationReceiver's Intent");

		Database database = new Database(context);
		database.open();

		/* Get Challenge & User from Database.
		 * We cast the Challenge to a LimitedChallenge, because we know for a fact it is one, otherwise this BroadcastReceiver wouldn't have been called.
		 */
		this.challenge = (LimitedChallenge) database.getChallengeById(challengeId);
		this.user = database.getUserById(userId);

		/* Get Submission to the passed Challenge whose author is passed User, if such one exists */
		Submission submission = database.getSubmissionByFilter(
				Database.COL_SUB_CHALLENGE_ID + "=" + challengeId + " AND " +
				Database.COL_SUB_AUTHOR_ID + "=" + userId
		);

		database.close();

		/* If User hasn't made a Submission to the given Challenge yet, we notify him */
		return submission == null;
	}

	@Override
	public void onReceive(Context context, Intent intent)
	{
		if(!shouldNotify(context, intent))
		{
			Debug.print("No notify");
			return;
		}

		String appName = context.getString(R.string.app_name);

		/* Initialize NotificationChannel for out notification */
		NotificationChannel channel = new NotificationChannel(NOTIFICATION_CHANNEL, appName, NotificationManager.IMPORTANCE_HIGH);
		channel.setDescription(appName);

		/* Get NotificationManager Service & create the NotificationChannel */
		NotificationManager manager = context.getSystemService(NotificationManager.class);
		manager.createNotificationChannel(channel);

		/* Build the notification */
		Notification.Builder builder = new Notification.Builder(context, NOTIFICATION_CHANNEL)
				.setSmallIcon(R.drawable.example_pfp)
				.setContentTitle("Challenge Expiring Soon!")
				.setContentText(this.challenge.title + " expires in an hour. Don't miss out!")
				.setAutoCancel(true);
		/* Dispatch notification */
		manager.notify(0, builder.build());
	}
}
