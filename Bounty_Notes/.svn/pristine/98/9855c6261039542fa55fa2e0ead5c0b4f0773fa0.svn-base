package com.example.gridview;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
public class TextAdapter extends BaseAdapter {
    Context mContext;
    String[] listText;

    public TextAdapter(Context c, String[] listText) {
        mContext = c;
        
        this.listText = listText;
    }

    public int getCount() {
        return listText.length;
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    // create a new ImageView for each item referenced by the Adapter
	public View getView(int position, View convertView, ViewGroup parent) {
        TextView textView = null;
        if (convertView == null) {  // if it's not recycled, initialize some attributes
        	textView = new TextView(mContext);
        	textView.setBackgroundColor(Color.GRAY);
        	textView.setPadding(8, 8, 8, 8);
        	textView.setTextColor(Color.WHITE);
        		//textView.setHeight((int) mContext.getResources().getDimension(R.dimen.large_textsize));
        	
        } else {
        	textView = (TextView) convertView;
        }

        textView.setText(listText[position]);
        return textView;
    }

    // references to our images
   
}