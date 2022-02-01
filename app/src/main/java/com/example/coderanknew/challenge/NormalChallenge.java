package com.example.coderanknew.challenge;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.example.coderanknew.R;
import com.example.coderanknew.challenge.fragments.ChallengeOverviewFragment;
import com.example.coderanknew.user.UserPreview;

import java.util.Date;

public class NormalChallenge extends Challenge
{
	public String title, content;

	public NormalChallenge(long id, long authorId, Date creationDate, String title, String content)
	{
		super(id, authorId, creationDate);

		this.title = title;
		this.content = content;
	}

	public NormalChallenge(long authorId, String title, String content, Date creationDate)
	{
		this(0L, authorId, creationDate, title, content);
	}

	@Override
	public ChallengeOverviewFragment<?> createOverviewFragment()
	{
		return new NormalChallengeOverviewFragment(this);
	}

	public static class NormalChallengeOverviewFragment extends ChallengeOverviewFragment<NormalChallenge>
	{
		public NormalChallengeOverviewFragment(NormalChallenge challenge)
		{
			super(challenge);
		}

		@Override
		protected View inflate(LayoutInflater inflater, ViewGroup container)
		{
			return inflater.inflate(R.layout.fragment_challenge_overview, container, false);
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
		}
	}
}
