package com.example.coderanknew.syntaxhighlighting;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Tokenizer
{
	private static final HashMap<String, Token.Type> RESERVED;

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

	private final String source;
	private int index;
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

	public void run()
	{
		this.tokens = new ArrayList<>();

		while(hasCurrent())
		{
			skipInvalid();

			if(!hasCurrent()) break;

			tokens.add(scanToken());
			advance();
		}
	}

	private Token scanToken()
	{
		if(source.startsWith("//", index))
		{
			return scanComment();
		}

		char current = current();

		if(Character.isDigit(current) || current == '.')
		{
			return scanNumber();
		}

		if(current == '"' || current == '\'')
		{
			return scanString();
		}

		int startIndex = index;
		String word = scanWord();
		int endIndex = index;

		if(RESERVED.containsKey(word))
		{
			return new Token(RESERVED.get(word), startIndex, endIndex, word);
		}

		if(Character.isUpperCase(word.charAt(0)))
		{
			return new Token(Token.Type.TYPE, startIndex, endIndex, word);
		}

		return new Token(Token.Type.ID, startIndex, endIndex, word);
	}

	private Token scanComment()
	{
		int startIndex = index;

		advance(); advance();

		while(hasCurrent())
		{
			if(current() == '\n') break;
			advance();
		}

		return new Token(Token.Type.COMMENT, startIndex, index, source.substring(startIndex, index));
	}

	private Token scanNumber()
	{
		int startIndex = index;

		while(hasCurrent())
		{
			char current = current();
			if(!Character.isDigit(current) & current != '.') break;

			advance();
		}

		return new Token(Token.Type.NUMBER, startIndex, index, source.substring(startIndex, index));
	}

	private Token scanString()
	{
		int startIndex = index;

		char opener = current();

		advance();

		while(hasCurrent())
		{
			if(current() == opener)
			{
				advance();
				break;
			}

			advance();
		}

		return new Token(Token.Type.STRING, startIndex, index, source.substring(startIndex, index));
	}

	private String scanWord()
	{
		StringBuilder word = new StringBuilder();

		while(hasCurrent())
		{
			char current = current();

			if(Character.isWhitespace(current)) break;
			if(!Character.isLetterOrDigit(current)) break;

			word.append(current);
			advance();
		}

		return word.toString();
	}

	private void skipInvalid()
	{
		while(hasCurrent())
		{
			if(isValid(current())) break;

			advance();
		}
	}

	private char current()
	{
		return source.charAt(index);
	}

	private boolean hasCurrent()
	{
		return index < source.length();
	}

	private void advance()
	{
		index++;
	}

	private boolean isValid(char ch)
	{
		return Character.isLetterOrDigit(ch) || ch == '.' || ch == '/' || ch == '"' || ch == '\'';
	}
}
