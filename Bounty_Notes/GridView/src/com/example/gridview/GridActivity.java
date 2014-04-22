package com.example.gridview;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;



//import com.example.gridview.adapters.NavDrawerListAdapter;
import com.example.gridview.adapters.StaggeredAdapter;
import com.example.gridview.dao.Comment;
import com.example.gridview.dao.DataManager;
//import com.example.gridview.dao.NavDrawerItem;
import com.origamilabs.library.views.StaggeredGridView;
import com.origamilabs.library.views.StaggeredGridView.OnItemClickListener;
import com.origamilabs.library.views.StaggeredGridView.OnItemLongClickListener;

public class GridActivity extends Activity implements OnClickListener, OnItemClickListener, OnItemLongClickListener
{


	CommentsManager mgrComment;
	ImageView img_txtNote;
	public static boolean isUpdated = false;
	StaggeredAdapter adapter;
	StaggeredGridView gridView;
	List<Comment> values;
	boolean isInSelectionMode;
	private ActionMode mActionMode;
	int counter;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		new DbHelper(getApplicationContext());

		mgrComment = new CommentsManager(this);
		mgrComment.open();
		values = mgrComment.getAllComments();
		mgrComment.close();


		gridView = (StaggeredGridView) this.findViewById(R.id.staggeredGridView1);
		img_txtNote = (ImageView) this.findViewById(R.id.img_txtNote);
		img_txtNote.setOnClickListener(this);

		int margin = getResources().getDimensionPixelSize(R.dimen.margin);

		gridView.setItemMargin(margin); // set the GridView margin

		gridView.setPadding(margin, 0, margin, 0); // have the margin on the sides as well 

		adapter = new StaggeredAdapter(this, R.id.txtNote, values);
		gridView.setAdapter(adapter);

		gridView.setOnItemClickListener(this);
		gridView.setOnItemLongClickListener(this);

	}

	protected void onResume(){
		super.onResume();
		mgrComment.open();
		if(isUpdated){
			values = mgrComment.getAllComments();

			adapter = new StaggeredAdapter(this, R.id.txtNote, values);
			gridView.setAdapter(adapter);
			adapter.notifyDataSetChanged();
		}
	}

	protected void onPause(){
		super.onPause();
		mgrComment.close();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.grid, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}



	@Override
	public void onClick(View v) {
		Intent intent = null;
		switch(v.getId()){
		case R.id.img_txtNote:
			intent = new Intent(this, CommentAddEditActivity.class);
			startActivity(intent);
			break;
		}

	}

	@Override
	public void onItemClick(StaggeredGridView parent, View view, int position,
			long id) {
		Comment comment = values.get(position);
		if(!isInSelectionMode){
			DataManager.getInstance().setComment(comment);
			/*view.startAnimation(AnimationUtils.loadAnimation(this,
					R.anim.slide_up_left));*/
			Intent intent = new Intent(this, CommentAddEditActivity.class);
			startActivity(intent);
		}else{
			Log.i("Counter", "" + counter);
			if(comment.state){
				comment.state = false;
				counter--;
			}else{
				comment.state = true;
				counter++;
			}
			values.set(position, comment);
			adapter.notifyDataSetChanged();
			if(counter == 0){
				isInSelectionMode = false;
				mActionMode.finish();
			}
			if(mActionMode != null){
				mActionMode.setTitle("" + counter);
			}
		}

	}

	@Override
	public boolean onItemLongClick(StaggeredGridView parent, View view,
			int position, long id) {
		isInSelectionMode = true;

		Comment comment = values.get(position);
		if(comment.state){
			comment.state = false;
			counter--;
		}else{
			comment.state = true;
			counter++;
		}
		values.set(position, comment);
		adapter.notifyDataSetChanged();

		if(isInSelectionMode){
			if(mActionMode == null){
				mActionMode = GridActivity.this.startActionMode(new ActionBarCallBack());
			}
		}

		if(counter == 0){
			isInSelectionMode = false;
			mActionMode.finish();
		}
		if(mActionMode != null){
			mActionMode.setTitle("" + counter);
		}
		return true;
	}

	@Override
	public void onBackPressed() {
		if(isInSelectionMode){
			for(int i = 0; i< values.size();i++){
				Comment comment = values.get(i);
				comment.state = false;
				values.set(i, comment);
			}
			counter = 0;
			isInSelectionMode = false;
			adapter.notifyDataSetChanged();
			if(isInSelectionMode)
				mActionMode = GridActivity.this.startActionMode(new ActionBarCallBack());
			else if(mActionMode != null)
				mActionMode.finish();
		}else{
			super.onBackPressed();
		}
	}


	class ActionBarCallBack implements ActionMode.Callback {

		@Override
		public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
			Log.i("onActionItemClicked", "" + item.getItemId());
			if(item.getItemId() == R.id.item_delete){
				mgrComment.open();
				for(int i = 0; i< values.size();i++){
					Comment comment = values.get(i);
					if(comment.state){
						mgrComment.deleteComment(comment);
					}
				}
				mgrComment.close();
				counter = 0;
				isInSelectionMode = false;
				if(mActionMode != null){
					mActionMode.finish();
					mActionMode = null;
				}
				
				mgrComment.open();
				values = mgrComment.getAllComments();
				adapter = new StaggeredAdapter(GridActivity.this, R.id.txtNote, values);
				gridView.setAdapter(adapter);
				adapter.notifyDataSetChanged();
				mgrComment.close();
			}
			return false;
		}

		@Override
		public boolean onCreateActionMode(ActionMode mode, Menu menu) {
			Log.i("onCreateActionMode", "" + menu.size());
			mode.getMenuInflater().inflate(R.menu.contextual_menu, menu);
			return true;
		}

		@Override
		public void onDestroyActionMode(ActionMode mode) {
			Log.i("onDestroyActionMode", " 1");
			for(int i = 0; i< values.size();i++){
				Comment comment = values.get(i);
				comment.state = false;
				values.set(i, comment);
			}
			isInSelectionMode = false;
			counter = 0;
			adapter.notifyDataSetChanged();
			mActionMode = null;
		}

		@Override
		public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
			Log.i("onPrepareActionMode", "" + menu.size());
			mode.setTitle("" + counter);
			return false;
		}
	}

}
