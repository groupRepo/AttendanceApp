package eu.markmein;

import java.util.ArrayList;
import java.util.List;

import twitter4j.HashtagEntity;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

public class StaffViewFeedback extends Activity implements View.OnClickListener, OnItemSelectedListener {

	//Twitter
	private Twitter twitter;
	private ConfigurationBuilder cb = new ConfigurationBuilder();
	
	//hardcoded for now
	private String moduleOfferingCode = "CRN8081";
	private String firstName = "Jakub";
	
	GetTweets getTweets;
	
	private ArrayList<String> tweets;

	Spinner spModule;
	Button btMainMenu;
	Button btPost;

	Intent i;

	TextView tvTweets;
	EditText tweetText;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.staffviewfeedback);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		Log.e("Error", "In onCreate 1");
		initialize();

	}

	private void initialize() {
		btMainMenu = (Button) findViewById(R.id.btMainMenu);
		btMainMenu.setOnClickListener(this);
		btPost = (Button) findViewById(R.id.btPost);
		btPost.setOnClickListener(this);
		
		
		tweetText = (EditText) findViewById(R.id.tweetText); 
		tvTweets = (TextView) findViewById(R.id.tvTweets);
		spModule = (Spinner) findViewById(R.id.spModule);
		spModule.setOnItemSelectedListener(this);
		
		
		//twitter
		tweets = new ArrayList<String>();
		cb.setDebugEnabled(true)
		.setOAuthConsumerKey("aknBvnbA2MLQlDQP0WR5nA")
		.setOAuthConsumerSecret("4FytjOAS4YuUNeO7hh3GvPFx2Mhfva1TLO2GESeUo")
		.setOAuthAccessToken("1278762841-oRo6GJjR1OA4WlaDD3r6neCIhMyxlZYRIPQuqsg")
		.setOAuthAccessTokenSecret("XAEKaoEDygMmuROYkQEm5pKQ9k7pUbFoPyA6nyR2w");
		
		TwitterFactory tf = new TwitterFactory(cb.build());
		twitter = tf.getInstance();
		getTweets = new GetTweets();
		getTweets.execute("text");
		//getTweets();
		//Log.e("tweets", tweets.get(0).toString());
		//populate the tweets
		tvTweets.setText("");
		for(String s: tweets){
			tvTweets.append(s + "\n");
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btMainMenu:
			i = new Intent("ie.markmein.development.STAFFMENU");
			startActivity(i);
			break;
		case R.id.btPost:
			//if(tweetText.isDirty()){
				postTweet(tweetText.getText().toString());
			//}
			break;
		}
	}
	
	
	/*private void getTweets(){
		//get all tweets from timeline
		List<Status> statuses = null;
		try {
			statuses = twitter.getHomeTimeline();
		} catch (TwitterException e) {

		}
		
		//extract the tweets related to a given class
		for(Status s: statuses){
			HashtagEntity[] h = s.getHashtagEntities();
			for(int i = 0; i < h.length; i++){
				if(h[i].getText().startsWith(moduleOfferingCode)){
					tweets.add(h[i].getText());
				}
			}
		}
	}*/
	private void postTweet(String tweet){
		//post new status in format of "#CODE - Name: content of tweet"
		try {
			twitter.updateStatus("#" + moduleOfferingCode + " - " + firstName + ": " + tweet);
		} catch (TwitterException e) {

		}
	}

	@Override
	public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
			long arg3) {
		
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		
	}
	
	public class GetTweets extends AsyncTask<String, Void, String>{
		
		List<twitter4j.Status> statuses = null;
		@Override
		protected String doInBackground(String... params) {
			
			try {
				statuses = twitter.getHomeTimeline();
			} catch (TwitterException e) {

			}
			return null;
		}
		@Override
		protected void onPostExecute(String result) {
			
			super.onPostExecute(result);
			for(twitter4j.Status s: statuses){
				HashtagEntity[] h = s.getHashtagEntities();
				for(int i = 0; i < h.length; i++){
					if(h[i].getText().startsWith(moduleOfferingCode)){
						tweets.add(s.getText());
						Log.e("postExec", h[i].getText());
					}
				}
				
			}
			tvTweets.setText("");
			for(int i = 0; i < tweets.size(); i++){
				tvTweets.append(tweets.get(i) + "\n");
			}
		}
		
	}
}
