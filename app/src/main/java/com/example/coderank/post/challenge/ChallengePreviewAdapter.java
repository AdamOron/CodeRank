package com.example.coderank.post.challenge;

import android.app.Activity;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.example.coderank.R;
import com.example.coderank.user.UserPreview;
import java.util.List;

/**
 * Creates ChallengePreviews for a ListView of Challenges.
 */
public class ChallengePreviewAdapter extends ArrayAdapter<Challenge>
{
	/* Each ChallengePreview gets a color that matches its position. This color has no meaning, it's just for aesthetics. Here is an array of these color codes: */
	private static final String[] HEX_COLORS = {"#ffc000", "#ff0198", "#00b0f0", "#92d050"};

	private final Activity activity;

	public ChallengePreviewAdapter(@NonNull Activity context, @NonNull List<Challenge> objects)
	{
		super(context, R.layout.layout_challenge_preview, objects);

		this.activity = context;
	}

	@NonNull
	@Override
	public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent)
	{
		LayoutInflater inflater = activity.getLayoutInflater();

		/* If convertView is null, inflate it. If it isn't, we can reuse it. */
		if(convertView == null)
			convertView = inflater.inflate(R.layout.layout_challenge_preview, parent, false);

		Challenge challenge = getItem(position);

		TextView tvChallengeTitle = convertView.findViewById(R.id.tvChallengeTitle);
		tvChallengeTitle.setText(challenge.title);

		UserPreview upChallengeAuthor = convertView.findViewById(R.id.upChallengeAuthor);
		upChallengeAuthor.setUser(challenge.authorId);

		RatingBar rbChallengeAvgRating = convertView.findViewById(R.id.rbChallengeAvgRating);
		rbChallengeAvgRating.setRating(challenge.getAvgRating(activity));

		LinearLayout lvBackground = convertView.findViewById(R.id.lvBackground);
		lvBackground.getBackground().setTint(getColor(position));

		return convertView;
	}

	/**
	 * @param position the position of the Challenge within the ListView.
	 * @return the color of the ChallengePreview.
	 */
	private int getColor(int position)
	{
		return Color.parseColor(HEX_COLORS[position % HEX_COLORS.length]);
	}
}
