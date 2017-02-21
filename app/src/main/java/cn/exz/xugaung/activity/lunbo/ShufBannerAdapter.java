package cn.exz.xugaung.activity.lunbo;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.List;

/**
 * Created by mdw on 2016/3/24.
 */
public class ShufBannerAdapter extends PagerAdapter {

    private Context context;
    private List<ImageView> mImageViews;

    public ShufBannerAdapter(Context context, List<ImageView> mImages) {
        this.context = context;
        this.mImageViews = mImages;

    }

    @Override
    public int getCount() {
        return mImageViews==null?0:mImageViews.size();
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        container.addView(mImageViews.get(position));

        return mImageViews.get(position);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(mImageViews.get(position));
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }
}
