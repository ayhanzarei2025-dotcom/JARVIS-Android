package com.jarvis.ai;
import android.app.*;
import android.content.*;
import android.os.*;
public class JarvisVoiceService extends Service {
 public int onStartCommand(Intent i,int flags,int id){
  String ch="jarvis_voice";
  NotificationManager n=(NotificationManager)getSystemService(NOTIFICATION_SERVICE);
  if(Build.VERSION.SDK_INT>=26)n.createNotificationChannel(new NotificationChannel(ch,"JARVIS Voice",NotificationManager.IMPORTANCE_LOW));
  Notification x=new Notification.Builder(this,ch).setContentTitle("JARVIS 1.021").setContentText("Voice service active").setSmallIcon(android.R.drawable.ic_btn_speak_now).build();
  if(Build.VERSION.SDK_INT>=29)startForeground(21,x,android.content.pm.ServiceInfo.FOREGROUND_SERVICE_TYPE_MICROPHONE);else startForeground(21,x);
  return START_STICKY;
 }
 public IBinder onBind(Intent i){return null;}
}