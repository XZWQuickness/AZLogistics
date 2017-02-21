package cn.exz.xugaung.activity.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import cn.exz.xugaung.activity.R;
import cn.exz.xugaung.activity.bean.CarInfoBean;
import cn.exz.xugaung.activity.bean.GoodsListBean;

/**
 * Created by pc on 2016/7/18.
 */
public class GoodsInfoAdapter<T> extends BaseAdapter {
    private List<T> objects = new ArrayList<T>();
    private Context context;

    public GoodsInfoAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        Set set = new LinkedHashSet<CarInfoBean>();
        set.addAll(objects);
        objects.clear();
        objects.addAll(set);
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
        final List<GoodsListBean> list = (List<GoodsListBean>) objects;
        ViewHodler viewHodler = null;
        if (convertView == null) {
            viewHodler = new ViewHodler();
            convertView = LayoutInflater.from(context).inflate(R.layout.adapter_goods_info
                    , null);
            viewHodler.tv_goodsName = (TextView) convertView.findViewById(R.id.tv_goodsName);
            viewHodler.tv_standard = (TextView) convertView.findViewById(R.id.tv_standard);
            viewHodler.tv_noticeCount = (TextView) convertView.findViewById(R.id.tv_noticeCount);
            viewHodler.tv_trueCount = (TextView) convertView.findViewById(R.id.tv_trueCount);
            viewHodler.tv_weight = (TextView) convertView.findViewById(R.id.tv_weight);
            viewHodler.tv_coefficient = (TextView) convertView.findViewById(R.id.tv_coefficient);
            viewHodler.tv_position = (TextView) convertView.findViewById(R.id.tv_position);
            convertView.setTag(viewHodler);
        } else {
            viewHodler = (ViewHodler) convertView.getTag();
        }
        viewHodler.tv_goodsName.setText(list.get(position).getGoodsName());//货物名称
        viewHodler.tv_standard.setText(list.get(position).getStandard() + "支");//规格
        viewHodler.tv_noticeCount.setText(list.get(position).getNoticeCount());//通知件数
        viewHodler.tv_trueCount.setText(list.get(position).getTrueCount());//实际件数
        viewHodler.tv_weight.setText(list.get(position).getWeight());//理重
        viewHodler.tv_coefficient.setText(list.get(position).getCoefficient());//标准系数
        viewHodler.tv_position.setText(list.get(position).getPosition());//仓位

        return convertView;
    }

    class ViewHodler {

        TextView tv_goodsName;
        TextView tv_standard;
        TextView tv_noticeCount;
        TextView tv_trueCount;
        TextView tv_weight;
        TextView tv_coefficient;
        TextView tv_position;

    }
}
