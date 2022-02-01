package com.example.coderanknew.syntaxhighlighting;

import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.widget.TextView;

import java.util.HashMap;

public class SyntaxHighlighter
{
	private static final HashMap<Token.Type, Integer> COLORS;

	static
	{
		COLORS = new HashMap<>();
		COLORS.put(Token.Type.COMMENT, Color.rgb(255, 0, 111));
		COLORS.put(Token.Type.STRING, Color.rgb(7, 107, 0));
		COLORS.put(Token.Type.NUMBER, Color.rgb(255, 157, 0));
		COLORS.put(Token.Type.TYPE, Color.rgb(171, 0,255));
		COLORS.put(Token.Type.KEYWORD, Color.rgb(0, 111, 255));
		COLORS.put(Token.Type.ID, Color.WHITE);
	}

	private static int getColor(Token token)
	{
		return COLORS.getOrDefault(token.type, Color.WHITE);
	}

	private final String source;

	public SyntaxHighlighter(String source)
	{
		this.source = source;
	}

	public SpannableStringBuilder run()
	{
		Tokenizer tokenizer = new Tokenizer(source);
		tokenizer.run();

		SpannableStringBuilder builder = new SpannableStringBuilder(source);

		for(Token token : tokenizer.getTokens())
		{
			builder.setSpan(new ForegroundColorSpan(getColor(token)), token.startIndex, token.endIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		}

		return builder;
	}
}
