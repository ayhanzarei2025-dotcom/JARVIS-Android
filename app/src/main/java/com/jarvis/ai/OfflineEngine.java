package com.jarvis.ai;
import android.content.*;import android.os.BatteryManager;import java.text.*;import java.util.*;
public final class OfflineEngine{
 private final Context c;public OfflineEngine(Context x){c=x;}
 public String answer(String q){String l=q.trim().toLowerCase(new Locale("fa","IR")).replace("؟","").trim();
  if(l.matches(".*(سلام|درود|hello|hi).*"))return "سلام! من جارویس هستم. آماده‌ام.";
  if(l.matches(".*(حالت چطوره|حالت خوبه|خوبی|چه خبر).*"))return "خوبم، همه هسته‌ها فعال‌اند و آماده‌ام کمک کنم.";
  if(l.matches(".*(امروز چه روزیه|امروز چه روزی است|روز هفته).*"))return "امروز "+new SimpleDateFormat("EEEE، d MMMM yyyy",new Locale("fa","IR")).format(new Date())+" است.";
  if(l.matches(".*(امروز چندمه|تاریخ امروز|تاریخ چنده).*"))return "امروز "+new SimpleDateFormat("d MMMM yyyy",new Locale("fa","IR")).format(new Date())+" است.";
  if(l.matches(".*(ساعت چنده|الان ساعت|زمان).*"))return "الان ساعت "+new SimpleDateFormat("HH:mm:ss",new Locale("fa","IR")).format(new Date())+" است.";
  if(l.matches(".*(باتری|شارژ).*")){BatteryManager b=(BatteryManager)c.getSystemService(Context.BATTERY_SERVICE);return "شارژ دستگاه "+b.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY)+" درصد است.";}
  if(l.matches(".*(هوا چطوره|آب و هوا|هوا امروز|دمای هوا).*"))return "برای وضعیت واقعی و لحظه‌ای هوا باید به دادهٔ هواشناسی دسترسی داشته باشم؛ در حالت کاملاً آفلاین دمای بیرون را جعل نمی‌کنم.";
  String math=math(l);if(math!=null)return math;
  if(l.contains("فتوسنتز"))return "فتوسنتز فرایندی است که گیاهان با نور، آب و دی‌اکسیدکربن مواد آلی می‌سازند و معمولاً اکسیژن آزاد می‌کنند.";
  if(l.contains("جاذبه"))return "جاذبه نیروی جذب بین جرم‌هاست و باعث می‌شود اجسام نزدیک زمین به سمت زمین شتاب بگیرند.";
  if(l.contains("اتم چیست")||l.equals("اتم"))return "اتم واحد بنیادی یک عنصر است و از هستهٔ شامل پروتون و نوترون و الکترون‌ها تشکیل می‌شود.";
  if(l.contains("سلول چیست")||l.equals("سلول"))return "سلول کوچک‌ترین واحد بنیادی ساختار و عملکرد بیشتر جانداران است.";
  if(l.contains("انرژی چیست")||l.equals("انرژی"))return "انرژی توانایی ایجاد تغییر یا انجام کار است و شکل‌هایی مانند حرکتی، پتانسیل، گرمایی و شیمیایی دارد.";
  if(l.contains("نیرو چیست")||l.equals("نیرو"))return "نیرو برهم‌کنشی است که می‌تواند حرکت یا شکل جسم را تغییر دهد و با واحد نیوتن سنجیده می‌شود.";
  if(l.contains("dna")||l.contains("دی ان ای"))return "DNA مولکول حامل اطلاعات ژنتیکی بیشتر جانداران است.";
  if(l.contains("مولکول چیست"))return "مولکول مجموعه‌ای از دو یا چند اتم پیوندخورده است.";
  if(l.contains("آفلاین"))return "حالت آفلاین فعال است؛ محاسبات، پاسخ‌های پایه و مدل زبانی محلی بدون اینترنت در دسترس‌اند.";
  return null;
 }
 private String math(String q){try{String s=q.replace("جمع","").replace("منها","-").replace("ضرب","*").replace("تقسیم","/").replace("به علاوه","+").replace("بعلاوه","+").replace('×','*').replace('÷','/').replace('−','-').replaceAll("[^0-9+*/. -]"," ").replaceAll("\s+"," ").trim();if(!s.matches(".*[+*/-].*"))return null;String[] a=s.split("\s*[+*/-]\s*");if(a.length!=2)return null;int x=Integer.parseInt(a[0]),y=Integer.parseInt(a[1]);if(x<0||x>999||y<0||y>999)return null;char op=s.replaceAll("[0-9 .]","").charAt(0);if(op=='/'&&y==0)return "تقسیم بر صفر ممکن نیست.";long z=op=='+'?x+y:op=='-'?x-y:op=='*'?(long)x*y:x/y;return "پاسخ: "+z;}catch(Exception e){return null;}}
}