package com.jarvis.ai;
import android.service.notification.*;import android.app.Notification;
public class JarvisNotificationListener extends NotificationListenerService{
 public void onNotificationPosted(StatusBarNotification s){if(s.getNotification()==null)return;CharSequence t=s.getNotification().extras.getCharSequence("android.title");CharSequence x=s.getNotification().extras.getCharSequence("android.text");new NotificationStore(this).add(s.getPackageName(),t==null?"":t.toString(),x==null?"":x.toString());Notification.Action[] a=s.getNotification().actions;if(a!=null)for(Notification.Action z:a)if(z.getRemoteInputs()!=null&&z.getRemoteInputs().length>0){NotificationAction.set(z,s.getPackageName());break;}}
}