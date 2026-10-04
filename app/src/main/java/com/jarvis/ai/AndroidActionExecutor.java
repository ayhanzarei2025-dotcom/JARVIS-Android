package com.jarvis.ai;
import android.content.Context;
import android.content.Intent;
import android.provider.Settings;
public final class AndroidActionExecutor {
 public static String run(Context c,String action) {
  String a=action.trim().toLowerCase();
  try {
   if(a.equals("wifi")) { c.startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)); return "Opening Wi-Fi settings."; }
   if(a.equals("bluetooth")) { c.startActivity(new Intent(Settings.ACTION_BLUETOOTH_SETTINGS).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)); return "Opening Bluetooth settings."; }
   if(a.equals("settings")) { c.startActivity(new Intent(Settings.ACTION_SETTINGS).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)); return "Opening Android settings."; }
   if(a.equals("accessibility")) { c.startActivity(new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)); return "Opening Accessibility settings."; }
   if(a.equals("home")) { JarvisAccessibilityService.performGlobal(2); return "Home command sent."; }
   if(a.equals("back")) { JarvisAccessibilityService.performGlobal(1); return "Back command sent."; }
   if(a.equals("recents")) { JarvisAccessibilityService.performGlobal(3); return "Recents command sent."; }
  } catch(Exception e) { return "Action failed."; }
  return "No matching action.";
 }
}