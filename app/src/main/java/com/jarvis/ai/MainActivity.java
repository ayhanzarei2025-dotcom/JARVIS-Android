package com.jarvis.ai;
import android.app.*; import android.os.*; import android.content.*; import android.content.pm.PackageManager; import android.speech.*; import android.view.*; import android.widget.*; import java.util.*;
public class MainActivity extends Activity{
 static{System.loadLibrary("jarviscore");}
 private native String nativeProcess(String input);
 private TextView chat,status; private EditText input; private ConversationStore memory; private NotificationStore notes; private AIClient ai; private LanguageManager voice; private CameraController camera; private View hud;
 protected void onCreate(Bundle b){super.onCreate(b);setContentView(R.layout.activity_main);
  chat=findViewById(R.id.chat);status=findViewById(R.id.status);input=findViewById(R.id.input);hud=findViewById(R.id.hudOverlay);memory=new ConversationStore(this);notes=new NotificationStore(this);ai=new AIClient();voice=new LanguageManager(this);camera=new CameraController();
  findViewById(R.id.send).setOnClickListener(v->send()); findViewById(R.id.voice).setOnClickListener(v->listen()); findViewById(R.id.stop).setOnClickListener(v->voice.speak("",1)); findViewById(R.id.memory).setOnClickListener(v->show("MEMORY",memory.recent(30))); findViewById(R.id.briefing).setOnClickListener(v->show("BRIEFING",notes.briefing()));
  findViewById(R.id.camera).setOnClickListener(v->{hud.setVisibility(hud.getVisibility()==View.VISIBLE?View.GONE:View.VISIBLE);});
  findViewById(R.id.notifications).setOnClickListener(v->startActivity(new Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS")));
  findViewById(R.id.permissions).setOnClickListener(v->requestPermissions(new String[]{android.Manifest.permission.CAMERA,android.Manifest.permission.RECORD_AUDIO},7));
  findViewById(R.id.capture).setOnClickListener(v->show("VISION","Camera capture pipeline is ready; connect an AI vision endpoint in the next build."));
  findViewById(R.id.settings).setOnClickListener(v->setupAI());
  status.setText("CORE ONLINE • C++/JNI READY"); append("JARVIS","Ready.");
 }
 private void send(){String q=input.getText().toString().trim();if(q.isEmpty())return;input.setText("");append("YOU",q);memory.add("user",q);String l=q.toLowerCase(Locale.ROOT);
  if(l.startsWith("search ")||l.startsWith("web ")){WebSearch.open(this,q.substring(q.indexOf(' ')+1));append("JARVIS","Opening web search.");return;}
  String local=nativeProcess(q); if(!local.startsWith("OPEN-DOMAIN")){append("JARVIS",local);memory.add("jarvis",local);voice.speak(local,1);return;}
  ai.ask(q+"\nContext:\n"+memory.recent(12),x->{append("JARVIS",x);memory.add("assistant",x);voice.speak(x,1);});
 }
 private void listen(){if(checkSelfPermission(android.Manifest.permission.RECORD_AUDIO)!=PackageManager.PERMISSION_GRANTED){requestPermissions(new String[]{android.Manifest.permission.RECORD_AUDIO},8);return;} Intent i=new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);i.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);startActivityForResult(i,99);}
 protected void onActivityResult(int r,int c,Intent d){super.onActivityResult(r,c,d);if(r==99&&c==RESULT_OK&&d!=null){ArrayList<String>x=d.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);if(x!=null&&!x.isEmpty()){input.setText(x.get(0));send();}}}
 private void append(String who,String x){chat.append("\n"+who+": "+x+"\n");}
 private void show(String t,String x){new AlertDialog.Builder(this).setTitle(t).setMessage(x).setPositiveButton("OK",null).show();}
 private void setupAI(){LinearLayout l=new LinearLayout(this);l.setOrientation(LinearLayout.VERTICAL);EditText e=new EditText(this);e.setHint("AI endpoint URL");EditText k=new EditText(this);k.setHint("API key (optional)");l.addView(e);l.addView(k);new AlertDialog.Builder(this).setTitle("AI SETUP").setView(l).setPositiveButton("SAVE",(d,w)->{ai.configure(e.getText().toString(),k.getText().toString());show("JARVIS","AI provider configured.");}).setNegativeButton("CANCEL",null).show();}
}
