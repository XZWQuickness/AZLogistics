package cn.exz.xugaung.activity.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.exz.xugaung.activity.ArriveActivity;
import cn.exz.xugaung.activity.OrderDetailActivity;
import cn.exz.xugaung.activity.R;
import cn.exz.xugaung.activity.bean.OrderBean;

import static cn.exz.xugaung.activity.R.id.iv_01;

/**
 * Created by pc on 2016/7/18.
 */
public class NewstOrderAdapter<T> extends BaseAdapter {
    private List<T> objects = new ArrayList<T>();
    private Context context;
    private List<String> state;

    public NewstOrderAdapter(Context context, List<String> state) {
        this.context = context;
        this.state = state;
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
        final List<OrderBean> list = (List<OrderBean>) objects;
        ViewHodler viewHodler = null;
        if (convertView == null) {
            viewHodler = new ViewHodler();
            convertView = LayoutInflater.from(context).inflate(R.layout.adapter_newst_order, null);
            viewHodler.rl_newst_order = (RelativeLayout) convertView.findViewById(R.id.rl_newst_order);
            viewHodler.ll_state = (LinearLayout) convertView.findViewById(R.id.ll_state);
            viewHodler.tv_createAddress = (TextView) convertView.findViewById(R.id.tv_createAddress);
            viewHodler.iv_01 = (ImageView) convertView.findViewById(iv_01);
            viewHodler.iv_02 = (ImageView) convertView.findViewById(R.id.iv_02);
            viewHodler.tv_createDate = (TextView) convertView.findViewById(R.id.tv_createDate);
            viewHodler.tv_poundNum = (TextView) convertView.findViewById(R.id.tv_poundNum);
            viewHodler.tv_weiwancheng = (TextView) convertView.findViewById(R.id.tv_weiwancheng);
            viewHodler.tv_finishAddress = (TextView) convertView.findViewById(R.id.tv_finishAddress);
            viewHodler.tv_finishDate = (TextView) convertView.findViewById(R.id.tv_finishDate);
            viewHodler.v_lin_01 = (View) convertView.findViewById(R.id.v_lin_01);
            viewHodler.v_lin = (View) convertView.findViewById(R.id.v_lin);

            convertView.setTag(viewHodler);
        } else {
            viewHodler = (ViewHodler) convertView.getTag();
        }


        viewHodler.tv_createAddress.setText(list.get(position).getCreateAddress());
        viewHodler.tv_createDate.setText( list.get(position).getCreateDate() + " 出发");
        viewHodler.tv_poundNum.setText("磅单号:" + list.get(position).getPoundNum());

        if (list.get(position).getOrderState().equals("进行中")) {
            viewHodler.ll_state.setVisibility(View.GONE);
            viewHodler.tv_weiwancheng.setVisibility(View.VISIBLE);
            viewHodler.iv_02.setBackgroundResource(R.drawable.paizhao);
            viewHodler.iv_01.setBackgroundResource(R.drawable.proceed);
        } else if (list.get(position).getOrderState().equals("已完成")) {
            viewHodler.tv_weiwancheng.setVisibility(View.GONE);
            viewHodler. iv_01.setBackgroundResource(R.drawable.accomplish);
            viewHodler.ll_state.setVisibility(View.VISIBLE);
            viewHodler.iv_02.setBackgroundResource(R.drawable.chakan_detail);
            viewHodler.tv_finishAddress.setText(list.get(position).getFinishAddress());
            viewHodler.tv_finishDate.setText( list.get(position).getFinishDate()+" 到达");
        }
        viewHodler.rl_newst_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (list.get(position).getOrderState().equals("进行中")) {
                    Intent intent = new Intent(context, ArriveActivity.class);
                    intent.putExtra("orderId", list.get(position).getOrderId());
                    context.startActivity(intent);

                } else if (list.get(position).getOrderState().equals("已完成")) {
                    Intent intentDetail = new Intent(context, OrderDetailActivity.class);
                    intentDetail.putExtra("orderId", list.get(position).getOrderId());
                    context.startActivity(intentDetail);
                }

            }
        });
        if (position == state.size() - 1) {
            viewHodler.v_lin.setVisibility(View.VISIBLE);
        } else {
            viewHodler.v_lin.setVisibility(View.GONE);
        }
        if (position == list.size() - 1 || position == state.size() - 1) {
            viewHodler.v_lin_01.setVisibility(View.GONE);
        } else {
            viewHodler.v_lin_01.setVisibility(View.VISIBLE);
        }
        return convertView;
    }

    class ViewHodler {

        RelativeLayout rl_newst_order;
        TextView tv_createAddress;
        TextView tv_createDate;
        TextView tv_poundNum;
        TextView tv_weiwancheng;
        TextView tv_finishAddress;
        TextView tv_finishDate;
        LinearLayout ll_state;
        ImageView iv_02;
        ImageView iv_01;
        View v_lin;
        View v_lin_01;

    }
}
