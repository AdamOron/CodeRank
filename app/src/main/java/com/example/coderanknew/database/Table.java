package com.example.coderanknew.database;

import com.example.coderanknew.database.columns.Column;
import com.example.coderanknew.database.columns.ColumnFactory;
import java.util.List;

public class Table<T>
{
	public final String NAME;
	public final String CREATE_STATEMENT;

	private List<Column> columns;
	
	public Table(Class<T> type, String NAME)
	{
		this.NAME = NAME;
		this.initColumns(type);
		this.CREATE_STATEMENT = this.generateCreateStatement();
	}
	
	private String generateCreateStatement()
	{
		StringBuilder statement = new StringBuilder("CREATE TABLE IF NOT EXISTS ");
		statement.append(NAME);
		statement.append('(');

		for(int i = 0; i < columns.size(); i++)
		{
			statement.append(generateColumnStatement(columns.get(i)));
			if(i < columns.size() - 1) statement.append(", ");
		}

		statement.append(')');
		return statement.toString();
	}
	
	private static String generateColumnStatement(Column column)
	{
		return column.name() + (column.isPrimaryKey() ? " INTEGER PRIMARY KEY AUTOINCREMENT" : " " + column.type().string); 
	}

	private void initColumns(Class<?> type)
	{
		this.columns = new ColumnFactory(type).generateColumns();
	}
}
