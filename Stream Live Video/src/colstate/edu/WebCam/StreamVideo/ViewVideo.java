package colstate.edu.WebCam.StreamVideo;

import java.io.IOException;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.hardware.Camera;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class ViewVideo extends SurfaceView implements SurfaceHolder.Callback{

	SurfaceHolder mHolder;
	Camera mCamera;
	
	ViewVideo(Context context) {  
		super(context);        
	// Install a SurfaceHolder.Callback so we get notified when the        
	// underlying surface is created and destroyed.        
	mHolder = getHolder();        
	mHolder.addCallback(this);        
	mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);    
	}
	
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		// TODO Auto-generated method stub
		
	}

	public void surfaceCreated(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		  // The Surface has been created, acquire the camera and tell it where        
		// to draw.        
		mCamera = Camera.open();        
		try {           
			mCamera.setPreviewDisplay(holder); 
			mCamera.startPreview();
			} 
		catch (IOException exception) {          
			mCamera.release();        
			mCamera = null;       
			// TODO: add more exception handling logic here        
		}
		
	}

	public void surfaceDestroyed(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		
	}


}
