package com.example.myapplication.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;

import com.example.myapplication.db.CoolWeatherDB;
import com.example.myapplication.model.City;
import com.example.myapplication.model.County;
import com.example.myapplication.model.Province;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;
import java.util.Locale;

/**
 * 处理服务器返回的数据
 */

public class Utility {
    /**
     * 解析和处理返回的省级数据
     * @param coolWeatherDB 数据库操作对象
     * @param response 返回的数据
     * @return 如果解析成功并添加进表，则返回true,否则返回false
     */
    public synchronized static boolean handleProvincesResponse(CoolWeatherDB coolWeatherDB, String response){
        if(!TextUtils.isEmpty(response)){
            String[] allProvinces=response.split(",");
            if(allProvinces!=null&&allProvinces.length>0){
                for (String p:allProvinces){
                    String[] array=p.split("\\|");
                    Province province=new Province();
                    province.setProvinceCode(array[0]);
                    province.setProvinceName(array[1]);
                    Log.d("MyActivity","provinces code:"+array[0]+" name:"+array[1]);
                    //将解析出来的数据存储到Province表
                    coolWeatherDB.saveProvince(province);
                }
                return true;
            }
        }
        return false;
    }

    /**
     * 解析和处理服务器返回的市级数据
     * @param coolWeatherDB 数据库操作对象
     * @param response 服务器返回的数据
     * @param provinceId 所在的省级id
     * @return 如果解析成功并添加进表中，则返回true,否则返回false
     */
    public static boolean handleCitiesResponse(CoolWeatherDB coolWeatherDB, String response, int provinceId){
        if(!TextUtils.isEmpty(response)){
            String[] allCities=response.split(",");
            if(allCities!=null&&allCities.length>0){
                for (String c:allCities){
                    String[] array=c.split("\\|");
                    City city=new City();
                    city.setCityCode(array[0]);
                    city.setCityName(array[1]);
                    city.setProvinceId(provinceId);
                    Log.d("MyActivity","city code:"+array[0]+" name:"+array[1]);
                    //将解析出来的数据存储到oCity表
                    coolWeatherDB.saveCity(city);
                }
                return true;
            }
        }
        return false;
    }

    /**
     * 解析和处理服务器返回的县级数据
     * @param coolWeatherDB 数据库操作对象
     * @param response 服务器返回的数据
     * @param cityId 所在的市级id
     * @return 如果解析成功并添加进表中，则返回true,否则返回false
     */
    public static boolean handleCountiesResponse(CoolWeatherDB coolWeatherDB, String response, int cityId){
        if(!TextUtils.isEmpty(response)){
            String[] allCounties=response.split(",");
            if(allCounties!=null&&allCounties.length>0){
                for (String c:allCounties){
                    String[] array=c.split("\\|");
                    County county=new County();
                    county.setCountyCode(array[0]);
                    county.setCountyName(array[1]);
                    county.setCityId(cityId);
                    Log.d("MyActivity","county code:"+array[0]+" name:"+array[1]);
                    //将解析出来的数据村塾到County表
                    coolWeatherDB.saveCounty(county);
                }
                return true;
            }
        }
        return false;
    }

    /**
     * 解析服务器返回的JSON数据，并将解析出的数据存入到本地
     * 服务器返回的数据样例：<br/>
     * <b>{"weatherinfo":<br/>
     *      {"city":"昆山","cityid":"101190404","temp1":"20C","temp2":"9C",
     *        "weather":"多云转小雨","img1":"dl.gif","img2":"n7.gif","ptime":"11:00"}
     * <br/>}</b>
     * @param context context
     * @param response 返回的数据
     */
    public static void handleWeatherResponse(Context context, String response){
        try {
            JSONObject jsonObject=new JSONObject(response);
            JSONObject weatherInfo=jsonObject.getJSONObject("weatherinfo");
            String cityName=weatherInfo.getString("city");
            String weathercode=weatherInfo.getString("cityid");
            String temp1=weatherInfo.getString("temp1");
            String temp2=weatherInfo.getString("temp2");
            String weatherDesp=weatherInfo.getString("weather");
            String publishTime=weatherInfo.getString("ptime");
            saveWeatherInfo(context,cityName,weathercode,temp1,temp2,weatherDesp,publishTime);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 将服务器返回的所有天气信息存储到SharedPrefernces文件中.
     * 服务器返回的数据样例：<br/>
     * <b>{"weatherinfo":<br/>
     *      {"city":"昆山","cityid":"101190404","temp1":"20C","temp2":"9C",
     *        "weather":"多云转小雨","img1":"dl.gif","img2":"n7.gif","ptime":"11:00"}
     * <br/>}</b>
     * @param context context
     * @param cityName json数据中的city
     * @param weathercode json数据中的cityid
     * @param temp1 json数据中的temp1
     * @param temp2 json数据中的temp2
     * @param weatherDesp json数据中的weather
     * @param publishTime json数据中的ptime
     */
    private static void saveWeatherInfo(Context context, String cityName, String weathercode, String temp1, String temp2, String weatherDesp, String publishTime) {
        SimpleDateFormat sdf= null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            sdf = new SimpleDateFormat("yyyy年M月d日", Locale.CHINA);
        }
        SharedPreferences.Editor editor= PreferenceManager.getDefaultSharedPreferences(context).edit();
        editor.putBoolean("city_selected",true);
        editor.putString("city_name",cityName);
        editor.putString("weathercode",weathercode);
        editor.putString("temp1",temp1);
        editor.putString("temp2",temp2);
        editor.putString("weather_desp",weatherDesp);
        editor.putString("publish_time",publishTime);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            editor.putString("current_date",sdf.format(new Date()));
        }
        editor.commit();
    }
}
