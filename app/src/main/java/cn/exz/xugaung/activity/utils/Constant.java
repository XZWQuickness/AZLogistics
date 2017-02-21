package cn.exz.xugaung.activity.utils;

/**
 * @author pc
 */
public class Constant {

    /**
     * 域名
     */
    public static String URL = "http://azwl.xusteel.cn/";

    public static String URLIMG = "http://azwl.xusteel.cn";

    /**
     * 发送验证码
     */
    public static String SEND_CODE = URL + "App/Driver/User/VerifyCode.aspx";

    /**
     * 注册
     */
    public static String REGISTER = URL + "App/Driver/User/Register.aspx";

    /**
     * 登录
     */
    public static String LOGIN = URL + "App/Driver/User/Login.aspx";

    /**
     * 司机信息
     */
    public static String DriverInfo = URL + "App/Driver/User/DriverInfo.aspx";


    /**
     *审核结果接口
     */
    public static String IS_PASS_CHECK = URL + "App/Driver/User/CheckResult.aspx";

    /**
     *提交审核信息接口
     */
    public static String SUBINT_CHECK_INFO = URL + "App/Driver/User/CommitCheckInfo.aspx";


    /**
     *编辑审核信息接口
     */
    public static String EDit_CHECK_INFO = URL + "App/Driver/User/EditCheckInfo.aspx";

    /**
     * 修改个人资料
     */
    public static String EDit_USERINFO = URL + "App/Driver/User/ModifyUserInfo.aspx";


    /**
     * 修改司机信息
     */
    public static String CHANGE_DRIVER_INFO = URL + "App/Driver/User/ModifyDriverInfo.aspx";


    /**
     * 意见反馈
     */
    public static String FEEDBACK = URL + "App/Driver/User/Feedback.aspx";

    /**
     * 忘记密码
     */
    public static String FORGET_PASSWORD = URL
            + "App/Driver/User/FindPassword.aspx";

    /**
     * 修改密码
     */
    public static String MODIFY_PASSWORD = URL
            + "App/Driver/User/ModifyPassword.aspx";


    /**
     * 车辆信息
     */
    public static String TRUCKINFO = URL
            + "App/Driver/User/TruckInfo.aspx";

    /**
     * 首页banner图
     */
    public static String BANNER_LIST = URL
            + "App/Driver/Home/BannerList.aspx";


    /**
     * 立即出发接口(创建订单)
     */
    public static String CREATE_ORDER = URL
            + "App/Driver/Order/Create.aspx";


    /**
     * 进行中的订单id
     */
    public static String GET_UNDERWAYy_ORDER_ID = URL
            + "App/Driver/Order/OngoingOrderIds.aspx";

    /**
     * 实时定位接口
     */
    public static String REALTIME_LOCATION = URL
            + "App/Driver/Order/RealtimeLocate.aspx";


    /**
     * 订单
     */
    public static String ORDER_LIST = URL
            + "App/Driver/Order/OrderList.aspx";


    /**
     * 最新订单列表（4条数据）
     */
    public static String LATEST_ORDER_LIST = URL
            + "App/Driver/Home/LatestOrderList.aspx";

    /**
     * 订单详情
     */
    public static String ORDER_DETAIL = URL
            + "App/Driver/Order/OrderDetail.aspx";

    /**
     * 到达目的地接口(完成订单)
     */
    public static String ORDER_FINISH = URL
            + "App/Driver/Order/Finish.aspx";



    /**
     * 高德地图天气接口
     */
    public static String WEATHER = "http://restapi.amap.com/v3/weather/weatherInfo";

    /**
     * 公告信息
     */
    public static String AFFICHER_INFO =URL+ "App/Driver/Home/AnnouncementList.aspx";


    /**
     * 用户协议
     */
    public static String USER_AGREEMENT="http://xgjt.xzsem.cn/help.aspx?id=2";

    /**
     * 关于我们
     */
    public static String ABOUT="http://xgjt.xzsem.cn/about.aspx";


    /**
     * 历史上的今天
     */
    public static String LISHI_DAY="http://api.juheapi.com/japi/toh";

    /**
     * 添加车辆check
     */
    public static String ADD_TRUCK_CHECK=URL+"App/Driver/User/AddTruckCheck.aspx";

    /**
     * 添加车辆
     */
    public static String ADD_TRUCK=URL+"App/Driver/User/AddTruck.aspx";

    /**
     * 编辑车辆
     */
    public static String EDIT_TRUCK=URL+"App/Driver/User/EditTruck.aspx";

    /**
     * 删除车辆
     */
    public static String DELETE_TRUCK=URL+"App/Driver/User/DeleteTruck.aspx";




}
