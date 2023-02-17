package com.demus.mayor;

import com.demus.mayor.home.HomeActivity;
import com.demus.mayor.models.User;
import com.demus.mayor.R;
import com.onesignal.OneSignal;
import com.onesignal.OneSignal.IdsAvailableHandler;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

public class SplashScreenActivity extends AppCompatActivity {
	
	/**
     * The thread to process splash screen events
     */
    private Thread mSplashThread;
    
    static AppCompatActivity current;

   /** Called when the activity is first created. */
   @Override
   public void onCreate(Bundle savedInstanceState) {
       super.onCreate(savedInstanceState);

        // Splash screen view
        setContentView(R.layout.activity_splash_screen);
        
        current = this;
       
        // The thread to wait for splash screen events
        mSplashThread =  new Thread() {
           @Override
           public void run(){
               try {
                   synchronized(this) {
                       // Wait given period of time or exit on touch
                       wait(2000);
                   }
               }
               catch(InterruptedException ex) {
               }
               
               //Go to to the next line of action
               GoToNext();
           }
        };
       
        mSplashThread.start();
   }
       
   /**
    * Method for running the next line of action.  
    */
   public void GoToNext() {
	   OneSignal.idsAvailable(new IdsAvailableHandler() {
			
		   @Override
		   public void idsAvailable(final String userID, String regID) {
			   if(userID != null) {
				   new Thread() {
					   @Override
					   public void run() {
						   OneSignal.sendTag("userType", "subscriber");
					   }
				   }.start();
				}
				
				User user = new User().getAppOwner(SplashScreenActivity.this);
			   	
				Intent intent = null;
			   	if(user != null)
			   		intent = new Intent(SplashScreenActivity.this, HomeActivity.class);
			   	else
			   		intent = new Intent(SplashScreenActivity.this, TermsOfServiceActivity.class);
			   	
			   	intent.putExtra("key", userID);
			   	startActivity(intent);
				finish();
			}
	   });
   }
}
