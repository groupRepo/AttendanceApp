package eu.markmein;

import eu.markmein.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class Splash extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash);
		displaySplash();
	}
	
	private void displaySplash() {
		Thread timer = new Thread(){
			
			@Override
			public void run() {
				super.run();
				try {
					sleep(2500);
				} catch (InterruptedException e) {

					e.printStackTrace();
				}finally{
					Intent openLoginScreen = new Intent("eu.markmein.LOGIN");
					startActivity(openLoginScreen);
				}
			}
		};
		timer.start();		
	}

	@Override
	protected void onPause() {
		super.onPause();
		finish();
	}
}