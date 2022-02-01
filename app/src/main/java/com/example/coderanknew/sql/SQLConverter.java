package com.example.coderanknew.sql;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayOutputStream;
import java.util.Date;

public class SQLConverter
{
	public static String dateToString(Date date)
	{
		return date.getDay() + "." + date.getMonth() + "." + date.getYear();
	}

	public static Date dateFromString(String dateString)
	{
		String[] split = dateString.split("\\.");
		return new Date(Integer.parseInt(split[0]), Integer.parseInt(split[1]), Integer.parseInt(split[2]));
	}

	public static byte[] bitmapToBlob(Bitmap bitmap)
	{
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
		return bos.toByteArray();
	}

	public static Bitmap bitmapFromBlob(byte[] blob)
	{
		return BitmapFactory.decodeByteArray(blob, 0, blob.length);
	}
}
