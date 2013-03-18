package eu.markmein;

import eu.markmein.R;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.RequestToken;
import twitter4j.conf.ConfigurationBuilder;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

public class StaffViewFeedback extends Activity implements View.OnClickListener {


	private static Twitter twitter;
	private static RequestToken requestToken;
	private ConnectionDetector cd;
	private static SharedPreferences mSharedPreferences;

	AlertDialogManager alert = new AlertDialogManager();

	Spinner spModule;
	Button btMainMenu;
	Button btTwitterLogin;

	Intent i;

	TextView tvtweets;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.staffviewfeedback);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		Log.e("Error", "In onCreate 1");

		cd = new ConnectionDetector(getApplicationContext());

		if (!cd.isConnectingToInternet()) {
			// Internet Connection is not present
			alert.showAlertDialog(StaffViewFeedback.this, "Internet Connection Error", "Please connect to working Internet connection", false);
			// stop executing code by return
			return;
		}
		initialize();

		mSharedPreferences = getApplicationContext().getSharedPreferences("MyPref", 0);
	}

	private void initialize() {
		btMainMenu = (Button) findViewById(R.id.btMainMenu);
		btMainMenu.setOnClickListener(this);
		btTwitterLogin = (Button) findViewById(R.id.btTwitterLogin);
		btTwitterLogin.setOnClickListener(this);
		tvtweets = (TextView) findViewById(R.id.tvTweets);
		spModule = (Spinner) findViewById(R.id.spModule);

		//Twitter
		ConfigurationBuilder cb = new ConfigurationBuilder();
		cb.setDebugEnabled(true)
		.setOAuthConsumerKey("409bNvYkmypjmHBpGQlkAQ")
		.setOAuthConsumerSecret("Pd1kJiXDklZ89JaaHOXycKT0DI27xIgvNj1EYizkiM");

		TwitterFactory tf = new TwitterFactory(cb.build());
		twitter = tf.getInstance();

		try {
			requestToken = twitter
					.getOAuthRequestToken("http://www.markmein.eu");
			this.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(requestToken.getAuthenticationURL())));
		} catch (TwitterException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btMainMenu:
			i = new Intent("ie.markmein.development.STAFFMENU");
			startActivity(i);
			break;
		case R.id.btTwitterLogin:
			break;
		}
	}
}
