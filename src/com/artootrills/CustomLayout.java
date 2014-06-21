package com.artootrills;

import java.util.Vector;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.TranslateAnimation;
import android.widget.OverScroller;

public class CustomLayout extends ViewGroup implements OnGestureListener{

	int columnCount;
	static int cardWidth;
	static int cardHeightMin;
	static int cardHeightMax;
	static int columnPadding;
	static int rowPadding;
	Vector<Position> prevPosition = new Vector<CustomLayout.Position>();
	int prevCount = 0;
	int prevHeight = 0;
	Context context;
	Point point = new Point();
	GestureDetector detector;
	OverScroller mScroller;
	TranslateAnimation translate;
	int y[] ;
	Position pos;
	final ValueAnimator mScrollAnimator = ValueAnimator.ofFloat(0,1);

	int height;
	LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
	static int totalWidth;

	public GestureDetector getGestureDetector() {
		return detector;
	}

	@Override
	public void addView(View child) {
		super.addView(child);
	}

	private class Position
	{
		private int x;
		private int y;

		public int getX() {
			return x;
		}
		public void setX(int x) {
			this.x = x;
		}
		public int getY() {
			return y;
		}
		public void setY(int y) {
			this.y = y;
		}
	}

	public CustomLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.context = context;
		init();
	}

	public CustomLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		init();
	}

	public CustomLayout(Context context) {
		super(context);
		this.context = context;
		init();
	}

	private void init() {
		detector = new GestureDetector(getContext(), this);
		mScroller = new OverScroller(getContext());
		columnCount = 2;
		columnPadding = (int) (getResources().getDisplayMetrics().scaledDensity*10);
		rowPadding = (int) (getResources().getDisplayMetrics().scaledDensity*10);
	}


	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int width = 0;
		height = getPaddingTop();
		int count =getChildCount();
		totalWidth = MeasureSpec.getSize(widthMeasureSpec)/columnCount;
		cardWidth = totalWidth - columnPadding;
		width = totalWidth*columnCount;
		cardHeightMin = (int) (MeasureSpec.getSize(heightMeasureSpec)/7);
		cardHeightMax = MeasureSpec.getSize(heightMeasureSpec)/3;
		y = new int[columnCount];
		int index;
		for(int i=0;i<count;i++)
		{
			index = (i)%columnCount;
			View child = getChildAt(i);
			measureChild(child, widthMeasureSpec,heightMeasureSpec);
			y[index] = y[index] + child.getMeasuredHeight() +columnPadding;
		}
		height = y[determineMax(y)] +columnPadding;
		setMeasuredDimension(resolveSize(width, widthMeasureSpec),resolveSize(height, heightMeasureSpec));
	}

	private int determineMin(int y[]) {
		int lowest = 0;
		for(int i=1;i<y.length;i++)
		{
			if(y[i]<y[lowest])
			{
				lowest = i;
			}
		}
		return lowest;
	}

	private int determineMax(int y[]) {
		int highest = 0;
		for(int i=1;i<y.length;i++)
		{
			if(y[i]>y[highest])
			{
				highest = i;
			}
		}
		return highest;
	}

	@Override
	protected void onLayout(boolean arg0, int arg1, int arg2, int arg3, int arg4) {
		int count = getChildCount();
		int lpx =0 , lpy =0 , x=0;
		y = new int[columnCount];
		View child ;
		int delay = 0;
		for(int i=count;i>0;i--)
		{
			child = getChildAt(i-1);
			int height = child.getMeasuredHeight();
			pos = new Position();
			lpx = x*cardWidth;
			x = (++x)%columnCount;
			int index = (i-1)%columnCount;
			lpy = y[index];
			child.layout(lpx+rowPadding,lpy+columnPadding, lpx+cardWidth, lpy+height+columnPadding);
			y[index] = lpy+height+columnPadding;
			/*if(prevCount == 0)
			{
				pos.setY(-(lpy+height+columnPadding));
				pos.setX(lpx);
				prevPosition.add(count-i, pos);
			}
			else if(i>prevCount)
			{
				pos.setY(-(lpy+height+columnPadding));
				pos.setX(lpx);
				prevPosition.add(count-i, pos);
			}
			if(((prevPosition.elementAt(count-i).getY()!=lpy)&&prevCount==0)||(prevCount!=count&&i==count))
			{
				translate = new TranslateAnimation(0,
						0, prevPosition.elementAt(count-i).getY(), 0);
				translate.setDuration(400);
				prevPosition.elementAt(count-i).setX(lpx);
				prevPosition.elementAt(count-i).setY(lpy);
			}
			else if(prevCount!=count&&i<=prevCount)
			{
				int initialX = prevPosition.elementAt(count-i).getX()+getLeft();
				int initialY = prevPosition.elementAt(count-i).getY();
				float finalX = child.getLeft();
				float finalY = child.getTop();
				translate = new TranslateAnimation(Animation.ABSOLUTE ,initialX-finalX , Animation.ABSOLUTE, 0,
						Animation.ABSOLUTE, initialY - finalY,Animation.ABSOLUTE, 0);
				translate.setStartOffset(delay);
				translate.setDuration(500);
				prevPosition.elementAt(count-i).setX(child.getLeft());
				prevPosition.elementAt(count-i).setY(child.getTop());
			}
			delay+=50;*/
		}
		if(prevCount>count&&getScrollY()>0&&height!=getMeasuredHeight())
		{
			if(getScrollY()==prevHeight-getMeasuredHeight())
			{
				custom(height-getMeasuredHeight());
			}
			else
			{
				customScroll(Math.min( height - prevHeight, height - getMeasuredHeight()));
			}
		}
		else
		{
			scrollTo(0, 0);
		}
		prevCount = count;
		prevHeight = height;
	}

	@Override 
	public boolean onTouchEvent(MotionEvent event) { 
		return detector.onTouchEvent(event); 
	} 
	
	@Override public boolean onDown(MotionEvent e) 
	{ 
		return false; 
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent e) {
		return onTouchEvent(e);
	}

	@Override public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) 
	{ 
		int width = getMeasuredHeight() ;
		int scrollHeight = height - getHeight();
		int scrollX = getScrollX();
		int scrollY = getScrollY();
		mScroller.forceFinished(true);
		mScroller.fling(scrollX,scrollY, 0, -(int)velocityY, 
				0, width, 0, scrollHeight);
		mScrollAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
			@Override
			public void onAnimationUpdate(ValueAnimator valueAnimator) {
				if (!mScroller.isFinished()) {
					mScroller.computeScrollOffset();
					scrollTo(mScroller.getCurrX(), (int) mScroller.getCurrY());
				} else {
					mScrollAnimator.cancel();
				}
			}
		});
		if(getScrollY()+getHeight()<height&&getScrollY()+getHeight()>getMeasuredHeight())
		{
			mScrollAnimator.start();
		}
		return false;
	}

	@Override public void onLongPress(MotionEvent e) 
	{ 

	} 
	@Override public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, final float distanceY) 
	{ 
		customScroll(distanceY);
		return false;
	}

	public void custom(int distance) {

		scrollBy(0, distance);
	}

	public void customScroll(float distanceY)
	{
		if(getScrollY()+distanceY<height-getMeasuredHeight()
				&&getScrollY()+distanceY>0
				)
		{
			scrollBy(0, (int) (distanceY));
		}
	}

	protected void onScrollFinished() {
		setScrollY(mScroller.getFinalY());
	}

	@Override public void onShowPress(MotionEvent e) 
	{ 
		
	}
	@Override public boolean onSingleTapUp(MotionEvent e) 
	{
		return false; 
	}

}
