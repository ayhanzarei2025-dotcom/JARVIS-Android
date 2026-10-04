package com.jarvis.ai;
import android.content.Context;
public final class ToolRegistry{public String execute(Context c,String action){return AndroidActionExecutor.run(c,action);}}