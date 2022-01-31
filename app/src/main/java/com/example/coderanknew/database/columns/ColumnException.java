package com.example.coderanknew.database.columns;

public class ColumnException extends RuntimeException
{
	private static final long serialVersionUID = -9204749980889789246L;

	private ColumnException(String message)
	{
		super(message);
	}
	
	public static ColumnException invalidType(Class<?> type)
	{
		return new ColumnException(type.toString() + " is an invalid Column type.");
	}
	
	public static ColumnException invalidName(String name)
	{
		return new ColumnException("\"" + name + "\" is an invalid Column name.");
	}
	
	public static ColumnException duplicateName(String name)
	{
		return new ColumnException("Column \"" + name + "\" already exists.");
	}
	
	public static ColumnException multiplePrimaryKeys()
	{
		return new ColumnException("Multiple Primary Key Columns are illegal.");
	}
	
	public static ColumnException invalidPrimaryKey(Column column)
	{
		return new ColumnException("Column \"" + column.toString() + "\" is an invalid Primary Key.");
	}
}
