package cn.exz.xugaung.activity.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Base64;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Zerain
 * @package com.shuxiang.starchef.uitls
 * @project StarChef
 * @date 2015-11-4
 */
public class Utils {

    // 将文件转为String
    public static String fileToString(String path) throws Exception {
        File file = new File(path);
        InputStream inStream = new FileInputStream(file);
        byte[] buffer = new byte[1024];
        int len = -1;
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        while ((len = inStream.read(buffer)) != -1) {
            outStream.write(buffer, 0, len);
        }
        byte[] data = outStream.toByteArray();
        String s = new String(Base64.encode(data, Base64.DEFAULT));
        outStream.close();
        inStream.close();
        return s;
    }
    /**
     * 校验某个服务是否还活着
     * serviceName :传进来的服务的名称
     */
    public static boolean isServiceRunning(Context context,String serviceName){
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> infos = am.getRunningServices(100);
        for(ActivityManager.RunningServiceInfo info : infos){
            String name = info.service.getClassName();
            if(name.equals(serviceName)){
                return true;
            }
        }
        return false;
    }
    public static String encodeBase64File(String path) throws Exception {
        File file = new File(path);
        FileInputStream inputFile = new FileInputStream(file);
        byte[] buffer = new byte[(int) file.length()];
        inputFile.read(buffer);
        inputFile.close();
        return Base64.encodeToString(buffer, Base64.DEFAULT);
    }

    /**
     * 判断进程是否运行
     *
     * @return
     */
    public static boolean isProessRunning(Context context, String proessName) {

        boolean isRunning = false;
        ActivityManager am = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);

        List<ActivityManager.RunningAppProcessInfo> lists = am.getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo info : lists) {
            if (info.processName.equals(proessName)) {
                isRunning = true;
            }
        }

        return isRunning;
    }

    public static String sHA1(Context context) {
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(
                    context.getPackageName(), PackageManager.GET_SIGNATURES);
            byte[] cert = info.signatures[0].toByteArray();
            MessageDigest md = MessageDigest.getInstance("SHA1");
            byte[] publicKey = md.digest(cert);
            StringBuffer hexString = new StringBuffer();
            for (int i = 0; i < publicKey.length; i++) {
                String appendString = Integer.toHexString(0xFF & publicKey[i])
                        .toUpperCase(Locale.US);
                if (appendString.length() == 1)
                    hexString.append("0");
                hexString.append(appendString);
                hexString.append(":");
            }
            return hexString.toString();
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean textIsEmpty(String result) {
        boolean isNull = false;
        if (!TextUtils.isEmpty(result)) {
            isNull = true;
        } else {
            isNull = false;
        }

        return isNull;
    }

    /**
     * 用于解决ScrollView嵌套listview时，出现listview只能显示一行的问题
     *
     * @param listView
     */
    public static void setListViewHeightBasedOnChildren(ListView listView) {
        // 获取ListView对应的Adapter
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }

        int totalHeight = 0;
        for (int i = 0, len = listAdapter.getCount(); i < len; i++) {
            // listAdapter.getCount()返回数据项的数目
            View listItem = listAdapter.getView(i, null, listView);
            // 计算子项View 的宽高
            listItem.measure(0, 0);
            // 统计所有子项的总高度
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        // listView.getDividerHeight()获取子项间分隔符占用的高度
        // params.height最后得到整个ListView完整显示需要的高度
        listView.setLayoutParams(params);
    }

    /**
     * 检测网络是否可用
     *
     * @return
     */
    public static boolean isNetworkConnected(Context c) {
        ConnectivityManager cm = (ConnectivityManager) c
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        return ni != null && ni.isConnectedOrConnecting();
    }


    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    // 把string转成文件
    public static File stringToFile(String res) throws Exception {
        byte[] data = Base64.decode(res, Base64.DEFAULT);

        String dir = android.os.Environment.getExternalStorageDirectory()
                .getAbsolutePath()
                + File.separator
                + "rjcache"
                + File.separator + "chatRecord";
        File dirFile = new File(dir);
        if (!dirFile.exists()) {
            dirFile.mkdirs();
        }
        File distFile = File.createTempFile("recRecord", ".amr", dirFile);

        distFile = byteToFile(data, distFile.getAbsolutePath());

        return distFile;
    }

    // public static void diaLogShow(Activity context,View view1, AlertDialog
    // builder1 ){
    //
    // builder1.setView(context
    // .getLayoutInflater()
    // .inflate(
    // // R.layout.dialog_money_number,
    // null));
    // builder1.show();
    // builder1.getWindow().setContentView(
    // view1);
    //
    //
    //
    // }
    // 字符串类型日期转化成date类型
    @SuppressLint("SimpleDateFormat")
    public static Date strToDate(String style, String date) {
        SimpleDateFormat formatter = new SimpleDateFormat(style);
        try {
            return formatter.parse(date);
        } catch (Exception e) {
            e.printStackTrace();
            return new Date();
        }
    }

    @SuppressLint("SimpleDateFormat")
    public static String clanderTodatetime(Calendar calendar, String style) {
        SimpleDateFormat formatter = new SimpleDateFormat(style);
        return formatter.format(calendar.getTime());
    }

    @SuppressLint("SimpleDateFormat")
    public static String dateToStr(String style, Date date) {
        SimpleDateFormat formatter = new SimpleDateFormat(style);
        return formatter.format(date);
    }

    // 将byte写入文件
    public static File byteToFile(byte[] buf, String filePath) throws Exception {
        BufferedOutputStream bos = null;
        FileOutputStream fos = null;
        File file = null;
        file = new File(filePath);
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        if (!file.exists()) {
            file.createNewFile();
        }
        fos = new FileOutputStream(file);
        bos = new BufferedOutputStream(fos);
        bos.write(buf);
        if (bos != null) {
            bos.close();
        }
        if (fos != null) {
            fos.close();
        }
        return file;
    }

    /**
     * bitmap转为base64
     *
     * @param bitmap
     * @return
     */
    public static String bitmapToBase64(Bitmap bitmap) {

        String result = null;
        ByteArrayOutputStream baos = null;
        try {
            if (bitmap != null) {
                baos = new ByteArrayOutputStream();
                bitmap.compress(CompressFormat.JPEG, 100, baos);// 100表示不压缩

                baos.flush();
                baos.close();

                byte[] bitmapBytes = baos.toByteArray();
                result = Base64.encodeToString(bitmapBytes, Base64.DEFAULT);

            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (baos != null) {
                    baos.flush();
                    baos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    /**
     * base64转为bitmap
     *
     * @param base64Data
     * @return
     */
    public static Bitmap base64ToBitmap(String base64Data) {
        byte[] bytes = Base64.decode(base64Data, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }

    /*
     * 图片转成string
     * 
     * @param bitmap
     * 
     * @return
     */
    public static String convertIconToString(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();// outputstream
        bitmap.compress(CompressFormat.PNG, 100, baos);
        byte[] appicon = baos.toByteArray();// 转为byte数组
        return Base64.encodeToString(appicon, Base64.DEFAULT);

    }

    public static AlertDialog diaLog(Activity context, View v, View v2) {
        AlertDialog alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.setView(context.getLayoutInflater().inflate(0, null));

        return alertDialog;
    }

    public static Bitmap uriTobitmap(Uri uri, Context c) {
        Bitmap poto = null;
        try {
            poto = MediaStore.Images.Media.getBitmap(c.getContentResolver(),
                    uri);
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return poto;
    }

    public static String BitmapToString(Bitmap boto, Context c) {
        @SuppressWarnings("unused")
        String poto = null;
        Uri uri = Uri.parse(MediaStore.Images.Media.insertImage(
                c.getContentResolver(), boto, null, null));
        return poto = uri.toString();
    }

    public static Bitmap compressImage(Bitmap image) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(CompressFormat.JPEG, 100, baos);// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 100;
        while (baos.toByteArray().length / 1024 > 100) { // 循环判断如果压缩后图片是否大于100kb,大于继续压缩
            baos.reset();// 重置baos即清空baos
            image.compress(CompressFormat.JPEG, options, baos);// 这里压缩options%，把压缩后的数据存放到baos中
            options -= 10;// 每次都减少10
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());// 把压缩后的数据baos存放到ByteArrayInputStream中
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);// 把ByteArrayInputStream数据生成图片
        return bitmap;
    }

    /**
     * string转成bitmap
     *
     * @param st
     */
    public static Bitmap convertStringToIcon(String st) {
        // OutputStream out;
        Bitmap bitmap = null;
        try {
            // out = new FileOutputStream("/sdcard/aa.jpg");
            byte[] bitmapArray;
            bitmapArray = Base64.decode(st, Base64.DEFAULT);
            bitmap = BitmapFactory.decodeByteArray(bitmapArray, 0,
                    bitmapArray.length);
            // bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
            return bitmap;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * @param activity
     * @return 检查是否有网络
     */
    public static boolean isNetworkAvailable(Activity activity) {
        Context context = activity.getApplicationContext();
        // 获取手机所有连接管理对象（包括对wi-fi,net等连接的管理）
        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connectivityManager == null) {
            return false;
        } else {
            // 获取NetworkInfo对象
            NetworkInfo[] networkInfo = connectivityManager.getAllNetworkInfo();

            if (networkInfo != null && networkInfo.length > 0) {
                for (int i = 0; i < networkInfo.length; i++) {
                    System.out.println(i + "===状态==="
                            + networkInfo[i].getState());
                    System.out.println(i + "===类型==="
                            + networkInfo[i].getTypeName());
                    // 判断当前网络状态是否为连接状态
                    if (networkInfo[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * 得到当前的时间
     */
    public static String getCurrentHour() {
        System.currentTimeMillis();
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        int second = calendar.get(Calendar.SECOND);
        return  hour + ":" + minute + ":" + second;
    }

    @SuppressLint("ShowToast")
    public static void toast(Context c, String message) {
        Toast.makeText(c, message, Toast.LENGTH_SHORT).show();
        // toast.setGravity(Gravity.CENTER, 0, 0);
        // toast.show();
    }

    /**
     * @param c     跳转
     * @param clazz
     */
    public static void startActivity(Context c, Class<?> clazz) {
        Intent intent = new Intent(c, clazz);
        c.startActivity(intent);
    }


    /**
     * @param c     跳转
     * @param clazz
     */
    public static void startActivityForResult(Activity c, Class<?> clazz) {
        Intent intent = new Intent(c, clazz);
        c.startActivityForResult(intent, 100);
    }


    /**
     * 提示框
     *
     * @param context
     * @param text
     * @author Zerain
     */
    public static void t(Context context, String text) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
    }

    /**
     * 得到屏幕的宽度
     *
     * @param context
     * @return int
     * @author Zerain
     */
    public static int getScreenWidth(Context context) {
        return context.getResources().getDisplayMetrics().widthPixels;
    }

    /**
     * 得到屏幕的高度
     *
     * @param context
     * @return int
     * @author Zerain
     */
    public static int getScreenHeight(Context context) {
        return context.getResources().getDisplayMetrics().heightPixels;
    }

    /**
     * 得到设备的密度
     *
     * @param context
     * @return float
     * @author Zerain
     */
    public static float getScreenDensity(Context context) {
        return context.getResources().getDisplayMetrics().density;
    }

    /**
     * 判断是否为手机号
     *
     * @param mobiles
     * @return
     * @author Zerain
     */
    public static boolean isMobileNO(String mobiles) {
        Pattern p = Pattern
                .compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");
        Matcher m = p.matcher(mobiles);

        return m.matches();

    }

    public static String TurnDouble(String s) {
        double d = Double.parseDouble(s);
        DecimalFormat df = new DecimalFormat("0.00");
        String ss = df.format(d);
        return ss;
    }

    public static String TurnDouble(Double s) {
        DecimalFormat df = new DecimalFormat("0.00");
        String ss = df.format(s);
        return ss;
    }


}
