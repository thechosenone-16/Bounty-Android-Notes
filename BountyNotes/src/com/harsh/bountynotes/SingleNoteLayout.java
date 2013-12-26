package com.harsh.bountynotes;

import java.util.Random;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.location.GpsStatus.Listener;
import android.text.format.DateUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

public class SingleNoteLayout extends LinearLayout {
	Context mContext;
	Listener mListener;
	
	static int VIEW_TAG = 1;
	private int color[] = { Color.GRAY, Color.MAGENTA, Color.CYAN, Color.GREEN,
			Color.YELLOW };

	public SingleNoteLayout(Context context) {
		super(context);
		mContext = context;
		
	}

	public SingleNoteLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
	}

	public void setupNoteView(NoteItem noteitem) {
		LayoutInflater inflater = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.singlenote_layout, null);
		TextView noteText = (TextView) view.findViewById(R.id.noteText);
		TextView noteTime = (TextView) view.findViewById(R.id.noteTime);
		TextView noteTitle = (TextView) view.findViewById(R.id.noteTitle);
		LinearLayout noteTitleLayout = (LinearLayout) view
				.findViewById(R.id.noteTitlelayout);
		noteTitle.setPaintFlags(noteTitle.getPaintFlags()
				| Paint.UNDERLINE_TEXT_FLAG);
		noteTitleLayout.setVisibility(View.GONE);
		noteText.setText(noteitem.getNoteText());
		noteText.setTextColor(Color.BLACK);
		if (noteitem.getNoteText().length() < 12) {
			noteText.setTextSize(30);
		} else {
			noteText.setTextSize(12);
		}

		noteTime.setText(DateUtils.formatDateTime(mContext,
				Long.parseLong(noteitem.getNoteCreatedTime()),
				DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_SHOW_TIME));

		noteTime.setTextColor(Color.BLACK);
		if (noteitem.getNoteTitle() != null) {
			if (noteitem.getNoteTitle().equalsIgnoreCase("")) {
				noteTitle.setVisibility(View.GONE);
				noteTitleLayout.setVisibility(View.GONE);
			} else {
				noteTitle.setVisibility(View.VISIBLE);
				noteTitleLayout.setVisibility(View.VISIBLE);
				noteTitle.setText(noteitem.getNoteTitle());
				noteTitle.setTextColor(Color.BLACK);
			}
		} else {
			noteTitle.setVisibility(View.GONE);
			noteTitleLayout.setVisibility(View.GONE);
		}
		view.setBackgroundColor(color[new Random().nextInt(5)]);
		addView(view);
		view.setTag(VIEW_TAG);
	}


}
