package csu.example.video;



import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore.Video;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.VideoView;

public class SendVideo extends Activity implements OnClickListener {
    /** Called when the activity is first created. */
	private static final int CAMERA_VIDEO_REQ=1338;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        View videoButton= findViewById(R.id.button1);
		videoButton.setOnClickListener(this);
    }

	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()){
		case R.id.button1:
			Log.d("start", "Start");
			Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_VIDEO_CAPTURE);
			startActivityForResult(cameraIntent, CAMERA_VIDEO_REQ);
			break;
		}
		
	}
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {   
	  Log.d("Before if", " Aboout to start If");
		if (requestCode == CAMERA_VIDEO_REQ) {   
		   Log.d("URI", data.getData().toString());
		  VideoView video = (VideoView) findViewById(R.id.video);
		  //ImageView image = (ImageView) findViewById(R.id.video1);
		  //image.setImageBitmap(vid);
	      video.setVideoURI(data.getData());
	      video.start();
		  
    }   
	}  
}