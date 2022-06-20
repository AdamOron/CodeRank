package com.example.coderank.sql;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import androidx.annotation.Nullable;
import com.example.coderank.post.Post;
import com.example.coderank.post.challenge.Challenge;
import com.example.coderank.post.challenge.limited.LimitedChallenge;
import com.example.coderank.post.rating.Rating;
import com.example.coderank.user.User;
import com.example.coderank.post.challenge.normal.NormalChallenge;
import com.example.coderank.post.comment.Comment;
import com.example.coderank.post.submission.Submission;
import com.example.coderank.utils.Debug;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * The Database manager.
 */
public class Database extends SQLiteOpenHelper
{
    /**
     * ============================================================================================
     *                                  USER TABLE PROPERTIES
     * ============================================================================================
     */

    private static final String TBL_USERS = "tblUsers";

    public static final String COL_U_ID = "userId";
    public static final String COL_U_FIRSTNAME = "userFirstName";
    public static final String COL_U_LASTNAME = "userLastName";
    public static final String COL_U_EMAIL = "userEmail";
    public static final String COL_U_USERNAME = "userUsername";
    public static final String COL_U_PASSWORD_HASH = "userPasswordHash";
    public static final String COL_U_PICTURE = "userPicture";

    public static final String[] COLS_USERS = {COL_U_ID, COL_U_FIRSTNAME, COL_U_LASTNAME, COL_U_EMAIL, COL_U_USERNAME, COL_U_PICTURE};

    private static final String CREATE_TBL_USERS =
            "CREATE TABLE IF NOT EXISTS " + TBL_USERS + "(" +
                    COL_U_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    COL_U_FIRSTNAME + " VARCHAR," +
                    COL_U_LASTNAME + " VARCHAR," +
                    COL_U_EMAIL + " VARCHAR," +
                    COL_U_USERNAME + " VARCHAR," +
                    COL_U_PASSWORD_HASH + " VARCHAR, " +
                    COL_U_PICTURE + " BLOB)";

    /**
     * ============================================================================================
     *                                  CHALLENGE TABLE PROPERTIES
     * ============================================================================================
     */

    public static final String TBL_CHALLENGES = "tblChallenge";

    /* Abstract Challenge */
    public static final String COL_CH_ID = "chId";
    public static final String COL_CH_TYPE = "chType";
    public static final String COL_CH_AUTHOR_ID = "chAuthorId";
    public static final String COL_CH_CREATE_DATE = "chCreationDate";

    /* Normal Challenge */
    public static final String COL_CH_TITLE = "chTitle";
    public static final String COL_CH_CONTENT = "chContent";

    /* Limited Time Challenge */
    public static final String COL_CH_END_DATE = "chEndDate";

    public static final String[] COLS_CHALLENGES = {COL_CH_ID, COL_CH_TYPE, COL_CH_AUTHOR_ID, COL_CH_CREATE_DATE, COL_CH_TITLE, COL_CH_CONTENT, COL_CH_END_DATE};

    private static final String CREATE_TBL_CHALLENGES =
            "CREATE TABLE IF NOT EXISTS " + TBL_CHALLENGES + "(" +
                    COL_CH_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    COL_CH_TYPE + " INTEGER, " +
                    COL_CH_AUTHOR_ID + " INTEGER, " +
                    COL_CH_CREATE_DATE + " INTEGER, " +
                    COL_CH_TITLE + " VARCHAR, " +
                    COL_CH_CONTENT + " VARCHAR, " +
                    COL_CH_END_DATE + " INTEGER)";

    /**
     * ============================================================================================
     *                                  SUBMISSION TABLE PROPERTIES
     * ============================================================================================
     */

    public static final String TBL_SUBMISSIONS = "tblSubmissions";

    public static final String COL_SUB_ID = "subId";
    public static final String COL_SUB_AUTHOR_ID = "subAuthorId";
    public static final String COL_SUB_CHALLENGE_ID = "subChallengeId";
    public static final String COL_SUB_CONTENT = "subContent";
    public static final String COL_SUB_LANG = "subLang";
    public static final String COL_SUB_DATE = "subDate";

    public static final String[] COLS_SUBMISSIONS = {COL_SUB_ID, COL_SUB_AUTHOR_ID, COL_SUB_CHALLENGE_ID, COL_SUB_CONTENT, COL_SUB_LANG, COL_SUB_DATE};

    private static final String CREATE_TBL_SUBMISSIONS =
            "CREATE TABLE IF NOT EXISTS " + TBL_SUBMISSIONS + "(" +
                    COL_SUB_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    COL_SUB_AUTHOR_ID + " INTEGER," +
                    COL_SUB_CHALLENGE_ID + " INTEGER," +
                    COL_SUB_CONTENT + " VARCHAR," +
                    COL_SUB_LANG + " INTEGER," +
                    COL_SUB_DATE + " INTEGER)";

    /**
     * ============================================================================================
     *                                  COMMENT TABLES PROPERTIES
     * ============================================================================================
     */

    public static final String TBL_CH_COMMENTS = "tblChComments";
    public static final String TBL_SUB_COMMENTS = "tblSubComments";

    public static final String COL_COM_ID = "comId";
    public static final String COL_COM_AUTHOR_ID = "comAuthorId";
    public static final String COL_COM_CONTENT = "comContent";
    public static final String COL_COM_DATE = "comDate";
    public static final String COL_COM_POST = "comParent";

    public static final String[] COLS_COMMENTS = {COL_COM_ID, COL_COM_AUTHOR_ID, COL_COM_CONTENT, COL_COM_DATE, COL_COM_POST};

    private static final String CREATE_TBL_CH_COMMENTS = "CREATE TABLE IF NOT EXISTS " + TBL_CH_COMMENTS + "(" +
            COL_COM_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            COL_COM_AUTHOR_ID + " INTEGER," +
            COL_COM_CONTENT + " VARCHAR," +
            COL_COM_DATE + " INTEGER," +
            COL_COM_POST + " INTEGER)";

    private static final String CREATE_TBL_SUB_COMMENTS = "CREATE TABLE IF NOT EXISTS " + TBL_SUB_COMMENTS + "(" +
            COL_COM_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            COL_COM_AUTHOR_ID + " INTEGER," +
            COL_COM_CONTENT + " VARCHAR," +
            COL_COM_DATE + " INTEGER," +
            COL_COM_POST + " INTEGER)";

    /**
     * ============================================================================================
     *                                  RATING TABLES PROPERTIES
     * ============================================================================================
     */

    public static final String TBL_CH_RATINGS       = "tblChRatings";
    public static final String TBL_SUB_RATINGS      = "tblSubRatings";

    public static final String COL_RATING_AUTHOR_ID = "ratingAuthorId";
    public static final String COL_RATING_POST_ID = "ratingPostId";
    public static final String COL_RATING_RATING = "ratingRating";

    public static final String[] COLS_RATINGS = {COL_RATING_AUTHOR_ID, COL_RATING_POST_ID, COL_RATING_RATING};

    private static final String CREATE_TBL_CH_RATINGS = "CREATE TABLE IF NOT EXISTS " + TBL_CH_RATINGS + "(" +
            COL_RATING_AUTHOR_ID + " INTEGER," +
            COL_RATING_POST_ID + " VARCHAR," +
            COL_RATING_RATING + " INTEGER)";

    private static final String CREATE_TBL_SUB_RATINGS = "CREATE TABLE IF NOT EXISTS " + TBL_SUB_RATINGS + "(" +
            COL_RATING_AUTHOR_ID + " INTEGER," +
            COL_RATING_POST_ID + " INTEGER," +
            COL_RATING_RATING + " VARCHAR)";

    /**
     * ============================================================================================
     *                                  DATABASE PROPERTIES
     * ============================================================================================
     */

    private static final int DB_VERSION = 1;
    public static final String DB_NAME = "database.db";

    private SQLiteDatabase database;

    public Database(@Nullable Context context)
    {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase)
    {
        sqLiteDatabase.execSQL(CREATE_TBL_USERS);
        sqLiteDatabase.execSQL(CREATE_TBL_CHALLENGES);
        sqLiteDatabase.execSQL(CREATE_TBL_SUBMISSIONS);
        sqLiteDatabase.execSQL(CREATE_TBL_CH_COMMENTS);
        sqLiteDatabase.execSQL(CREATE_TBL_SUB_COMMENTS);
        sqLiteDatabase.execSQL(CREATE_TBL_CH_RATINGS);
        sqLiteDatabase.execSQL(CREATE_TBL_SUB_RATINGS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1)
    {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TBL_USERS);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TBL_CHALLENGES);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TBL_SUBMISSIONS);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TBL_CH_COMMENTS);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TBL_SUB_COMMENTS);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TBL_CH_RATINGS);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TBL_SUB_RATINGS);

        onCreate(sqLiteDatabase);
    }

    public void open()
    {
        this.database = this.getWritableDatabase();
    }

    /**
     * ============================================================================================
     *                                  USER TABLE METHODS
     * ============================================================================================
     */

    public User insertUser(User user, String passwordHash)
    {
        ContentValues values = new ContentValues();
        values.put(COL_U_FIRSTNAME, user.firstName);
        values.put(COL_U_LASTNAME, user.lastName);
        values.put(COL_U_EMAIL, user.email);
        values.put(COL_U_USERNAME, user.username);
        values.put(COL_U_PASSWORD_HASH, passwordHash);
        values.put(COL_U_PICTURE, DataConverter.bitmapToBlob(user.picture));

        user.id = database.insert(TBL_USERS, null, values);

        return user;
    }

    /**
     * @param user the User to insert.
     * @return the ID of the inserted User.
     */
    public long updateUserById(User user)
    {
        ContentValues values = new ContentValues();
        values.put(COL_U_ID, user.id);
        values.put(COL_U_FIRSTNAME, user.firstName);
        values.put(COL_U_LASTNAME, user.lastName);
        values.put(COL_U_EMAIL, user.email);
        values.put(COL_U_USERNAME, user.username);
        values.put(COL_U_PICTURE, DataConverter.bitmapToBlob(user.picture));

        return database.update(TBL_USERS, values, COL_U_ID + "=" + user.id, null);
    }

    /**
     * @param userId the ID of the User.
     * @return the amount of Users deleted.
     */
    public long deleteUserById(long userId)
    {
        return database.delete(TBL_USERS, COL_U_ID + "=" + userId, null);
    }

    /**
     * @param selection the selection of the User.
     * @return the User matching the selection.
     */
    public User getUserByFilter(String selection)
    {
        Cursor cursor = database.query(TBL_USERS, COLS_USERS, selection, null, null, null, null);
        if(!cursor.moveToFirst()) return null;
        return singleUserFromCursor(cursor);
    }

    /**
     * @param selection the selection of the User.
     * @param orderBy the order of the Users returned.
     * @return a List of users matching the given selection and order.
     */
    public ArrayList<User> getAllUsersByFilter(String selection, String orderBy)
    {
        Cursor cursor = database.query(TBL_USERS, COLS_USERS, selection, null, null, null, orderBy);
        return allUsersFromCursor(cursor);
    }

    /**
     * @param userId the ID of the User.
     * @return the User matching the ID.
     */
    public User getUserById(long userId)
    {
        Cursor cursor=database.query(TBL_USERS, COLS_USERS, COL_U_ID + "=" + userId, null, null, null, null);
        if(!cursor.moveToFirst()) return null;
        return singleUserFromCursor(cursor);
    }

    /**
     * @return all Users from the DB.
     */
    public ArrayList<User> getAllUsers()
    {
        Cursor cursor = database.query(TBL_USERS, COLS_USERS, null, null, null, null, null);
        return allUsersFromCursor(cursor);
    }

    /**
     * @param cursor the Cursor.
     * @return List of all Users from Cursor.
     */
    private static ArrayList<User> allUsersFromCursor(Cursor cursor)
    {
        ArrayList<User> list = new ArrayList<>();

        if(cursor.moveToFirst())
        {
            do
            {
                list.add(singleUserFromCursor(cursor));
            }
            while(cursor.moveToNext());
        }

        return list;
    }

    /**
     * @param cursor
     * @return the User from the Cursor's current position.
     */
    private static User singleUserFromCursor(Cursor cursor)
    {
        long id = cursor.getLong(cursor.getColumnIndexOrThrow(COL_U_ID));
        String firstName = cursor.getString(cursor.getColumnIndexOrThrow(COL_U_FIRSTNAME));
        String lastName = cursor.getString(cursor.getColumnIndexOrThrow(COL_U_LASTNAME));
        String email = cursor.getString(cursor.getColumnIndexOrThrow(COL_U_EMAIL));
        String username = cursor.getString(cursor.getColumnIndexOrThrow(COL_U_USERNAME));
        byte[] picture = cursor.getBlob(cursor.getColumnIndexOrThrow(COL_U_PICTURE));

        return new User(id, firstName, lastName, email, username, DataConverter.bitmapFromBlob(picture));
    }

    /**
     * ============================================================================================
     *                                  CHALLENGE TABLE METHODS
     * ============================================================================================
     */

    /**
     * @param challenge the Challenge to insert.
     */
    public void insertChallenge(Challenge challenge)
    {
        ContentValues values = new ContentValues();
        challenge.write(values);

        long insertedId = database.insert(TBL_CHALLENGES, null, values);

        Log.d("data", "User " + insertedId + " inserted to database.");

        challenge.id = insertedId;
    }

    /**
     * @param minDate the minimum Date.
     * @param amount the amount of Challenges to return.
     * @return list of given amount of Challenges since minDate.
     */
    public List<Challenge> getChallengesBySubmissionCount(Date minDate, int amount)
    {
        long minDateLong = DataConverter.dateToLong(minDate);

        String query = String.format(Locale.US,
                "SELECT %s.* FROM %s LEFT JOIN %s ON %s.%s = %s.%s AND %s.%s > %d GROUP BY %s.%s ORDER BY COUNT(%s.%s) DESC LIMIT %d",
                TBL_CHALLENGES, TBL_CHALLENGES, TBL_SUBMISSIONS, TBL_CHALLENGES, COL_CH_ID, TBL_SUBMISSIONS, COL_SUB_CHALLENGE_ID, TBL_SUBMISSIONS, COL_SUB_DATE, minDateLong, TBL_CHALLENGES, COL_CH_ID, TBL_SUBMISSIONS, COL_SUB_CHALLENGE_ID, amount
        );

        Cursor cursor = database.rawQuery(query, null);
        return allChallengesFromCursor(cursor);
    }

    /**
     * @param challengeId the ID of the Challenge.
     * @return the amount of submissions for the given challenge.
     */
    public int getSubmissionCountForChallenge(long challengeId)
    {
        String query = String.format(Locale.US,
                "SELECT count(*) FROM %s WHERE %s.%s = %d",
                TBL_SUBMISSIONS, TBL_SUBMISSIONS, COL_SUB_CHALLENGE_ID, challengeId
        );

        Cursor cursor = database.rawQuery(query, null);
        cursor.moveToFirst();
        int count = cursor.getInt(0);
        cursor.close();
        return count;
    }

    /**
     * @param challengeId the ID of the Challenge.
     * @return amount of Challenges deleted.
     */
    public long deleteChallengeById(long challengeId)
    {
        return database.delete(TBL_CHALLENGES, COL_CH_ID + "=" + challengeId, null);
    }

    /**
     * @param selection the selection of Challenges.
     * @return the Challenge matching the given selection.
     */
    public Challenge getChallengeByFilter(String selection)
    {
        Cursor cursor = database.query(TBL_CHALLENGES, COLS_CHALLENGES, selection, null, null, null, null);
        return singleChallengeFromCursor(cursor);
    }

    /**
     * @param amount the amount of Challenges to return.
     * @return list of newest Challenges with given amount.
     */
    public List<Challenge> getNewChallenges(int amount)
    {
        String finalQuery = String.format(Locale.US,
                "SELECT %s.* FROM %s ORDER BY %s.%s DESC LIMIT %d",
                TBL_CHALLENGES, TBL_CHALLENGES, Database.TBL_CHALLENGES, Database.COL_CH_ID, amount
        );

        Cursor cursor = database.rawQuery(finalQuery, null);
        return allChallengesFromCursor(cursor);
    }

    /**
     * @param selection the selection of Challenges.
     * @param OrderBy the order of the Challenges.
     * @return list of Challenges matching given selection and order.
     */
    public ArrayList<Challenge> getAllChallengesByFilter(String selection, String OrderBy)
    {
        Cursor cursor = database.query(TBL_CHALLENGES, COLS_CHALLENGES, selection, null, null, null, OrderBy);
        return allChallengesFromCursor(cursor);
    }

    /**
     * @param challengeId the ID of the Challenge.
     * @return Challenge matching given ID.
     */
    public Challenge getChallengeById(long challengeId)
    {
        Cursor cursor=database.query(TBL_CHALLENGES, COLS_CHALLENGES,COL_CH_ID + "=" + challengeId, null, null, null, null);
        if(!cursor.moveToFirst()) return null;
        return singleChallengeFromCursor(cursor);
    }

    /**
     * @return all Challenges in the DB.
     */
    public ArrayList<Challenge> getAllChallenges()
    {
        Cursor cursor = database.query(TBL_CHALLENGES, COLS_CHALLENGES, null, null, null, null, null);
        return allChallengesFromCursor(cursor);
    }

    /**
     * @param cursor the Cursor to read from.
     * @return return all Challenges from the Cursor.
     */
    private static ArrayList<Challenge> allChallengesFromCursor(Cursor cursor)
    {
        ArrayList<Challenge> list = new ArrayList<>();

        if(cursor.moveToFirst())
        {
            do
            {
                list.add(singleChallengeFromCursor(cursor));
            }
            while(cursor.moveToNext());
        }

        return list;
    }

    /**
     * @param cursor
     * @return the Challenge from the Cursor's current position.
     */
    private static Challenge singleChallengeFromCursor(Cursor cursor)
    {
        int type = cursor.getInt(cursor.getColumnIndexOrThrow(COL_CH_TYPE));

        switch(type)
        {
        case Challenge.TYPE_NORMAL:
            return new NormalChallenge(
                    cursor.getLong(cursor.getColumnIndexOrThrow(COL_CH_ID)), // Challenge ID
                    cursor.getLong(cursor.getColumnIndexOrThrow(COL_CH_AUTHOR_ID)), // Challenge Author ID
                    DataConverter.dateFromLong(cursor.getLong(cursor.getColumnIndexOrThrow(COL_CH_CREATE_DATE))), // Challenge Creation Date
                    cursor.getString(cursor.getColumnIndexOrThrow(COL_CH_TITLE)), // NormalChallenge Title
                    cursor.getString(cursor.getColumnIndexOrThrow(COL_CH_CONTENT)) // NormalChallenge Content
            );

        case Challenge.TYPE_LIMITED:
            return new LimitedChallenge(
                    cursor.getLong(cursor.getColumnIndexOrThrow(COL_CH_ID)), // Challenge ID
                    cursor.getLong(cursor.getColumnIndexOrThrow(COL_CH_AUTHOR_ID)), // Challenge Author ID
                    DataConverter.dateFromLong(cursor.getLong(cursor.getColumnIndexOrThrow(COL_CH_CREATE_DATE))), // Challenge Creation Date
                    cursor.getString(cursor.getColumnIndexOrThrow(COL_CH_TITLE)), // NormalChallenge Title
                    cursor.getString(cursor.getColumnIndexOrThrow(COL_CH_CONTENT)), // NormalChallenge Content
                    DataConverter.midnightCalendarFromLong(cursor.getLong(cursor.getColumnIndexOrThrow(COL_CH_END_DATE))) // LimitedChallenge End Date
            );

        default:
            return null;
        }
    }

    /**
     * ============================================================================================
     *                                  SUBMISSION TABLE METHODS
     * ============================================================================================
     */

    /**
     * @param sub the Submission to insert.
     */
    public void insertSubmission(Submission sub)
    {
        ContentValues values = new ContentValues();
        values.put(COL_SUB_CHALLENGE_ID, sub.challengeId);
        values.put(COL_SUB_AUTHOR_ID, sub.authorId);
        values.put(COL_SUB_CONTENT, sub.content);
        values.put(COL_SUB_LANG, sub.lang);
        values.put(COL_SUB_DATE, DataConverter.dateToLong(sub.date));

        long insertedId = database.insert(TBL_SUBMISSIONS, null, values);

        Log.d("data", "Submission " + insertedId + " inserted to database.");

        sub.id = insertedId;
    }

    /**
     * @param sub the Submission to update.
     * @return the long of the updates Submissions.
     */
    public long updateSubmissionById(Submission sub)
    {
        ContentValues values = new ContentValues();
        values.put(COL_SUB_ID, sub.id);
        values.put(COL_SUB_CHALLENGE_ID, sub.challengeId);
        values.put(COL_SUB_AUTHOR_ID, sub.authorId);
        values.put(COL_SUB_CONTENT, sub.content);
        values.put(COL_SUB_LANG, sub.lang);
        values.put(COL_SUB_DATE, DataConverter.dateToLong(sub.date));

        return database.update(TBL_SUBMISSIONS, values, COL_SUB_ID + "=" + sub.id, null);
    }

    /**
     * @param subId the ID of the Submission.
     * @return the amount of deleted Submissions.
     */
    public long deleteSubmissionById(long subId)
    {
        return database.delete(TBL_SUBMISSIONS, COL_SUB_ID + "=" + subId, null);
    }

    /**
     * @param selection the selection of the Submission.
     * @return the Submission matching the selection.
     */
    public Submission getSubmissionByFilter(String selection)
    {
        Cursor cursor = database.query(TBL_SUBMISSIONS, COLS_SUBMISSIONS, selection, null, null, null, null);
        cursor.moveToFirst();
        return cursor.getCount() > 0 ? singleSubmissionFromCursor(cursor) : null;
    }

    /**
     * @param selection the selection of the Submission.
     * @param OrderBy the order of the Submission.
     * @return a list of Submissions matching the selection and orderBy.
     */
    public ArrayList<Submission> getAllSubmissionsByFilter(String selection, String OrderBy)
    {
        Cursor cursor = database.query(TBL_SUBMISSIONS, COLS_SUBMISSIONS, selection, null, null, null, OrderBy);
        cursor.moveToFirst();
        return allSubmissionsFromCursor(cursor);
    }

    /**
     * @param subId the ID of the Submission.
     * @return the Submission matching the ID.
     */
    public Submission getSubmissionById(long subId)
    {
        Cursor cursor=database.query(TBL_SUBMISSIONS, COLS_SUBMISSIONS, COL_SUB_ID + "=" + subId, null, null, null, null);
        cursor.moveToFirst();
        return cursor.getCount() > 0 ? singleSubmissionFromCursor(cursor) : null;
    }

    /**
     * @return a list with all Submissions from the DB.
     */
    public ArrayList<Submission> getAllSubmissions()
    {
        Cursor cursor = database.query(TBL_SUBMISSIONS, COLS_SUBMISSIONS, null, null, null, null, null);
        cursor.moveToFirst();
        return allSubmissionsFromCursor(cursor);
    }

    /**
     * @param cursor the Cursor.
     * @return all Submissions in the Cursor.
     */
    private static ArrayList<Submission> allSubmissionsFromCursor(Cursor cursor)
    {
        ArrayList<Submission> list = new ArrayList<>();

        if(cursor.moveToFirst())
        {
            do
            {
                list.add(singleSubmissionFromCursor(cursor));
            }
            while(cursor.moveToNext());
        }

        return list;
    }

    /**
     * @param cursor
     * @return the User from the Cursor's current position.
     */
    private static Submission singleSubmissionFromCursor(Cursor cursor)
    {
        long id = cursor.getLong(cursor.getColumnIndexOrThrow(COL_SUB_ID));
        long challengeId = cursor.getLong(cursor.getColumnIndexOrThrow(COL_SUB_CHALLENGE_ID));
        long authorId = cursor.getLong(cursor.getColumnIndexOrThrow(COL_SUB_AUTHOR_ID));
        String content = cursor.getString(cursor.getColumnIndexOrThrow(COL_SUB_CONTENT));
        String lang = cursor.getString(cursor.getColumnIndexOrThrow(COL_SUB_LANG));
        Date date = DataConverter.dateFromLong(cursor.getLong(cursor.getColumnIndexOrThrow(COL_SUB_DATE)));

        return new Submission(id, challengeId, authorId, content, lang, date);
    }

    /**
     * ============================================================================================
     *                                  COMMENT TABLES METHODS
     * ============================================================================================
     */

    /**
     * @param com the Comment to insert.
     * @param table the Table to insert into.
     */
    public void insertComment(Comment com, String table)
    {
        ContentValues values = new ContentValues();
        values.put(COL_COM_AUTHOR_ID, com.authorId);
        values.put(COL_COM_CONTENT, com.content);
        values.put(COL_COM_DATE, DataConverter.dateToLong(com.creationDate));
        values.put(COL_COM_POST, com.postId);

        long insertedId = database.insert(table, null, values);

        Log.d("data", "User " + insertedId + " inserted to database.");

        com.id = insertedId;
    }

    /**
     * @param com the Comment to update.
     * @param table the Table of the Comment.
     * @return the amount of updated Comments.
     */
    public long updateCommentById(Comment com, String table)
    {
        ContentValues values = new ContentValues();
        values.put(COL_COM_ID, com.id);
        values.put(COL_COM_AUTHOR_ID, com.authorId);
        values.put(COL_COM_CONTENT, com.content);
        values.put(COL_COM_DATE, DataConverter.dateToLong(com.creationDate));
        values.put(COL_COM_POST, com.postId);

        return database.update(table, values, COL_COM_ID + "=" + com.id, null);
    }

    /**
     * @param subId the ID of the Submission.
     * @param table the Table to delete from.
     * @return the amount of deleted comments.
     */
    public long deleteCommentById(long subId, String table)
    {
        return database.delete(table, COL_COM_ID + "=" + subId, null);
    }

    /**
     * @param selection the selection of the comment.
     * @param table the table to select from.
     * @return Comment matching selection from table.
     */
    public Comment getCommentByFilter(String selection, String table)
    {
        Cursor cursor = database.query(table, COLS_COMMENTS, selection, null, null, null, null);
        return cursor.getCount() > 0 ? singleCommentFromCursor(cursor) : null;
    }

    /**
     * @param selection the selection of comments.
     * @param orderBy the order of the comments.
     * @param table the table to select from.
     * @return a list of the selected comments.
     */
    public ArrayList<Comment> getAllCommentsByFilter(String selection, String orderBy, String table)
    {
        Cursor cursor = database.query(table, COLS_COMMENTS, selection, null, null, null, orderBy);
        return allCommentsFromCursor(cursor);
    }

    /**
     * @param subId the ID of the Submission.
     * @param table the table of the Submission.
     * @return the Submission.
     */
    public Comment getCommentById(long subId, String table)
    {
        Cursor cursor=database.query(table, COLS_COMMENTS, COL_COM_ID + "=" + subId, null, null, null, null);
        return cursor.getCount() > 0 ? singleCommentFromCursor(cursor) : null;
    }

    /**
     * @param table the table to get from.
     * @return get all comments from table.
     */
    public ArrayList<Comment> getAllComments(String table)
    {
        Cursor cursor = database.query(table, COLS_COMMENTS, null, null, null, null, null);
        return allCommentsFromCursor(cursor);
    }

    /**
     * @param cursor the Cursor.
     * @return all comments from the cursor.
     */
    private static ArrayList<Comment> allCommentsFromCursor(Cursor cursor)
    {
        ArrayList<Comment> list = new ArrayList<>();

        if(cursor.moveToFirst())
        {
            do
            {
                list.add(singleCommentFromCursor(cursor));
            }
            while(cursor.moveToNext());
        }

        return list;
    }

    /**
     * @param cursor
     * @return the User from the Cursor's current position.
     */
    private static Comment singleCommentFromCursor(Cursor cursor)
    {
        long id = cursor.getLong(cursor.getColumnIndexOrThrow(COL_COM_ID));
        long authorId = cursor.getLong(cursor.getColumnIndexOrThrow(COL_COM_AUTHOR_ID));
        String content = cursor.getString(cursor.getColumnIndexOrThrow(COL_COM_CONTENT));
        Date date = DataConverter.dateFromLong(cursor.getLong(cursor.getColumnIndexOrThrow(COL_COM_DATE)));
        long parent = cursor.getLong(cursor.getColumnIndexOrThrow(COL_COM_POST));

        return new Comment(id, authorId, content, date, parent);
    }

    /**
     * ============================================================================================
     *                                  RATING TABLES METHODS
     * ============================================================================================
     */

    /**
     * @param authorId the author of the rating.
     * @param postId the post that was rated.
     * @return return a rating matching the parameters.
     */
    private static String selectRating(long authorId, long postId)
    {
        return COL_RATING_POST_ID + "=" + postId + " AND " + COL_RATING_AUTHOR_ID + "=" + authorId;
    }

    /**
     * @param rating the Rating to insert.
     */
    public void insertChallengeRating(Rating rating)
    {
        insertRating(rating, TBL_CH_RATINGS);
    }

    /**
     * @param rating the rating to insert.
     */
    public void insertSubmissionRating(Rating rating)
    {
        insertRating(rating, TBL_SUB_RATINGS);
    }

    /**
     * @param rating the rating to insert.
     * @param table the table to insert into.
     */
    private void insertRating(Rating rating, String table)
    {
        ContentValues values = new ContentValues();
        values.put(COL_RATING_AUTHOR_ID, rating.authorId);
        values.put(COL_RATING_POST_ID, rating.postId);
        values.put(COL_RATING_RATING, rating.rating);

        long insertedId = database.insert(table, null, values);

        Log.d("data", "Rating " + insertedId + " inserted to database.");
    }

    /**
     * @param rating the rating to update.
     * @return the amount of ratings updated.
     */
    public long updateChallengeRating(Rating rating)
    {
        return updateRatingByFilter(rating, selectRating(rating.authorId, rating.postId), TBL_CH_RATINGS);
    }

    /**
     * @param rating the rating to update.
     * @return the amount of updated ratings.
     */
    public long updateSubmissionRating(Rating rating)
    {
        return updateRatingByFilter(rating, selectRating(rating.authorId, rating.postId), TBL_SUB_RATINGS);
    }

    /**
     * @param rating the rating to update.
     * @param filter the filter of the rating.
     * @param table the table of the rating.
     * @return the amount of updated ratings.
     */
    private long updateRatingByFilter(Rating rating, String filter, String table)
    {
        ContentValues values = new ContentValues();
        values.put(COL_RATING_AUTHOR_ID, rating.authorId);
        values.put(COL_RATING_POST_ID, rating.postId);
        values.put(COL_RATING_RATING, rating.rating);

        return database.update(table, values, filter, null);
    }

    /**
     * @param authorId the author of the rating.
     * @param postId the post that was rated.
     * @param table the table of the rating.
     * @return return the rating matching the parameters.
     */
    public Rating getRating(long authorId, long postId, String table)
    {
        return getRatingByFilter(selectRating(authorId, postId), table);
    }

    /**
     * @param selection the selection of the rating.
     * @param table the table of the rating.
     * @return rating matching the parameters.
     */
    private Rating getRatingByFilter(String selection, String table)
    {
        Cursor cursor = database.query(table, COLS_RATINGS, selection, null, null, null, null);
        return cursor.moveToFirst() ? singleRatingFromCursor(cursor) : null;
    }

    /**
     * @param post the post
     * @return average rating of the post
     */
    public float getAvgPostRating(Post post)
    {
        String query = String.format(Locale.US,
                "SELECT avg(%s.%s) FROM %s WHERE %s.%s = %d",
                post.TBL_RATINGS, COL_RATING_RATING, post.TBL_RATINGS, post.TBL_RATINGS, COL_RATING_POST_ID, post.id);

        Cursor cursor = database.rawQuery(query, null);
        cursor.moveToFirst();
        Debug.print(cursor.toString());
        float avg = cursor.getFloat(0);
        cursor.close();
        return avg;
    }

    /**
     * @param challengeId the ID of the challenge
     * @return all ratings of the challenge
     */
    public List<Rating> getAllRatingsForChallenge(long challengeId)
    {
        return getAllRatingsByFilter(COL_RATING_POST_ID + "=" + challengeId, COL_RATING_AUTHOR_ID + " DESC", TBL_CH_RATINGS);
    }

    /**
     * @param submissionId the ID of the submission.
     * @return all ratings of the submission.
     */
    public List<Rating> getAllRatingsForSubmission(long submissionId)
    {
        return getAllRatingsByFilter(COL_RATING_POST_ID + "=" + submissionId, COL_RATING_AUTHOR_ID + " DESC", TBL_SUB_RATINGS);
    }

    /**
     * @param selection the selection.
     * @param orderBy the order.
     * @param table the table of the rating.
     * @return all ratings matching parameters.
     */
    private List<Rating> getAllRatingsByFilter(String selection, String orderBy, String table)
    {
        Cursor cursor = database.query(table, COLS_RATINGS, selection, null, null, null, orderBy);
        return allRatingsFromCursor(cursor);
    }

    /**
     * @param table the table
     * @return all ratings from table
     */
    private ArrayList<Rating> getAllRatings(String table)
    {
        Cursor cursor = database.query(table, COLS_RATINGS, null, null, null, null, null);
        return allRatingsFromCursor(cursor);
    }

    /**
     * @param cursor the cursor
     * @return all ratings from the cursor
     */
    private static ArrayList<Rating> allRatingsFromCursor(Cursor cursor)
    {
        ArrayList<Rating> list = new ArrayList<>();

        if(cursor.moveToFirst())
        {
            do
            {
                list.add(singleRatingFromCursor(cursor));
            }
            while(cursor.moveToNext());
        }

        return list;
    }

    /**
     * @param cursor
     * @return the User from the Cursor's current position.
     */
    private static Rating singleRatingFromCursor(Cursor cursor)
    {
        long authorId = cursor.getLong(cursor.getColumnIndexOrThrow(COL_RATING_AUTHOR_ID));
        long postId = cursor.getLong(cursor.getColumnIndexOrThrow(COL_RATING_POST_ID));
        float rating = cursor.getFloat(cursor.getColumnIndexOrThrow(COL_RATING_RATING));

        return new Rating(authorId, postId, rating);
    }
}
