package com.android.hospital.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;

import com.github.snowdream.android.app.updater.UpdateFormat;
import com.github.snowdream.android.app.updater.UpdateManager;
import com.github.snowdream.android.app.updater.UpdateOptions;
import com.github.snowdream.android.app.updater.UpdatePeriod;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


/**
 * @author Ukey zhang yeshentianyue@sina.com
 * @ClassName: Util
 * @Description: TODO(工具类)
 * @date 2012-12-22 上午12:54:09
 */
public class Util {

    /**
     * @param @return 设定文件
     * @return String    返回类型
     * @throws
     * @Title: toSimpleDate
     * @Description: TODO(格式化时间)
     */
    public static String toSimpleDate() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date(System.currentTimeMillis());//获取当前时间
        String str = format.format(date);
        return str;
    }

    /**
     * @param @return 设定文件
     * @return String    返回类型
     * @throws
     * @Title: toTest_No
     * @Description: TODO(格式化检验号)
     */
    public static String toTest_No(String dateStr) {
        switch (dateStr.length()) {
            case 1:
                dateStr = "000" + dateStr;
                break;
            case 2:
                dateStr = "00" + dateStr;
                break;
            case 3:
                dateStr = "0" + dateStr;
                break;
        }
        SimpleDateFormat format = new SimpleDateFormat("yyMMdd");
        Date date = new Date(System.currentTimeMillis());//获取当前时间
        String str = format.format(date);
        return str + dateStr;
    }

    /**
     * @param @param  dateStr
     * @param @return 设定文件
     * @return String    返回类型
     * @throws
     * @Title: toDateOfBirth
     * @Description: TODO(格式化病人出生日期)
     */
    public static String toDateOfBirth(String dateStr) {
        String[] date_of_birthArr = dateStr.split("/");
        String[] date_of_birthArr2 = date_of_birthArr[2].split(" ");
        StringBuffer sBuffer = new StringBuffer();
        sBuffer.append(date_of_birthArr2[0]).append("-").append(date_of_birthArr[0]).append("-").append(date_of_birthArr[1]);
        return sBuffer.toString();
    }

    public static String toQueryTime(int c) {
        if (c >= 10)
            return String.valueOf(c);
        else
            return "0" + String.valueOf(c);
    }

    // 同时要注意整个系统取的是什么时间。比如是数据库时间，服务器时间，jdk时间,还是客户端时间。我这里取的是当前系统时间

    // 同时，如果当前时间是2012-2-29 那么，today.getMonth()的值是一。

    public static int userBirthdayGetAge(String birth) {

        try {
            String birthday = toDateOfBirth(birth);
            DebugUtil.debug("当前生日是--->" + birthday);

            Calendar cal = Calendar.getInstance();

            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

            String mDateTime = formatter.format(cal.getTime());// 当前时间

            Date today = formatter.parse(mDateTime);

            DebugUtil.debug("当前生日是--->" + today);

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

            Date birday = sdf.parse(birthday);// 当前对当前的情况

            int age = today.getYear() - birday.getYear();

            if (today.getMonth() == birday.getMonth()

                    && today.getDate() == birday.getDate()

                    && birday.getYear() % 4 != 0 && today.getYear() != 0

                    && birday.getMonth() != 1 && today.getMonth() != 1) {// 月份和日期都与当前时间相同(除去生日和当前年是闰年，并且是二月的情况)

                return age;

            } else if (today.getMonth() < birday.getMonth()) {// 生日的月份大于当前时间的月份

                return age - 1;

            } else if (birday.getMonth() == 1 && birday.getDate() == 29// 生日是闰年，当前年不一定是闰年

                    && today.getMonth() == 1) {// 生日是闰年的情况,并且本月是二月

                if (today.getYear() % 4 == 0) {// 当前年是闰年 2月有二十九

                    if (today.getDate() < birday.getDate()) {//

                        return age - 1;

                    } else {

                        return age;

                    }

                } else {// 当前是年二月是二十八天

                    if (today.getDate() < birday.getDate() - 1) {

                        return age - 1;

                    } else {

                        return age;

                    }

                }

            } else if (today.getMonth() == 1 && today.getDate() == 29

                    && birday.getMonth() == 1) { // 当前年是闰年，生日年不一定是闰年

                if (birday.getYear() % 4 == 0) {// 生日年是闰年 二月有二十九天

                    if (today.getDate() < birday.getDate()) {//

                        return age - 1;

                    } else {

                        return age;

                    }

                } else {// 生日年二月是二十八天

                    if (today.getDate() + 1 < birday.getDate()) {

                        return age - 1;

                    } else {

                        return age;

                    }

                }

            } else if (today.getMonth() > birday.getMonth()) {// 不是同一年，生日的月份不大于当前的月份的情况

                return age;

            } else if (today.getDate() < birday.getDate()) {// 正常的日期(非闰年) 当前 小于 出生

                return age - 1;

            } else if (today.getDate() == birday.getDate()) {// 当前 等于 出生

                return age;

            } else {

                return age; // 当前 大于 出生

            }
        } catch (Exception e) {
            // TODO: handle exception
        }
        return 0;
    }

    /**
     * @param @param  date
     * @param @return
     * @param @throws Exception    设定文件
     * @return Date    返回类型
     * @throws
     * @Title: converToDate
     * @Description: TODO(格式化体温图时间格式)
     */
    public static Date converToDate(String date) throws Exception {
        DateFormat df = new SimpleDateFormat("yy-MM-dd HH:mm");
        return df.parse(date);
    }

    public static String converToDateUtc(String date) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date utcDate = null;
        try {
            utcDate = df.parse(date);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return String.valueOf(utcDate.getTime());
    }

    public static String converToDateUtc2(String date) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date utcDate = null;
        try {
            utcDate = df.parse(date);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return String.valueOf(utcDate.getTime());
    }

    public static String getServerIP(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("ip_set", Context.MODE_WORLD_WRITEABLE);
        String ip = sharedPreferences.getString("server_ip", "192.168.2.101:8080");
        return ip;
    }

    public static void setServerIP(Context context, String ip) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("ip_set", Context.MODE_WORLD_WRITEABLE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("server_ip", ip);
        editor.commit();
    }

    public static String getVersion(Context context) {
        String version;
        try {
            version = context.getPackageManager().getPackageInfo(context.getPackageName(),
                    PackageManager.GET_META_DATA).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            version = "UnknownVersion";
        }
        return version;
    }

    public void autoUpdate(Context context) {
        UpdateManager manager = new UpdateManager(context);

        UpdateOptions options = new UpdateOptions.Builder(context)
                .checkUrl("http://" + Util.getServerIP(context) + "/WebServiceServer/download/updateinfo.xml")
                .updateFormat(UpdateFormat.XML)
                .updatePeriod(new UpdatePeriod(UpdatePeriod.EACH_TIME))
                .checkPackageName(true)
                .build();
        manager.check(context, options, new TLUpdateListener());
    }

}
