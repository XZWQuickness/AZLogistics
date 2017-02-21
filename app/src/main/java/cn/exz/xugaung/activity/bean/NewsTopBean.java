package cn.exz.xugaung.activity.bean;

/**
 * Created by pc on 2016/8/17.
 */

public class NewsTopBean {


    /**
     * des : 在711年前的今天，1305年8月23日 (农历八月初三)，苏格兰民族英雄威廉·华莱士逝世。
     * month : 8
     * year : 1305
     * _id : 13050823
     * pic : http://juheimg.oss-cn-hangzhou.aliyuncs.com/toh/201108/23/A622282817.jpg
     * title : 苏格兰民族英雄威廉·华莱士逝世
     * day : 23
     * lunar :
     */
    private String des;
    private int month;
    private int year;
    private String _id;
    private String pic;
    private String title;
    private int day;
    private String lunar;

    public void setDes(String des) {
        this.des = des;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public void setLunar(String lunar) {
        this.lunar = lunar;
    }

    public String getDes() {
        return des;
    }

    public int getMonth() {
        return month;
    }

    public int getYear() {
        return year;
    }

    public String get_id() {
        return _id;
    }

    public String getPic() {
        return pic;
    }

    public String getTitle() {
        return title;
    }

    public int getDay() {
        return day;
    }

    public String getLunar() {
        return lunar;
    }
}
