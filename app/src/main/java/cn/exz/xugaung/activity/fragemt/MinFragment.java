package cn.exz.xugaung.activity.fragemt;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import org.greenrobot.eventbus.Subscribe;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import cn.exz.xugaung.activity.CarInfoActivity;
import cn.exz.xugaung.activity.DriverInfoActivity;
import cn.exz.xugaung.activity.FeedBackActivity;
import cn.exz.xugaung.activity.R;
import cn.exz.xugaung.activity.SttingActivity;
import cn.exz.xugaung.activity.UserInfoActivity;
import cn.exz.xugaung.activity.utils.Constant;
import cn.exz.xugaung.activity.utils.ConstantValue;
import cn.exz.xugaung.activity.utils.MainSendEvent;
import cn.exz.xugaung.activity.utils.SPutils;
import cn.exz.xugaung.activity.utils.Utils;

/**
 * Created by pc on 2016/7/14.
 */
@ContentView(R.layout.fragment_min)
public class MinFragment extends BaseFragment implements View.OnClickListener {

    @ViewInject(R.id.tv_title)
    private TextView tvTiltle;

    @ViewInject(R.id.ll_back)
    private LinearLayout llBack;

    @ViewInject(R.id.rl_driver_info)
    private RelativeLayout rl_driver_info;

    @ViewInject(R.id.rl_car_info)
    private RelativeLayout rl_car_info;

    @ViewInject(R.id.rl_stting_info)
    private RelativeLayout rlStting;

    @ViewInject(R.id.rl_user_info)
    private RelativeLayout rlUserInfo;

    @ViewInject(R.id.rl_feedback_info)
    private RelativeLayout rl_feedback_info;

    @ViewInject(R.id.iv_head_phto)
    private ImageView iv_head_phto;

    @ViewInject(R.id.tv_nickname)
    private TextView tv_nickname;

    @ViewInject(R.id.tv_account)
    private TextView tv_account;


    @Override
    public void initView() {

        llBack.setVisibility(View.INVISIBLE);
//        ImageLoader.getInstance().displayImage(Constant.URLIMG + ConstantValue.driverImg, iv_head_phto);
//        tv_nickname.setText(ConstantValue.nickName);
        tv_account.setText(SPutils.getString(getActivity(), "name"));
        tvTiltle.setText("我的");
        rl_car_info.setOnClickListener(this);
        rlStting.setOnClickListener(this);
        rl_feedback_info.setOnClickListener(this);
        rlUserInfo.setOnClickListener(this);
        rl_driver_info.setOnClickListener(this);
    }

    @Override
    public void initData() {

    }

    //主线程接收消息
    @Subscribe
    public void onEventMainThread(MainSendEvent event) {
        if (event != null) {
            int t = (int) event.getT();
            if (t == 2) {
                ImageLoader.getInstance().displayImage(Constant.URLIMG + ConstantValue.driverImg,iv_head_phto );
                tv_nickname.setText(ConstantValue.nickName);
            }
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_user_info://个人信息
                Utils.startActivityForResult(getActivity(), UserInfoActivity.class);
                break;

            case R.id.rl_driver_info://司机信息
                Utils.startActivity(getActivity(), DriverInfoActivity.class);
                break;


            case R.id.rl_car_info://车辆信息
                Utils.startActivity(getActivity(), CarInfoActivity.class);
                break;

            case R.id.rl_stting_info://设置
                Utils.startActivity(getActivity(), SttingActivity.class);
                break;

            case R.id.rl_feedback_info://意见反馈
                Utils.startActivity(getActivity(), FeedBackActivity.class);
                break;

        }
    }


}
