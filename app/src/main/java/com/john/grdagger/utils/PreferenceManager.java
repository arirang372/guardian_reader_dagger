package com.john.grdagger.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by john on 3/11/2016.
 */
public class PreferenceManager
{
    public static SharedPreferences.Editor editor;
    public static SharedPreferences pref;

    public PreferenceManager(Context c)
    {
        pref = android.preference.PreferenceManager.getDefaultSharedPreferences(c);
        editor = pref.edit();
    }

    public void put(String key, boolean value)
    {
        editor.putBoolean(key, value);
        //editor.commit();
        editor.apply();
    }

    public void put(String key, String value)
    {
        editor.putString(key, value);
        //editor.commit();
        editor.apply();
    }

    public void remove(String key)
    {
        editor.remove(key);
        //editor.commit();
        editor.apply();
    }

    public void put(String key, int value)
    {
        editor.putInt(key, value);
        //editor.commit();
        editor.apply();
    }

    public String getStringValue(String key)
    {
        return pref.getString(key, "");
    }

    public boolean getBooleanValue(String key)
    {
        return pref.getBoolean(key, false);
    }

    public int getInt(String key)
    {
        return pref.getInt(key, 0);
    }
}
