package eu.markmein;

import java.io.IOException;
import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

public class Login extends Activity {

	LogMeIn logMeIn;
	DBHandler db;
	Intent i;

	ArrayList<NameValuePair> postParameters;
	// Values for id and password at the time of the login attempt.
	public static String mUserID;
	private String mPassword;
	// UI references.
	private EditText mUserIDView;
	private EditText mPasswordView;

	View focusView = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		initialize();

		findViewById(R.id.sign_in_button).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				mUserID = mUserIDView.getText().toString();
				mPassword = mPasswordView.getText().toString();
				validateLogin();
			}
		});
	}

	private void initialize() {
		mUserIDView = (EditText) findViewById(R.id.loginid);
		mPasswordView = (EditText) findViewById(R.id.password);
		mUserIDView.setText(mUserID);
	
	}

	public void validateLogin() {
		// Reset errors.
		mUserIDView.setError(null);
		mPasswordView.setError(null);

		// Store values at the time of the login attempt.
		boolean cancel = false;

		// Check for a valid password.
		if (TextUtils.isEmpty(mPassword)) {
			mPasswordView.setError(getString(R.string.error_field_required));
			focusView = mPasswordView;
			cancel = true;
		} else if (mPassword.length() < 8) {
			mPasswordView.setError(getString(R.string.error_invalid_password));
			focusView = mPasswordView;
			cancel = true;
		}
		// Check for a valid id string.
		if (TextUtils.isEmpty(mUserID)) {
			mUserIDView.setError(getString(R.string.error_field_required));
			focusView = mUserIDView;
			cancel = true;
		} else if (mUserID.length() < 8) {
			mUserIDView.setError(getString(R.string.error_field_required));
			focusView = mUserIDView;
			cancel = true;
		}

		if (cancel) {
			focusView.requestFocus();
		} else {
			logMeIn = new LogMeIn();
			logMeIn.execute("text");
		}
	}

	class LogMeIn extends AsyncTask<String, Void, String>{

		@Override
		protected String doInBackground(String... params) {
			db = new DBHandler();
			JSONArray ja = null;
			postParameters = new ArrayList<NameValuePair>();
			postParameters.add(new BasicNameValuePair("uID", mUserID)); 
			postParameters.add(new BasicNameValuePair("uPWord", mPassword));
			try {
				JSONObject jo = null;
				ja = db.executeQuery(DBHandler.VALIDATE_LOGIN, postParameters);
				jo = ja.getJSONObject(0);
				String user = jo.getString("type");

				String student = "S";
				String staff = "L";
				if(staff.equals(user)){
					i = new Intent("eu.markmein.STAFFMENU");
					startActivity(i);
				}else if(student.equals(user)){
					i = new Intent("eu.markmein.STUDENTMENU");
					startActivity(i);
				}else{
					i = new Intent("eu.markmein.LOGIN");
					startActivity(i);
				}
			} catch (ClientProtocolException e1) {
				e1.printStackTrace();
			} catch (IOException e1) {
				e1.printStackTrace();
			} catch (JSONException e1) {
				e1.printStackTrace();
			}
			return null;
		}
		
		public void showAlert(final String title, final String message){
			Login.this.runOnUiThread(new Runnable() {
				public void run() {
					AlertDialog.Builder builder = new AlertDialog.Builder(Login.this);
					builder.setTitle(title);
					builder.setMessage(message) 
					.setCancelable(false)
					.setPositiveButton("OK", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
						}
					});                    
					AlertDialog alert = builder.create();
					alert.show();              
				}
			});
		}
	}
}