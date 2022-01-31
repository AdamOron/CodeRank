package com.example.coderanknew.database.columns;

import android.util.Log;

import com.example.coderanknew.database.columns.annots.ColumnSettings;
import com.example.coderanknew.database.columns.annots.IgnoreColumn;
import com.example.coderanknew.database.columns.annots.PrimaryKey;
import com.example.coderanknew.database.columns.types.ColumnType;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class ColumnFactory
{
	private final Class<?> type;
	private final HashSet<String> existingColumns;
	private Column primaryKey;
	
	public ColumnFactory(Class<?> type)
	{
		this.type = type;
		this.existingColumns = new HashSet<>();
		this.primaryKey = null;
	}

	public List<Column> generateColumns()
	{
		List<Column> columns = new ArrayList<>();

		for(Field field : type.getFields())
		{
			if(field.isAnnotationPresent(IgnoreColumn.class))
			{
				continue;
			}

			columns.add(generateColumn(field));
		}

		return columns;
	}

	private Column generateColumn(Field field)
	{
		Column column = new Column(field, findColumnType(field), findColumnName(field));

		if(field.isAnnotationPresent(PrimaryKey.class))
		{
			makePrimaryKey(column);
		}

		return column;
	}
	
	private void makePrimaryKey(Column column)
	{
		if(primaryKey != null)
		{
			throw ColumnException.multiplePrimaryKeys();
		}
		
		if(column.type() != ColumnType.INTEGER)
		{
			throw ColumnException.invalidPrimaryKey(column);
		}
		
		column.setPrimaryKey();
		primaryKey = column;
	}
	
	private ColumnType findColumnType(Field field)
	{
		Class<?> fieldType = field.getType();

		if(fieldType.equals(int.class) || fieldType.equals(long.class))
		{
			return ColumnType.INTEGER;
		}

		if(fieldType.equals(String.class))
		{
			return ColumnType.VARCHAR;
		}

		throw ColumnException.invalidType(fieldType);
	}
	
	private String findColumnName(Field field)
	{
		ColumnSettings settings = field.getAnnotation(ColumnSettings.class);
		
		if(settings == null)
		{
			return generateColumnName(field);
		}
		
		validateSpecifiedColumnName(settings.value());
		return settings.value();
	}

	private void validateSpecifiedColumnName(String columnName)
	{
		if(!validateColumnName(columnName))
		{
			throw ColumnException.duplicateName(columnName);
		}
	}
	
	private String generateColumnName(Field field)
	{
		String desc = field.getName();
		
		while(!validateColumnName(desc))
		{
			desc += "_";
		}
		
		return desc;
	}
	
	private boolean validateColumnName(String columnName)
	{
		if(existingColumns.contains(columnName))
		{
			return false;
		}

		existingColumns.add(columnName);
		return true;
	}
}
