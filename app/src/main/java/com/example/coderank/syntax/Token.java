package com.example.coderank.syntax;

import androidx.annotation.NonNull;

/**
 * Represents a programming language Token.
 * Each word within code is a Token.
 *
 * For example:
 * Statement: if(a == 3) return;
 * Tokens: [ if(keyword), a(variable), ==(operator), 3(literal), return(keyword) ]
 */
public class Token
{
	/* All Token Types */
	public enum Type
	{
		/* Comment */
		COMMENT,
		/* String literal Token (for example "Hello World") */
		STRING,
		/* Number literal Token (for example 123, 3.5) */
		NUMBER,
		/* Type definition Token (for example int, float) */
		TYPE,
		/* Variable ID Token */
		ID,
		/* Keyword Token (for example if, while, return) */
		KEYWORD,
		/* Function ID Token */
		FUNC,
	}

	/* Each Token keeps track of its Type */
	public Type type;
	/* Each Token keeps track of its indices within the code it appears in */
	public int startIndex, endIndex;
	/* The Token's String */
	public String string;

	/**
	 * Constructs a new Token.
	 *
	 * @param type the Type of the Token.
	 * @param startIndex the starting index of the Token within the code.
	 * @param endIndex the ending index of the Token within the code.
	 * @param string the String of the Token.
	 */
	public Token(Type type, int startIndex, int endIndex, String string)
	{
		this.type = type;
		this.startIndex = startIndex;
		this.endIndex = endIndex;
		this.string = string;
	}

	@NonNull
	@Override
	public String toString()
	{
		return "\"" + string + "\", " + type.toString();
	}
}
