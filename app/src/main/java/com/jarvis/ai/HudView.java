package com.jarvis.ai;
import android.content.*; import android.graphics.*; import android.view.*;
public class HudView extends View{
 private final Paint p=new Paint(1); public HudView(Context c,android.util.AttributeSet a){super(c,a);p.setStyle(Paint.Style.STROKE);p.setStrokeWidth(3);}
 protected void onDraw(Canvas c){super.onDraw(c);p.setColor(Color.CYAN);float x=getWidth()/2f,y=getHeight()/2f;c.drawCircle(x,y,90,p);c.drawLine(x-130,y,x+130,y,p);c.drawLine(x,y-130,x,y+130,p);p.setStyle(Paint.Style.FILL);p.setTextSize(26);c.drawText("JARVIS",30,50,p);p.setStyle(Paint.Style.STROKE);}
}
