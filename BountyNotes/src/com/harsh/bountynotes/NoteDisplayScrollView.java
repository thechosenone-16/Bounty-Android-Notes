package com.harsh.bountynotes;

import java.util.Iterator;
import java.util.List;

import android.content.Context;
import android.os.AsyncTask;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.ScrollView;

public class NoteDisplayScrollView extends ScrollView {

	LinearLayout mMainLayout;
	LinearLayout mLeftPanel;
	LinearLayout mRightPanel;
	Context mContext;
	Animation animation, animation1, animation2;

	public NoteDisplayScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
		mMainLayout = new LinearLayout(context);
		mRightPanel = new LinearLayout(mContext);
		mLeftPanel = new LinearLayout(mContext);
		mMainLayout.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.WRAP_CONTENT));
		mMainLayout.setWeightSum(2);
		mMainLayout.setOrientation(LinearLayout.HORIZONTAL);
		mRightPanel.setWeightSum(1);
		mRightPanel.setOrientation(LinearLayout.VERTICAL);
		LinearLayout.LayoutParams mRightPanelParams = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		mRightPanelParams.weight = 1;
		mRightPanel.setLayoutParams(mRightPanelParams);
		LinearLayout.LayoutParams mLeftPanelParams = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		mLeftPanelParams.weight = 1;
		mLeftPanel.setLayoutParams(mLeftPanelParams);
		mLeftPanel.setOrientation(LinearLayout.VERTICAL);
		addView(mMainLayout);
		mMainLayout.addView(mLeftPanel);
		mMainLayout.addView(mRightPanel);
		animation = AnimationUtils
				.loadAnimation(mContext, R.anim.slide_in_left);
		animation.setStartOffset(0);
		animation.setDuration(500);
		animation1 = AnimationUtils
				.loadAnimation(mContext, R.anim.slide_in_top);
		animation1.setStartOffset(0);
		animation1.setDuration(500);
		animation2 = AnimationUtils.loadAnimation(mContext,
				R.anim.slide_in_right);
		animation2.setStartOffset(0);
		animation2.setDuration(500);
	}

	public NoteDisplayScrollView(Context context) {
		this(context, null);
	}

	public void addSingleNote(NoteItem item) {
		SingleNoteLayout noteLayout = new SingleNoteLayout(mContext);
		noteLayout.setGravity(Gravity.CENTER);
		noteLayout.setupNoteView(item);
		noteLayout.setPadding(4, 4, 4, 4);

		int i = 0;
		View v1 = null, v2 = null;

		while (mLeftPanel.getChildAt(i) != null) {
			v1 = mLeftPanel.getChildAt(i);
			v2 = mRightPanel.getChildAt(i);

			mLeftPanel.removeViewAt(i);
			if (v2 != null) {
				mRightPanel.removeViewAt(i);

			}
			mRightPanel.addView(v1, i);
			v1.startAnimation(animation);
			if (v1 != null && v2 != null) {
				mLeftPanel.addView(v2, i);
				v2.startAnimation(animation2);
			}
			i++;
		}
		mLeftPanel.addView(noteLayout, 0);
		noteLayout.startAnimation(animation1);
	}

	public void addNotesInScreen(List<NoteItem> notesList) {
		int i = 0;
		mLeftPanel.removeAllViews();
		mRightPanel.removeAllViews();
		for (Iterator iterator = notesList.iterator(); iterator.hasNext();) {
			NoteItem noteItem = (NoteItem) iterator.next();

			AddNoteAsyncTask myTask = new AddNoteAsyncTask(mContext, noteItem,
					i);
			myTask.execute();// myTask.executeOnExecutor()

			i++;
		}
	}

	private class AddNoteAsyncTask extends
			AsyncTask<Void, Void, SingleNoteLayout> {

		Context mContext;
		NoteItem noteItem;

		int i = 0;

		public AddNoteAsyncTask(Context context, NoteItem noteItem, int i) {
			this.mContext = context;
			this.noteItem = noteItem;

			this.i = i;
		}

		@Override
		protected SingleNoteLayout doInBackground(Void... arg0) {
			SingleNoteLayout noteLayout = new SingleNoteLayout(mContext);
			noteLayout.setGravity(Gravity.CENTER);
			noteLayout.setupNoteView(noteItem);
			noteLayout.setPadding(4, 4, 4, 4);
			return noteLayout;
		}

		@Override
		protected void onPostExecute(SingleNoteLayout noteLayout) {
			if (i % 2 == 0) {
				mLeftPanel.addView(noteLayout);
				noteLayout.startAnimation(animation1);
			} else {
				mRightPanel.addView(noteLayout);
				noteLayout.startAnimation(animation);
			}
		}

	}

}
