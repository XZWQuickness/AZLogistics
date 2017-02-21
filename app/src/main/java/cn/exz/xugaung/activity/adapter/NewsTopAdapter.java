package cn.exz.xugaung.activity.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

import cn.exz.xugaung.activity.R;
import cn.exz.xugaung.activity.bean.NewsTopBean;

/**
 * Created by pc on 2016/7/26.
 */

public class NewsTopAdapter<T> extends BaseAdapter {
    private List<T> objects = new ArrayList<T>();
    private Context context;

    public NewsTopAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return objects.size();
    }


    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return objects.size();
    }

    /**
     * 更新适配器数据
     */
    public void updateAdapter() {
        this.notifyDataSetChanged();
    }

    /**
     * 清空适配器数据
     */
    public void clearAdapter() {
        objects.clear();
    }

    /**
     * 返回适配器中的数据
     */
    public List<T> getAdapterData() {
        return objects;
    }

    /**
     * 添加多条记录
     *
     * @param alist      数据集合
     * @param isClearOld 是否清空原数据
     */
    public void addendData(List<T> alist, boolean isClearOld) {
        if (alist == null) {
            return;
        }
        if (isClearOld) {
            objects.clear();
        }
        objects.addAll(alist);

    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final List<NewsTopBean> list = (List<NewsTopBean>) objects;
        ViewHodler viewHodler = null;
        if (convertView == null) {
            viewHodler = new ViewHodler();
            convertView = LayoutInflater.from(context).inflate(R.layout.adapter_news_top, null);
            viewHodler.photo = (ImageView) convertView.findViewById(R.id.photo);
            viewHodler.content = (TextView) convertView.findViewById(R.id.content);
            convertView.setTag(viewHodler);
        } else {
            viewHodler = (ViewHodler) convertView.getTag();
        }
        ImageLoader.getInstance().displayImage(list.get(position).getPic(),viewHodler.photo);
        viewHodler.content.setText(list.get(position).getDes());

        return convertView;
    }

    class ViewHodler {

        ImageView photo;
        TextView content;

    }
}