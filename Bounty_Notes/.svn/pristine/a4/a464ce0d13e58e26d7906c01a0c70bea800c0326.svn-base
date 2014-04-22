package com.example.gridview;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.example.gridview.dao.Comment;


public class CommentsManager {

	// Database fields
	private SQLiteDatabase database;
	private DbHelper dbHelper;
	private String[] allColumns = {
			DbHelper.COLUMN_ID,
			DbHelper.COLUMN_TITLE,
			DbHelper.COLUMN_COMMENT,
			DbHelper.COLUMN_UPDATED_DATE};

	public CommentsManager(Context context) {
		dbHelper = new DbHelper(context);
	}

	public void open() throws SQLException {
		database = dbHelper.getWritableDatabase();
	}

	public void close() {
		dbHelper.close();
	}

	public Comment createComment(Comment comment) {
		if(database == null){
			open();
		}
		ContentValues values = new ContentValues();
		values.put(DbHelper.COLUMN_TITLE, comment.title);
		values.put(DbHelper.COLUMN_COMMENT, comment.comment);
		values.put(DbHelper.COLUMN_UPDATED_DATE, comment.updatedAt);
		long insertId = database.insert(DbHelper.TABLE_COMMENTS, null,
				values);
		comment.id = (int)insertId;
		return comment;
	}
	
	public Comment updateComment(Comment comment) {
		if(database == null){
			open();
		}
		ContentValues values = new ContentValues();
		values.put(DbHelper.COLUMN_TITLE, comment.title);
		values.put(DbHelper.COLUMN_COMMENT, comment.comment);
		values.put(DbHelper.COLUMN_UPDATED_DATE, comment.updatedAt);
		database.update(DbHelper.TABLE_COMMENTS, values, DbHelper.COLUMN_ID + "=" + comment.id,null);
		return comment;
	}

	public void deleteComment(Comment comment) {
		long id = comment.id;
		System.out.println("Comment deleted with id: " + id);
		database.delete(DbHelper.TABLE_COMMENTS, DbHelper.COLUMN_ID
				+ " = " + id, null);
	}

	public int countRecords(){
		int numOfRecords = 0;
		Cursor cursor = database.query(DbHelper.TABLE_COMMENTS,
				allColumns, null, null, null, null, null);
		numOfRecords = cursor.getCount();
		cursor.close();

		return numOfRecords;
	}


	public List<Comment> getAllComments() {
		List<Comment> comments = new ArrayList<Comment>();

		Cursor cursor = database.query(DbHelper.TABLE_COMMENTS,
				allColumns, null, null, null, null, DbHelper.COLUMN_UPDATED_DATE + " DESC");

		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			Comment comment = cursorToComment(cursor);
			comments.add(comment);
			cursor.moveToNext();
		}
		// make sure to close the cursor
		cursor.close();
		return comments;
	}

	private Comment cursorToComment(Cursor cursor) {
		Comment comment = new Comment();
		comment.id = cursor.getLong(0);
		comment.title = cursor.getString(1);
		comment.comment = cursor.getString(2);
		comment.updatedAt = cursor.getString(3);
		return comment;
	}

	
} 