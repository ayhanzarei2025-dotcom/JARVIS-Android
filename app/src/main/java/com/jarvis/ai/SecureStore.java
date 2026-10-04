package com.jarvis.ai;
import android.content.Context;
import android.content.SharedPreferences;
import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKey;
public final class SecureStore {
 private final SharedPreferences prefs;
 public SecureStore(Context c) throws Exception {
  MasterKey key=new MasterKey.Builder(c).setKeyScheme(MasterKey.KeyScheme.AES256_GCM).build();
  prefs=EncryptedSharedPreferences.create(c,"jarvis_secure",key,
   EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
   EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM);
 }
 public void put(String k,String v){prefs.edit().putString(k,v==null?"":v).apply();}
 public String get(String k,String d){return prefs.getString(k,d);}
}