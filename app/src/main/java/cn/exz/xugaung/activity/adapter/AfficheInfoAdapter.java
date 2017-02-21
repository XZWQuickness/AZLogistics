package cn.exz.xugaung.activity.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.exz.xugaung.activity.R;
import cn.exz.xugaung.activity.bean.AfficheInfoBean;

/**
 * Created by pc on 2016/8/5.
 */

public class AfficheInfoAdapter<T> extends BaseAdapter {
    private List<T> objects = new ArrayList<T>();
    private Context context;

    public AfficheInfoAdapter(Context context) {
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
        return 0;
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
        final List<AfficheInfoBean> list = (List<AfficheInfoBean>) objects;
        ViewHodler viewHodler = null;
        if (convertView == null) {
            viewHodler = new ViewHodler();
            convertView = LayoutInflater.from(context).inflate(R.layout.adapter_affcher_info
                    , null);
            viewHodler.tv_title = (TextView) convertView.findViewById(R.id.tv_title);
            viewHodler.tv_create_date = (TextView) convertView.findViewById(R.id.tv_create_date);
            convertView.setTag(viewHodler);
        } else {
            viewHodler = (ViewHodler) convertView.getTag();
        }
        viewHodler.tv_title.setText(list.get(position).getTitle());
        viewHodler.tv_create_date.setText(list.get(position).getCreateDate());


        return convertView;
    }

    class ViewHodler {

        TextView tv_title;
        TextView tv_create_date;

    }
}
