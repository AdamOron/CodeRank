package com.example.coderanknew.challenge.fragments;

import android.app.Activity;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import com.example.coderanknew.challenge.Challenge;
import com.example.coderanknew.rating.Rating;
import com.example.coderanknew.submission.Submission;
import com.example.coderanknew.user.LoginManager;
import com.example.coderanknew.R;
import com.example.coderanknew.comment.Comment;
import com.example.coderanknew.comment.CommentAdapter;
import com.example.coderanknew.sql.Database;
import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public abstract class ChallengeOverviewFragment<C extends Challenge> extends Fragment implements ChallengeSubmissionsFragment.OnSubmissionsChangeListener
{
	protected View view;
	protected C challenge;

	private TextView tvSubCount;

	private CommentAdapter commentAdapter;
	private ArrayList<Comment> comments;
	private EditText commentField;

	public ChallengeOverviewFragment(C challenge)
	{
		this.challenge = challenge;
	}

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		view = inflate(inflater, container);

		initVars();

		prepare();

		return view;
	}

	@Override
	public void onChange(List<Submission> newSubmissions)
	{
		String subCount = newSubmissions.size() + "";

		tvSubCount.setText(subCount);
	}

	protected abstract View inflate(LayoutInflater inflater, ViewGroup container);

	protected abstract void prepare();

	private void initVars()
	{
		initCommentManager();

		this.tvSubCount = view.findViewById(R.id.tvSubCount);
	}

	private void initCommentManager()
	{
		this.commentField = view.findViewById(R.id.etComment);

		/* Listen to Comment Submit button */
		View bSubmitComment = view.findViewById(R.id.bSubmitComment);
		bSubmitComment.setOnClickListener(view -> submitComment());

		initCommentListView();
	}

	private void initCommentListView()
	{
		this.comments = new ArrayList<>();
		this.commentAdapter = new CommentAdapter(requireActivity(), Rating.Context.CHALLENGE, comments);

		updateComments();

		ListView listView = view.findViewById(R.id.lvComments);
		listView.setAdapter(commentAdapter);
		listView.setClickable(true);
		listView.setOnItemClickListener((parent, view, position, id) -> Log.d("Comment Author ", comments.get(position).authorId + ""));
	}

	private void updateComments()
	{
		Database database = new Database(getActivity());
		database.open();

		List<Comment> newComments = database.getAllCommentsByFilter(
				Database.COL_COM_PARENT + "=" + this.challenge.id
				, Database.COL_COM_ID + " DESC",
				Database.TBL_CH_COMMENTS);

		database.close();

		this.comments.clear();
		this.comments.addAll(newComments);

		this.commentAdapter.notifyDataSetChanged();
	}

	private void submitComment()
	{
		/* Construct comment */
		Comment comment = new Comment(
				LoginManager.getLoggedIn().id, // Comment author is the User that's logged in
				commentField.getText().toString(), // Get comment content from commentField EditText
				new Date(), // Comment submission date is current date
				challenge.id // Comment parent 'thread' is this challenge
		);

		/* Reset comment field (must happen after we construct the Comment) */
		resetCommentField();

		/* Insert comment into ChallengeComments table */
		Database db = new Database(getActivity());
		db.open();
		db.insertComment(comment, Database.TBL_CH_COMMENTS);
		db.close();

		/* Update comments */
		updateComments();
	}

	private void resetCommentField()
	{
		/* Clear EditText */
		commentField.setText(null);

		/* Close Keyboard */
		Activity activity = getActivity();
		InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
		View currentFocus = activity.getCurrentFocus();
		if(currentFocus != null) imm.hideSoftInputFromWindow(currentFocus.getWindowToken(), 0);
	}
}