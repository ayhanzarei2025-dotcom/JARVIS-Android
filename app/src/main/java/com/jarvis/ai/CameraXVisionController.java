package com.jarvis.ai;
import com.google.common.util.concurrent.ListenableFuture;
import android.graphics.*;
import androidx.camera.core.*;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import java.io.*;
import java.util.concurrent.*;
public final class CameraXVisionController{
 private final AppCompatActivity a;private final PreviewView v;private ImageCapture capture;private ProcessCameraProvider provider;private ScheduledExecutorService scheduler;private volatile boolean live=false;private AIClient liveAi;private AIClient.Callback liveCb;private int intervalMs=2000;
 public CameraXVisionController(AppCompatActivity x,PreviewView p){a=x;v=p;}
 public void start(){start(2000);}
 public void start(int interval){intervalMs=interval;if(a.checkSelfPermission(android.Manifest.permission.CAMERA)!=0){a.requestPermissions(new String[]{android.Manifest.permission.CAMERA},42);return;}ListenableFuture<ProcessCameraProvider> f=ProcessCameraProvider.getInstance(a);f.addListener(()->{try{provider=f.get();Preview preview=new Preview.Builder().build();capture=new ImageCapture.Builder().setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY).setJpegQuality(70).build();preview.setSurfaceProvider(v.getSurfaceProvider());provider.unbindAll();provider.bindToLifecycle(a,CameraSelector.DEFAULT_BACK_CAMERA,preview,capture);}catch(Exception ignored){}},ContextCompat.getMainExecutor(a));}
 public void startLive(AIClient ai,AIClient.Callback cb,int interval){liveAi=ai;liveCb=cb;live=true;intervalMs=interval;start(interval);if(scheduler!=null)scheduler.shutdownNow();scheduler=Executors.newSingleThreadScheduledExecutor();scheduler.scheduleWithFixedDelay(this::analyzeFrame,1200,intervalMs,TimeUnit.MILLISECONDS);}
 private void analyzeFrame(){if(!live||capture==null||liveAi==null)return;File file=new File(a.getCacheDir(),"jarvis_live.jpg");ImageCapture.OutputFileOptions o=new ImageCapture.OutputFileOptions.Builder(file).build();capture.takePicture(o,ContextCompat.getMainExecutor(a),new ImageCapture.OnImageSavedCallback(){public void onImageSaved(ImageCapture.OutputFileResults r){try{byte[] b=read(file);liveAi.vision("این فریم زنده دوربین JARVIS است. فقط موارد مهم و قابل مشاهده را کوتاه به فارسی بگو.",b,liveCb);}catch(Exception e){}}public void onError(ImageCaptureException e){}});}
 public void shot(AIClient ai,AIClient.Callback cb){if(capture==null){cb.done("دوربین آماده نیست.");return;}File file=new File(a.getCacheDir(),"vision.jpg");ImageCapture.OutputFileOptions o=new ImageCapture.OutputFileOptions.Builder(file).build();capture.takePicture(o,ContextCompat.getMainExecutor(a),new ImageCapture.OnImageSavedCallback(){public void onImageSaved(ImageCapture.OutputFileResults r){try{ai.vision("این تصویر را ببین و موارد مهم را فقط به فارسی توضیح بده.",read(file),cb);}catch(Exception e){cb.done("خواندن تصویر ناموفق بود.");}}public void onError(ImageCaptureException e){cb.done("گرفتن تصویر ناموفق بود: "+e.getMessage());}});}
 private byte[] read(File f)throws Exception{ByteArrayOutputStream o=new ByteArrayOutputStream();try(InputStream i=new FileInputStream(f)){byte[] b=new byte[8192];int n;while((n=i.read(b))>0)o.write(b,0,n);}return o.toByteArray();}
 public void stop(){live=false;if(scheduler!=null){scheduler.shutdownNow();scheduler=null;}if(provider!=null)provider.unbindAll();v.setVisibility(android.view.View.GONE);}
}