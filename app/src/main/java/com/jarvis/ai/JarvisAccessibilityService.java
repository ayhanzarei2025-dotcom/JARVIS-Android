package com.jarvis.ai;
import android.accessibilityservice.AccessibilityService;
import android.view.accessibility.AccessibilityEvent;
public class JarvisAccessibilityService extends AccessibilityService {
 static JarvisAccessibilityService instance;
 public void onServiceConnected(){instance=this;}
 public void onAccessibilityEvent(AccessibilityEvent e){}
 public void onInterrupt(){}
 public static void performGlobal(int action){
  if(instance==null)return;
  if(action==1)instance.performGlobalAction(GLOBAL_ACTION_BACK);
  else if(action==2)instance.performGlobalAction(GLOBAL_ACTION_HOME);
  else if(action==3)instance.performGlobalAction(GLOBAL_ACTION_RECENTS);
 }
}