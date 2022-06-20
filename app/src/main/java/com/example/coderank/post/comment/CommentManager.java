package com.example.coderank.post.comment;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import androidx.annotation.Nullable;
import com.example.coderank.post.Post;
import com.example.coderank.R;
import com.example.coderank.sql.Database;
import com.example.coderank.user.LoginManager;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * CommentManager View & logic.
 */
public class CommentManager extends LinearLayout
{
    /* The Post whose Comments we are managing */
    private Post post;

    /* Converts Comment objects into Views */
    private CommentAdapter commentAdapter;
    /* List of all Comments */
    private ArrayList<Comment> comments;
    /* New Comment field */
    private EditText commentField;

    public CommentManager(Context context, @Nullable AttributeSet attributes)
    {
        super(context, attributes);
        /* Inflate the view */
        inflate(context, R.layout.layout_comment_manager, this);
    }

    /**
     * Set the Post of the CommentManager.
     *
     * @param post the Post.
     */
    public void setPost(Post post)
    {
        this.post = post;
        /* Initialize CommentManager now that we have a post */
        init();
    }

    /**
     * Refresh the CommentManager.
     * Re-reads all Comments.
     */
    public void refresh()
    {
        this.commentAdapter.notifyDataSetChanged();
    }

    /**
     * Initialize the CommentManager.
     */
    private void init()
    {
        this.commentField = findViewById(R.id.etComment);

        /* Listen to Comment Submit button, if clicked attempt to submit Comment */
        View bSubmitComment = findViewById(R.id.bSubmitComment);
        bSubmitComment.setOnClickListener(view -> submitComment());
        /* Initialize the ListView */
        initCommentListView();
    }

    /**
     * Initializes Comments ListView
     */
    private void initCommentListView()
    {
        /* Initialize list */
        this.comments = new ArrayList<>();

        /* Create CommentAdapter */
        this.commentAdapter = new CommentAdapter((Activity) getContext(), post, comments);

        /* Update Comments to initialize them */
        updateComments();

        /* Get & prepare ListView */
        ListView commentList = findViewById(R.id.lvComments);
        commentList.setAdapter(commentAdapter);
        commentList.setClickable(true);
    }

    /**
     * Update Comments List.
     * Re-reads all Comments from Database.
     */
    private void updateComments()
    {
        Database database = new Database(getContext());
        database.open();

        List<Comment> newComments = database.getAllCommentsByFilter(
                Database.COL_COM_POST + "=" + this.post.id
                , Database.COL_COM_ID + " DESC",
                post.TBL_COMMENTS);

        database.close();

        /* Replace old comments with new */
        this.comments.clear();
        this.comments.addAll(newComments);

        /* Notify the Adapter that the list has changed */
        this.commentAdapter.notifyDataSetChanged();
    }

    /**
     * Submit new Comment.
     */
    private void submitComment()
    {
        String content = commentField.getText().toString();

        /* If new Comment is invalid ,exit */
        if(!ensureFieldsSet(content))
            return;

        /* Construct comment */
        Comment comment = new Comment(
                LoginManager.getLoggedIn().id, // Comment author is the User that's logged in
                content, // Get comment content from commentField EditText
                new Date(), // Comment submission date is current date
                this.post.id // Comment parent 'thread' is this challenge
        );

        /* Reset comment field (must happen after we construct the Comment) */
        resetCommentField();

        /* Insert comment into ChallengeComments table */
        Database db = new Database(getContext());
        db.open();
        db.insertComment(comment, post.TBL_COMMENTS);
        db.close();

        /* Update comments */
        updateComments();
    }

    /**
     * @param content the new Comment's content.
     * @return whether the new Comment is valid or not.
     */
    private boolean ensureFieldsSet(String content)
    {
        return !content.isEmpty();
    }

    private void resetCommentField()
    {
        /* Clear EditText */
        commentField.setText(null);

        /* Close Keyboard */
        Activity activity = (Activity) getContext();
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        View currentFocus = activity.getCurrentFocus();
        if(currentFocus != null) imm.hideSoftInputFromWindow(currentFocus.getWindowToken(), 0);
    }
}
