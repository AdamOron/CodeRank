package com.example.coderanknew.challenge.fragments.overview;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;
import com.example.coderanknew.challenge.Challenge;
import com.example.coderanknew.user.LoginManager;
import com.example.coderanknew.R;
import com.example.coderanknew.user.User;
import com.example.coderanknew.comment.Comment;
import com.example.coderanknew.comment.CommentAdapter;
import com.example.coderanknew.sql.Database;
import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public abstract class ChallengeOverviewFragment<C extends Challenge> extends Fragment
{
	protected View view;

	protected C challenge;

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
		this.view = inflate(inflater, container);

		prepareCommentManager();

		prepare();

		return this.view;
	}

	protected abstract View inflate(LayoutInflater inflater, ViewGroup container);

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
		listView.setOnItemClickListener((parent, view, position, id) -> Log.d("Comment Author ", comments.get(position).authorId + ""));
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