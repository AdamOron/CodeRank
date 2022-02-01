package com.example.coderanknew.syntaxhighlighting;

import androidx.annotation.NonNull;

public class Token
{
	public enum Type
	{
		COMMENT,
		STRING,
		NUMBER,
		TYPE,
		ID,
		KEYWORD,
	}

	public Type type;
	public int startIndex, endIndex;
	public String string;

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
