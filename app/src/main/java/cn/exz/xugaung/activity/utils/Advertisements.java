package cn.exz.xugaung.activity.utils;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout.LayoutParams;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import cn.exz.xugaung.activity.R;
import cn.exz.xugaung.activity.adapter.AdvertisementAdapter;


/**
 * @author swz
 *         <p>
 *         Advertisements - 广告基类
 *         </p>
 */

public class Advertisements implements OnPageChangeListener
{
	private boolean bannerIsMOVE=false;
	private ViewPager vpAdvertise;
	private Context context;
	private LayoutInflater inflater;
	private boolean fitXY;
	private int timeDratioin;// 多长时间切换一次pager
	private final int CURRENTPAGE_MESSAGE = 1234;
	List<View> views;
	// 底部小点图片
	private ImageView[] dots;

	// 记录当前选中位置
	private int currentIndex;

	Timer timer;
	TimerTask task;
	int count = 0;

	private Handler runHandler = new Handler()
	{
		@Override
		public void handleMessage(Message msg)
		{
			switch (msg.what)
			{
			case CURRENTPAGE_MESSAGE:
				int currentPage = (Integer) msg.obj;
				setCurrentDot(currentPage);
				vpAdvertise.setCurrentItem(currentPage);
				break;
			}
		};
	};

	public Advertisements(Context context, boolean fitXY,
			LayoutInflater inflater, int timeDratioin)
	{
		this.context = context;
		this.fitXY = fitXY;
		this.inflater = inflater;
		this.timeDratioin = timeDratioin;
	}

	public View initView(final JSONArray advertiseArray)
	{
		View view = inflater.inflate(R.layout.layout_ad_play, null);
		vpAdvertise = (ViewPager) view.findViewById(R.id.vpAdvertise);
		vpAdvertise.setOnPageChangeListener(this);
		views = new ArrayList<View>();
		vpAdvertise.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View view, MotionEvent motionEvent) {
				switch (motionEvent.getAction()) {
					case MotionEvent.ACTION_DOWN:
						EventBus.getDefault().post(new MainSendEvent(bannerIsMOVE));
						break;
				}
				return true;
			}
		});
		LinearLayout ll = (LinearLayout) view.findViewById(R.id.ll);// 获取轮播图片的点的parent，用于动态添加要显示的点

		for (int i = 0; i < advertiseArray.length(); i++)
		{
			if (fitXY)
			{
				views.add(inflater.inflate(R.layout.layout_ad_item_fitxy, null));
			} else
			{
				views.add(inflater.inflate(R.layout.layout_ad_item_fitcenter,
						null));
			}
			ll.addView(inflater.inflate(R.layout.layout_ad_dot, null));

		}
		initDots(view, ll);

		LayoutParams para = (LayoutParams) vpAdvertise.getLayoutParams();// 获取按钮的布局
		DisplayMetrics dm = new DisplayMetrics();
		((Activity) context).getWindowManager().getDefaultDisplay()
				.getMetrics(dm);
		int widthPixels = dm.widthPixels;
		para.height = (int) (widthPixels * 300 / 1080);// 设置图片的高度
		System.err.println(para.height + "-------------");
		para.width = widthPixels;
		vpAdvertise.setLayoutParams(para);
		AdvertisementAdapter adapter = new AdvertisementAdapter(context, views,
				advertiseArray);
		vpAdvertise.setOffscreenPageLimit(3);
		vpAdvertise.setAdapter(adapter);

		timer = new Timer();
		task = new TimerTask()
		{
			@Override
			public void run()
			{
				int currentPage = count % advertiseArray.length();
				count++;
				Message msg = Message.obtain();
				msg.what = CURRENTPAGE_MESSAGE;
				msg.obj = currentPage;
				runHandler.sendMessage(msg);
			}
		};
		timer.schedule(task, 0, timeDratioin);
		return view;
	}

	private void initDots(View view, LinearLayout ll)
	{
		dots = new ImageView[views.size()];

		// 循环取得小点图片
		for (int i = 0; i < views.size(); i++)
		{
			dots[i] = (ImageView) ll.getChildAt(i);
			dots[i].setEnabled(true);// 都设为灰色
		}

		currentIndex = 0;
		dots[currentIndex].setEnabled(false);// 设置为黄色，即选中状态
	}

	private void setCurrentDot(int position)
	{
		if (position < 0 || position > views.size() - 1
				|| currentIndex == position)
		{
			return;
		}

		dots[position].setEnabled(false);
		dots[currentIndex].setEnabled(true);

		currentIndex = position;
	}

	@Override
	public void onPageScrollStateChanged(int arg0)
	{

	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2)
	{

	}

	@Override
	public void onPageSelected(int position)
	{
		count = position;
		setCurrentDot(position);
	}

}
