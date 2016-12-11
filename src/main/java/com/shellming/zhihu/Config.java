package com.shellming.zhihu;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by ruluo1992 on 10/4/2016.
 */
public class Config {
    public static String COOKIE = "_za=d68deac9-ef95-4c08-927c-86d44d2e24c8; udid=\"AACALZ5ZlgmPTnYcrn8wtW0x_fWHls7VReY=|1457520934\"; d_c0=\"ACDAVq5BogmPToJ0DO9rJGhnHnGsBwexb8A=|1458287676\"; _zap=e361797c-0081-4a9f-81fe-28a073707640; _xsrf=c32890165761f8054f32693621241122; s-q=%E5%81%9A%E7%AC%94%E8%AE%B0; s-i=1; sid=21qouhho; s-t=autocomplete; q_c1=93221b3d51744164a1bb6b9ba9c772c2|1478161896000|1478161896000; l_n_c=1; l_cap_id=\"ZWY4NzJmMzgxMGQ4NDY0YzhkN2U3NzExODRmYzZjNzY=|1478174369|0509efff5ffaa018c5c7ae747c2f4f3ee67db7cb\"; cap_id=\"ZTM0M2ZiYWNkZmE4NDUyYTk4ODEyNDFmZDBmYzZmYzE=|1478174369|4eea1c176ca583a212d7d959ea5aa1e6769efd30\"; __utmt=1; login=\"OTc0N2MyMTI2YThhNDc5NmJiMGVmN2FmNjIxMjZmNzc=|1478174512|b5082827957a08d20d19a526547860b4f1190160\"; a_t=\"2.0AABAll-9ogoXAAAANLRCWAAAQJZfvaIKACDAVq5BogkXAAAAYQJVTTG0QlgAbr926ZqVn-exdZP-WhxWSjxNaGA0aIq7BjQwM2ODvnoda0CyLZZZMw==\"; z_c0=Mi4wQUFCQWxsLTlvZ29BSU1CV3JrR2lDUmNBQUFCaEFsVk5NYlJDV0FCdXYzYnBtcFdmNTdGMWtfNWFIRlpLUEUxb1lB|1478174516|29a74dc1a3a197588cd1e7fd1a862fd48cddd61b; __utma=51854390.2038558715.1478005998.1478170145.1478172445.9; __utmb=51854390.18.10.1478172445; __utmc=51854390; __utmz=51854390.1478172445.9.6.utmcsr=127.0.0.1:8888|utmccn=(referral)|utmcmd=referral|utmcct=/duanzi; __utmv=51854390.100--|2=registration_date=20161003=1^3=entry_date=20161003=1";
    public static String TOKEN = "2d964c950259c4c0b119276db383c11a";

    public static Date oldDate;
    public static Date answerFrom;
    static {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-M-d H:m:s");
        // 指定一个日期
        try {
            oldDate = dateFormat.parse("1992-09-13 13:24:16");
            answerFrom = dateFormat.parse("2016-10-01 00:00:00");
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}
