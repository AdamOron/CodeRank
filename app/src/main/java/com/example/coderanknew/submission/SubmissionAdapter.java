package com.example.coderanknew.submission;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.example.coderanknew.R;
import com.example.coderanknew.syntaxhighlighting.SyntaxHighlighter;
import com.example.coderanknew.user.UserPreview;
import java.util.List;

public class SubmissionAdapter extends ArrayAdapter<Submission>
{
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
        LayoutInflater inflater = activity.getLayoutInflater();

        if(convertView == null)
        {
            convertView = inflater.inflate(R.layout.layout_submission, parent, false);;
        }

        Submission submission = getItem(position);

        UserPreview upSubAuthor = convertView.findViewById(R.id.upSubAuthor);
        upSubAuthor.setUser(submission.authorId);

        TextView tvSubLang = convertView.findViewById(R.id.tvSubLang);
        tvSubLang.setText(submission.lang);

        /* Set Submission content TextView to highlighted syntax */
        SyntaxHighlighter highlighter = new SyntaxHighlighter(submission.content);
        TextView tvSubContent = convertView.findViewById(R.id.tvSubContent);
        tvSubContent.setText(highlighter.run());

        return convertView;
    }
}
