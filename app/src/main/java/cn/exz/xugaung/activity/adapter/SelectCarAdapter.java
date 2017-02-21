package cn.exz.xugaung.activity.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import cn.exz.xugaung.activity.R;
import cn.exz.xugaung.activity.bean.CarInfoBean;

/**
 * Created by pc on 2016/7/18.
 */
public class SelectCarAdapter<T> extends BaseAdapter {
    private List<T> objects = new ArrayList<T>();
    private Activity context;

    public SelectCarAdapter(Activity context) {
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
        final List<CarInfoBean> list = (List<CarInfoBean>) objects;
        ViewHodler viewHodler = null;
        if (convertView == null) {
            viewHodler = new ViewHodler();
            convertView = LayoutInflater.from(context).inflate(R.layout.adapter_carinfo, null);
            viewHodler.tv_plateNum = (TextView) convertView.findViewById(R.id.tv_plateNum);
            viewHodler.tv_check = (TextView) convertView.findViewById(R.id.tv_check);
            viewHodler.iv_truckImg = (ImageView) convertView.findViewById(R.id.iv_truckImg);
            viewHodler.iv_check = (ImageView) convertView.findViewById(R.id.iv_check);
            viewHodler.iv_check_car = (ImageView) convertView.findViewById(R.id.iv_check_car);
            convertView.setTag(viewHodler);
        } else {
            viewHodler = (ViewHodler) convertView.getTag();
        }
        int w = context.getResources().getDisplayMetrics().widthPixels;
        int h = context.getResources().getDisplayMetrics().heightPixels;
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) viewHodler.iv_truckImg.getLayoutParams();
        params.width = w / 4 - w / 2;
        params.width = w / 8;
        viewHodler.iv_truckImg.setLayoutParams(params);
        viewHodler.tv_plateNum.setText(list.get(position).getPlateNum());

        switch (list.get(position).getIsCheck()) {//"isCheck": "1",(1:审核通过  2：审核未通过 3：审核中)

            case "1"://1:审核通过
                viewHodler.iv_check.setBackgroundResource(R.drawable.tongguo);
                viewHodler.tv_check.setText("车辆行驶证证已通过平台审核");
                break;

            case "2": //2：审核未通过
                viewHodler.iv_check.setBackgroundResource(R.drawable.weitongguo);
                viewHodler.tv_check.setText("车辆行驶证证未通过平台审核");
                break;

            case "0"://3：审核
                viewHodler.iv_check.setBackgroundResource(R.drawable.remind);
                viewHodler.iv_check.setBackgroundResource(R.drawable.tongguo);
                viewHodler.tv_check.setText("车辆行驶证证正在审核中");
                break;
        }
        viewHodler.iv_check_car.setBackgroundResource(R.drawable.off);
        if(context.getIntent().getStringExtra("plateNum").equals(list.get(position).getPlateNum())){
            viewHodler.iv_check_car.setBackgroundResource(R.drawable.on);
        }

        return convertView;
    }

    class ViewHodler {

        TextView tv_plateNum;
        TextView tv_check;
        ImageView iv_truckImg;
        ImageView iv_check;
        ImageView iv_check_car;

    }
}
