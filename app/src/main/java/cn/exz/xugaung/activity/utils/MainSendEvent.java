package cn.exz.xugaung.activity.utils;

/**
 * @author mmxs
 */
public class MainSendEvent<T> {
    protected String mstrMsg;
    private T t;

    public MainSendEvent(String msg) {
        mstrMsg = msg;
    }

    public String getStringMsgData() {
        return mstrMsg;
    }

    public MainSendEvent(T t) {
        this.t = t;
    }

    public T getT() {
        return t;
    }
}
