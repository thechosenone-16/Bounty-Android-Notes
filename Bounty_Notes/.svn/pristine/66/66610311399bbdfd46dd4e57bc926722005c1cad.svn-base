package com.example.gridview;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gridview.dao.Comment;
import com.example.gridview.dao.DataManager;
import com.example.gridview.util.Utils;

public class CommentAddEditActivity extends Activity{

	private EditText txtHeader, txtComment;
	private TextView txtUpdated;
	Comment comment;
	boolean recordAdded;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.addedit);

		txtHeader = (EditText)findViewById(R.id.edt_txt_comment_header);
		txtComment = (EditText)findViewById(R.id.edt_txt_comment);
		txtUpdated = (TextView)findViewById(R.id.txt_comment_updated);
		if (android.os.Build.VERSION.SDK_INT >= 11){
			getActionBar().setDisplayHomeAsUpEnabled(true);
		}

		comment = DataManager.getInstance().getComment();
		if(comment != null){
			txtHeader.setText(comment.title);
			txtComment.setText(comment.comment);
			txtUpdated.setText(comment.updatedAt);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.save, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_saves) {
			recordAdded = true;
			addUpdateComment();
			DataManager.getInstance().setComment(null);
			CommentAddEditActivity.this.finish();
		}else  if(id == android.R.id.home){
			addUpdateComment();
			this.finish();
			DataManager.getInstance().setComment(null);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	@Override
	public void onBackPressed() {
		if(!recordAdded){
			addUpdateComment();
		}
		DataManager.getInstance().setComment(null);
		super.onBackPressed();
	}


	private void addUpdateComment(){
		if(txtHeader.getText().toString().trim().length() > 0 || txtComment.getText().toString().trim().length() > 0){
			CommentsManager commentsManager = new CommentsManager(getApplicationContext());
			if(comment == null){
				Comment comment = new Comment();
				comment.title = txtHeader.getText().toString().trim();
				comment.comment = txtComment.getText().toString().trim();
				comment.updatedAt = Utils.convertStringToDate(System.currentTimeMillis());
				commentsManager.createComment(comment);
				GridActivity.isUpdated = true;
			}else{
				if(!comment.title.equalsIgnoreCase(txtHeader.getText().toString().trim()) ||
						!comment.comment.equalsIgnoreCase(txtComment.getText().toString().trim())){
					comment.title = txtHeader.getText().toString().trim();
					comment.comment = txtComment.getText().toString().trim();
					comment.updatedAt = Utils.convertStringToDate(System.currentTimeMillis());
					commentsManager.updateComment(comment);
					GridActivity.isUpdated = true;
				}
			}

		}else{
			Toast.makeText(CommentAddEditActivity.this, "Empty note discarded.", Toast.LENGTH_SHORT).show();
		}
	}
}
