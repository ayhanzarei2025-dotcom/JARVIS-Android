package com.jarvis.ai;
import android.content.*; import android.net.Uri;
public class WebSearch{public static void open(Context c,String q){try{c.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.com/search?q="+Uri.encode(q))));}catch(Exception ignored){}}}
