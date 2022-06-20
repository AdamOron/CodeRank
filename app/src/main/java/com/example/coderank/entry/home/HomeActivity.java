package com.example.coderank.entry.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;
import com.example.coderank.post.challenge.activities.ChallengeCreateActivity;
import com.example.coderank.R;
import com.example.coderank.post.challenge.Challenge;
import com.example.coderank.sql.Database;
import com.example.coderank.sql.DataConverter;
import com.example.coderank.user.LoginManager;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import java.security.InvalidParameterException;
import java.util.List;

/**
 * The Home screen of the app.
 * Shows ChallengePreviews.
 */
public class HomeActivity extends FragmentActivity
{
	/* This Activity will contain two Fragments, their names are: */
	private static final String[] FRAG_TITLES = {"Trending", "New"};

	/* ViewPager for swiping between Fragments */
	private ViewPager2 viewPager;
	/* The */
	private HomeFragmentAdapter homeFragmentAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);

		initVars();
	}

	/**
	 * Called when we return to this Activity from another one, using back button for example.
	 */
	@Override
	protected void onResume()
	{
		super.onResume();
		/* Refresh the Fragments when we return */
		this.homeFragmentAdapter.refresh();
	}

	private void initVars()
	{
		/* This will never happen */
		if(!LoginManager.isLoggedIn())
			throw new IllegalArgumentException("Can't enter HomeActivity without being logged in");

		initTabs();

		/* If Create Challenge button was clicked, enter ChallengeCreateActivity */
		View bCreateChallenge = findViewById(R.id.bCreateChallenge);
		bCreateChallenge.setOnClickListener(view -> enterCreateChallenge());
	}

	/**
	 * Initialize the two tabs/Fragments.
	 */
	private void initTabs()
	{
		/* Index is 0 by default */
		int index = 0;
		/* if viewPager isn't null, set the index to the viewPager's index */
		if(viewPager != null)
			index = viewPager.getCurrentItem();

		/* Initialize/re-initialize viewPager */
		viewPager = findViewById(R.id.vpHome);
		/* Set viewPager's Adapter to a new HomeFragmentAdapter */
		viewPager.setAdapter(
				/* Save the Adapter to local homeFragmentAdapter as well */
				this.homeFragmentAdapter = new HomeFragmentAdapter(getSupportFragmentManager(), getLifecycle())
		);

		/* The View that shows the two tab/Fragment names on top */
		TabLayout tlHomeTabs = findViewById(R.id.tlHomeTabs);
		/* Initialize tlHomeTabs using TabLayoutMediator */
		new TabLayoutMediator(tlHomeTabs, viewPager,
		(tab, position) ->
		{
			/* Set the new tab's title */
			tab.setText(FRAG_TITLES[position]);
		})
		.attach();

		/* By default, we are viewing the first tab */
		viewPager.setCurrentItem(index);
	}

	private void enterCreateChallenge()
	{
		Intent intent = new Intent(this, ChallengeCreateActivity.class);
		startActivity(intent);
	}

	/**
	 * Returns the Challenges with the most Submissions created since the given date.
	 *
	 * @return 10 most trending Challenges since the given date.
	 */
	private List<Challenge> getTrendingChallenges()
	{
		long weekInMillis =
			7 * // 1 week = 7 days
				(24 * // 1 day = 24 hours
						(60 * // 1 hour = 60 minutes
								(60 * 1000))); // 1 minute = 60 seconds

		Database database = new Database(this);
		database.open();
		List<Challenge> challenges = database.getChallengesBySubmissionCount(DataConverter.dateFromLong(System.currentTimeMillis() - weekInMillis), 10);
		database.close();
		return challenges;
	}

	/**
	 * @return the 10 newest Challenges.
	 */
	private List<Challenge> getNewChallenges()
	{
		Database database = new Database(this);
		database.open();
		List<Challenge> challenges = database.getNewChallenges(10);
		database.close();
		return challenges;
	}

	/**
	 * Adapter for the two Fragments.
	 */
	private class HomeFragmentAdapter extends FragmentStateAdapter
	{
		/* Each Fragment is merely a Challenge ListView Fragment */
		private ChallengeListFragment trendingChallenges, newChallenges;

		public HomeFragmentAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle)
		{
			super(fragmentManager, lifecycle);
		}

		public void refresh()
		{
			/* Only refresh Challenges if the object isn't null */
			if(trendingChallenges != null)
				trendingChallenges.setChallenges(getTrendingChallenges());

			/* Only refresh Challenges if the object isn't null */
			if(newChallenges != null)
				newChallenges.setChallenges(getNewChallenges());
		}

		@NonNull
		@Override
		public Fragment createFragment(int position)
		{
			switch(position)
			{
				case 0:
					/* Initialize and return trending Challenges Fragment */
					return this.trendingChallenges = new ChallengeListFragment(getTrendingChallenges());

				case 1:
					/* Initialize and return newest Challenges Fragment */
					return this.newChallenges = new ChallengeListFragment(getNewChallenges());
			}

			/* This will never happen */
			throw new InvalidParameterException(position + " is an invalid Challenge Fragment position.");
		}

		/**
		 * @return the amount of Fragments, which is the amount of the Fragment titles.
		 */
		@Override
		public int getItemCount()
		{
			return FRAG_TITLES.length;
		}
	}
}