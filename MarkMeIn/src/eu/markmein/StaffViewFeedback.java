package eu.markmein;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import twitter4j.HashtagEntity;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class StaffViewFeedback extends Activity implements View.OnClickListener {

	//Twitter
	private Twitter twitter;
	private ConfigurationBuilder cb = new ConfigurationBuilder();

	//hardcoded for now
	private String moduleOfferingCode;
	private String fullName;

	DBHandler db;
	ArrayList<NameValuePair> postParameters;
	ArrayList<String> forModuleSpinner = new ArrayList<String>();
	ArrayList<String> modulesIds = new ArrayList<String>();
	GetLecturersModules getLectMods;
	String lecturerId;

	Spinner spModule;
	Button btGetTweets, btMainMenu, btPost;

	ProgressDialog dialog;
	Intent i;

	TextView tvTweets;
	EditText etTweetText;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		dialog = ProgressDialog.show(StaffViewFeedback.this, "", "Loading...", true);
		setContentView(R.layout.staffviewfeedback);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		try {
			initialize();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	private void initialize() throws InterruptedException {
		lecturerId = Login.mUserID;
		btGetTweets = (Button)findViewById(R.id.btGetTweets);
		btGetTweets.setOnClickListener(this);
		btMainMenu = (Button) findViewById(R.id.btMainMenu);
		btMainMenu.setOnClickListener(this);
		btPost = (Button) findViewById(R.id.btPost);
		btPost.setOnClickListener(this);
		etTweetText = (EditText) findViewById(R.id.tweetText); 
		tvTweets = (TextView) findViewById(R.id.tvTweets);
		spModule = (Spinner) findViewById(R.id.spModule);

		//twitter
		cb.setDebugEnabled(true)
		.setOAuthConsumerKey("aknBvnbA2MLQlDQP0WR5nA")
		.setOAuthConsumerSecret("4FytjOAS4YuUNeO7hh3GvPFx2Mhfva1TLO2GESeUo")
		.setOAuthAccessToken("1278762841-oRo6GJjR1OA4WlaDD3r6neCIhMyxlZYRIPQuqsg")
		.setOAuthAccessTokenSecret("XAEKaoEDygMmuROYkQEm5pKQ9k7pUbFoPyA6nyR2w");

		TwitterFactory tf = new TwitterFactory(cb.build());
		twitter = tf.getInstance();
		getLectMods = new GetLecturersModules();
		getLectMods.execute("tect");
		Thread.sleep(10);
		populateSpinner(spModule, forModuleSpinner);
	}

	private void populateSpinner(Spinner spinnerIn, ArrayList<String> sampleIn) {
		sampleIn.add(0, "Select Item");
		ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, sampleIn);
		spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinnerIn.setAdapter(spinnerArrayAdapter);		
	}

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btGetTweets:
			if(spModule.getSelectedItemPosition() == 0 ){
				showToast("Select A Module");
			}else{
				dialog = ProgressDialog.show(StaffViewFeedback.this, "", "Retrieving Tweets", true);
				int index = spModule.getSelectedItemPosition() -1;
				moduleOfferingCode = modulesIds.get(index);
				//tvTweets.setText("Select A Module");
				GetTweets getTweets;
				getTweets = new GetTweets();
				getTweets.execute("text");
			}
			break;
		case R.id.btMainMenu:
			i = new Intent("eu.markmein.STAFFMENU");
			startActivity(i);
			break;
		case R.id.btPost:
			if((spModule.getSelectedItemPosition() == 0)||(TextUtils.isEmpty(etTweetText.getText().toString()))){
				if(spModule.getSelectedItemPosition() == 0){
					showToast("Select A Module");
				}
				if(TextUtils.isEmpty(etTweetText.getText().toString())){
					etTweetText.setError(getString(R.string.error_field_required));
				}
			}else{
				dialog = ProgressDialog.show(StaffViewFeedback.this, "", "Posting Tweet", true);
				int indexA = spModule.getSelectedItemPosition() -1;
				moduleOfferingCode = modulesIds.get(indexA);
				PostTweet postTweet = new PostTweet();
				postTweet.execute("String");
			}
			break;
		}
	}
	
	public void showToast( final CharSequence text){
		runOnUiThread(new Runnable() {
			public void run()
			{
				Context context = getApplicationContext();
				int duration = Toast.LENGTH_LONG;
				Toast toast = Toast.makeText(context, text, duration);
				toast.setGravity(Gravity.TOP, 0, 75);
				toast.show();
			}
		});
	}

	public class GetTweets extends AsyncTask<String, Void, String>{

		List<twitter4j.Status> statuses = null;
		private ArrayList<String> tweets = new ArrayList<String>();
		@Override
		protected String doInBackground(String... params) {
			try {
				statuses = twitter.getHomeTimeline();
			} catch (TwitterException e) {
				Log.e("twitter", e.toString());
			}
			dialog.cancel();
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
					}
				}
			}
			tvTweets.setText("");
			for(int i = 0; i < tweets.size(); i++){
				tvTweets.append(tweets.get(i) + "\n");
			}
		}

	}
	class GetLecturersModules extends AsyncTask<String, Void, String>{

		@Override
		protected String doInBackground(String... params) {
			db = new DBHandler();
			JSONArray ja = null;
			postParameters= DBHandler.prepareParams("lecturerId",lecturerId);
			try {
				JSONObject jo = null;
				ja = db.executeQuery(DBHandler.GET_LECTURERES_CLASSES, postParameters);
				for(int i = 0; i< ja.length(); i ++){
					jo = ja.getJSONObject(i);
					forModuleSpinner.add(jo.getString("code") + "-" + jo.getString("name"));
					modulesIds.add(jo.getString("code"));
				}
			} catch (Exception e) {
				Log.e("Error", "In doInBackground Exception" + e.toString() );
			}
			postParameters= DBHandler.prepareParams("id",lecturerId);

			JSONArray ja1 = null;
			try{
				JSONObject jo1 = null;
				ja1 = db.executeQuery(DBHandler.GET_NAME, postParameters);
				jo1 = ja1.getJSONObject(0);
				fullName = jo1.getString("name");
			}catch(Exception e){
				Log.e("Error1", "In doInBackground Exception1" + e.toString());
			}
			dialog.cancel();
			return null;
		}
	}

	public class PostTweet extends AsyncTask<String, Void, String>{

		@Override
		protected String doInBackground(String... params) {
			try {
				twitter.updateStatus("#" + moduleOfferingCode + " " + fullName + ": " + etTweetText.getText().toString());
				showAlert("Info", "Tweet Sent!\n Return To Main Menu?");
			} catch (TwitterException e) {

			}
			dialog.cancel();
			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			etTweetText.setText("");
		}
	}
	
	public void showAlert(final String title, final String message){
		StaffViewFeedback.this.runOnUiThread(new Runnable() {
			public void run() {
				AlertDialog.Builder builder = new AlertDialog.Builder(StaffViewFeedback.this);
				builder.setTitle(title);
				builder.setMessage(message) 
				.setCancelable(false)
				.setNegativeButton("No", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
					}
				})
				.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						i = new Intent("eu.markmein.STAFFMENU");
						startActivity(i);
					}
				});                    
				AlertDialog alert = builder.create();
				alert.show();              
			}
		});
	}
}
