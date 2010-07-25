package edu.colstate.cs.webcam;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import android.content.Context;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioRecord;
import android.media.AudioTrack;
import android.media.MediaRecorder;
import android.util.Log;

public class AudioUtils {
	
	private static AudioUtils mAudioUtils = null;
	private WebcamApp app = null;
	private AudioTrack audioTrack = null;
	
	public static AudioUtils getAudioUtils(Context context)
	{
		if (mAudioUtils == null)
		{
			mAudioUtils = new AudioUtils(context);
		}
		return mAudioUtils;
	}
	
	public AudioUtils(Context context)
	{
		mContext = context;
		app = WebcamApp.getApp(context);
	}
	
	Context mContext = null;
	boolean isRecording = false;
	ByteArrayOutputStream audioData = null;

	public void stop()
	{
		isRecording = false;
	}
	
	public void record() {
		  int frequency = 11025;
		  int channelConfiguration = AudioFormat.CHANNEL_CONFIGURATION_MONO;
		  int audioEncoding = AudioFormat.ENCODING_PCM_16BIT;

/*		  File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/reverseme.pcm");
		   
		  // Delete any previous recording.
		  if (file.exists())
		    file.delete();


		  // Create the new file.
		  try {
		    file.createNewFile();
		  } catch (IOException e) {
		    throw new IllegalStateException("Failed to create " + file.toString());
		  }
*/		   
		  try {
		    // Create a DataOuputStream to write the audio data into the saved file.
	//	    OutputStream os = new FileOutputStream(file);
			  audioData = new ByteArrayOutputStream();
			 OutputStream os = audioData;
		    BufferedOutputStream bos = new BufferedOutputStream(os);
		    DataOutputStream dos = new DataOutputStream(bos);
		     
		    // Create a new AudioRecord object to record the audio.
		    int bufferSize = AudioRecord.getMinBufferSize(frequency, channelConfiguration,  audioEncoding);
		    AudioRecord audioRecord = new AudioRecord(MediaRecorder.AudioSource.MIC, 
		                                              frequency, channelConfiguration, 
		                                              audioEncoding, bufferSize);
		   
		    short[] buffer = new short[bufferSize];   
		    audioRecord.startRecording();
		    isRecording = true;


		    while (isRecording) {
		      int bufferReadResult = audioRecord.read(buffer, 0, bufferSize);
		      
		      // Convert short[] array to byte[] array
		      if (bufferReadResult > 0)
		      {
		    	  byte byteArray[] = new byte[bufferReadResult*2];
		    	  for (int i=0; i < bufferReadResult; i++)
		    	  {
		    		  byteArray[2*i] = (byte) buffer[i];
		    		  byteArray[2*i+1] = (byte) (buffer[i] >>> 8);
		    	  }
		    	  
		    	  app.sendAudioData(byteArray, bufferReadResult*2);
		      }
		    }


		    audioRecord.stop();
		    dos.close();
		   
		  } catch (Throwable t) {
		    Log.e("AudioRecord","Recording Failed");
		  }
		}
	public void play() {
		    // Create a new AudioTrack object using the same parameters as the AudioRecord
		    // object used to create the file.
		    int minSize =AudioTrack.getMinBufferSize( 11025, AudioFormat.CHANNEL_CONFIGURATION_MONO, AudioFormat.ENCODING_PCM_16BIT );        
		    		    
		     audioTrack = new AudioTrack(AudioManager.STREAM_MUSIC, 
		                                           11025, 
		                                           AudioFormat.CHANNEL_CONFIGURATION_MONO,
		                                           AudioFormat.ENCODING_PCM_16BIT, 
		                                           minSize, 
		                                           AudioTrack.MODE_STREAM);
		    // Start playback
		    audioTrack.play();
		  
		}	
	
	    public void playMusicData(byte audioData[], int length)
	    {
	    	audioTrack.write(audioData,0, length);
	    }
	
}
