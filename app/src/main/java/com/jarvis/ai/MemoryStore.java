package com.jarvis.ai;
import android.content.*;import android.database.sqlite.*;import android.database.Cursor;
public final class MemoryStore extends SQLiteOpenHelper{
 public MemoryStore(Context c){super(c,"jarvis_memory.db",null,1);}
 public void onCreate(SQLiteDatabase d){d.execSQL("CREATE TABLE memory(id INTEGER PRIMARY KEY AUTOINCREMENT,type TEXT,content TEXT,created INTEGER)");d.execSQL("CREATE INDEX memory_time ON memory(created)");}
 public void onUpgrade(SQLiteDatabase d,int a,int b){}
 public void add(String t,String s){ContentValues v=new ContentValues();v.put("type",t);v.put("content",s);v.put("created",System.currentTimeMillis());getWritableDatabase().insert("memory",null,v);}
 public String recent(int n){StringBuilder s=new StringBuilder();Cursor c=getReadableDatabase().rawQuery("SELECT type,content FROM memory ORDER BY created DESC LIMIT ?",new String[]{String.valueOf(n)});try{while(c.moveToNext())s.append(c.getString(0)).append(": ").append(c.getString(1)).append("\n");}finally{c.close();}return s.toString();}
}