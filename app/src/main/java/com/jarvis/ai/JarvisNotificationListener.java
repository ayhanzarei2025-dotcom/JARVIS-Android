package com.jarvis.ai;
import android.service.notification.NotificationListenerService; import android.service.notification.StatusBarNotification;
public class JarvisNotificationListener extends NotificationListenerService{
 public void onNotificationPosted(StatusBarNotification s){if(s.getNotification()==null)return; CharSequence t=s.getNotification().extras.getCharSequence("android.title"); CharSequence x=s.getNotification().extras.getCharSequence("android.text"); new NotificationStore(this).add(s.getPackageName(),t==null?"":t.toString(),x==null?"":x.toString());}
}
