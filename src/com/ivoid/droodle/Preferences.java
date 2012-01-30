package com.ivoid.droodle;

import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class Preferences
{
    public static final String LOGGEDIN = "loggedin";
    public static final String USERNAME = "username";
    public static final String PASSWORD = "passowrd";
    public static final String LASTTIME = "lasttime";
    public static final String URL = "url";

    public static boolean isLogged(Context ctx)
    {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(ctx);
        return prefs.getBoolean(LOGGEDIN, false);
    }

    public static void setLogged(Context context, boolean logged)
    {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        prefs.edit().putBoolean(LOGGEDIN, logged).commit();
    }
    
    public static Map<String, String> getCredentials(Context context)
    {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        String user = prefs.getString(USERNAME, null);
        String pass = prefs.getString(PASSWORD, null);
        String url = prefs.getString(URL, null);
        
        Map<String, String> creds = new HashMap<String, String>();
        creds.put("username", user);
        creds.put("password", pass);
        creds.put("url", url);
        
        return creds;
    }
    
    public static void clearPrivateInfo(Context ctx)
    {
        SharedPreferences.Editor ed = PreferenceManager.getDefaultSharedPreferences(ctx).edit();
        ed.remove(LOGGEDIN)
          .remove(USERNAME)
          .remove(PASSWORD)
          .remove(URL)
          .commit();
    }

    public static void saveLogin(Context context, Map<String, String> creds)
    {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor ed = prefs.edit();
        ed.putString(USERNAME, creds.get("username"))
          .putString(PASSWORD, creds.get("password"))
          .putString(URL, creds.get("url"))
          .commit();
        setLogged(context, true);
    }
}