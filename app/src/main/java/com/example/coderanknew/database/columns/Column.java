package com.example.coderanknew.database.columns;

import com.example.coderanknew.database.columns.types.ColumnType;

import java.lang.reflect.Field;

public class Column
{
	private Field field;
	private ColumnType type;
	private String name;
	private boolean isPrimaryKey;

	Column(Field field, ColumnType type, String name)
	{
		this.field = field;
		this.type = type;
		this.name = name;
		this.isPrimaryKey = false;
	}

	void setPrimaryKey()
	{
		this.isPrimaryKey = true;
	}

	public Object get(Object object) throws IllegalAccessException
	{
		return field.get(object);
	}

	public ColumnType type()
	{
		return type;
	}

	public String name()
	{
		return name;
	}

	public boolean isPrimaryKey()
	{
		return isPrimaryKey;
	}

	@Override
	public String toString()
	{
		return (isPrimaryKey ? "PRIMARY KEY: " : "") + type.toString() + " " + name;
	}
}
