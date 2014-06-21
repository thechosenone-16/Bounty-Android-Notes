package com.artootrills;

import android.animation.LayoutTransition;
import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.EditText;

public class MainActivity extends Activity{

	CustomLayout layout;
	LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
	LayoutInflater inflater;
	OnClickListener listener;
	LayoutTransition transition;
	String texts[] = new String[]{
			"Artoo" , "Android Kitkat", "Grameen Foundation" , "Android Programming is Fun",
			"Technology to the Rescue", "Artoo empowers social enterprises to capture, analyze and process information remotely through smartphones / tablets",
			"Brighter India Foundation", 
			};
	CustomTextView view[] = new CustomTextView[7];

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
		layout = (CustomLayout)findViewById(R.id.custom);
		transition = new LayoutTransition();
		layout.setLayoutTransition(transition);
		listener = new OnClickListener() {

			@Override
			public void onClick(View v) {
				transition.setStagger(LayoutTransition.DISAPPEARING, 100);
				layout.removeView(v);
			}
		};
		for(int i=0;i<texts.length;i++)
		{
			view[i] = (CustomTextView) inflater.inflate(R.layout.element, null);
			view[i].setText(texts[i]);
			view[i].setLayoutParams(params);
			view[i].setOnClickListener(listener);
			layout.addView(view[i]);
		}
	}

	public void addView(View view)
	{
		EditText edit = (EditText)findViewById(R.id.editText);
		CustomTextView txt = (CustomTextView) inflater.inflate(R.layout.element, null);
		String t = edit.getText().toString();
		int i=0;
		for(i=0;i<t.length();i++)
		{
			if(t.charAt(i)!=38)
			{
				break;
			}
		}
		if(i!=t.length())
		{
			txt.setText(t);txt.setOnClickListener(listener);
			layout.addView(txt);
			transition.setStagger(LayoutTransition.APPEARING, 100);
			layout.setLayoutTransition(transition);
			layout.setScrollY(0);
			edit.setText("");
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		return super.onTouchEvent(event);
	}

}
