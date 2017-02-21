package cn.exz.xugaung.activity.bean;

import java.io.Serializable;

/**
 * Created by pc on 2016/7/22.
 */

public class CarInfoBean implements Serializable{

    /**
     * truckId : 1
     * plateNum : 车牌号
     * truckImg : 车辆图片
     * isCheck : 1
     * failedReason : 未通过原因
     */

    private String truckId;
    private String plateNum;
    private String truckImg;
    private String drivingLicenseImg;
    private String isCheck;
    private String message;

    public String getDrivingLicenseImg() {
        return drivingLicenseImg;
    }

    public void setDrivingLicenseImg(String drivingLicenseImg) {
        this.drivingLicenseImg = drivingLicenseImg;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTruckId() {
        return truckId;
    }

    public void setTruckId(String truckId) {
        this.truckId = truckId;
    }

    public String getPlateNum() {
        return plateNum;
    }

    public void setPlateNum(String plateNum) {
        this.plateNum = plateNum;
    }

    public String getTruckImg() {
        return truckImg;
    }

    public void setTruckImg(String truckImg) {
        this.truckImg = truckImg;
    }

    public String getIsCheck() {
        return isCheck;
    }

    public void setIsCheck(String isCheck) {
        this.isCheck = isCheck;
    }

}
