package com.jarvis.ai;
import android.content.*; import android.speech.tts.TextToSpeech; import java.util.*; import java.util.Locale;
public class LanguageManager implements TextToSpeech.OnInitListener{
 private TextToSpeech tts; public LanguageManager(Context c){tts=new TextToSpeech(c,this);}
 public void onInit(int s){if(s==TextToSpeech.SUCCESS)tts.setLanguage(new Locale("en","US"));}
 public void speak(String x,float rate){if(tts!=null){tts.setSpeechRate(rate);tts.speak(x,TextToSpeech.QUEUE_FLUSH,null,"jarvis");}}
 public void setLanguage(String code){if(tts!=null){Locale l=new Locale(code);tts.setLanguage(l);}}
}
