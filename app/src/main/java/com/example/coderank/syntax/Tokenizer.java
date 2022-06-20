package com.example.coderank.syntax;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Responsible for extracting Tokens from code.
 */
public class Tokenizer
{
	/* Global map of Token Strings to their matching Types */
	private static final HashMap<String, Token.Type> RESERVED;

	/* Initialize & populate the map */
	static
	{
		RESERVED = new HashMap<>();
		/* Data Types */
		RESERVED.put("void", Token.Type.TYPE);
		RESERVED.put("byte", Token.Type.TYPE);
		RESERVED.put("boolean", Token.Type.TYPE);
		RESERVED.put("short", Token.Type.TYPE);
		RESERVED.put("char", Token.Type.TYPE);
		RESERVED.put("int", Token.Type.TYPE);
		RESERVED.put("float", Token.Type.TYPE);
		RESERVED.put("double", Token.Type.TYPE);
		RESERVED.put("long", Token.Type.TYPE);
		/* Keywords */
		RESERVED.put("abstract", Token.Type.KEYWORD);
		RESERVED.put("assert", Token.Type.KEYWORD);
		RESERVED.put("break", Token.Type.KEYWORD);
		RESERVED.put("case", Token.Type.KEYWORD);
		RESERVED.put("catch", Token.Type.KEYWORD);
		RESERVED.put("class", Token.Type.KEYWORD);
		RESERVED.put("const", Token.Type.KEYWORD);
		RESERVED.put("continue", Token.Type.KEYWORD);
		RESERVED.put("default", Token.Type.KEYWORD);
		RESERVED.put("do", Token.Type.KEYWORD);
		RESERVED.put("else", Token.Type.KEYWORD);
		RESERVED.put("extends", Token.Type.KEYWORD);
		RESERVED.put("false", Token.Type.KEYWORD);
		RESERVED.put("final", Token.Type.KEYWORD);
		RESERVED.put("finally", Token.Type.KEYWORD);
		RESERVED.put("for", Token.Type.KEYWORD);
		RESERVED.put("goto", Token.Type.KEYWORD);
		RESERVED.put("if", Token.Type.KEYWORD);
		RESERVED.put("implements", Token.Type.KEYWORD);
		RESERVED.put("import", Token.Type.KEYWORD);
		RESERVED.put("instanceof", Token.Type.KEYWORD);
		RESERVED.put("interface", Token.Type.KEYWORD);
		RESERVED.put("native", Token.Type.KEYWORD);
		RESERVED.put("new", Token.Type.KEYWORD);
		RESERVED.put("null", Token.Type.KEYWORD);
		RESERVED.put("package", Token.Type.KEYWORD);
		RESERVED.put("private", Token.Type.KEYWORD);
		RESERVED.put("protected", Token.Type.KEYWORD);
		RESERVED.put("public", Token.Type.KEYWORD);
		RESERVED.put("return", Token.Type.KEYWORD);
		RESERVED.put("static", Token.Type.KEYWORD);
		RESERVED.put("strictfp", Token.Type.KEYWORD);
		RESERVED.put("super", Token.Type.KEYWORD);
		RESERVED.put("switch", Token.Type.KEYWORD);
		RESERVED.put("synchronized", Token.Type.KEYWORD);
		RESERVED.put("this", Token.Type.KEYWORD);
		RESERVED.put("throw", Token.Type.KEYWORD);
		RESERVED.put("throws", Token.Type.KEYWORD);
		RESERVED.put("transient", Token.Type.KEYWORD);
		RESERVED.put("true", Token.Type.KEYWORD);
		RESERVED.put("try", Token.Type.KEYWORD);
		RESERVED.put("volatile", Token.Type.KEYWORD);
		RESERVED.put("while", Token.Type.KEYWORD);
	}

	/* A Tokenizer stores the source code it needs to tokenize */
	private final String source;
	/* Keep track of current index within code */
	private int index;
	/* List of Tokens */
	private List<Token> tokens;

	public Tokenizer(String source)
	{
		this.source = source;
		this.index = 0;
	}

	public List<Token> getTokens()
	{
		return tokens;
	}

	/**
	 * Tokenization logic.
	 */
	public void run()
	{
		this.tokens = new ArrayList<>();

		/* As long as there's another Token to scan */
		while(hasCurrent())
		{
			/* Skip all invalid chars (whitespace, e.t.c) */
			skipInvalid();
			/* If we reached end after skipping invalid chars, exit loop */
			if(!hasCurrent()) break;
			/* Scan current Token, add it to the list & advance to next char */
			tokens.add(scanToken());
			advance();
		}
	}

	/**
	 * Scans current Token.
	 *
	 * @return current Token.
	 */
	private Token scanToken()
	{
		/* If our Token starts with '//', it's a comment */
		if(source.startsWith("//", index))
			return scanComment();

		/* Save current char */
		char current = current();

		/* If current char is a digit or a '.', Token is a number */
		if(Character.isDigit(current) || current == '.')
			return scanNumber();

		/* If current char is quotation mark, it's a String literal */
		if(current == '"' || current == '\'')
			return scanString();

		/* If we found no other match, we'll scan for reserved keyword */
		/* Save starting index */
		int startIndex = index;
		/* Scan word String */
		String word = scanWord();
		/* Save ending index */
		int endIndex = index;

		/* If scanned word is a reserved keyword, return a matching Token */
		if(RESERVED.containsKey(word))
			return new Token(RESERVED.get(word), startIndex, endIndex, word);

		/* If scanned word starts with an uppercase letter, it's an Object type */
		if(Character.isUpperCase(word.charAt(0)))
			return new Token(Token.Type.TYPE, startIndex, endIndex, word);

		/* If scanned word is not the end of the code, and after it comes a bracket, it's a function ID */
		if(hasCurrent() && current() == '(')
			return new Token(Token.Type.FUNC, startIndex, endIndex, word);

		/* If no other match found, scanned word is a variable ID */
		return new Token(Token.Type.ID, startIndex, endIndex, word);
	}

	/**
	 * Scans comment Token.
	 *
	 * @return scanned comment Token.
	 */
	private Token scanComment()
	{
		/* Save starting index */
		int startIndex = index;
		/* Skip two chars (every comment beings with '//') */
		advance(); advance();

		while(hasCurrent())
		{
			/* If we reached the end of the line, comment has ended */
			if(current() == '\n') break;
			/* Otherwise advance to next char */
			advance();
		}

		/* Return new Token matching the comment */
		return new Token(Token.Type.COMMENT, startIndex, index, source.substring(startIndex, index));
	}

	/**
	 * Scans number Token.
	 *
	 * @return scanned number Token.
	 */
	private Token scanNumber()
	{
		int startIndex = index;

		while(hasCurrent())
		{
			char current = current();
			/* If current char is not digit or floating point, the number ended */
			if(!Character.isDigit(current) && current != '.') break;
			/* Otherwise, advance to next char */
			advance();
		}

		/* Return new Token matching the number */
		return new Token(Token.Type.NUMBER, startIndex, index, source.substring(startIndex, index));
	}

	/**
	 * Scans String Token.
	 *
	 * @return scanned String Token.
	 */
	private Token scanString()
	{
		int startIndex = index;

		/*
		 * Save opening symbol - " or '.
		 * We need to save this because the String only ends when we encounter it again.
		 * For example, in the String: "Hello'World" we need to stop after ...World", and not after
		 * ...Hello'.
		 */
		char opener = current();

		advance();

		while(hasCurrent())
		{
			/* If we encountered opening symbol, the String is closed. Advance past the symbol and exit. */
			if(current() == opener)
			{
				advance();
				break;
			}
			/* Otherwise, scan next char */
			advance();
		}

		/* Return new Token matching the String */
		return new Token(Token.Type.STRING, startIndex, index, source.substring(startIndex, index));
	}

	/**
	 * Scan entire word Token, until the next whitespace.
	 *
	 * @return scanned word Token.
	 */
	private String scanWord()
	{
		/* Init empty StringBuilder */
		StringBuilder word = new StringBuilder();

		while(hasCurrent())
		{
			char current = current();
			/* If current char is whitespace, we reached end of word */
			if(Character.isWhitespace(current)) break;
			/* If current char isn't letter or digit, we reached end of word */
			if(!Character.isLetterOrDigit(current)) break;
			/* Append current char to word */
			word.append(current);
			/* Advance to next char */
			advance();
		}

		/* Compose the StringBuilder into a String and return */
		return word.toString();
	}

	/**
	 * Skip all invalid chars.
	 * Valid chars are specified in the 'isValid' function.
	 */
	private void skipInvalid()
	{
		while(hasCurrent())
		{
			/* If current char is valid, we skipped all invalid chars */
			if(isValid(current())) break;
			/* Otherwise, advance to next char */
			advance();
		}
	}

	/**
	 * @return current character within source code.
	 */
	private char current()
	{
		return source.charAt(index);
	}

	/**
	 * @return whether current index is valid.
	 */
	private boolean hasCurrent()
	{
		return index < source.length();
	}

	/**
	 * Advance to next char by incrementing current index.
	 */
	private void advance()
	{
		index++;
	}

	/**
	 * Checks and returns whether a character is valid.
	 * A valid character is a letter, digit, dot, forward slash, quotation marks.
	 *
	 * @param ch the char to be validated.
	 * @return whether given char is valid or not.
	 */
	private static boolean isValid(char ch)
	{
		return Character.isLetterOrDigit(ch) || ch == '.' || ch == '/' || ch == '"' || ch == '\'';
	}
}
