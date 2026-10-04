package com.jarvis.ai;
import android.os.Handler; import android.os.Looper; import java.io.*; import java.net.*; import org.json.*;
public class AIClient {
 public interface Callback { void done(String text); }
 private String endpoint=""; private String apiKey="";
 public void configure(String e,String k){endpoint=e==null?"":e.trim();apiKey=k==null?"":k.trim();}
 public void ask(String prompt,Callback cb){
  new Thread(()->{
   String result;
   try{
    if(endpoint.isEmpty()) result="AI provider is not configured. Use AI SETUP.";
    else {
     HttpURLConnection c=(HttpURLConnection)new URL(endpoint).openConnection();
     c.setRequestMethod("POST"); c.setDoOutput(true); c.setConnectTimeout(15000); c.setReadTimeout(30000);
     c.setRequestProperty("Content-Type","application/json");
     if(!apiKey.isEmpty()) c.setRequestProperty("Authorization","Bearer "+apiKey);
     JSONObject body=new JSONObject(); body.put("prompt",prompt);
     try(OutputStream os=c.getOutputStream()){os.write(body.toString().getBytes("UTF-8"));}
     int code=c.getResponseCode();
     InputStream stream=code>=400?c.getErrorStream():c.getInputStream();
     BufferedReader br=new BufferedReader(new InputStreamReader(stream));
     StringBuilder s=new StringBuilder(); String l; while((l=br.readLine())!=null)s.append(l);
     result=s.toString(); c.disconnect();
    }
   }catch(Exception e){result="AI connection error: "+e.getMessage();}
   final String response=result;
   new Handler(Looper.getMainLooper()).post(()->cb.done(response));
  }).start();
 }
}
