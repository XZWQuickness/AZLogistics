package cn.exz.xugaung.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.RequiresApi;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import cn.exz.xugaung.activity.app.RectifyRemoteService;
import cn.exz.xugaung.activity.utils.Constant;
import cn.exz.xugaung.activity.utils.SPutils;
import cn.exz.xugaung.activity.utils.Utils;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;

/**
 * Created by pc on 2016/7/18.
 * 设置
 */
@ContentView(R.layout.activity_stting)
public class SttingActivity extends BaseActivity implements View.OnClickListener {

    @ViewInject(R.id.rl_alter_password) //修改密码
    private RelativeLayout rl_alter_password;

    @ViewInject(R.id.tv_title)
    private TextView title;
    @ViewInject(R.id.ll_back)
    private LinearLayout ll_back;

    @ViewInject(R.id.rl_share)
    private RelativeLayout rl_share;

    @ViewInject(R.id.rl_user_agreement)
    private RelativeLayout rl_user_agreement;

    @ViewInject(R.id.rl_about_us)
    private RelativeLayout rl_about_us;


    @ViewInject(R.id.tv_finish_login)
    private TextView tv_finish_login;
    String path_img="";

    public void initView() {
        title.setText("设置");
        rl_alter_password.setOnClickListener(this);
        ll_back.setOnClickListener(this);
        tv_finish_login.setOnClickListener(this);
        rl_share.setOnClickListener(this);
        rl_user_agreement.setOnClickListener(this);
        rl_about_us.setOnClickListener(this);
        sendImgFriend();
        path_img = Environment.getExternalStorageDirectory().getAbsolutePath() + "/ic_logo.png";
    }

    @Override
    public void initData() {

    }

    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onClick(View view) {
        Intent intent=new Intent(SttingActivity.this,WebViewActivity.class);
        switch (view.getId()) {
            case R.id.rl_alter_password: //修改密码
                Utils.startActivity(SttingActivity.this, AlterPasswordActivity.class);
                break;
            case R.id.ll_back:
                finish();
                break;
            case R.id.tv_finish_login:
                SPutils.save(SttingActivity.this, "password", "");
                Intent finishIentent = new Intent(SttingActivity.this, LoginActivity.class);
                startActivity(finishIentent);
                RectifyRemoteService.clerOrderId();
                finishAffinity();
                break;

            case R.id.rl_share:
                showShare();
                break;
            case R.id.rl_user_agreement://用户协议
                intent.putExtra("info", Constant.USER_AGREEMENT);
                intent.putExtra("name", "用户协议");
                startActivity(intent);
                break;
            case R.id.rl_about_us://关于们
                intent.putExtra("info", Constant.ABOUT);
                intent.putExtra("name", "关于我们");
                startActivity(intent);
                break;


        }
    }
    private void showShare() {
        ShareSDK.initSDK(this);
        OnekeyShare oks = new OnekeyShare();
        //关闭sso授权
        oks.disableSSOWhenAuthorize();

// 分享时Notification的图标和文字  2.5.9以后的版本不调用此方法
        //oks.setNotification(R.drawable.ic_launcher, getString(R.string.app_name));
        // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
        oks.setTitle("分享");
        // titleUrl是标题的网络链接，仅在人人网和QQ空间使用
        oks.setTitleUrl("http://sharesdk.cn");
        // text是分享文本，所有平台都需要这个字段
        oks.setText("我是分享文本");
        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
        //oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
        // url仅在微信（包括好友和朋友圈）中使用
        oks.setUrl("http://sharesdk.cn");
        oks.setImagePath(path_img);
        // comment是我对这条分享的评论，仅在人人网和QQ空间使用
        oks.setComment("我是测试评论文本");
        // site是分享此内容的网站名称，仅在QQ空间使用
        oks.setSite(getString(R.string.app_name));
        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
        oks.setSiteUrl("http://sharesdk.cn");

// 启动分享GUI
        oks.show(this);
    }
    public void sendImgFriend(){
        InputStream abpath=getClass().getResourceAsStream("/assets/picture/ic_logo.png");
        try {
            writetoSDCard(InputStreamToByte(abpath));

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    /**
     *
     * @param bs
     * 把字节数组写到SDCard中,然后再读取该图片
     */
    public void writetoSDCard(byte []bs){
        try {
            FileOutputStream out=new FileOutputStream(new File("/sdcard/ic_logo.png")); //重新命名的图片为test.png.想要获取的图片的路径就是该图片的路径
            try {
                out.write(bs);
                out.flush();
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
    private byte[] InputStreamToByte(InputStream is) throws IOException {
        ByteArrayOutputStream bytestream = new ByteArrayOutputStream();
        int ch;
        while ((ch = is.read()) != -1) {
            bytestream.write(ch);
        }
        byte imgdata[] = bytestream.toByteArray();
        bytestream.close();
        return imgdata;
    }
}
