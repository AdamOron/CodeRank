package com.example.coderanknew.comment;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RatingBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.example.coderanknew.R;
import com.example.coderanknew.rating.Rating;
import com.example.coderanknew.sql.Database;
import com.example.coderanknew.user.UserPreview;
import java.util.List;

public class CommentAdapter extends ArrayAdapter<Comment>
{
    private final Activity activity;
    private final Rating.Context ratingContext;

    public CommentAdapter(@NonNull Activity activity, Rating.Context ratingContext, @NonNull List<Comment> objects)
    {
        super(activity, R.layout.comment_list, objects);

        this.activity = activity;
        this.ratingContext = ratingContext;
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

        RatingBar rbRating = convertView.findViewById(R.id.rbCommentRating);
        Rating rating = getCommentRating(comment);
        updateRating(rbRating, rating);

        return convertView;
    }

    private void updateRating(RatingBar rbRating, Rating rating)
    {
        if(rating == null)
        {
            rbRating.setVisibility(View.INVISIBLE);
        }
        else
        {
            rbRating.setRating(rating.rating);
        }
    }

    private Rating getCommentRating(Comment comment)
    {
        Database db = new Database(activity);
        db.open();
        Rating rating = db.getRating(comment.authorId, comment.contextId, ratingContext.table);
        db.close();
        return rating;
    }
}
