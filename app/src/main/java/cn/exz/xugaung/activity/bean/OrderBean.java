package cn.exz.xugaung.activity.bean;

/**
 * Created by pc on 2016/7/26.
 */

public class OrderBean {

    private String orderId;

    public String getOrderId() { return this.orderId; }

    public void setOrderId(String orderId) { this.orderId = orderId; }

    private String orderState;

    public String getOrderState() { return this.orderState; }

    public void setOrderState(String orderState) { this.orderState = orderState; }

    private String poundNum;

    public String getPoundNum() { return this.poundNum; }

    public void setPoundNum(String poundNum) { this.poundNum = poundNum; }

    private String createAddress;

    public String getCreateAddress() { return this.createAddress; }

    public void setCreateAddress(String createAddress) { this.createAddress = createAddress; }

    private String createDate;

    public String getCreateDate() { return this.createDate; }

    public void setCreateDate(String createDate) { this.createDate = createDate; }

    private String finishAddress;

    public String getFinishAddress() { return this.finishAddress; }

    public void setFinishAddress(String finishAddress) { this.finishAddress = finishAddress; }

    private String finishDate;

    public String getFinishDate() { return this.finishDate; }

    public void setFinishDate(String finishDate) { this.finishDate = finishDate; }
}
