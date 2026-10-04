package com.jarvis.ai;
import android.content.*; import android.database.Cursor; import android.database.sqlite.*;
public class NotificationStore extends SQLiteOpenHelper{
 public NotificationStore(Context c){super(c,"notifications.db",null,1);}
 public void onCreate(SQLiteDatabase d){d.execSQL("CREATE TABLE n(id INTEGER PRIMARY KEY AUTOINCREMENT, app TEXT, title TEXT, text TEXT, ts INTEGER)");}
 public void onUpgrade(SQLiteDatabase d,int a,int b){}
 public void add(String app,String title,String text){getWritableDatabase().execSQL("INSERT INTO n(app,title,text,ts) VALUES(?,?,?,?)",new Object[]{app,title,text,System.currentTimeMillis()});}
 public String briefing(){StringBuilder s=new StringBuilder(); Cursor c=getReadableDatabase().rawQuery("SELECT app,title,text FROM n ORDER BY id DESC LIMIT 20",null); try{while(c.moveToNext())s.append(c.getString(0)).append(" — ").append(c.getString(1)).append(": ").append(c.getString(2)).append("\n");}finally{c.close();} return s.length()==0?"No recent notifications.":s.toString();}
}
