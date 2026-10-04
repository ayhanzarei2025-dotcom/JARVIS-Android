package com.jarvis.ai;
import android.app.*;
import android.content.*;
import android.os.*;
public final class PerformanceManager{
 public enum Mode{BATTERY,BALANCED,PERFORMANCE,TURBO}
 private final Context c;private Mode mode=Mode.BALANCED;
 public PerformanceManager(Context x){c=x;}
 public Mode mode(){return mode;}
 public void setMode(Mode m){mode=m;}
 public int gpuLayers(){switch(mode){case BATTERY:return 4;case PERFORMANCE:return 24;case TURBO:return 32;default:return 12;}}
 public int visionIntervalMs(){switch(mode){case BATTERY:return 3500;case PERFORMANCE:return 1200;case TURBO:return 800;default:return 2000;}}
 public String label(){return mode.name();}
 public String status(){BatteryManager b=(BatteryManager)c.getSystemService(Context.BATTERY_SERVICE);int battery=b.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY);return "⚡ "+label()+" • GPU layers "+gpuLayers()+" • 🔋 "+battery+"%";}
}