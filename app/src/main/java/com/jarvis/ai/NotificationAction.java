package com.jarvis.ai;
import android.app.*;import android.content.*;import android.service.notification.*;import android.os.*;import android.widget.*;import android.app.RemoteInput;import java.util.*;
public final class NotificationAction{
 static Notification.Action latest;static String pkg="";
 public static void set(Notification.Action a,String p){latest=a;pkg=p;}
 public static String replyLatest(Context c,String text){try{if(latest==null||latest.actionIntent==null)return "پاسخ قابل ارسال پیدا نشد.";RemoteInput[] rs=latest.getRemoteInputs();if(rs==null||rs.length==0)return "این اعلان قابلیت پاسخ مستقیم ندارد.";Intent i=new Intent();for(RemoteInput r:rs)i.putExtra(r.getResultKey(),text);Bundle b=new Bundle();b.putCharSequence(rs[0].getResultKey(),text);RemoteInput.addResultsToIntent(rs,i,b);latest.actionIntent.send(c,0,i);return "انجام شد.";}catch(Exception e){return "ارسال پاسخ ناموفق بود.";}}
}