package com.jarvis.ai;
import java.text.*;import java.util.*;import java.util.regex.*;
public final class OfflineEngine{
 public static String answer(String q){String l=q.trim().toLowerCase(Locale.ROOT);
  if(l.matches("سلام|hello|hi|hey jarvis|درود"))return "سلام. JARVIS آنلاین است و در حالت آفلاین هم آماده‌ام.";
  if(l.matches(".*(who are you|what are you|تو کی هستی|خودت را معرفی کن).*"))return "من JARVIS هستم؛ هسته آفلاین من برای فرمان‌ها و پرسش‌های ساده فعال است.";
  if(l.matches(".*(time|ساعت).*"))return new SimpleDateFormat("HH:mm:ss",Locale.US).format(new Date());
  if(l.matches(".*(date|today|تاریخ|امروز).*"))return new SimpleDateFormat("yyyy-MM-dd",Locale.US).format(new Date());
  if(l.equals("status")||l.contains("وضعیت"))return "OFFLINE CORE: ONLINE • SIMPLE QA: READY • MEMORY: READY • ACTIONS: READY";
  if(l.equals("help")||l.contains("کمک"))return "آفلاین: ساعت، تاریخ، محاسبه، وضعیت و چند پاسخ ساده. برای پاسخ‌های باز به اینترنت و مدل زبانی نیاز است.";
  String math=q.replaceAll("[^0-9+\\-*/(). ]","").trim();
  if(math.matches("[0-9+\\-*/(). ]+")&&math.matches(".*[+\\-*/].*"))try{return "نتیجه: "+calc(math);}
  catch(Exception ignored){}
  return null;
 }
 private static double calc(String s){return new Object(){int p=-1;char c;void n(){c=++p<s.length()?s.charAt(p):'\0';}double e(){n();double x=t();while(c=='+'||c=='-'){char o=c;n();double y=t();x=o=='+'?x+y:x-y;}return x;}double t(){double x=f();while(c=='*'||c=='/'){char o=c;n();double y=f();x=o=='*'?x*y:x/y;}return x;}double f(){if(c=='('){n();double x=e();if(c==')')n();return x;}int st=p;while((c>='0'&&c<='9')||c=='.'){n();}return Double.parseDouble(s.substring(st,p));}}.e();}
}