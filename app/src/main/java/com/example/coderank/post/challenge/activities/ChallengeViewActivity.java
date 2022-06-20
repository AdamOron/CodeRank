package com.example.coderank.post.challenge.activities;

import android.os.Bundle;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.widget.ViewPager2;
import com.example.coderank.R;
import com.example.coderank.post.challenge.Challenge;
import com.example.coderank.post.challenge.fragments.ChallengeFragmentAdapter;
import com.example.coderank.sql.Database;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class ChallengeViewActivity extends FragmentActivity
{
	private static final String[] FRAG_TITLES = {"Overview", "Submissions"};

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_challenge_view);

		initVars();
	}

	private void initVars()
	{
		/* ViewPager2 for swiping between the tabs */
		ViewPager2 viewPager = findViewById(R.id.challengeViewPager);
		/* Set its adapter to a new ChallengeFragmentAdapter */
		viewPager.setAdapter(new ChallengeFragmentAdapter(getSupportFragmentManager(), getLifecycle(), getChallenge()));

		/* TabLayout view for displaying the tabs name on top */
		TabLayout tlChallengeTabs = findViewById(R.id.tlChallengeTabs);

		/* Mediator for initializing the tab names - for each tab, get its name from the FRAG_TITLES array */
		new TabLayoutMediator(tlChallengeTabs, viewPager,
				(tab, position) -> tab.setText(FRAG_TITLES[position]))
		.attach();

		/* By default, we are viewing the first tab */
		viewPager.setCurrentItem(0);
	}

	/**
	 * Extracts given Challenge ID from Intent, then reads Challenge object from Database.
	 *
	 * @return the Challenge that we are viewing.
	 */
	private Challenge getChallenge()
	{
		Bundle intentExtras = getIntent().getExtras();
		long challengeId = intentExtras.getLong(Challenge.KEY_ID, Challenge.INVALID_ID);

		/* This will never happen */
		if(challengeId == Challenge.INVALID_ID)
			throw new IllegalArgumentException("No Challenge ID was passed to ChallengeViewActivity.");

		Database database = new Database(this);
		database.open();

		Challenge challenge = database.getChallengeById(challengeId);

		database.close();

		/* This will never happen */
		if(challenge == null)
			throw new IllegalArgumentException("Challenge ID passed to ChallengeViewActivity doesn't exist in ChallengeDatabase.");

		return challenge;
	}
}