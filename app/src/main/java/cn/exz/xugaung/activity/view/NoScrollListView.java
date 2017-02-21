package cn.exz.xugaung.activity.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ListView;

public class NoScrollListView extends ListView  {

	public NoScrollListView(Context context)
	{
		super(context);
	}

	public NoScrollListView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
	{
		int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
				MeasureSpec.AT_MOST);
		super.onMeasure(widthMeasureSpec, expandSpec);
	}

	//通过重新dispatchTouchEvent方法来禁止滑动
	//通过此方法禁止gridview进行上下滑动
	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		// TODO Auto-generated method stub
		if (ev.getAction() == MotionEvent.ACTION_MOVE) {
			return true;// 禁止gridview滑动
		}
		return super.dispatchTouchEvent(ev);
	}







}
