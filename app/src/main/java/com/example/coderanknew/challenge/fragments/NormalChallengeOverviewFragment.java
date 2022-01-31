package com.example.coderanknew.challenge.fragments;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.example.coderanknew.R;
import com.example.coderanknew.user.UserPreview;

public class NormalChallengeOverviewFragment extends ChallengeOverviewFragment
{
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

        TextView tvContent = view.findViewById(R.id.tvTopicContent);
        tvContent.setText(challenge.content);
    }
}
