package com.jarvis.ai;
import android.app.*;import android.content.*;import android.media.AudioManager;import android.provider.Settings;
public final class ActionEngine{
 public static String run(Context c,String q){String l=q.toLowerCase(java.util.Locale.ROOT).trim();
  if(l.equals("back")||l.contains("go back")||l.contains("برگرد")){if(JarvisAccessibilityService.performBack())return "برگشت انجام شد.";return "برای Back، دسترسی Accessibility را فعال کنید.";}
  if(l.equals("home")||l.contains("go home")||l.contains("صفحه اصلی")){if(JarvisAccessibilityService.performHome())return "به صفحه اصلی رفتم.";return "برای Home، دسترسی Accessibility را فعال کنید.";}
  AudioManager a=(AudioManager)c.getSystemService(Context.AUDIO_SERVICE);
  if(l.contains("volume up")||l.contains("صدای بیشتر")){a.adjustVolume(AudioManager.ADJUST_RAISE,AudioManager.FLAG_SHOW_UI);return "صدا افزایش یافت.";}
  if(l.contains("volume down")||l.contains("صدای کمتر")){a.adjustVolume(AudioManager.ADJUST_LOWER,AudioManager.FLAG_SHOW_UI);return "صدا کاهش یافت.";}
  if(l.contains("wifi settings")||l.contains("تنظیمات وای فای")){c.startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));return "تنظیمات Wi‑Fi باز شد.";}
  if(l.contains("bluetooth settings")||l.contains("تنظیمات بلوتوث")){c.startActivity(new Intent(Settings.ACTION_BLUETOOTH_SETTINGS).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));return "تنظیمات Bluetooth باز شد.";}
  if(l.startsWith("open ")||l.startsWith("باز کن ")){String name=l.substring(l.indexOf(' ')+1).trim();Intent i=c.getPackageManager().getLaunchIntentForPackage(resolve(c,name));if(i!=null){c.startActivity(i);return "باز شد: "+name;}return "برنامه پیدا نشد: "+name;}
  return null;
 }
 private static String resolve(Context c,String n){if(n.contains("youtube"))return "com.google.android.youtube";if(n.contains("chrome"))return "com.android.chrome";if(n.contains("telegram"))return "org.telegram.messenger";if(n.contains("settings")||n.contains("تنظیمات"))return "com.android.settings";return n;}
}