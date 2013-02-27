package ie.markmein.development;

import twitter4j.Twitter;
import twitter4j.auth.RequestToken;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

public class StaffViewFeedback extends Activity implements View.OnClickListener {

	static String TWITTER_CONSUMER_KEY = "";
	static String TWITTER_CONSUMER_SECRET = "";

	static String PREFERENCE_NAME = "twitter_oauth";
	static final String PREF_KEY_OAUTH_TOKEN = "oauth_token";
	static final String PREF_KEY_OAUTH_SECRET = "oauth_token_secret";
	static final String PREF_KEY_TWITTER_LOGIN = "isTwitterLogedIn";
	static final String TWITTER_CALLBACK_URL = "oauth://t4jsample";

	static final String URL_TWITTER_AUTH = "auth_url";
	static final String URL_TWITTER_OAUTH_VERIFIER = "oauth_verifier";
	static final String URL_TWITTER_OAUTH_TOKEN = "oauth_token";

	private static Twitter twitter;
	private static RequestToken requestToken;
	private ConnectionDetector cd;
	private static SharedPreferences mSharedPreferences;

	AlertDialogManager alert = new AlertDialogManager();

	Spinner spDept, spModule;
	Button btMainMenu;

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

		// Check if twitter keys are set
		if (TWITTER_CONSUMER_KEY.trim().length() == 0 || TWITTER_CONSUMER_SECRET.trim().length() == 0) {
			// Internet Connection is not present
			alert.showAlertDialog(StaffViewFeedback.this,"Twitter oAuth tokens", "Please set your twitter oauth tokens first!", false);
			// stop executing code by return
			return;
		}

		initialize();

		mSharedPreferences = getApplicationContext().getSharedPreferences("MyPref", 0);
	}

	private void initialize() {
		btMainMenu = (Button) findViewById(R.id.btMainMenu);
		btMainMenu.setOnClickListener(this);
		tvtweets = (TextView) findViewById(R.id.tvTweets);
		spDept = (Spinner) findViewById(R.id.spDept);
		spModule = (Spinner) findViewById(R.id.spModule);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btMainMenu:
			i = new Intent("ie.markmein.development.STAFFMENU");
			startActivity(i);
			break;
		}
	}
}
