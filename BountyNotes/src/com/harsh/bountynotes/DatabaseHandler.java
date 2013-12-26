package com.harsh.bountynotes;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHandler extends SQLiteOpenHelper {

	private static final int DATABASE_VERSION = 1;

	private static final String DATABASE_NAME = "notesDatabase";

	private static final String TABLE_NOTES = "notes";

	private static final String KEY_ID = "id";

	private static final String KEY_TIME_CREATED = "time";

	private static final String KEY_NOTE_TEXT = "notetext";

	private static final String KEY_NOTE_TITLE = "title";

	private static final String KEY_CREATED_AT = "createdtime";

	private static final String CREATE_NOTES_TABLE = "CREATE TABLE "
			+ TABLE_NOTES + "(" + KEY_ID + " INTEGER PRIMARY KEY,"
			+ KEY_NOTE_TEXT + " TEXT," + KEY_NOTE_TITLE + " TEXT,"
			+ KEY_TIME_CREATED + " INTEGER," + KEY_CREATED_AT
			+ " TIMESTAMP DEFAULT CURRENT_TIMESTAMP" + ")";

	Context mContext;

	// "INSERT INTO test (t) values (strftime("%s", CURRENT_TIME));"
	public DatabaseHandler(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(CREATE_NOTES_TABLE);

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_NOTES);
		onCreate(db);
	}

	public long insertNote(NoteItem noteitem) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_TIME_CREATED, noteitem.getNoteSetTime());
		values.put(KEY_NOTE_TEXT, noteitem.getNoteText());
		values.put(KEY_CREATED_AT, noteitem.getNoteCreatedTime());
		values.put(KEY_NOTE_TITLE, noteitem.getNoteTitle());

		long id = db.insert(TABLE_NOTES, null, values);

		return id;
	}

	public NoteItem getNoteItem(String notetext) {
		SQLiteDatabase db = this.getReadableDatabase();

		String selectQuery = "SELECT  * FROM " + TABLE_NOTES + " WHERE "
				+ KEY_NOTE_TEXT + " = " + notetext;

		Cursor c = db.rawQuery(selectQuery, null);

		if (c != null)
			c.moveToFirst();

		NoteItem nm = new NoteItem();
		nm.setNoteText(c.getString(c.getColumnIndexOrThrow(KEY_NOTE_TEXT)));
		nm.setNoteSetTime(c.getString(c.getColumnIndexOrThrow(KEY_TIME_CREATED)));
		nm.setNoteCreatedTime(c.getString(c
				.getColumnIndexOrThrow(KEY_CREATED_AT)));
		nm.setNoteTitle(c.getString(c.getColumnIndexOrThrow(KEY_NOTE_TITLE)));

		return nm;
	}

	public List<NoteItem> getAllNotes() {
		List<NoteItem> notelist = new ArrayList<NoteItem>();
		String selectQuery = "SELECT  * FROM " + TABLE_NOTES + " ORDER BY "
				+ KEY_ID + " DESC";
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor c = db.rawQuery(selectQuery, null);

		if (c.moveToFirst()) {
			do {
				NoteItem nm = new NoteItem();
				nm.setNoteText(c.getString(c
						.getColumnIndexOrThrow(KEY_NOTE_TEXT)));
				nm.setNoteSetTime(c.getString(c
						.getColumnIndexOrThrow(KEY_TIME_CREATED)));
				nm.setNoteCreatedTime(c.getString(c
						.getColumnIndexOrThrow(KEY_CREATED_AT)));
				nm.setNoteTitle(c.getString(c
						.getColumnIndexOrThrow(KEY_NOTE_TITLE)));

				notelist.add(nm);
			} while (c.moveToNext());
		}

		return notelist;
	}

	public void emptyDatabase() {
		SQLiteDatabase db = this.getReadableDatabase();
		db.execSQL("DELETE FROM " + TABLE_NOTES);
	}

	public void closeDB() {
		SQLiteDatabase db = this.getReadableDatabase();
		if (db != null && db.isOpen())
			db.close();
	}
}
