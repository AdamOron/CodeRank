package com.example.coderanknew.comment;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.example.coderanknew.R;
import com.example.coderanknew.user.UserPreview;
import java.util.List;

public class CommentAdapter extends ArrayAdapter<Comment>
{
    private final Activity activity;

    public CommentAdapter(@NonNull Activity activity, @NonNull List<Comment> objects)
    {
        super(activity, R.layout.comment_list, objects);

        this.activity = activity;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent)
    {
        LayoutInflater inflater = activity.getLayoutInflater();

        if(convertView == null)
        {
            convertView = inflater.inflate(R.layout.comment_list, parent, false);;
        }

        Comment comment = getItem(position);

        UserPreview upCommentAuthor = convertView.findViewById(R.id.upCommentAuthor);
        upCommentAuthor.setUser(comment.authorId);

        TextView tvContent = convertView.findViewById(R.id.tvCommentContent);
        tvContent.setText(comment.content);

        return convertView;
    }
}
