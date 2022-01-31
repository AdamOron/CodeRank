package com.example.coderanknew.database.columns.types;

public enum ColumnType
{
	INTEGER("INTEGER"),
	VARCHAR("VARCHAR");

	public final String string;

	ColumnType(String string)
	{
		this.string = string;
	}
}
