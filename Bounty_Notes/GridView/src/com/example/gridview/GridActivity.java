package com.example.gridview;

import java.util.ArrayList;
import java.util.List;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.AnimatorInflater;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.example.gridview.adapters.NavDrawerListAdapter;
import com.example.gridview.adapters.StaggeredAdapter;
import com.example.gridview.dao.Comment;
import com.example.gridview.dao.DataManager;
import com.example.gridview.dao.NavDrawerItem;
import com.origamilabs.library.views.StaggeredGridView;
import com.origamilabs.library.views.StaggeredGridView.OnItemClickListener;
import com.origamilabs.library.views.StaggeredGridView.OnItemLongClickListener;

@SuppressLint("NewApi")
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
	private DrawerLayout mDrawerLayout;
	private ListView mDrawerList;
	private ActionBarDrawerToggle mDrawerToggle;
	private ArrayList<NavDrawerItem> navDrawerItems;
	private NavDrawerListAdapter navAdapter;
	// slide menu items
	private String[] navMenuTitles;
	private TypedArray navMenuIcons;

	int counter;
	private boolean isInEditMode;
	private View currentView;
	 private Animation animZoomIn;
	 private Animation animZoomOut;
	 private static final int ANIMATION_DURATION = 300;
	 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

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

		// load slide menu items
		navMenuTitles = getResources().getStringArray(R.array.sliding_options);

		// nav drawer icons from resources
		navMenuIcons = getResources()
				.obtainTypedArray(R.array.nav_drawer_icons);

		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerList = (ListView) findViewById(R.id.list_slidermenu);

		navDrawerItems = new ArrayList<NavDrawerItem>();

		// adding nav drawer items to array
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[0], navMenuIcons.getResourceId(0, -1)));
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[1], navMenuIcons.getResourceId(1, -1)));
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[2], navMenuIcons.getResourceId(2, -1)));
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[3], navMenuIcons.getResourceId(3, -1)));
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[4], navMenuIcons.getResourceId(4, -1)));


		// Recycle the typed array
		navMenuIcons.recycle();


		mDrawerList.setOnItemClickListener(new SlideMenuClickListener());

		// setting the nav drawer list adapter
		navAdapter = new NavDrawerListAdapter(getApplicationContext(),
				navDrawerItems);
		mDrawerList.setAdapter(navAdapter);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setHomeButtonEnabled(true);

		mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
				R.drawable.ic_drawer, //nav menu toggle icon
				R.string.drawer_open, // nav drawer open - description for accessibility
				R.string.drawer_close // nav drawer close - description for accessibility
				) {
			public void onDrawerClosed(View view) {
				setTitle("Bounty Android Notes");
				// calling onPrepareOptionsMenu() to show action bar icons
				invalidateOptionsMenu();
			}

			public void onDrawerOpened(View drawerView) {
				setTitle("Bounty Notes");
				// calling onPrepareOptionsMenu() to hide action bar icons
				invalidateOptionsMenu();
			}
		};
		mDrawerLayout.setDrawerListener(mDrawerToggle);
		
		animZoomIn = AnimationUtils.loadAnimation(getApplicationContext(),
				R.anim.zoom_in);
		//animZoomIn.setFillAfter(true);
		animZoomIn.setAnimationListener(new AnimationListener() {
			
			@Override
			public void onAnimationStart(Animation animation) {
				
			}
			
			@Override
			public void onAnimationRepeat(Animation animation) {
				
			}
			
			@Override
			public void onAnimationEnd(Animation animation) {
				Intent intent = new Intent(GridActivity.this, CommentAddEditActivity.class);
    			startActivity(intent);
			}
		});
		animZoomOut = AnimationUtils.loadAnimation(getApplicationContext(),
				R.anim.zoom_out);
		animZoomOut.setAnimationListener(new AnimationListener() {
			
			@Override
			public void onAnimationStart(Animation animation) {
				
			}
			
			@Override
			public void onAnimationRepeat(Animation animation) {
				
			}
			
			@Override
			public void onAnimationEnd(Animation animation) {
    			animZoomOut.setFillAfter(false);
			}
		});
		
	}

	@Override
	public void setTitle(CharSequence title) {
		getActionBar().setTitle(title);
	}

	/**
	 * When using the ActionBarDrawerToggle, you must call it during
	 * onPostCreate() and onConfigurationChanged()...
	 */

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		// Sync the toggle state after onRestoreInstanceState has occurred.
		mDrawerToggle.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		// Pass any configuration change to the drawer toggls
		mDrawerToggle.onConfigurationChanged(newConfig);
	}
	
	/* *
	 * Called when invalidateOptionsMenu() is triggered
	 */
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		// if nav drawer is opened, hide the action items
		boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
		menu.findItem(R.id.action_settings).setVisible(!drawerOpen);
		return super.onPrepareOptionsMenu(menu);
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

		
		if(isInEditMode){
			//currentView.startAnimation(animZoomOut);
			isInSelectionMode = false;
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
		// toggle nav drawer on selecting action bar app icon/title
		if (mDrawerToggle.onOptionsItemSelected(item)) {
			return true;
		}
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
			currentView = view;
			isInEditMode = true;
			DataManager.getInstance().setComment(comment);
			/*Intent intent = new Intent(GridActivity.this, CommentAddEditActivity.class);
			startActivity(intent);
			overridePendingTransition( R.anim.zoom_in,  R.anim.zoom_out);*/
			//animZoomIn.setTarget(view);
			//view.startAnimation(animZoomIn);
			//animZoomIn.start();
			Intent intent = new Intent(GridActivity.this, CommentAddEditActivity.class);
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

	/**
	 * Slide menu item click listener
	 * */
	private class SlideMenuClickListener implements
	ListView.OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			Log.i("onItemClick ", "" + position);
			mDrawerList.setItemChecked(position, true);
			mDrawerList.setSelection(position);
			navAdapter.setSelectedPosition(position);
			setTitle(navMenuTitles[position]);
			// display view for selected nav drawer item
			mDrawerLayout.closeDrawer(mDrawerList);
		}
	}

}
