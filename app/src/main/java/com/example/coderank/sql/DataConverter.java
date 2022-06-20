package com.example.coderank.sql;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import com.example.coderank.utils.MidnightCalendar;
import java.io.ByteArrayOutputStream;
import java.util.Calendar;
import java.util.Date;

/**
 * Helper class to convert data types so they can be saved in the database.
 */
public class DataConverter
{
	/**
	 * @param midnightCalendar the MidnightCalendar to convert.
	 * @return long representation of the MidnightCalendar.
	 */
	public static long midnightCalendarToLong(MidnightCalendar midnightCalendar)
	{
		return midnightCalendar.getCalendar().getTimeInMillis();
	}

	/**
	 * @param dateLong the long representation of the MidnightCalendar.
	 * @return the MidnightCalendar represented by the long.
	 */
	public static MidnightCalendar midnightCalendarFromLong(long dateLong)
	{
		Calendar midnight = Calendar.getInstance();
		midnight.setTimeInMillis(dateLong);
		return new MidnightCalendar(midnight);
	}

	/**
	 * @param date the Date to convert.
	 * @return the long representation of the Date.
	 */
	public static long dateToLong(Date date)
	{
		return date.getTime();
	}

	/**
	 * @param dateLong the long representing the Date.
	 * @return the Date represented by the long.
	 */
	public static Date dateFromLong(long dateLong)
	{
		return new Date(dateLong);
	}

	/**
	 * @param bitmap the Bitmap to convert.
	 * @return a byte[] representing the Bitmap.
	 */
	public static byte[] bitmapToBlob(Bitmap bitmap)
	{
		Bitmap resized = Bitmap.createScaledBitmap(bitmap, 256, 256, false);
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		resized.compress(Bitmap.CompressFormat.JPEG, 75, bos);
		return bos.toByteArray();
	}

	/**
	 * @param blob the byte[] representing a Bitmap.
	 * @return the Bitmap represented by the byte[].
	 */
	public static Bitmap bitmapFromBlob(byte[] blob)
	{
		return BitmapFactory.decodeByteArray(blob, 0, blob.length);
	}
}
