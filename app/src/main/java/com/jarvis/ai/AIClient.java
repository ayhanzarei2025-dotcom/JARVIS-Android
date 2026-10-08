package com.jarvis.ai;
import android.os.*;import android.util.Base64;import org.json.*;import java.io.*;import java.net.*;import java.nio.charset.StandardCharsets;

public class AIClient{
 public interface Callback{void done(String x);}
 String endpoint="https://api.openai.com/v1/chat/completions",key="",model="gpt-4o-mini",authHeader="Authorization",authPrefix="Bearer";
 public void configure(String e,String k,String m){configure(e,k,m,"Authorization","Bearer");}
 public void configure(String e,String k,String m,String h,String p){
  if(e!=null&&!e.trim().isEmpty())endpoint=normalizeEndpoint(e.trim());
  key=k==null?"":k.trim();
  if(m!=null&&!m.trim().isEmpty())model=m.trim();
  authHeader=h==null||h.trim().isEmpty()?"Authorization":h.trim();
  authPrefix=p==null?"":p.trim();
 }
 String normalizeEndpoint(String e){
  if(e.endsWith("/chat/completions"))return e;
  if(e.endsWith("/"))e=e.substring(0,e.length()-1);
  if(e.endsWith("/v1"))return e+"/chat/completions";
  if(e.endsWith("/api"))return e+"/v1/chat/completions";
  return e;
 }
 public void ask(String p,Callback cb){post(p,null,cb);}
 public void vision(String p,byte[] j,Callback cb){post(p,j,cb);}
 void post(String p,byte[] img,Callback cb){new Thread(()->{String out;HttpURLConnection c=null;try{
  c=(HttpURLConnection)new URL(endpoint).openConnection();c.setRequestMethod("POST");c.setDoOutput(true);c.setConnectTimeout(15000);c.setReadTimeout(60000);
  c.setRequestProperty("Content-Type","application/json");
  if(!key.isEmpty())c.setRequestProperty(authHeader,authPrefix.isEmpty()?key:authPrefix+" "+key);
  JSONObject b=new JSONObject();b.put("model",model);JSONArray ms=new JSONArray();JSONObject m=new JSONObject().put("role","user");
  if(img==null)m.put("content",p);else{JSONArray a=new JSONArray();a.put(new JSONObject().put("type","text").put("text",p));a.put(new JSONObject().put("type","image_url").put("image_url",new JSONObject().put("url","data:image/jpeg;base64,"+Base64.encodeToString(img,Base64.NO_WRAP))));m.put("content",a);}
  ms.put(m);b.put("messages",ms);
  try(OutputStream o=c.getOutputStream()){o.write(b.toString().getBytes(StandardCharsets.UTF_8));}
  int code=c.getResponseCode();InputStream is=code>=200&&code<300?c.getInputStream():c.getErrorStream();BufferedReader br=new BufferedReader(new InputStreamReader(is,StandardCharsets.UTF_8));StringBuilder s=new StringBuilder();String z;while((z=br.readLine())!=null)s.append(z);
  if(code<200||code>=300)out="AI error "+code+": "+s;else out=new JSONObject(s.toString()).getJSONArray("choices").getJSONObject(0).getJSONObject("message").optString("content",s.toString());
 }catch(Exception e){out="AI connection error: "+e.getMessage();}finally{if(c!=null)c.disconnect();}String x=out;new Handler(Looper.getMainLooper()).post(()->cb.done(x));}).start();}
}