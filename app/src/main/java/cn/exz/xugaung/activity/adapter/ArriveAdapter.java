package cn.exz.xugaung.activity.adapter;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import cn.exz.xugaung.activity.R;
import cn.exz.xugaung.activity.bean.PhotoBean;


/**
 * Created by pc on 2016/11/10.
 */

public class ArriveAdapter<T> extends RecyclerView.Adapter<ArriveAdapter.ViewHolder> {
    private LayoutInflater mInflater;
    private List<T> objects = new ArrayList<T>();
    private Context context;

    public ArriveAdapter(Context context) {
        this.context = context;
        mInflater = LayoutInflater.from(context);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(View v) {
            super(v);
            mImg = (ImageView) v.findViewById(R.id.iv_photo);
        }

        ImageView mImg;
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

    /**
     * 更新适配器数据
     */
    public void updateAdapter() {
        this.notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return objects.size() + 1;
    }

    /**
     * 创建ViewHolder
     */
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = mInflater.inflate(R.layout.adapter_photo,
                viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    /**
     * 设置值
     */
    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int position) {
        List<PhotoBean> list = (List) objects;
        if (position == list.size()) {
            viewHolder.mImg.setImageBitmap(BitmapFactory.decodeResource(context.
                    getResources(), R.drawable.photo_pai));
            if (position == 5) {
                viewHolder.mImg.setVisibility(View.GONE);
            }
        } else {
            Glide.with(context)                             //配置上下文
                    .load(Uri.fromFile(new File(list.get(position).getPhoto())))      //设置图片路径(fix #8,文件名包含%符号 无法识别和显示)
                    .error(R.drawable.head_photo)           //设置错误图片
                    .placeholder(R.drawable.head_photo)     //设置占位图片
                    .diskCacheStrategy(DiskCacheStrategy.ALL)//缓存全尺寸
                    .into(viewHolder.mImg);
        }
        viewHolder.mImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onClick != null) {
                    onClick.onClick();
                }
            }
        });
    }

    public void setOnClick(onClick onClick) {
        this.onClick = onClick;
    }

    onClick onClick;

    public interface onClick {
        void onClick();
    }
}
