package com.jarvis.ai;
import android.content.*;import android.os.BatteryManager;import java.text.*;import java.util.*;import java.util.regex.*;
public final class OfflineEngine{
 private final Context c;public OfflineEngine(Context x){c=x;}
 public String answer(String q){String l=q.trim().toLowerCase(Locale.ROOT);
  if(l.matches(".*(سلام|hello|hi).*"))return "سلام! من جارویس هستم. آماده‌ام.";
  if(l.matches(".*(حالت چطوره|خوبی|چه خبر).*"))return "عالی‌ام و آماده‌ام که کمکت کنم.";
  if(l.matches(".*(امروز چه روزیه|امروز چندمه|تاریخ امروز|امروز).*")&&l.matches(".*(روز|تاریخ|چندمه).*"))return "امروز "+new SimpleDateFormat("EEEE، d MMMM yyyy",new Locale("fa","IR")).format(new Date())+" است.";
  if(l.matches(".*(ساعت چنده|ساعت).*"))return "الان ساعت "+new SimpleDateFormat("HH:mm:ss",Locale.getDefault()).format(new Date())+" است.";
  if(l.matches(".*(باتری|شارژ).*")){BatteryManager b=(BatteryManager)c.getSystemService(Context.BATTERY_SERVICE);return "شارژ دستگاه "+b.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY)+" درصد است.";}
  if(l.matches(".*(هوا چطوره|آب و هوا|هوا).*"))return "برای آب‌وهوای لحظه‌ای به اینترنت یا دادهٔ هواشناسی ذخیره‌شده نیاز دارم؛ در حالت کاملاً آفلاین نمی‌توانم دمای واقعی بیرون را حدس بزنم.";
  String math=math(l);if(math!=null)return math;
  if(l.contains("فتوسنتز"))return "فتوسنتز فرایندی است که گیاهان با کمک نور، آب و دی‌اکسیدکربن مواد آلی می‌سازند و اکسیژن آزاد می‌کنند.";
  if(l.contains("جاذبه"))return "جاذبه نیرویی است که اجسام دارای جرم را به سوی یکدیگر جذب می‌کند.";
  if(l.contains("اتم"))return "اتم کوچک‌ترین واحد معمول یک عنصر است و از هسته و الکترون‌ها تشکیل شده است.";
  if(l.contains("سلول"))return "سلول واحد بنیادی ساختار و عملکرد جانداران است.";
  if(l.contains("انرژی"))return "انرژی توانایی انجام کار یا ایجاد تغییر است و شکل‌های مختلفی مانند حرکتی، پتانسیل، گرمایی و شیمیایی دارد.";
  if(l.contains("آفلاین"))return "حالت آفلاین فعال است و پاسخ‌های پایه، محاسبات و دانش علمی ذخیره‌شده بدون اینترنت کار می‌کنند.";
  return null;
 }
 private String math(String q){try{
  String s=q.replace('×','*').replace('÷','/').replace('−','-').replaceAll("[^0-9+*/. -]","");
  if(!s.matches(".*[+*/-].*"))return null;String[] a=s.trim().split("\\s*[+*/-]\\s*");if(a.length!=2)return null;
  int x=Integer.parseInt(a[0]),y=Integer.parseInt(a[1]);if(x<0||x>999||y<0||y>999)return null;char op=s.replaceAll("[0-9 .]","").charAt(0);long z=op=='+'?x+y:op=='-'?x-y:op=='*'?(long)x*y:y==0?Long.MIN_VALUE:x/y;return z==Long.MIN_VALUE?"تقسیم بر صفر ممکن نیست.":"پاسخ: "+z;
 }catch(Exception e){return null;}}
}