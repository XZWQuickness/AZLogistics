package cn.exz.xugaung.activity.app;

import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.PowerManager;
import android.os.RemoteException;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMapUtils;
import com.amap.api.maps.model.LatLng;
import com.amap.api.trace.TraceLocation;
import com.android.process.aidl.IProcessService;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.HttpParams;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONObject;
import org.xutils.http.RequestParams;

import java.io.File;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;

import cn.exz.xugaung.activity.bean.NewEntity;
import cn.exz.xugaung.activity.utils.Constant;
import cn.exz.xugaung.activity.utils.ConstantValue;
import cn.exz.xugaung.activity.utils.MainSendEvent;
import cn.exz.xugaung.activity.utils.SPutils;
import cn.exz.xugaung.activity.utils.Utils;
import cn.exz.xugaung.activity.utils.XUtilsApi;
import cn.jpush.android.api.JPushInterface;
import okhttp3.Call;
import okhttp3.Response;

import static cn.exz.xugaung.activity.app.MyAppclication.address;
import static cn.exz.xugaung.activity.app.MyAppclication.latitude;
import static cn.exz.xugaung.activity.app.MyAppclication.longitude;

/**
 * Author: river
 * Date: 2016/6/1 17:36
 * Description: 本地服务
 */
public class RemoteService extends Service implements AMapLocationListener {
    String TAG = "RemoteService";
    boolean isLocation;
    private ServiceBinder mServiceBinder;
    String filePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Test/";
    String fileName = "log.txt";
    private RemoteServiceConnection mRemoteServiceConn;
    private PowerManager.WakeLock wakeLock = null;
    @Override
    public void onCreate() {
        super.onCreate();
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        mServiceBinder = new ServiceBinder();

        if (mRemoteServiceConn == null) {
            mRemoteServiceConn = new RemoteServiceConnection();
        }
        initLocation();
        new Thread(new MyThread()).start();
    }


    private void writeTxtToFile(String strcontent, String filePath, String fileName) {
        //生成文件夹之后，再生成文件，不然会出错
        makeFilePath(filePath, fileName);

        String strFilePath = filePath + fileName;
        // 每次写入时，都换行写
        String strContent = strcontent + "\r\n";
        try {
            File file = new File(strFilePath);
            if (!file.exists()) {
                Log.d("TestFile", "Create the file:" + strFilePath);
                file.getParentFile().mkdirs();
                file.createNewFile();
            }
            RandomAccessFile raf = new RandomAccessFile(file, "rwd");
            raf.seek(file.length());
            raf.write(strContent.getBytes());
            raf.close();
        } catch (Exception e) {
            Log.e("TestFile", "Error on write File:" + e);
        }
    }

    // 生成文件
    public File makeFilePath(String filePath, String fileName) {
        File file = null;
        makeRootDirectory(filePath);
        try {
            file = new File(filePath + fileName);
            if (!file.exists()) {
                file.createNewFile();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return file;
    }

    // 生成文件夹
    public static void makeRootDirectory(String filePath) {
        File file = null;
        try {
            file = new File(filePath);
            if (!file.exists()) {
                file.mkdir();
            }
        } catch (Exception e) {
            Log.i("error:", e + "");
        }
    }


    //===============================高德定位======================================

    private AMapLocationClient locationClient = null;
    private AMapLocationClientOption locationOption = null;

    private void initLocation() {

        locationClient = new AMapLocationClient(this.getApplicationContext());
        locationOption = new AMapLocationClientOption();
        // 设置定位模式为高精度模式
        locationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        // 设置定位监听
        locationClient.setLocationListener(this);

        // 设置是否需要显示地址信息
        locationOption.setNeedAddress(true);
        locationOption.setGpsFirst(false);//可选，设置是否gps优先，只在高精度模式下有效。默认关闭
        locationOption.setHttpTimeOut(30000);//可选，设置网络请求超时时间。默认为30秒。在仅设备模式下无效
        locationOption.setInterval(3000);//可选，设置定位间隔。默认为2秒
        locationOption.setNeedAddress(true);//可选，设置是否返回逆地理地址信息。默认是true
        locationOption.setOnceLocation(false);//可选，设置是否单次定位。默认是false
        locationOption.setOnceLocationLatest(false);//可选，设置是否等待wifi刷新，默认为false.如果设置为true,会自动变为单次定位，持续定位时不要使用
        AMapLocationClientOption.setLocationProtocol(AMapLocationClientOption.AMapLocationProtocol.HTTP);//可选， 设置网络请求的协议。可选HTTP或者HTTPS。默认为HTTP
        locationOption.setSensorEnable(false);//可选，设置是否使用传感器。默认是false
        locationOption.setWifiScan(true); //可选，设置是否开启wifi扫描。默认为true，如果设置为false会同时停止主动刷新，停止以后完全依赖于系统刷新，定位位置可能存在误差
        locationOption.setLocationCacheEnable(false); //可选，设置是否使用缓存定位，默认为true
        // 设置定位参数
        locationClient.setLocationOption(locationOption);
        // 启动定位
        locationClient.startLocation();
        getUnderwayOrderIDS();

    }

    /**
     * 将AMapLocation List 转为TraceLocation list
     *
     * @param list
     * @return
     */
    public List<TraceLocation> parseTraceLocationList(
            List<AMapLocation> list) {
        List<TraceLocation> traceList = new ArrayList<TraceLocation>();
        if (list == null) {
            return traceList;
        }
        for (int i = 0; i < list.size(); i++) {
            TraceLocation location = new TraceLocation();
            AMapLocation amapLocation = list.get(i);
            location.setBearing(amapLocation.getBearing());
            location.setLatitude(amapLocation.getLatitude());
            location.setLongitude(amapLocation.getLongitude());
            location.setSpeed(amapLocation.getSpeed());
            location.setTime(amapLocation.getTime());
            traceList.add(location);
        }
        return traceList;
    }

    // 定位监听
    @Override
    public void onLocationChanged(final AMapLocation loc) {
        if (null != loc && loc.getLocationType() != 0 && loc.getLocationType() != 2
                && getDistance(latitude, longitude, loc.getLatitude(), loc.getLongitude()) > 1) {//
            address = loc.getAddress();
            latitude = loc.getLatitude();
            longitude = loc.getLongitude();
            if (!TextUtils.isEmpty(orderIds)) {
                sendLocate(loc.getAddress(), loc.getLatitude(), loc.getLongitude());
            }
        }

    }
    /*
    *
    * 获取直线距离
    * 旧的oldLa 纬度  旧的oldLo 经纬  新的纬度 newLa 新的经度 newtLo
    * */

    public float getDistance(double oldLa, double oldLo, double newLa, double newtLo) {
        float distance = AMapUtils.calculateLineDistance(new LatLng(oldLa, oldLo), new LatLng
                (newLa, newtLo));
        return distance;
    }


    private String orderIds = "";


    String url = "http://azwl.xusteel.cn/App/Driver/Order/RealtimeLocate.aspx?driverId=";

    private void sendLocate(String address, final double longitude, final double latitude) {
        final HttpParams params = new HttpParams();
        params.put("driverId", ConstantValue.DRIVERID);
        params.put("orderIds", orderIds);
        params.put("address", address);
        params.put("longitude", longitude + "");
        params.put("latitude", latitude + "");
        params.put("deviceType", "1");
        params.put("registrationID", JPushInterface.getRegistrationID(this));
        url += TextUtils.isEmpty(ConstantValue.DRIVERID) ? SPutils.getString(this, "driverId") :
                ConstantValue.DRIVERID;
        url += "&orderIds=" + (TextUtils.isEmpty(orderIds) ? SPutils.getString(this, "orderIds") :
                orderIds);
        url += "&address=" + address;
        url += "&longitude=" + longitude;
        url += "&latitude=" + latitude;
        url += "&deviceType=1";
        url += "&registrationID=" + JPushInterface.getRegistrationID(this);
        OkGo.post(Constant.REALTIME_LOCATION)//
                .tag(this)
                .params(params)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        url += "\r\n" + s;
                        writeTxtToFile(Utils.getCurrentHour() + " " + longitude + "," + latitude + "orderId=" + orderIds, filePath, fileName);
                        url = "http://azwl.xusteel.cn/App/Driver/Order/RealtimeLocate.aspx?driverId=";
                    }

                });
    }

    /*
*
* 进行中的订单id
* */
    public void getUnderwayOrderIDS() {
        if (!TextUtils.isEmpty(ConstantValue.DRIVERID)) {
            SPutils.save(RemoteService.this, "driverId", ConstantValue.DRIVERID);
        }
        XUtilsApi x = new XUtilsApi();
        RequestParams p = new RequestParams(Constant.GET_UNDERWAYy_ORDER_ID);
        p.addBodyParameter("driverId", TextUtils.isEmpty(ConstantValue.DRIVERID) ? SPutils.getString(this, "driverId") : ConstantValue.DRIVERID);
        x.sendUrl(getApplicationContext(), "post", p, false, new XUtilsApi.URLSuccessListenter() {

            @Override
            public void OnSuccess(NewEntity content, JSONObject result) {
                if (content.getResult().equals(ConstantValue.RESULT)) {
                    JSONObject optJSONObject = result.optJSONObject("info");
                    orderIds = optJSONObject.optString("orderIds");
                }
            }
        });
    }

    //主线程接收消息
    @Subscribe
    public void onEventMainThread(MainSendEvent event) {
        if (event != null) {
            String Update = event.getStringMsgData();
            if (!TextUtils.isEmpty(Update) && Update.equals("Update")) {
                getUnderwayOrderIDS();
            }
        }
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        Log.i(TAG, TAG + " onStartCommand");

        //  绑定远程服务
        bindService(new Intent(this, RemoteService.class), mRemoteServiceConn, Context.BIND_IMPORTANT);
        Settings.System.putInt(getContentResolver(), Settings.System.WIFI_SLEEP_POLICY, Settings.System.WIFI_SLEEP_POLICY_NEVER);
        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mServiceBinder;
    }


    /**
     * 通过AIDL实现进程间通信
     */
    class ServiceBinder extends IProcessService.Stub {
        @Override
        public String getServiceName() throws RemoteException {
            return "RemoteService";
        }
    }

    /**
     * 连接远程服务
     */
    class RemoteServiceConnection implements ServiceConnection {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            try {

                // 与远程服务通信
                IProcessService process = IProcessService.Stub.asInterface(service);
                Log.i(TAG, "连接" + process.getServiceName() + "服务成功");
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            // RemoteException连接过程出现的异常，才会回调,unbind不会回调
            // 监测，远程服务已经死掉，则重启远程服务
            Log.i(TAG, "远程服务挂掉了,远程服务被杀死");

            // 启动远程服务
            startService(new Intent(RemoteService.this, LocalService.class));

            // 绑定远程服务
            bindService(new Intent(RemoteService.this, LocalService.class), mRemoteServiceConn, Context.BIND_IMPORTANT);
        }
    }


    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            // 要做的事情
            if (msg.what == 1) {
                acquireWakeLock(RemoteService.this);
                String locationClientStr = "";
                if (null != locationClient) {
                    locationClient.startLocation();
                } else if (null == locationClient) {
                    initLocation();
                }
            }

        }
    };

    private void acquireWakeLock(Context context) {

        if (null == wakeLock) {
            PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);

            wakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK //保持CPU 运转，屏幕和键盘灯有可能是关闭的

                    | PowerManager.ON_AFTER_RELEASE, getClass()

                    .getCanonicalName());

        }
        if (null != wakeLock) {
            wakeLock.acquire();
        }

    }

    public class MyThread implements Runnable {
        @Override
        public void run() {

            while (true) {
                try {
                    Thread.sleep(3000);// 线程暂停3秒，单位毫秒
                    Message message = new Message();
                    message.what = 1;
                    handler.sendMessage(message);// 发送消息
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
    }
}
