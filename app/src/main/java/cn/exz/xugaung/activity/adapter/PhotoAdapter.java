package cn.exz.xugaung.activity.adapter;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import cn.exz.xugaung.activity.R;
import cn.exz.xugaung.activity.bean.PhotoBean;

/**
 * Created by pc on 2016/7/26.
 */

public class PhotoAdapter<T> extends BaseAdapter {
    private List<T> objects = new ArrayList<T>();
    private Context context;

    public PhotoAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        if (objects.size() == 4) {
            return 4;
        }
        return objects.size() + 1;
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
        final List<PhotoBean> list = (List<PhotoBean>) objects;
        ViewHodler viewHodler = null;
        if (convertView == null) {
            viewHodler = new ViewHodler();
            convertView = LayoutInflater.from(context).inflate(R.layout.adapter_photo, null);
            viewHodler.iv_photo = (ImageView) convertView.findViewById(R.id.iv_photo);
            convertView.setTag(viewHodler);
        } else {
            viewHodler = (ViewHodler) convertView.getTag();
        }
        if (position == list.size()) {
            viewHodler.iv_photo.setImageBitmap(BitmapFactory.decodeResource(context.
                    getResources(), R.drawable.photo_pai));
            if (position == 5) {
                viewHodler.iv_photo.setVisibility(View.GONE);
            }
        } else {
            Glide.with(context)                             //配置上下文
                    .load(Uri.fromFile(new File(list.get(position).getPhoto())))      //设置图片路径(fix #8,文件名包含%符号 无法识别和显示)
                    .error(R.drawable.head_photo)           //设置错误图片
                    .placeholder(R.drawable.head_photo)     //设置占位图片
                    .diskCacheStrategy(DiskCacheStrategy.ALL)//缓存全尺寸
                    .into( viewHodler.iv_photo);
        }


        return convertView;
    }

    class ViewHodler {

        ImageView iv_photo;

    }
}