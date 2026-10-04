package com.jarvis.ai;
import android.content.*;import android.graphics.*;import android.os.*;import android.view.*;
public class HudView extends View{
 Paint p=new Paint(1);float rot=0;
 public HudView(Context c,android.util.AttributeSet a){super(c,a);setLayerType(View.LAYER_TYPE_SOFTWARE,null);post(tick);}
 Runnable tick=new Runnable(){public void run(){rot+=1.2f;invalidate();postDelayed(this,35);}};
 protected void onDraw(Canvas c){super.onDraw(c);float cx=getWidth()/2f,cy=getHeight()*.42f;float s=Math.min(getWidth(),getHeight());float r=s*.13f;
  p.setStyle(Paint.Style.STROKE);p.setStrokeWidth(3);
  p.setColor(Color.argb(55,70,230,255));c.drawCircle(cx,cy,r+72,p);c.drawCircle(cx,cy,r+102,p);
  p.setColor(Color.argb(100,80,240,255));c.drawArc(cx-r-25,cy-r-25,cx+r+25,cy+r+25,rot,80,false,p);c.drawArc(cx-r-25,cy-r-25,cx+r+25,cy+r+25,rot+180,60,false,p);
  Path helmet=new Path();helmet.moveTo(cx-r*1.65f,cy+r*.1f);helmet.quadTo(cx-r*1.45f,cy-r*1.5f,cx,cy-r*1.8f);helmet.quadTo(cx+r*1.45f,cy-r*1.5f,cx+r*1.65f,cy+r*.1f);helmet.lineTo(cx+r*1.25f,cy+r*1.65f);helmet.lineTo(cx-r*1.25f,cy+r*1.65f);helmet.close();p.setColor(Color.argb(115,55,170,205));p.setStyle(Paint.Style.FILL);c.drawPath(helmet,p);
  p.setStyle(Paint.Style.STROKE);p.setStrokeWidth(4);p.setColor(Color.rgb(90,235,255));c.drawPath(helmet,p);
  p.setStrokeWidth(5);p.setColor(Color.rgb(115,250,255));c.drawLine(cx-r*.9f,cy-r*.05f,cx-r*.18f,cy+r*.05f,p);c.drawLine(cx+r*.18f,cy+r*.05f,cx+r*.9f,cy-r*.05f,p);
  p.setStrokeWidth(2);p.setColor(Color.argb(170,90,235,255));c.drawLine(cx,cy+r*.25f,cx,cy+r*.95f,p);c.drawArc(cx-r*.5f,cy+r*.45f,cx+r*.5f,cy+r*1.35f,15,150,false,p);
  p.setTextSize(11);p.setStyle(Paint.Style.FILL);p.setColor(Color.rgb(95,235,255));c.drawText("JARVIS CORE",cx-39,cy-r-125,p);c.drawText("AI",cx-r-120,cy-30,p);c.drawText("VISION",cx+r+75,cy+20,p);c.drawText("MEMORY",cx-r-120,cy+70,p);
 }
}