package com.jarvis.ai;
import android.content.*;import android.content.pm.*;import android.provider.Settings;import java.util.*;
public final class AndroidActionExecutor{
 public static String run(Context c,String raw){String a=raw.trim().toLowerCase(Locale.ROOT);try{
  if(a.equals("wifi")||a.contains("wi-fi")){c.startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));return "صفحه Wi-Fi باز شد.";}
  if(a.equals("bluetooth")){c.startActivity(new Intent(Settings.ACTION_BLUETOOTH_SETTINGS).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));return "صفحه Bluetooth باز شد.";}
  if(a.equals("settings")||a.contains("تنظیمات")){c.startActivity(new Intent(Settings.ACTION_SETTINGS).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));return "تنظیمات Android باز شد.";}
  if(a.contains("accessibility")||a.contains("دسترسی")){c.startActivity(new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));return "دسترسی Accessibility باز شد.";}
  if(a.equals("home")||a.contains("خانه")){JarvisAccessibilityService.performGlobal(2);return "Home اجرا شد.";}
  if(a.equals("back")||a.contains("برگرد")){JarvisAccessibilityService.performGlobal(1);return "Back اجرا شد.";}
  if(a.equals("recents")||a.contains("برنامه‌های اخیر")){JarvisAccessibilityService.performGlobal(3);return "برنامه‌های اخیر باز شد.";}
  if(a.startsWith("open ")||a.startsWith("باز کن ")){String q=a.replaceFirst("^(open |باز کن )","").trim();PackageManager pm=c.getPackageManager();Intent direct=pm.getLaunchIntentForPackage(q);if(direct!=null){direct.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);c.startActivity(direct);return "برنامه باز شد.";}for(ApplicationInfo info:pm.getInstalledApplications(PackageManager.GET_META_DATA)){String label=String.valueOf(pm.getApplicationLabel(info)).toLowerCase(Locale.ROOT);if(label.contains(q)){Intent i=pm.getLaunchIntentForPackage(info.packageName);if(i!=null){i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);c.startActivity(i);return "برنامه «"+pm.getApplicationLabel(info)+"» باز شد.";}}}return "برنامه پیدا نشد: "+q;}
 }catch(Exception e){return "اجرای فرمان ناموفق بود.";}return "این فرمان هنوز به ابزار JARVIS متصل نیست.";}
}