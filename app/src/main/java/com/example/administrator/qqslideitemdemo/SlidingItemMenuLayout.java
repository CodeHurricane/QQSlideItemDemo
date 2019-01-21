package com.example.administrator.qqslideitemdemo;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.LinearLayout;
import android.widget.Scroller;

public class SlidingItemMenuLayout extends LinearLayout {

	private Scroller mScroller;
	private View leftChild;
	private View rightChild;

	public SlidingItemMenuLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		setOrientation(LinearLayout.HORIZONTAL);
		mScroller = new Scroller(getContext(), null, true);
	}
	
	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		leftChild = getChildAt(0);
		rightChild = getChildAt(1);
	}
	
	private float startX ;
	private float startY ;
	private float dx;
	private float dy;
	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN:
			startX = ev.getX();
			startY = ev.getY();
			super.dispatchTouchEvent(ev);
			return true;
		case MotionEvent.ACTION_MOVE:
			dx = ev.getX() - startX;
			dy = ev.getY() - startY;
			if(Math.abs(dx) - Math.abs(dy)  > ViewConfiguration.getTouchSlop()){
				if(getScrollX() + (-dx)>rightChild.getWidth()||getScrollX() + (-dx)<0){
					return true;
				}
				this.scrollBy((int)-dx, 0);
				startX = ev.getX();
				startY = ev.getY();
				return true;
			}
			break;
		case MotionEvent.ACTION_UP:
			int offset = (getScrollX()/(float)rightChild.getWidth()) > 0.5f ? rightChild.getWidth()-getScrollX() : -getScrollX();
			mScroller.startScroll(getScrollX(), getScrollY(), offset, 0);
			invalidate();
			startX = 0;
			startY = 0;
			dx = 0;
			dy = 0;
			break;

		default:
			break;
		}
		return super.dispatchTouchEvent(ev);
	}
	
	@Override
	public void computeScroll(){
		if(mScroller.computeScrollOffset()){
			this.scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
			postInvalidate();
		}
	}
}
