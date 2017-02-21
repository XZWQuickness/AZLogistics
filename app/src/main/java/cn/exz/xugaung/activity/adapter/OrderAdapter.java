package cn.exz.xugaung.activity.adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.exz.xugaung.activity.ArriveActivity;
import cn.exz.xugaung.activity.OrderDetailActivity;
import cn.exz.xugaung.activity.R;
import cn.exz.xugaung.activity.bean.CarInfoBean;
import cn.exz.xugaung.activity.bean.OrderBean;

/**
 * Created by jianghejie on 15/11/26.
 */
public class OrderAdapter<T> extends RecyclerView.Adapter<OrderAdapter.ViewHolder> {
    private List<T> objects = new ArrayList<T>();
    private Activity context;

    public OrderAdapter(Activity activity) {
        this.context = activity;
    }


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


    //创建新View，被LayoutManager所调用
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        final List<CarInfoBean> list = (List<CarInfoBean>) objects;
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.adapter_order, viewGroup, false);
        return new ViewHolder(view);
    }

    //将数据与界面进行绑定的操作
    @Override
    public void onBindViewHolder(ViewHolder viewHodler, final int position) {
        final List<OrderBean> list = (List<OrderBean>) objects;
        viewHodler.tv_createAddress.setText(list.get(position).getCreateAddress());
        viewHodler.tv_createDate.setText(list.get(position).getCreateDate() + " 出发");
        viewHodler.tv_poundNum.setText("磅单号:" + list.get(position).getPoundNum());

        if (list.get(position).getOrderState().equals("进行中")) {
            viewHodler.ll_state.setVisibility(View.GONE);
            viewHodler.iv_02.setBackgroundResource(R.drawable.paizhao);
            viewHodler.iv_01.setBackgroundResource(R.drawable.proceed);
        } else if (list.get(position).getOrderState().equals("已完成")) {
            viewHodler.tv_weiwancheng.setVisibility(View.GONE);
            viewHodler.iv_01.setBackgroundResource(R.drawable.accomplish);
            viewHodler.iv_02.setBackgroundResource(R.drawable.chakan_detail);
            viewHodler.tv_finishAddress.setText(list.get(position).getFinishAddress());
            viewHodler.tv_finishDate.setText(list.get(position).getFinishDate()+" 到达");
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
        viewHodler.tv_createAddress.setText(list.get(position).getCreateAddress());
        viewHodler.tv_createDate.setText(list.get(position).getCreateDate() + " 出发");
        viewHodler.tv_poundNum.setText("磅单号:" + list.get(position).getPoundNum());

        if (list.get(position).getOrderState().equals("进行中")) {
            viewHodler.ll_state.setVisibility(View.GONE);
            viewHodler.iv_02.setBackgroundResource(R.drawable.paizhao);
        } else if (list.get(position).getOrderState().equals("已完成")) {
            viewHodler.tv_weiwancheng.setVisibility(View.GONE);
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
    }

    //获取数据的数量
    @Override
    public int getItemCount() {
        return objects.size();
    }

    //自定义的ViewHolder，持有每个Item的的所有界面元素
    public static class ViewHolder extends RecyclerView.ViewHolder {
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

        public ViewHolder(View view) {
            super(view);
            rl_newst_order = (RelativeLayout) view.findViewById(R.id.rl_newst_order);
            ll_state = (LinearLayout) view.findViewById(R.id.ll_state);
            tv_createAddress = (TextView) view.findViewById(R.id.tv_createAddress);
            iv_01 = (ImageView) view.findViewById(R.id.iv_01);
            iv_02 = (ImageView) view.findViewById(R.id.iv_02);
            tv_createDate = (TextView) view.findViewById(R.id.tv_createDate);
            tv_poundNum = (TextView) view.findViewById(R.id.tv_poundNum);
            tv_weiwancheng = (TextView) view.findViewById(R.id.tv_weiwancheng);
            tv_finishAddress = (TextView) view.findViewById(R.id.tv_finishAddress);
            tv_finishDate = (TextView) view.findViewById(R.id.tv_finishDate);
        }
    }
}
