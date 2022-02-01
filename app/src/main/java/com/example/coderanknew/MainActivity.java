package com.example.coderanknew;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.widget.TextView;

import com.example.coderanknew.syntaxhighlighting.SyntaxHighlighter;
import com.example.coderanknew.syntaxhighlighting.Token;
import com.example.coderanknew.syntaxhighlighting.Tokenizer;

public class MainActivity extends Activity
{
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		enterLogin();
	}

	private void enterLogin()
	{
		startActivity(new Intent(this, LoginActivity.class));
	}
}
