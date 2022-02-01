package com.example.coderanknew.sql;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.coderanknew.challenge.Challenge;
import com.example.coderanknew.user.User;
import com.example.coderanknew.challenge.NormalChallenge;
import com.example.coderanknew.comment.Comment;
import com.example.coderanknew.submission.Submission;

import java.util.ArrayList;
import java.util.Date;

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

    public static final String[] COLS_USERS = {COL_U_ID, COL_U_FIRSTNAME, COL_U_LASTNAME, COL_U_EMAIL, COL_U_USERNAME};

    private static final String CREATE_TBL_USERS =
            "CREATE TABLE IF NOT EXISTS " + TBL_USERS + "(" +
                    COL_U_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    COL_U_FIRSTNAME + " VARCHAR," +
                    COL_U_LASTNAME + " VARCHAR," +
                    COL_U_EMAIL + " VARCHAR," +
                    COL_U_USERNAME + " VARCHAR," +
                    COL_U_PASSWORD_HASH + " VARCHAR)";

    /**
     * ============================================================================================
     *                                  CHALLENGE TABLE PROPERTIES
     * ============================================================================================
     */

    private static final String TBL_CHALLENGES = "tblChallenge";

    public static final String COL_CH_ID = "chId";
    public static final String COL_CH_AUTHOR = "chAuthor";
    public static final String COL_CH_TITLE = "chTitle";
    public static final String COL_CH_CONTENT = "chContent";
    public static final String COL_CH_DATE = "chCreationDate";

    public static final String[] COLS_CHALLENGES = {COL_CH_ID, COL_CH_AUTHOR, COL_CH_TITLE, COL_CH_CONTENT, COL_CH_DATE};

    private static final String CREATE_TBL_CHALLENGES =
            "CREATE TABLE IF NOT EXISTS " + TBL_CHALLENGES + "(" +
                    COL_CH_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    COL_CH_AUTHOR + " INTEGER," +
                    COL_CH_TITLE + " VARCHAR," +
                    COL_CH_CONTENT + " VARCHAR," +
                    COL_CH_DATE + " VARCHAR)";

    /**
     * ============================================================================================
     *                                  SUBMISSION TABLE PROPERTIES
     * ============================================================================================
     */

    private static final String TBL_SUBMISSIONS = "tblSubmissions";

    public static final String COL_SUB_ID = "subId";
    public static final String COL_SUB_AUTHOR_ID = "subAuthorId";
    public static final String COL_SUB_CHALLENGE_ID = "subChallengeId";
    public static final String COL_SUB_CONTENT = "subContent";
    public static final String COL_SUB_LANG = "subLang";

    public static final String[] COLS_SUBMISSIONS = {COL_SUB_ID, COL_SUB_AUTHOR_ID, COL_SUB_CHALLENGE_ID, COL_SUB_CONTENT, COL_SUB_LANG};

    private static final String CREATE_TBL_SUBMISSIONS =
            "CREATE TABLE IF NOT EXISTS " + TBL_SUBMISSIONS + "(" +
                    COL_SUB_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    COL_SUB_AUTHOR_ID + " INTEGER," +
                    COL_SUB_CHALLENGE_ID + " INTEGER," +
                    COL_SUB_CONTENT + " VARCHAR," +
                    COL_SUB_LANG + " INTEGER)";

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
    public static final String COL_COM_PARENT = "comParent";

    public static final String[] COLS_COMMENTS = {COL_COM_ID, COL_COM_AUTHOR_ID, COL_COM_CONTENT, COL_COM_DATE, COL_COM_PARENT};

    private static final String CREATE_TBL_CH_COMMENTS = "CREATE TABLE IF NOT EXISTS " + TBL_CH_COMMENTS + "(" +
            COL_COM_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            COL_COM_AUTHOR_ID + " INTEGER," +
            COL_COM_CONTENT + " VARCHAR," +
            COL_COM_DATE + " VARCHAR," +
            COL_COM_PARENT + " INTEGER)";

    private static final String CREATE_TBL_SUB_COMMENTS = "CREATE TABLE IF NOT EXISTS " + TBL_SUB_COMMENTS + "(" +
            COL_COM_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            COL_COM_AUTHOR_ID + " INTEGER," +
            COL_COM_CONTENT + " VARCHAR," +
            COL_COM_DATE + " VARCHAR," +
            COL_COM_PARENT + " INTEGER)";

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
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1)
    {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TBL_USERS);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TBL_CHALLENGES);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TBL_SUBMISSIONS);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TBL_CH_COMMENTS);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TBL_SUB_COMMENTS);

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
        values.put(COL_U_PICTURE, SQLConverter.bitmapToBlob(user.picture));

        long insertedId = database.insert(TBL_USERS, null, values);

        user.id = insertedId;

        return user;
    }

    public long updateUserById(User user)
    {
        ContentValues values = new ContentValues();
        values.put(COL_U_ID, user.id);
        values.put(COL_U_FIRSTNAME, user.firstName);
        values.put(COL_U_LASTNAME, user.lastName);
        values.put(COL_U_EMAIL, user.email);
        values.put(COL_U_USERNAME, user.username);
        values.put(COL_U_PICTURE, SQLConverter.bitmapToBlob(user.picture));

        return database.update(TBL_USERS, values, COL_U_ID + "=" + user.id, null);
    }

    public long deleteUserById(long userId)
    {
        return database.delete(TBL_USERS, COL_U_ID + "=" + userId, null);
    }

    public User getUserByFilter(String selection)
    {
        Cursor cursor = database.query(TBL_USERS, COLS_USERS, selection, null, null, null, null);
        if(!cursor.moveToFirst()) return null;
        return singleUserFromCursor(cursor);
    }

    public ArrayList<User> getAllUsersByFilter(String selection, String OrderBy)
    {
        Cursor cursor = database.query(TBL_USERS, COLS_USERS, selection, null, null, null, OrderBy);
        return allUsersFromCursor(cursor);
    }

    public User getUserById(long userId)
    {
        Cursor cursor=database.query(TBL_USERS, COLS_USERS, COL_U_ID + "=" + userId, null, null, null, null);
        if(!cursor.moveToFirst()) return null;
        return singleUserFromCursor(cursor);
    }

    public ArrayList<User> getAllUsers()
    {
        Cursor cursor = database.query(TBL_USERS, COLS_USERS, null, null, null, null, null);
        return allUsersFromCursor(cursor);
    }

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

        return new User(id, firstName, lastName, email, username, SQLConverter.bitmapFromBlob(picture));
    }

    /**
     * ============================================================================================
     *                                  CHALLENGE TABLE METHODS
     * ============================================================================================
     */

    public void insertChallenge(NormalChallenge challenge)
    {
        ContentValues values = new ContentValues();
        values.put(COL_CH_AUTHOR, challenge.authorId);
        values.put(COL_CH_TITLE, challenge.title);
        values.put(COL_CH_CONTENT, challenge.content);
        values.put(COL_CH_DATE, SQLConverter.dateToString(challenge.creationDate));

        long insertedId = database.insert(TBL_CHALLENGES, null, values);

        Log.d("data", "User " + insertedId + " inserted to database.");

        challenge.id = insertedId;
    }

    public long updateChallengeById(NormalChallenge challenge)
    {
        ContentValues values = new ContentValues();
        values.put(COL_CH_ID, challenge.id);
        values.put(COL_CH_AUTHOR, challenge.authorId);
        values.put(COL_CH_TITLE, challenge.title);
        values.put(COL_CH_CONTENT, challenge.content);
        values.put(COL_CH_DATE, SQLConverter.dateToString(challenge.creationDate));

        return database.update(TBL_CHALLENGES, values, COL_CH_ID + "=" + challenge.id, null);
    }

    public long deleteChallengeById(long challengeId)
    {
        return database.delete(TBL_CHALLENGES, COL_CH_ID + "=" + challengeId, null);
    }

    public Challenge getChallengeByFilter(String selection)
    {
        Cursor cursor = database.query(TBL_CHALLENGES, COLS_CHALLENGES, selection, null, null, null, null);
        return singleChallengeFromCursor(cursor);
    }

    public ArrayList<Challenge> getAllChallengesByFilter(String selection, String OrderBy)
    {
        Cursor cursor = database.query(TBL_CHALLENGES, COLS_CHALLENGES, selection, null, null, null, OrderBy);
        return allChallengesFromCursor(cursor);
    }

    public Challenge getChallengeById(long challengeId)
    {
        Cursor cursor=database.query(TBL_CHALLENGES, COLS_CHALLENGES,COL_CH_ID + "=" + challengeId, null, null, null, null);
        if(!cursor.moveToFirst()) return null;
        return singleChallengeFromCursor(cursor);
    }

    public ArrayList<Challenge> getAllChallenges()
    {
        Cursor cursor = database.query(TBL_CHALLENGES, COLS_CHALLENGES, null, null, null, null, null);
        return allChallengesFromCursor(cursor);
    }

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
        long id = cursor.getLong(cursor.getColumnIndexOrThrow(COL_CH_ID));
        long authorId = cursor.getLong(cursor.getColumnIndexOrThrow(COL_CH_AUTHOR));
        String title = cursor.getString(cursor.getColumnIndexOrThrow(COL_CH_TITLE));
        String content = cursor.getString(cursor.getColumnIndexOrThrow(COL_CH_CONTENT));
        Date date = SQLConverter.dateFromString(cursor.getString(cursor.getColumnIndexOrThrow(COL_CH_DATE)));

        return new NormalChallenge(id, authorId, date, title, content);
    }

    /**
     * ============================================================================================
     *                                  SUBMISSION TABLE METHODS
     * ============================================================================================
     */

    public void insertSubmission(Submission sub)
    {
        ContentValues values = new ContentValues();
        values.put(COL_SUB_CHALLENGE_ID, sub.challengeId);
        values.put(COL_SUB_AUTHOR_ID, sub.authorId);
        values.put(COL_SUB_CONTENT, sub.content);
        values.put(COL_SUB_LANG, sub.lang);

        long insertedId = database.insert(TBL_SUBMISSIONS, null, values);

        Log.d("data", "Submission " + insertedId + " inserted to database.");

        sub.id = insertedId;
    }

    public long updateSubmissionById(Submission sub)
    {
        ContentValues values = new ContentValues();
        values.put(COL_SUB_ID, sub.id);
        values.put(COL_SUB_CHALLENGE_ID, sub.challengeId);
        values.put(COL_SUB_AUTHOR_ID, sub.authorId);
        values.put(COL_SUB_CONTENT, sub.content);
        values.put(COL_SUB_LANG, sub.lang);

        return database.update(TBL_SUBMISSIONS, values, COL_SUB_ID + "=" + sub.id, null);
    }

    public long deleteSubmissionById(long subId)
    {
        return database.delete(TBL_SUBMISSIONS, COL_SUB_ID + "=" + subId, null);
    }

    public Submission getSubmissionByFilter(String selection)
    {
        Cursor cursor = database.query(TBL_SUBMISSIONS, COLS_SUBMISSIONS, selection, null, null, null, null);
        cursor.moveToFirst();
        return cursor.getCount() > 0 ? singleSubmissionFromCursor(cursor) : null;
    }

    public ArrayList<Submission> getAllSubmissionsByFilter(String selection, String OrderBy)
    {
        Cursor cursor = database.query(TBL_SUBMISSIONS, COLS_SUBMISSIONS, selection, null, null, null, OrderBy);
        cursor.moveToFirst();
        return allSubmissionsFromCursor(cursor);
    }

    public Submission getSubmissionById(long subId)
    {
        Cursor cursor=database.query(TBL_SUBMISSIONS, COLS_SUBMISSIONS, COL_SUB_ID + "=" + subId, null, null, null, null);
        cursor.moveToFirst();
        return cursor.getCount() > 0 ? singleSubmissionFromCursor(cursor) : null;
    }

    public ArrayList<Submission> getAllSubmissions()
    {
        Cursor cursor = database.query(TBL_SUBMISSIONS, COLS_SUBMISSIONS, null, null, null, null, null);
        cursor.moveToFirst();
        return allSubmissionsFromCursor(cursor);
    }

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

        return new Submission(id, challengeId, authorId, content, lang);
    }

    /**
     * ============================================================================================
     *                                  COMMENT TABLES METHODS
     * ============================================================================================
     */

    public void insertComment(Comment com, String table)
    {
        ContentValues values = new ContentValues();
        values.put(COL_COM_AUTHOR_ID, com.authorId);
        values.put(COL_COM_CONTENT, com.content);
        values.put(COL_COM_DATE, SQLConverter.dateToString(com.creationDate));
        values.put(COL_COM_PARENT, com.parent);

        long insertedId = database.insert(table, null, values);

        Log.d("data", "User " + insertedId + " inserted to database.");

        com.id = insertedId;
    }

    public long updateCommentById(Comment com, String table)
    {
        ContentValues values = new ContentValues();
        values.put(COL_COM_ID, com.id);
        values.put(COL_COM_AUTHOR_ID, com.authorId);
        values.put(COL_COM_CONTENT, com.content);
        values.put(COL_COM_DATE, SQLConverter.dateToString(com.creationDate));
        values.put(COL_COM_PARENT, com.parent);

        return database.update(table, values, COL_COM_ID + "=" + com.id, null);
    }

    public long deleteCommentById(long subId, String table)
    {
        return database.delete(table, COL_COM_ID + "=" + subId, null);
    }

    public Comment getCommentByFilter(String selection, String table)
    {
        Cursor cursor = database.query(table, COLS_COMMENTS, selection, null, null, null, null);
        return cursor.getCount() > 0 ? singleCommentFromCursor(cursor) : null;
    }

    public ArrayList<Comment> getAllCommentsByFilter(String selection, String orderBy, String table)
    {
        Cursor cursor = database.query(table, COLS_COMMENTS, selection, null, null, null, orderBy);
        return allCommentsFromCursor(cursor);
    }

    public Comment getCommentById(long subId, String table)
    {
        Cursor cursor=database.query(table, COLS_COMMENTS, COL_COM_ID + "=" + subId, null, null, null, null);
        return cursor.getCount() > 0 ? singleCommentFromCursor(cursor) : null;
    }

    public ArrayList<Comment> getAllComments(String table)
    {
        Cursor cursor = database.query(table, COLS_COMMENTS, null, null, null, null, null);
        return allCommentsFromCursor(cursor);
    }

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
        Date date = SQLConverter.dateFromString(cursor.getString(cursor.getColumnIndexOrThrow(COL_COM_DATE)));
        long parent = cursor.getLong(cursor.getColumnIndexOrThrow(COL_COM_PARENT));

        return new Comment(id, authorId, content, date, parent);
    }
}
