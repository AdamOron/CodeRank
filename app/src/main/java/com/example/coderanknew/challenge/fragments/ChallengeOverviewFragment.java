package com.example.coderanknew.challenge.fragments;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.example.coderanknew.challenge.Challenge;
import com.example.coderanknew.user.LoginManager;
import com.example.coderanknew.R;
import com.example.coderanknew.user.User;
import com.example.coderanknew.challenge.NormalChallenge;
import com.example.coderanknew.comment.Comment;
import com.example.coderanknew.comment.CommentAdapter;
import com.example.coderanknew.sql.Database;
import com.example.coderanknew.user.UserPreview;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public abstract class ChallengeOverviewFragment extends Fragment
{
	private View view;

	private Challenge challenge;

	private CommentAdapter commentAdapter;
	private ArrayList<Comment> comments;
	private EditText commentField;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		this.view = inflate(inflater, container);

		getChallenge();
		prepareCommentManager();

		prepare();

		return this.view;
	}

	protected abstract View inflate(LayoutInflater inflater, ViewGroup container)

	protected abstract void prepare();

	protected void prepareCommentManager()
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
		this.commentAdapter = new CommentAdapter(getActivity(), comments);

		readComments();

		ListView listView = view.findViewById(R.id.lvComments);
		listView.setAdapter(commentAdapter);
		listView.setClickable(true);
		listView.setOnItemClickListener((parent, view, position, id) -> Log.d("User", comments.get(position).authorId + ""));
	}

	private void readComments()
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

	private String getAuthorUsername()
	{
		Database userDatabase = new Database(getActivity());
		userDatabase.open();
		User author = userDatabase.getUserById(challenge.authorId);
		userDatabase.close();
		return author.username;
	}

	private void getChallenge()
	{
		final int INVALID_VALUE = -1;

		Bundle intentExtras = getActivity().getIntent().getExtras();
		long challengeId = intentExtras.getLong(NormalChallenge.KEY_ID, INVALID_VALUE);

		if(challengeId == INVALID_VALUE)
		{
			throw new IllegalArgumentException("No Challenge ID was passed to ChallengeViewActivity.");
		}

		Database challengeDatabase = new Database(getActivity());
		challengeDatabase.open();

		this.challenge = challengeDatabase.getChallengeById(challengeId);

		if(this.challenge == null)
		{
			throw new IllegalArgumentException("Challenge ID passed to ChallengeViewActivity doesn't exist in ChallengeDatabase.");
		}
	}

	private void submitComment()
	{
		long authorId = LoginManager.getLoggedIn().id;
		String content = commentField.getText().toString();
		long parent = challenge.id;

		Comment com = new Comment(authorId, content, new Date(), parent);

		Database db = new Database(getActivity());
		db.open();
		db.insertComment(com, Database.TBL_CH_COMMENTS);
		db.close();

		readComments();
	}
}