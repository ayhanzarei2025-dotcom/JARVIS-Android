package com.jarvis.ai;
import android.Manifest; import android.app.*; import android.content.*; import android.content.pm.PackageManager; import android.hardware.camera2.*; import android.view.SurfaceView;
public class CameraController{
 private CameraDevice cam;
 public void open(Activity a,SurfaceView v){if(a.checkSelfPermission(Manifest.permission.CAMERA)!=PackageManager.PERMISSION_GRANTED){a.requestPermissions(new String[]{Manifest.permission.CAMERA},42);return;} CameraManager m=(CameraManager)a.getSystemService(Context.CAMERA_SERVICE);try{String id=m.getCameraIdList()[0];m.openCamera(id,new CameraDevice.StateCallback(){public void onOpened(CameraDevice c){cam=c;}public void onDisconnected(CameraDevice c){c.close();}public void onError(CameraDevice c,int e){c.close();}},null);}catch(Exception ignored){}}
 public void close(){if(cam!=null){cam.close();cam=null;}}
}
