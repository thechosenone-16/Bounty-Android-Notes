package com.artootrills;

import java.util.Vector;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.TextUtils.EllipsizeCallback;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.TextView;

public class CustomTextView extends TextView{

	private Rect bounds = new Rect();
	static double minTextSize;
	static double maxTextSize;
	int actualLineCount;
	float textSize;
	String text;
	float spacing;
	Ellips ellips;
	TextPaint mTestPaint;
	int offset[];
	Vector<CharSequence> sequence;
	int parentWidth;
	int height;
	int totalWidth;
	int paddingLeft;
	int paddingTop;
	boolean increaseTextSize;
	boolean decreaseTextSize;

	public CustomTextView(Context context) {
		super(context);
		initialise();
	}

	public CustomTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initialise();
	}
	
	public CustomTextView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initialise();
	}

	private void initialise() {
		sequence = new Vector<CharSequence>();
		textSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 20, getResources().getDisplayMetrics());
		mTestPaint = getPaint();
		setTextColor(Color.BLACK);
		minTextSize = Math.ceil(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,15,getResources().getDisplayMetrics()));
		maxTextSize = Math.ceil(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,150 ,getResources().getDisplayMetrics()));
		paddingLeft = (int) (getResources().getDisplayMetrics().scaledDensity*10);
		paddingTop  = (int) (getResources().getDisplayMetrics().scaledDensity*10);
		setPadding(paddingLeft, paddingTop, paddingLeft, paddingTop);
		ellips = new Ellips();
	}

	@Override
	protected void onDraw(Canvas canvas) {
		if(increaseTextSize)
		{
			increaseTextSize();
		}
		else if(decreaseTextSize)
		{
			decreaseTextSize();
		}
		super.onDraw(canvas);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
	{
		totalWidth = CustomLayout.cardWidth - 2*CustomLayout.rowPadding;
		parentWidth = totalWidth - paddingLeft - paddingLeft;
		height = MeasureSpec.getSize(heightMeasureSpec); 
		mTestPaint.setTextSize(textSize);
		mTestPaint.getTextBounds((String) getText(), 0, getText().length(), bounds);
		text = getText().toString();
		ellips.init();
		ellips.calculteEllipsedString();
		actualLineCount = sequence.size();
		spacing = mTestPaint.getFontSpacing();
		height = (int) (actualLineCount*(Math.ceil(spacing)));
		if(height<CustomLayout.cardHeightMin)
		{
			height = CustomLayout.cardHeightMin;
			increaseTextSize = true;
		}
		else if(height>CustomLayout.cardHeightMax)
		{
			height = CustomLayout.cardHeightMax;
			decreaseTextSize = true;
		}
		height = height +paddingTop + paddingTop;
		setMeasuredDimension(totalWidth, height);
	}

	private void increaseTextSize()
	{
		int h = height - paddingTop - paddingTop;
		ellips.init();
		int tempHeight = 0;
		while(textSize<maxTextSize)
		{
			textSize++;
			mTestPaint.setTextSize(textSize);
			ellips.calculteEllipsedString();
			actualLineCount = sequence.size();
			mTestPaint.getTextBounds(text, 0, text.length(), bounds);
			spacing = (float) Math.max(Math.ceil(mTestPaint.getFontSpacing()),Math.ceil(bounds.height()));
			tempHeight = (int) (actualLineCount*(Math.ceil(spacing)));
			if(tempHeight>=h)
			{
				mTestPaint.setTextSize(--textSize);
				break;
			}
		}
	}

	private void decreaseTextSize()
	{
		int h = height - paddingTop - paddingTop;
		int tempHeight = 0;
		ellips.init();
		while(textSize>minTextSize)
		{
			textSize--;
			ellips.calculteEllipsedString();
			actualLineCount = sequence.size();
			mTestPaint.setTextSize(textSize);
			mTestPaint.getTextBounds(text, 0, text.length(), bounds);
			spacing = (float) Math.max(Math.ceil(mTestPaint.getFontSpacing()),Math.ceil(bounds.height()));
			tempHeight = (int) (actualLineCount*(Math.ceil(spacing)));
			if(tempHeight<h)
			{
				mTestPaint.setTextSize(++textSize);
				break;
			}
		}
	}

	private class Ellips implements EllipsizeCallback
	{
		String string;
		int start;
		int end;
		boolean wordEnd;
		
		public void init() {
			string = text;
		}

		public Ellips() {
			this.string = text;
			sequence.clear(); 
			wordEnd = false;
			start = -1;
			end = -1;
		}

		@Override
		public void ellipsized(int start, int end) {
			this.start = start;
			this.end = end;
		}

		public void calculteEllipsedString() {
			String tempString = string;
			sequence.clear();
			CharSequence seq;
			String temp;
			int lastIndex;
			for(int i=0;;i++)
			{
				seq = TextUtils.ellipsize(tempString, mTestPaint, parentWidth, TextUtils.TruncateAt.END
						, true, this);
				wordEnd = false;
				if(seq.toString().isEmpty())
				{
					break;
				}
				else
				{
					if(start!=0&&end!=0)
					{
						if(tempString.charAt(start)==38)
						{
							wordEnd = true;
						}
						if(wordEnd)
						{
							temp = tempString.substring(0, start);
							sequence.add(i, temp);
							tempString = tempString.substring(start, end);
						}
						else
						{
							temp = tempString.substring(0, start);
							lastIndex = temp.lastIndexOf(" ");
							if(lastIndex==-1||lastIndex==0)
							{
								lastIndex = start;
							}
							temp = temp.substring(0, lastIndex);
							sequence.add(i,temp);
							tempString = tempString.substring(lastIndex +1, end);
						}
					}
					else
					{
						temp = tempString.substring(0, tempString.length());
						tempString = tempString.substring(start, end);
						sequence.add(i, temp);
						break;
					}
				}
			}
			if(sequence.isEmpty())
			{
				sequence.add(0,tempString);
			}
		}

	}

}