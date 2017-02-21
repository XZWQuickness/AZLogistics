package cn.exz.xugaung.activity.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

import org.json.JSONArray;

import java.util.List;

import cn.exz.xugaung.activity.R;
import cn.exz.xugaung.activity.utils.XUtilsApi;

/**
 * @author swz
 *         AdvertisementAdapter 广告轮播adapter
 */
public class AdvertisementAdapter extends PagerAdapter {

    private Context context;
    private List<View> views;
    JSONArray advertiseArray;

    public AdvertisementAdapter() {
        super();
    }

    public AdvertisementAdapter(Context context, List<View> views,
                                JSONArray advertiseArray) {
        this.context = context;
        this.views = views;
        this.advertiseArray = advertiseArray;
    }

    @Override
    public int getCount() {
        return views.size();
    }

    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        return (arg0 == arg1);
    }

    @Override
    public void destroyItem(View container, int position, Object object) {
        ((ViewPager) container).removeView(views.get(position));
    }

    @Override
    public Object instantiateItem(View container, int position) {
        ((ViewPager) container).addView(views.get(position), 0);
        final int POSITION = position;
        View view = views.get(position);
        try {
            String head_img = advertiseArray.optJSONObject(position).optString(
                    "imgUrl");
            final String adUrl = advertiseArray.optJSONObject(position)
                    .optString("adUrl");
            ImageView ivAdvertise = (ImageView) view
                    .findViewById(R.id.ivAdvertise);
            XUtilsApi.imageLoad(ivAdvertise, head_img);
            // item的点击监听
            ivAdvertise.setOnClickListener(new OnClickListener() {
                @SuppressLint("ShowToast")
                @Override
                public void onClick(View v) {

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        return view;
    }

}
