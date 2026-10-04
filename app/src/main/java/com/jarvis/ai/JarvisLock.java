package com.jarvis.ai;
import java.security.*; import java.nio.charset.StandardCharsets;
public class JarvisLock{
 private static final String HASH="a6d4c0b9d8b9b7e6d0d7b3e5a4b0e0c2f2d0f0e9d0f3a7c1c1f3b2b7a5f1b2";
 public static boolean check(String p){return p!=null && sha(p).equals(HASH);}
 private static String sha(String s){try{byte[] b=MessageDigest.getInstance("SHA-256").digest(s.getBytes(StandardCharsets.UTF_8));StringBuilder x=new StringBuilder();for(byte v:b)x.append(String.format("%02x",v));return x.toString();}catch(Exception e){return "";}}
}
