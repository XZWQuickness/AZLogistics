package cn.exz.xugaung.activity.utils;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.DbManager;
import org.xutils.common.Callback.CommonCallback;
import org.xutils.common.util.LogUtil;
import org.xutils.http.RequestParams;
import org.xutils.image.ImageOptions;
import org.xutils.x;

import cn.exz.xugaung.activity.R;
import cn.exz.xugaung.activity.bean.NewEntity;

public class XUtilsApi {
    private Dialog dialog;
    private View dialogView;

    public Dialog makeProgressDialog(Context context) {

        Dialog dialog = new Dialog(context,
                R.style.loading_dialog);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(false);
        return dialog;
    }

    private DbManager.DaoConfig daoConfig;

    public DbManager.DaoConfig getDaoConfig() {
        return daoConfig;
    }

    /**
     * @return 数据库的操作
     */
    public DbManager.DaoConfig db() {

        daoConfig = new DbManager.DaoConfig().setDbName("lyj_db")// 创建数据库的名称
                .setDbVersion(1)// 数据库版本号
                .setDbUpgradeListener(new DbManager.DbUpgradeListener() {
                    @Override
                    public void onUpgrade(DbManager db, int oldVersion,
                                          int newVersion) {
                        // TODO: ...
                        // db.addColumn(...);
                        // db.dropTable(...);
                        // ...
                    }
                });// 数据库更新操作

        return this.daoConfig;
    }

    /**
     * 发送http请求
     *
     * @param context    上下文
     * @param urlType    请求类型 get or post
     * @param params     参数
     * @param listenter  成功和失败的监听
     * @param withDialog 是否需要dialog
     */
    @SuppressLint("DefaultLocale")
    public void sendUrl(final Context context, String urlType,
                        RequestParams params, final boolean withDialog,
                        final URLSuccessListenter listenter) {
        if (withDialog == true) {
            if (dialog == null) {
                dialog = makeProgressDialog(context);
                dialog.show();
                dialogView = View.inflate(context, R.layout.item_dialog, null);
                RelativeLayout layout = (RelativeLayout) dialogView
                        .findViewById(R.id.rl_bg);// ..


                // main.xml中的ImageView
                ImageView spaceshipImage = (ImageView) dialogView
                        .findViewById(R.id.iv_progress);
                TextView tipTextView = (TextView) dialogView
                        .findViewById(R.id.tv_text);// 提示文字
                // 加载动画
                Animation hyperspaceJumpAnimation = AnimationUtils.loadAnimation(
                        context, R.anim.loading_animation);
                // 使用ImageView显示动画
                spaceshipImage.startAnimation(hyperspaceJumpAnimation);
                dialog.setContentView(dialogView, new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.FILL_PARENT,
                        LinearLayout.LayoutParams.FILL_PARENT));

            } else {
                dialog.show();
            }
        }

        String type = urlType.toUpperCase();
        params.setMultipart(true);
        if (type.equals("POST")) {
            x.http().post(params, new CommonCallback<String>() {

                @Override
                public void onSuccess(String result) {
                    if (withDialog) {
                        dialog.dismiss();
                    }
                    if (listenter != null) {
                        NewEntity t = JSON.parseObject(result,
                                NewEntity.class);

                        try {
                            JSONObject object = new JSONObject(
                                    result);
                            listenter.OnSuccess(t, object);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                }

                @Override
                public void onError(Throwable ex, boolean isOnCallback) {
                    if (withDialog) {
                        dialog.dismiss();
                }
//                    Utils.toast(context, "网络请求出错!");
                }

                @Override
                public void onCancelled(CancelledException cex) {
                }

                @Override
                public void onFinished() {

                }

            });
        } else if (type.equals("GET")) {
            x.http().get(params, new CommonCallback<String>() {

                @Override
                public void onSuccess(String result) {
                    if (withDialog) {
                        dialog.dismiss();
                    }
                    if (listenter != null) {
                        NewEntity t = JSON.parseObject(result,
                                NewEntity.class);
                        try {
                            JSONObject object = new JSONObject(
                                    result);
                            listenter.OnSuccess(t, object);
                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }

                }

                @Override
                public void onError(Throwable ex, boolean isOnCallback) {
                    if (withDialog) {
                        dialog.dismiss();
                    }
                    Utils.toast(context, "网络请求出错!");
                }

                @Override
                public void onCancelled(CancelledException cex) {
                }

                @Override
                public void onFinished() {

                }

            });
        }


    }

    /**
     * @param v   需要加载的ImagView
     * @param url 加载的URL地址
     */
    public static void imageLoad(ImageView v, String url) {
        // 设置加载图片的参数
        ImageOptions options = new ImageOptions.Builder()
                // 是否忽略GIF格式的图片
                .setIgnoreGif(false)
                // 图片缩放模式
                .setImageScaleType(ScaleType.CENTER_CROP)
                // 下载中显示的图片
                .setLoadingDrawableId(R.drawable.empty_photo)
                // 下载失败显示的图片
                .setFailureDrawableId(R.drawable.ic_launcher)
                // 得到ImageOptions对象
                .build();
        // 加载图片
        x.image().bind(v, url, options, new CommonCallback<Drawable>() {
            @Override
            public void onSuccess(Drawable arg0) {
                LogUtil.e("下载成功");
            }

            @Override
            public void onFinished() {
                LogUtil.e("下载完成");
            }

            @Override
            public void onError(Throwable arg0, boolean arg1) {

                LogUtil.e("下载出错，" + arg0.getMessage());
            }

            @Override
            public void onCancelled(CancelledException arg0) {
                LogUtil.e("下载取消");
            }
        });
    }

    /**
     * @param v   需要加载的ImagView
     * @param url 加载的URL地址
     */
    public static void ImageLoad(ImageView v, String url,int icon) {
        // 设置加载图片的参数
        ImageOptions options = new ImageOptions.Builder()
                // 是否忽略GIF格式的图片
                .setIgnoreGif(false)
                // 图片缩放模式
                .setImageScaleType(ScaleType.CENTER_CROP)
                // 下载中显示的图片
                .setLoadingDrawableId(icon)
                // 下载失败显示的图片
                .setFailureDrawableId(icon)
                // 得到ImageOptions对象
                .build();
        // 加载图片
        x.image().bind(v, url, options, new CommonCallback<Drawable>() {
            @Override
            public void onSuccess(Drawable arg0) {
                LogUtil.e("下载成功");
            }

            @Override
            public void onFinished() {
                LogUtil.e("下载完成");
            }

            @Override
            public void onError(Throwable arg0, boolean arg1) {

                LogUtil.e("下载出错，" + arg0.getMessage());
            }

            @Override
            public void onCancelled(CancelledException arg0) {
                LogUtil.e("下载取消");
            }
        });
    }


    public interface URLSuccessListenter {
        public void OnSuccess(NewEntity content, JSONObject result);

    }
}
