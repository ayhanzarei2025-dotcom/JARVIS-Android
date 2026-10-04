package com.jarvis.ai;
import android.content.*; import android.database.sqlite.*; import android.database.Cursor;
public class ConversationStore extends SQLiteOpenHelper{
 public ConversationStore(Context c){super(c,"jarvis.db",null,1);}
 public void onCreate(SQLiteDatabase d){d.execSQL("CREATE TABLE messages(id INTEGER PRIMARY KEY AUTOINCREMENT, role TEXT, text TEXT, ts INTEGER)");}
 public void onUpgrade(SQLiteDatabase d,int a,int b){}
 public void add(String role,String text){getWritableDatabase().execSQL("INSERT INTO messages(role,text,ts) VALUES(?,?,?)",new Object[]{role,text,System.currentTimeMillis()});}
 public String recent(int n){StringBuilder s=new StringBuilder(); Cursor c=getReadableDatabase().rawQuery("SELECT role,text FROM messages ORDER BY id DESC LIMIT "+Math.max(1,n),null); try{while(c.moveToNext())s.insert(0,c.getString(0)+": "+c.getString(1)+"\n");}finally{c.close();} return s.toString();}
 public void clear(){getWritableDatabase().execSQL("DELETE FROM messages");}
}
