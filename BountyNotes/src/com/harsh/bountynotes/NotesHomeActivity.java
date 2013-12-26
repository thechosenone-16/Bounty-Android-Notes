package com.harsh.bountynotes;

import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class NotesHomeActivity extends Activity {

	FrameLayout mNoteCreateLayout;
	EditText mQuickNoteEditText;
	EditText mNotetext;
	EditText mNoteTitle;
	ImageButton quickNoteDone, mFrameLayoutButton, mSetDateTime;
	TextView mDateTimedisplaytext;
	String notetext;
	Button doneinframelayout;
	DatabaseHandler db;
	List<NoteItem> notesList;
	
	NoteDisplayScrollView mNoteDisplayScrollView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.notes_main_page);
		db = new DatabaseHandler(getApplicationContext());
		initializeViews();
		mNoteCreateLayout.setVisibility(View.GONE);
		notesList = db.getAllNotes();
		
		mNoteDisplayScrollView.addNotesInScreen(notesList);
		quickNoteDone.setOnClickListener(new View.OnClickListener() {
			NoteItem item;

			@Override
			public void onClick(View v) {
				notetext = mQuickNoteEditText.getText().toString();
				if (!notetext.isEmpty()
						&& !notetext.equalsIgnoreCase("Add New Note Here")) {
					mQuickNoteEditText.setHint("Add New Note Here");
					mQuickNoteEditText.setText("");
					item = new NoteItem(notetext, "0", getDateTime());
					Log.d("Harsh", db.insertNote(item) + "");
					mNoteDisplayScrollView.addSingleNote(item);

				}
			}
		});

		mFrameLayoutButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				mNoteCreateLayout.setVisibility(View.VISIBLE);

			}
		});

		doneinframelayout.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (mNotetext.getText().toString().equalsIgnoreCase("")
						|| mNotetext.getText().toString() == null) {
					Toast.makeText(getApplicationContext(),
							"Enter Some Note Text", Toast.LENGTH_SHORT).show();
				} else {

					mNoteCreateLayout.setVisibility(View.GONE);
					NoteItem newNote = new NoteItem();
					newNote.setNoteCreatedTime(getDateTime());
					newNote.setNoteText(mNotetext.getText().toString());
					newNote.setNoteTitle(mNoteTitle.getText().toString());
					mNotetext.setText("");
					mNoteTitle.setText("");
					Log.d("Harsh", db.insertNote(newNote) + "");
					mNoteDisplayScrollView.addSingleNote(newNote);
				}
			}
		});

	}

	private void initializeViews() {
		mNoteCreateLayout = (FrameLayout) findViewById(R.id.addnoteframelayout);
		mQuickNoteEditText = (EditText) findViewById(R.id.quicknoteedittext);
		mNotetext = (EditText) findViewById(R.id.currentnotetext);
		mNoteTitle = (EditText) findViewById(R.id.currentnotetitle);
		mFrameLayoutButton = (ImageButton) findViewById(R.id.framelayoutbutton);
		mSetDateTime = (ImageButton) findViewById(R.id.datetimepickerbutton);
		mDateTimedisplaytext = (TextView) findViewById(R.id.datetimedisplay);
		quickNoteDone = (ImageButton) findViewById(R.id.quicknotedone);
		doneinframelayout = (Button) findViewById(R.id.doneinframelayout);
		mNoteDisplayScrollView = (NoteDisplayScrollView) findViewById(R.id.notedisplayscrollview);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		getMenuInflater().inflate(R.menu.notes_home, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_delete:
			db.emptyDatabase();
			notesList = db.getAllNotes();
			mNoteDisplayScrollView.addNotesInScreen(notesList);
			return true;

		default:
			return super.onOptionsItemSelected(item);
		}
	}

	private String getDateTime() {

		return System.currentTimeMillis() + "";

	}
}
