package cn.exz.xugaung.activity.netutil;

import org.xutils.http.annotation.HttpResponse;

/**
 * Created by Administrator on 2016/7/5.
 */
@HttpResponse(parser = JsonResponseParser.class)
//每一个实体类必须加这句话，别忘了在baseactivity里面 初始化注解 x.view().inject(this);
public class NetEntity<T> {
    //    "result": "success",
//            "message": "操作成功",
//            "info": [{
    private String result;
    private String message;
    private T info;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getInfo() {
        return info;
    }

    public void setInfo(T info) {
        this.info = info;
    }
}
