package com.example.gridview;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DbHelper extends SQLiteOpenHelper {

	  public static final String TABLE_COMMENTS = "comments";
	  public static final String COLUMN_ID = "_id";
	  public static final String COLUMN_TITLE = "title";
	  public static final String COLUMN_COMMENT = "comment";
	  public static final String COLUMN_UPDATED_DATE= "updatedat";

	  private static final String DATABASE_NAME = "commments.db";
	  private static final int DATABASE_VERSION = 1;

	  // Database creation sql statement
	  private static final String DATABASE_CREATE = "create table "
	      + TABLE_COMMENTS + "(" + COLUMN_ID
	      + " integer primary key autoincrement, "
	      + COLUMN_TITLE
	      + " date,"
	      + COLUMN_UPDATED_DATE + " text not null,"
	      + COLUMN_COMMENT
	      + " text not null);";

	  public DbHelper(Context context) {
	    super(context, DATABASE_NAME, null, DATABASE_VERSION);
	  }

	  @Override
	  public void onCreate(SQLiteDatabase database) {
	    database.execSQL(DATABASE_CREATE);
	  }

	  @Override
	  public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	    db.execSQL("DROP TABLE IF EXISTS " + TABLE_COMMENTS);
	    onCreate(db);
	  }

	} 