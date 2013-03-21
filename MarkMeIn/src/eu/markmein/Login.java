package eu.markmein;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

@SuppressLint("NewApi")
@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR1)
public class Login extends Activity {

	LogIn logIn;
	DBHandler db;
	Intent i;
	ProgressDialog dialog;
	
	ArrayList<NameValuePair> postParameters;
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
		
		boolean finish = getIntent().getBooleanExtra("finish", false);
		if(finish){
			startActivity(new Intent(getApplicationContext(), Login.class));
			//showDialog(id, args)
			finish();
			return;
		}
		
		initialize();

		findViewById(R.id.sign_in_button).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				dialog = ProgressDialog.show(Login.this, "", "Logging In", true);
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
		} else if (mPassword.length() < 6) {
			mPasswordView.setError(getString(R.string.error_invalid_password));
			focusView = mPasswordView;
			cancel = true;
		}
		// Check for a valid id string.
		if (TextUtils.isEmpty(mUserID)) {
			mUserIDView.setError(getString(R.string.error_field_required));
			focusView = mUserIDView;
			cancel = true;
		} else if (mUserID.length() < 6) {
			mUserIDView.setError(getString(R.string.error_field_required));
			focusView = mUserIDView;
			cancel = true;
		}

		if (cancel) {
			focusView.requestFocus();
		} else {
			logIn = new LogIn();
			logIn.execute("text");
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

	class LogIn extends AsyncTask<String, Void, String>{
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
					dialog.cancel();
					i = new Intent("eu.markmein.STAFFMENU");
					showToast("Login Successfull");
					startActivity(i);
				}else if(student.equals(user)){
					dialog.cancel();
					i = new Intent("eu.markmein.STUDENTMENU");
					showToast("Login Successfull");
					startActivity(i);
					
				}else{
					i = new Intent("eu.markmein.LOGIN");
					showToast("Login Unsuccessfull");
					startActivity(i);
				}
			} catch (Exception e1) {
				e1.printStackTrace();
			} 
			return null;
		}
	}
}