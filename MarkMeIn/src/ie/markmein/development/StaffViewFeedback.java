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
			alert.showAlertDialog(StaffViewFeedback.this, "Internet Connection Error",
					"Please connect to working Internet connection", false);
			// stop executing code by return
			return;
		}

		// Check if twitter keys are set
		if(TWITTER_CONSUMER_KEY.trim().length() == 0 || TWITTER_CONSUMER_SECRET.trim().length() == 0){
			// Internet Connection is not present
			alert.showAlertDialog(StaffViewFeedback.this, "Twitter oAuth tokens", "Please set your twitter oauth tokens first!", false);
			// stop executing code by return
			return;
		}
		
		initialize();
		
		mSharedPreferences = getApplicationContext().getSharedPreferences("MyPref", 0);

		/*Twitter twitter = new TwitterFactory().getInstance();
		twitter.setOAuthConsumer("customer key", "customer secret");
		try {
			RequestToken requestToken = twitter.getOAuthRequestToken();
			AccessToken accessToken = null;
		} catch (TwitterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/

		/*Twitter twitter = new TwitterFactory().getInstance();
		try {
			User user = twitter.verifyCredentials();
			List<Status> statuses = twitter.getHomeTimeline();
			tvtweets.setText("Showing @" +user.getScreenName() + "'s home timeline.");
			for (Status status : statuses){
				tvtweets.setText("@" + status.getUser().getScreenName() + " - " + status.getText() + "\n");
			}
		} catch (TwitterException e) {
			Log.e("Error", "In onCreate ");
		}*/

		/*Log.e("Error", "In onCreate 2");
		getTwitterFeedback = new GetTwitterFeedback();
		getTwitterFeedback.execute("text");
		Log.e("Error", "In onCreate 3");*/
	}

	/*public JSONObject lastTweet(String userName)throws ClientProtocolException, IOException, JSONException{
		Log.e("Error", "In lastTweet 1");
		StringBuilder url = new StringBuilder(URL);
		Log.e("Error", "In lastTweet 2");
		url.append(userName);
		Log.e("Error", "In lastTweet 3");
		HttpGet httpGet = new HttpGet(url.toString());
		Log.e("Error", "In lastTweet 4");
		HttpResponse r = httpClient.execute(httpGet);
		Log.e("Error", "In lastTweet 5");
		int status = r.getStatusLine().getStatusCode();
		Log.e("Error", "In lastTweet 6");
		if(status == 200){
			Log.e("Error", "In lastTweet 7");
			HttpEntity e = r.getEntity();
			Log.e("Error", "In lastTweet 8");
			String data = EntityUtils.toString(e);
			Log.e("Error", "In lastTweet 9");
			JSONArray timeline = new JSONArray(data);
			Log.e("Error", "In lastTweet 10");
			JSONObject last = timeline.getJSONObject(0);
			Log.e("Error", "In lastTweet 11");
			return last;
		}else
		{
			Toast.makeText(StaffViewFeedback.this, "Error", Toast.LENGTH_SHORT);
			return null;
		}
	}*/

	private void initialize() {
		btMainMenu = (Button) findViewById(R.id.btMainMenu);
		btMainMenu.setOnClickListener(this);
		tvtweets = (TextView) findViewById(R.id.tvTweets);	
		spDept = (Spinner) findViewById(R.id.spDept);
		spModule = (Spinner) findViewById(R.id.spModule);
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.btMainMenu:
			i = new Intent("ie.markmein.development.STAFFMENU");
			startActivity(i);
			break;
		}
	}

	/*public class GetTwitterFeedback extends AsyncTask<String, Integer, String>{
		String strResponse = null;

		@Override
		protected String doInBackground(String... params) {
			Log.e("Error", "In AsyncTask 1");
			try{
				Log.e("Error", "In AsyncTask 2");
				json = lastTweet("mybringback");
				Log.e("Error", "In AsyncTask 3");
				return json.getString(params[0]);
			}catch(ClientProtocolException e){
				Log.e("Error", "In AsyncTask 4 " + e.toString());
			}catch(IOException e){
				Log.e("Error", "In AsyncTask 5 " + e.toString());
			}catch(JSONException e){
				Log.e("Error", "In AsyncTask 6 " + e.toString());
			}finally{
				try{
					return strResponse;
				}catch(Exception e){
					Log.e("Error", "In AsyncTask 5 " + e.toString());
				}
			}
			return null;
		}

		protected void onPostExecute(String result) {
			tvtweets.setText(result);
		}
	}*/
}
