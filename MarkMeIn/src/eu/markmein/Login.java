package eu.markmein;

import ie.markmein.development.R;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

public class Login extends Activity {
	
	//Testing a commit...
	LogMeIn logMeIn;
	HTTPUtils httpUtils;
	Intent intent;
	
	public static final String url = "http://80.93.22.88/phpDatabase/validateLogin.php";
	ArrayList<NameValuePair> postParameters;
	// Values for id and password at the time of the login attempt.
	private String mUserID;
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
		// Set up the login form.
	
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
			postParameters = new ArrayList<NameValuePair>();
			postParameters.add(new BasicNameValuePair("uID", mUserID)); 
			postParameters.add(new BasicNameValuePair("uPWord", mPassword));
			String strResponse = null;

			try{
				httpUtils = new HTTPUtils();
				strResponse = httpUtils.executeHttpPost(url, postParameters);
				String result = strResponse.toString();

				if(result.contains("1")){
					char student = 'R';
					char staff = 'S';
					if(staff == mUserID.charAt(0)){
						intent = new Intent("ie.markmein.development.STAFFMENU");
						startActivity(intent);
					}else if(student == mUserID.charAt(0)){
						intent = new Intent("ie.markmein.development.STUDENTMENU");
						startActivity(intent);
					}else{
						intent = new Intent("ie.markmein.development.LOGIN");
						startActivity(intent);
					}
				}
				else{
					showAlert();
				}
				/*JSONArray jArray = new JSONArray(result);
				JSONObject jsonData= null;
				try{
					Log.e("Length", "Array Length: " + jArray.length());
					for(int i = 0; i < jArray.length(); i++){
						jsonData = jArray.getJSONObject(i);
						Log.e("Returned", "SNo: " + jsonData.toString());

						Log.e("Returned", "SNo: " + jsonData.getString("SNo") + " SPass: " + jsonData.getString("SPass") + " SName: " + jsonData.getString("SName"));
					}
					return jsonData.toString();
				}catch(Exception e){
					Log.e("Error", "In LogMeIn 7: " + e.toString());
				}*/
			}catch(Exception e){
				Log.e("Error", "In LogMeIn 8: " + e.toString());
			}
			return null;
		}
	}

	public void showAlert(){
		Login.this.runOnUiThread(new Runnable() {
			public void run() {
				AlertDialog.Builder builder = new AlertDialog.Builder(Login.this);
				builder.setTitle("Login Error.");
				builder.setMessage("User not Found.") 
				.setCancelable(false)
				.setPositiveButton("OK", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						//intent = new Intent("ie.markmein.development.LOGIN");
						//startActivity(intent);
					}
				});                    
				AlertDialog alert = builder.create();
				alert.show();              
			}
		});
	}
}