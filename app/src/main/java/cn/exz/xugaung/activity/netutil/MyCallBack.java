package cn.exz.xugaung.activity.netutil;

import android.widget.Toast;

import org.xutils.common.Callback;
import org.xutils.ex.HttpException;
import org.xutils.x;

public class MyCallBack<ResultType> implements Callback.CommonCallback<ResultType>{
  
    @Override
    public void onSuccess(ResultType result) {  
        //可以根据公司的需求进行统一的请求成功的逻辑处理  
    }  
  
    @Override
    public void onError(Throwable ex, boolean isOnCallback) {
        //可以根据公司的需求进行统一的请求网络失败的逻辑处理
        Toast.makeText(x.app(), ex.getMessage(), Toast.LENGTH_LONG).show();
        if (ex instanceof HttpException) { // 网络错误
            HttpException httpEx = (HttpException) ex;
            int responseCode = httpEx.getCode();
            String responseMsg = httpEx.getMessage();
            String errorResult = httpEx.getResult();
            // ...
        } else { // 其他错误
            // ...
        }
    }  
  
    @Override
    public void onCancelled(CancelledException cex) {  
  
    }  
  
    @Override
    public void onFinished() {  
  
    }  
  
  
}  