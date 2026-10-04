package com.jarvis.ai;
import java.util.*;
public final class SafetyEngine{
 public enum Verdict{ALLOW,BLOCK}
 public static final class Result{public final Verdict verdict;public final String message;Result(Verdict v,String m){verdict=v;message=m;}}
 private static final String[] explicit={"+18","پورنو","سکس","sexual","nude","nudes","porn"};
 private static final String[] harm={ "چطور فلانی را بکشم","چطور یک نفر را بکشم","چگونه فلانی را بکشم","بهترین روش کشتن","چگونه بکشم","چطور بکشم","چگونه سلاح بسازم","چطور سلاح بسازم","ساخت بمب","ساخت مواد منفجره","چه موادی برای ساخت موشک","مواد لازم برای ساخت موشک","چگونه موشک بسازم","چطور موشک بسازم","how to kill","how do i kill","how to make a bomb","how to build a weapon","how to make a weapon"};
 public static Result check(String text){
  String s=text==null?"":text.toLowerCase(Locale.ROOT).replace("‌"," ").trim();
  for(String x:explicit)if(s.contains(x))return new Result(Verdict.BLOCK,"این نوع محتوای +18 در JARVIS در دسترس نیست.");
  for(String x:harm)if(s.contains(x))return new Result(Verdict.BLOCK,"نمی‌توانم برای کشتن، ساخت سلاح یا ساخت مواد منفجره دستورالعمل عملی ارائه کنم؛ اما درباره جنبه‌های علمی، تاریخی یا ایمنی می‌توانم توضیح بدهم.");
  return new Result(Verdict.ALLOW,"");
 }
 public static String output(String text){
  Result r=check(text);return r.verdict==Verdict.BLOCK?r.message:text;
 }
}