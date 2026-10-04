package com.jarvis.ai;
import android.content.*;
import java.util.*;
public final class BrainOrchestrator{
 public enum Brain{AUTO,LOCAL,CLOUD,GPT}
 public interface Callback{void done(String text);}
 private final Context c;private final OfflineEngine offline;private final OfflineLlmEngine local;private final AIClient cloud;private Brain mode=Brain.AUTO;
 public BrainOrchestrator(Context x,OfflineEngine o,OfflineLlmEngine l,AIClient a){c=x;offline=o;local=l;cloud=a;}
 public void setMode(Brain b){mode=b;}
 public Brain mode(){return mode;}
 public void ask(String q,String memory,Callback cb){
  SafetyEngine.Result safe=SafetyEngine.check(q);if(safe.verdict==SafetyEngine.Verdict.BLOCK){cb.done(safe.message);return;}
  String quick=offline.answer(q);if(mode==Brain.LOCAL){if(quick!=null){cb.done(quick);return;}local.ask(q,memory,s->cb.done(SafetyEngine.output(s)));return;}
  if(mode==Brain.GPT||mode==Brain.CLOUD){cloud.ask(system(q,memory),s->cb.done(SafetyEngine.output(s)));return;}
  if(quick!=null){cb.done(quick);return;}
  if(q.length()<90&&!q.contains("?")&&!q.contains("؟")){local.ask(q,memory,s->cb.done(SafetyEngine.output(s)));return;}
  cloud.ask(system(q,memory),s->{if(s.startsWith("AI connection error")||s.startsWith("AI error"))local.ask(q,memory,x->cb.done(SafetyEngine.output(x)));else cb.done(SafetyEngine.output(s));});
 }
 private String system(String q,String memory){return "تو JARVIS 2.01 هستی. فارسی پاسخ بده مگر کاربر زبان دیگری بخواهد. پاسخ طبیعی، دقیق و مختصر باشد. درخواست‌های خطرناک را انجام نده. حافظه مرتبط:\n"+memory+"\nکاربر: "+q;}
}