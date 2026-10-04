package com.jarvis.ai;
import android.content.*;
public class AccessStore{
 private final SharedPreferences p;
 public AccessStore(Context c){p=c.getSharedPreferences("access",0);}
 public boolean allowed(String app){return p.getBoolean(app,false);}
 public void set(String app,boolean v){p.edit().putBoolean(app,v).apply();}
}
