package com.jarvis.ai;
import android.content.Context;
import android.content.SharedPreferences;
import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKey;

public final class SecureStore {
 private final SharedPreferences prefs;
 public SecureStore(Context c) {
  SharedPreferences p;
  try {
   MasterKey key=new MasterKey.Builder(c).setKeyScheme(MasterKey.KeyScheme.AES256_GCM).build();
   p=EncryptedSharedPreferences.create(c,"jarvis_secure",key,
       EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
       EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM);
  } catch(Exception | LinkageError e) {
   // Recover gracefully if the encrypted preference keyset is damaged on an existing install.
   p=c.getSharedPreferences("jarvis_secure_recovery",Context.MODE_PRIVATE);
  }
  prefs=p;
 }
 public void put(String k,String v){prefs.edit().putString(k,v==null?"":v).apply();}
 public String get(String k,String d){try{return prefs.getString(k,d);}catch(Exception e){return d;}}
}
