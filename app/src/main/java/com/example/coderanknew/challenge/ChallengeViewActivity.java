package com.example.coderanknew.challenge;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;
import com.example.coderanknew.R;
import com.example.coderanknew.challenge.fragments.ChallengeOverviewFragment;
import com.example.coderanknew.challenge.fragments.ChallengeSubmissionsFragment;
import com.example.coderanknew.sql.Database;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import java.security.InvalidParameterException;

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
		ViewPager2 viewPager = findViewById(R.id.challengeViewPager);
		viewPager.setAdapter(new ChallengeFragmentAdapter(getSupportFragmentManager(), getLifecycle()));

		TabLayout tlChallengeTabs = findViewById(R.id.tlChallengeTabs);

		new TabLayoutMediator(tlChallengeTabs, viewPager, (tab, position) ->
		{
			tab.setText(FRAG_TITLES[position]);
			viewPager.setCurrentItem(position, true);
		})
		.attach();

		/* Start at first tab */
		viewPager.setCurrentItem(0);
	}

	private Challenge getChallenge()
	{
		final int INVALID_VALUE = -1;

		Bundle intentExtras = getIntent().getExtras();
		long challengeId = intentExtras.getLong(Challenge.KEY_ID, INVALID_VALUE);

		if(challengeId == INVALID_VALUE)
		{
			throw new IllegalArgumentException("No Challenge ID was passed to ChallengeViewActivity.");
		}

		Database database = new Database(this);
		database.open();

		Challenge challenge = database.getChallengeById(challengeId);

		database.close();

		if(challenge == null)
		{
			throw new IllegalArgumentException("Challenge ID passed to ChallengeViewActivity doesn't exist in ChallengeDatabase.");
		}

		return challenge;
	}

	private class ChallengeFragmentAdapter extends FragmentStateAdapter
	{
		private final Challenge challenge;

		private final ChallengeOverviewFragment<?> overviewFragment;
		private final ChallengeSubmissionsFragment submissionsFragment;

		public ChallengeFragmentAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle)
		{
			super(fragmentManager, lifecycle);

			this.challenge = getChallenge();

			this.overviewFragment = challenge.createOverviewFragment();
			this.submissionsFragment = new ChallengeSubmissionsFragment(challenge);
			this.submissionsFragment.setOnSubmissionsChangeListener(this.overviewFragment);
		}

		@NonNull
		@Override
		public Fragment createFragment(int position)
		{
			switch(position)
			{
				case 0:
					return overviewFragment;

				case 1:
					return submissionsFragment;
			}

			throw new InvalidParameterException(position + " is an invalid Challenge Fragment position.");
		}

		@Override
		public int getItemCount()
		{
			return 2;
		}
	}
}