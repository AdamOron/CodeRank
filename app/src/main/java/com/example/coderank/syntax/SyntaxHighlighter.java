package com.example.coderank.syntax;

import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import java.util.HashMap;

/**
 * Responsible for highlighting code, according to the Token types within the code.
 * Does so by returning SpannableStringBuilder, which can contain color properties.
 */
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
		COLORS.put(Token.Type.FUNC, Color.rgb(255, 204, 0));
	}

	/**
	 * Returns color matching a Token Type.
	 *
	 * @param token the Token we want to color.
	 * @return the color matching the given Token.
	 */
	private static int getColor(Token token)
	{
		/*
		 * Return the color that's specified for the given Token's type within the COLORS map.
		 * If there's no match for given Token's Type within COLORS map, return WHITE.
		 */
		return COLORS.getOrDefault(token.type, Color.WHITE);
	}

	/* The source code we want to color */
	private final String source;

	public SyntaxHighlighter(String source)
	{
		this.source = source;
	}

	/**
	 * @return SpannableStringBuilder that specifies colors for each Token in the source code.
	 */
	public SpannableStringBuilder run()
	{
		/* Tokenizer entire source code */
		Tokenizer tokenizer = new Tokenizer(source);
		tokenizer.run();
		/* Initialize SpannableStringBuilder with source code */
		SpannableStringBuilder builder = new SpannableStringBuilder(source);

		/* For each Token in the scanned Tokens */
		for(Token token : tokenizer.getTokens())
			/* Get the Token's matching color, and color all chars within the Token's starting & ending indices */
			builder.setSpan(new ForegroundColorSpan(getColor(token)), token.startIndex, token.endIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

		return builder;
	}
}
