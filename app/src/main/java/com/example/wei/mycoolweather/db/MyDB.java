package com.example.wei.mycoolweather.db;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.wei.mycoolweather.model.City;
import com.example.wei.mycoolweather.model.County;
import com.example.wei.mycoolweather.model.Province;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Wei on 2016/11/10.
 */
public class MyDB {
    public static final String DB_NAME = "cool_weather";
    public static final int VERSION = 1;
    private static MyDB myDB;
    private SQLiteDatabase db;

    private MyDB(Context context){
        DBOpenHelper dbOpenHelper = new DBOpenHelper(context,DB_NAME,null,VERSION);
        db = dbOpenHelper.getWritableDatabase();
    }

    public synchronized static MyDB getInstance(Context context) {
        if (myDB == null) {
            myDB = new MyDB(context);
        }
        return myDB;
    }

    public void saveProvince(Province province){
        if (province != null){
            ContentValues values = new ContentValues();
            values.put("province_name", province.getProvinceName());
            values.put("province_code", province.getProvinceCode());
            db.insert("Province",null,values);
        }
    }

    //从数据库读取全国所有省份信息
    public List<Province> loadProvince(){
        List<Province>list = new ArrayList<Province>();
        Cursor cursor = db.query("Province",null,null,null,null,null,null);
        if(cursor.moveToFirst()){
            do{
                Province p = new Province();
                p.setId(cursor.getInt(cursor.getColumnIndex("id")));
                p.setProvinceName(cursor.getString(cursor.getColumnIndex("province_name")));
                p.setProvinceCode(cursor.getString(cursor.getColumnIndex("province_code")));
                list.add(p);
            }while (cursor.moveToNext());
        }
        return list;
    }

    public void saveCity(City city){
        if (city!=null){
            ContentValues values = new ContentValues();
            values.put("city_name", city.getCityName());
            values.put("city_code", city.getCityCode());
            values.put("province_id", city.getProvinceId());
            db.insert("City",null,values);
        }
    }

    //从数据库中读取某省下的所有城市的信息
    public List<City> loadCities(int provinceId){
        List<City> list = new ArrayList<City>();
        Cursor cursor = db.query("City",null,
                "province_id = ?",new String[]{String.valueOf(provinceId)},null,null,null);
        if (cursor.moveToFirst()){
            do {
                City city = new City();
                city.setId(cursor.getInt(cursor.getColumnIndex("id")));
                city.setCityName(cursor.getString(cursor.getColumnIndex("city_name")));
                city.setCityCode(cursor.getString(cursor
                        .getColumnIndex("city_code")));
                city.setProvinceId(provinceId);
                list.add(city);
            }while (cursor.moveToNext());
        }

        return list;

    }

    public void saveCounty(County county){
        if (county != null){
            ContentValues values = new ContentValues();
            values.put("county_name", county.getCountyName());
            values.put("county_code", county.getCountyCode());
            values.put("city_id", county.getCityId());
            db.insert("County", null, values);
        }
    }

    //从数据库读取某城市下所有县的信息
    public List<County> loadCounty(int cityId){
        List<County> list = new ArrayList<County>();
        Cursor cursor = db.query("County",null,"city_id = ?",
                new String[]{String.valueOf(cityId)},null,null,null);

        if (cursor.moveToFirst()){
            do {
                County county = new County();
                county.setId(cursor.getInt(cursor.getColumnIndex("id")));
                county.setCountyName(cursor.getString(cursor
                        .getColumnIndex("county_name")));
                county.setCountyCode(cursor.getString(cursor
                        .getColumnIndex("county_code")));
                county.setCityId(cityId);
                list.add(county);
            } while (cursor.moveToNext());
        }
        return list;
    }

}
