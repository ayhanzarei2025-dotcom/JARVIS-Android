package com.jarvis.ai;
import android.content.*;import android.speech.tts.TextToSpeech;import java.util.*;
public class LanguageManager implements TextToSpeech.OnInitListener{
 private TextToSpeech tts;private String code="fa";public LanguageManager(Context c){tts=new TextToSpeech(c,this);}
 public void onInit(int s){if(s==TextToSpeech.SUCCESS){tts.setLanguage(new Locale("fa","IR"));tts.setSpeechRate(.95f);}}
 public void speak(String x,float rate){if(tts!=null&&!x.isEmpty()){tts.setSpeechRate(rate);tts.speak(x,TextToSpeech.QUEUE_FLUSH,null,"jarvis");}}
 public void setLanguage(String c){code=c;if(tts!=null)tts.setLanguage(c.equals("fa")?new Locale("fa","IR"):Locale.US);}
 public String getLanguage(){return code;}
}