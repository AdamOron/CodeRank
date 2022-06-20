package com.example.coderank.post.submission;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RatingBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.example.coderank.R;
import com.example.coderank.syntax.SyntaxHighlighter;
import com.example.coderank.user.UserPreview;
import java.util.List;

/**
 * Converts Submissions to a View. Used for displaying Submissions in ListView.
 */
public class SubmissionAdapter extends ArrayAdapter<Submission>
{
    /* The Activity that created this SubmissionAdapter */
    private final Activity activity;

    public SubmissionAdapter(@NonNull Activity activity, @NonNull List<Submission> objects)
    {
        super(activity, R.layout.layout_submission, objects);

        this.activity = activity;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent)
    {
        /* Get LayoutInflater */
        LayoutInflater inflater = activity.getLayoutInflater();

        /* If convertView is null, inflate it. If it isn't, we can reuse it. */
        if(convertView == null)
            convertView = inflater.inflate(R.layout.layout_submission, parent, false);

        /* Get the Submission at the position */
        Submission submission = getItem(position);

        /* Set UserPreview within Submission to the Submission author's ID */
        UserPreview upSubAuthor = convertView.findViewById(R.id.upSubAuthor);
        upSubAuthor.setUser(submission.authorId);
        /* Set Submission's programming language */
        TextView tvSubLang = convertView.findViewById(R.id.tvSubLang);
        tvSubLang.setText(submission.lang);
        /* Set Submission's average rating, using Post's 'getAvgRating' function */
        RatingBar rbSubmissionAvgRating = convertView.findViewById(R.id.rbSubmissionAvgRating);
        rbSubmissionAvgRating.setRating(submission.getAvgRating(getContext()));

        /* Create SyntaxHighlighter for Submission's content */
        SyntaxHighlighter highlighter = new SyntaxHighlighter(submission.content);
        /* Apply SyntaxHighlighting to content, and use it in TextView */
        TextView tvSubContent = convertView.findViewById(R.id.tvSubContent);
        tvSubContent.setText(highlighter.run());

        return convertView;
    }
}
