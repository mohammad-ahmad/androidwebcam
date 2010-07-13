package edu.colstate.cs.webcam;

import java.util.ArrayList;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;

public class WebcamApp {
	private boolean mIsBound = false;
    private WebcamService mBoundService = null;
    private Activity mActivity = null;
    static WebcamApp mApp = null;
    
    static WebcamApp getApp(Activity activity)
    {
    	if (mApp == null)
    	{
    		mApp = new WebcamApp(activity);
    	}
    	
    	return mApp;
    }

    // Service API methods
    void sendLoginRequest(final String userName, final String password, final String serverHost)
    {
    	// bind to service
    	doBindService();

   		Runnable runnable = new Runnable() {
	        public void run() {
	        	  boolean bLoginResult = mBoundService.login(userName, password, serverHost);
	        	  
	        	  // ::TODO:: notify client of login result
	        	  
	        	  // ::TODO:: Test service code.   
	        	  // Send text message to kurtn2 user, and call kurtn2 user
	        	  // ::TODO::  This code should eventually be removed
	        	  
	        	  if (userName.equals("kurtn2"))
	        		  return;
	        	  
	        	  if (bLoginResult)
	        	  {
	        		  ArrayList<Friend> friendList = mBoundService.getFriendList();
	        		  
	        		  if (friendList.size() > 0)
	        		  {
	        	       		for (Friend friend : friendList)
	        	       		{
	        	       			if (friend.getUser().startsWith("kurtn2"))
	        	       			{
	      	        			  mBoundService.sendTextMessage(friend, "Test Message");
	    	        			  mBoundService.callFriend(friend);
	        	       			}
	        	       		}
	        		  }
	        	  }	        	  
	        	  
	          }
	        };

	    // delay execution to allow for service to be bound
	    Handler handler = new Handler();
        handler.postDelayed(runnable, 3000);
        
    }


    public WebcamApp(Activity activity)
    {
    	mActivity = activity;
    	
    }
    
    
    private ServiceConnection mConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName className, IBinder service) {
            // This is called when the connection with the service has been
            // established, giving us the service object we can use to
            // interact with the service.  Because we have bound to a explicit
            // service that we know is running in our own process, we can
            // cast its IBinder to a concrete class and directly access it.
            mBoundService = ((WebcamService.WebcamServiceBinder)service).getService();

        }

        public void onServiceDisconnected(ComponentName className) {
            // This is called when the connection with the service has been
            // unexpectedly disconnected -- that is, its process crashed.
            // Because it is running in our same process, we should never
            // see this happen.
            mBoundService = null;
        }
    };    
    
    void doBindService() {
    	
    	if (mIsBound) return;
    	
 //   	Intent a = new Intent(mActivity, WebcamService.class);
 //   	mActivity.startService(a);
    	
        // Establish a connection with the service.  We use an explicit
        // class name because we want a specific service implementation that
        // we know will be running in our own process (and thus won't be
        // supporting component replacement by other applications).
    	mActivity.bindService(new Intent(mActivity, 
                WebcamService.class), mConnection, Context.BIND_AUTO_CREATE);
        mIsBound = true;
    }

    void doUnbindService() {
        if (mIsBound) {
            // Detach our existing connection.
        	mActivity.unbindService(mConnection);
            mIsBound = false;
        }
    }
}
