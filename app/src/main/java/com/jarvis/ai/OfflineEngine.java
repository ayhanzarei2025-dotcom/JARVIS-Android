package com.jarvis.ai;
import android.content.*;import android.os.BatteryManager;import java.text.*;import java.util.*;
public final class OfflineEngine{
 private final Context c;public OfflineEngine(Context x){c=x;}
 public String answer(String q){String l=q.trim().toLowerCase(Locale.ROOT);
  if(l.contains("سلام")||l.equals("hello")||l.equals("hi"))return "Hello. I am JARVIS.";
  if(l.contains("ساعت")||l.equals("time"))return new SimpleDateFormat("HH:mm:ss",Locale.getDefault()).format(new Date());
  if(l.contains("تاریخ")||l.equals("date"))return new SimpleDateFormat("yyyy/MM/dd",Locale.getDefault()).format(new Date());
  if(l.contains("باتری")||l.contains("battery")){BatteryManager b=(BatteryManager)c.getSystemService(Context.BATTERY_SERVICE);return "Battery: "+b.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY)+"%.";}
  if(l.contains("آفلاین")||l.contains("offline"))return "Offline mode is active. Basic local answers remain available without internet.";
  if(l.equals("status"))return "JARVIS 1.021 is online. Offline engine ready.";
  return null;
 }
}