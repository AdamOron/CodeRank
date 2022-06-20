package com.example.coderank.post.challenge.activities;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import com.example.coderank.R;
import com.example.coderank.post.challenge.Challenge;
import com.example.coderank.post.challenge.limited.LimitedChallenge;
import com.example.coderank.post.challenge.normal.NormalChallenge;
import com.example.coderank.user.LoginManager;
import com.example.coderank.sql.Database;
import com.example.coderank.utils.MidnightCalendar;
import java.time.LocalDate;
import java.util.Date;
import java.util.Locale;

public class ChallengeCreateActivity extends Activity
{
	private EditText etTitle, etContent;
	private CheckBox cbLimitedTime;
	private TextView tvEndDate;
	private LocalDate finalDate;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_challenge_create);

		initVars();
	}

	private void initVars()
	{
		if(!LoginManager.isLoggedIn())
			throw new IllegalStateException("Can't enter ChallengeCreateActivity without being logged in");

		etTitle = findViewById(R.id.etTopicTitle);
		etContent = findViewById(R.id.etTopicContent);

		View bSubmit = findViewById(R.id.bSubmit);
		bSubmit.setOnClickListener($ -> createChallenge());

		/* Listen to CheckBox changes. When CheckBox is changed, update the color of tvEndDate to represent whether we are using it or not. */
		cbLimitedTime = findViewById(R.id.cbLimitedTime);
		cbLimitedTime.setOnCheckedChangeListener((buttonView, isChecked) ->
						tvEndDate.setTextColor(getColor(
								isChecked ? R.color.text : R.color.hint
						))
		);

		initEndDate();
	}

	private void initEndDate()
	{
		/* Final date is initialized to today's date */
		finalDate = LocalDate.now();

		tvEndDate = findViewById(R.id.tvEndDate);

		tvEndDate.setOnClickListener(clicked ->
		{
			/* If cbLimitedTime isn't checked, the created challenge isn't a LimitedChallenge - don't display DatePicker */
			if(!cbLimitedTime.isChecked())
				return;

			DatePickerDialog dialog = new DatePickerDialog(this,
					/* Whenever a new date is picked, call helper 'setEndDate' function with the picked year/month/dayOfMonth. DateDialogPicker's 'month' is ranged 0-11, whereas LocalDate expects 1-12, so increase it by 1. */
					(datePicker, year, month, dayOfMonth) -> setEndDate(year, month + 1, dayOfMonth),
					/* Initialize DatePickerDialog to 'finalDate', which is initialized to today's date. LocalDate.getMonthValue() returns value ranged 1-12, whereas DateDialogPicker expects 0-11, so decrease it by 1. */
					finalDate.getYear(), finalDate.getMonthValue() - 1, finalDate.getDayOfMonth()
			);
			/* Set minimum date to the current date, resulting in the earliest selectable date being today */
			dialog.getDatePicker().setMinDate(System.currentTimeMillis());
			dialog.show();
		});

		updateTvEndDate();
	}

	/**
	 * Sets the final date to the given parameters and updates TextView accordingly.
	 *
	 * @param year the final year.
	 * @param month the final month.
	 * @param day the final day.
	 */
	private void setEndDate(int year, int month, int day)
	{
		/* Set 'finalDate' */
		finalDate = LocalDate.of(year, month, day);
		/* Update TextView */
		updateTvEndDate();
	}

	/**
	 * Updates end date TextView using local 'finalDate'.
	 */
	private void updateTvEndDate()
	{
		/* Set the TextView to display the final day/month/year values */
		tvEndDate.setText(String.format(Locale.US, "%d/%d/%d",
				finalDate.getDayOfMonth(), finalDate.getMonthValue(), finalDate.getYear()
		));
	}

	private void createChallenge()
	{
		/* Extract Challenge's fields */
		long authorId = LoginManager.getLoggedIn().id;
		String title = etTitle.getText().toString();
		String content = etContent.getText().toString();

		/* If fields aren't properly set, exit */
		if(!ensureFieldsSet(title, content))
			return;

		Challenge newChallenge;

		/* If limited time CheckBox is checked, we're creating a LimitedChallenge with en end date */
		if(cbLimitedTime.isChecked())
		{
			/* Create MidnightCalendar instance representing the final date */
			MidnightCalendar finalDateMidnight = new MidnightCalendar(finalDate);
			/* Create LimitedChallenge using helper function */
			newChallenge = createLimitedChallenge(authorId, title, content, finalDateMidnight);
		}
		else
		{
			/* Create NormalChallenge using helper function */
			newChallenge = createNormalChallenge(authorId, title, content);
		}

		/* Insert newly created Challenge into Database */
		Database challengeDatabase = new Database(this);
		challengeDatabase.open();
		challengeDatabase.insertChallenge(newChallenge);
		/* Enter ChallengeViewActivity for newly created Challenge */
		enterChallengeView(newChallenge.id);
	}

	/**
	 * Ensures all required Challenge fields are set.
	 * Display matching error message otherwise.
	 *
	 * @param title the title of the Challenge.
	 * @param content the content of the Challenge.
	 * @return whether the Challenge's fields are properly set or not.
	 */
	private boolean ensureFieldsSet(String title, String content)
	{
		/* If title is empty, there's a problem */
		if(title.isEmpty())
		{
			setError("Challenge title can't be empty");
			return false;
		}

		/* If content is too short, there's a problem */
		if(content.length() < 4)
		{
			setError("Challenge content can't be shorter than 4 characters");
			return false;
		}

		/* No problem found */
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

	private Challenge createLimitedChallenge(long authorId, String title, String content, MidnightCalendar endDate)
	{
		/* Use current Date as Challenge's creation date */
		return new LimitedChallenge(authorId, new Date(), title, content, endDate);
	}

	private Challenge createNormalChallenge(long authorId, String title, String content)
	{
		/* Use current Date as Challenge's creation date */
		return new NormalChallenge(authorId, new Date(), title, content);
	}

	/**
	 * Enters given ChallengeViewActivity for given Challenge's ID
	 *
	 * @param challengeId the Challenge we want to view.
	 */
	private void enterChallengeView(long challengeId)
	{
		Intent intent = new Intent(this, ChallengeViewActivity.class);
		intent.putExtra(Challenge.KEY_ID, challengeId);
		startActivity(intent);
	}
}