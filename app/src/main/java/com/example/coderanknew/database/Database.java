package com.example.coderanknew.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.HashMap;

public class Database extends SQLiteOpenHelper
{
	private final String NAME;
	private final int VERSION;

	private final HashMap<String, Table<?>> tables;
	private SQLiteDatabase database;

	public Database(Context context, String NAME, int VERSION)
	{
		super(context, NAME, null, VERSION);

		this.NAME = NAME;
		this.VERSION = VERSION;

		this.tables = new HashMap<>();
	}

	@Override
	public void onCreate(SQLiteDatabase db)
	{
		for(Table<?> table : tables.values())
		{
			db.execSQL(table.CREATE_STATEMENT);
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
	{
		for(Table<?> table : tables.values())
		{
			db.execSQL("DROP TABLE IF EXISTS " + table.NAME);
		}

		onCreate(db);
	}

	public void open()
	{
		this.database = getWritableDatabase();
	}

	public void add(Table<?> table)
	{
		tables.put(table.NAME, table);
	}

	public <T> Table<T> getTable(String tableName)
	{
		return (Table<T>) tables.get(tableName);
	}
}
