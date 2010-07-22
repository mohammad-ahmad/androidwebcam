package colstate.edu.WebCam.StreamVideo;



import java.io.IOException;

import android.app.Activity;
import android.content.Intent;
import android.hardware.Camera;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.VideoView;

public class StreamVideo extends Activity implements OnClickListener {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
        
        setContentView(R.layout.main);
        
        View videoButton= findViewById(R.id.videoButton);
		videoButton.setOnClickListener(this);
    }

	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()){
		case R.id.videoButton:
			Log.d("Make Surface", "Starting preview");
			ViewVideo mPreview = new ViewVideo(this);
			setContentView(mPreview);
		
	}
	}
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		
	}
}