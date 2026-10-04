package com.jarvis.ai;
import android.content.*;import android.database.sqlite.*;import android.database.Cursor;
public class ConversationStore extends SQLiteOpenHelper{
 public ConversationStore(Context c){super(c,"jarvis.db",null,2);}public void onCreate(SQLiteDatabase d){d.execSQL("CREATE TABLE messages(id INTEGER PRIMARY KEY AUTOINCREMENT, role TEXT, text TEXT, ts INTEGER)");d.execSQL("CREATE TABLE facts(id INTEGER PRIMARY KEY AUTOINCREMENT, text TEXT, ts INTEGER)");}
 public void onUpgrade(SQLiteDatabase d,int a,int b){if(a<2)d.execSQL("CREATE TABLE facts(id INTEGER PRIMARY KEY AUTOINCREMENT, text TEXT, ts INTEGER)");}
 public void add(String role,String text){getWritableDatabase().execSQL("INSERT INTO messages(role,text,ts) VALUES(?,?,?)",new Object[]{role,text,System.currentTimeMillis()});}
 public void remember(String text){getWritableDatabase().execSQL("INSERT INTO facts(text,ts) VALUES(?,?)",new Object[]{text,System.currentTimeMillis()});}
 public String recent(int n){StringBuilder s=new StringBuilder();Cursor c=getReadableDatabase().rawQuery("SELECT role,text FROM messages ORDER BY id DESC LIMIT "+Math.max(1,n),null);try{while(c.moveToNext())s.insert(0,c.getString(0)+": "+c.getString(1)+"\n");}finally{c.close();}return s.toString();}
 public String context(){StringBuilder s=new StringBuilder("LONG-TERM FACTS:\n");Cursor f=getReadableDatabase().rawQuery("SELECT text FROM facts ORDER BY id DESC LIMIT 40",null);try{while(f.moveToNext())s.append("- ").append(f.getString(0)).append("\n");}finally{f.close();}s.append("RECENT:\n").append(recent(16));return s.toString();}
 public void clear(){getWritableDatabase().execSQL("DELETE FROM messages");}
}