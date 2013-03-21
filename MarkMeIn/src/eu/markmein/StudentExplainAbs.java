package eu.markmein;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class StudentExplainAbs extends Activity implements View.OnClickListener {
	Spinner spMissedAttendance;
	EditText etExplaination;
	Button btSubmit, btClear;

	ArrayList<NameValuePair> postParameters;
	ArrayList<String> forMissedAttendancesSpinner = new ArrayList<String>();
	ArrayList<Integer> absenceIds = new ArrayList<Integer>();
	
	ProgressDialog dialog;

	String studentId, code, date, time, explaination, attendanceId;

	GetMissedAttendance getMissedAttendance;
	SubmitExplanation submitExplanation;
	
	DBHandler db;

	Intent i;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		dialog = ProgressDialog.show(StudentExplainAbs.this, "", "Loading...", true);
		setContentView(R.layout.studentexplainabs);
		studentId = Login.mUserID;
		initialize();
	}

	private void initialize() {
		spMissedAttendance = (Spinner) findViewById(R.id.spMissedAttendance);
		etExplaination = (EditText) findViewById(R.id.etReason);
		btSubmit = (Button) findViewById(R.id.btStuSubmitAbsence);
		btSubmit.setOnClickListener(this);
		btClear = (Button) findViewById(R.id.btStuClearAbsence);
		btClear.setOnClickListener(this);
		getMissedAttendance = new GetMissedAttendance();
		getMissedAttendance.execute("text");
		populateSpinner(spMissedAttendance, forMissedAttendancesSpinner);
	}

	private void populateSpinner(Spinner spinnerIn, ArrayList<String> sampleIn) {
		sampleIn.add(0, "Select Module");
		ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, sampleIn);
		spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinnerIn.setAdapter(spinnerArrayAdapter);		
	}

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.btStuSubmitAbsence:
			if((spMissedAttendance.getSelectedItemPosition() == 0)||(TextUtils.isEmpty(etExplaination.getText().toString()))){
				if (spMissedAttendance.getSelectedItemPosition() == 0) {
					showToast("Select A Class You Were Absent For");
				}
				if (TextUtils.isEmpty(etExplaination.getText().toString())) {
					etExplaination.setError(getString(R.string.error_field_required));
				}
			}else{
				dialog = ProgressDialog.show(StudentExplainAbs.this, "", "Submitting...", true);
				int index = spMissedAttendance.getSelectedItemPosition() - 1;
				attendanceId = absenceIds.get(index).toString();
				explaination = etExplaination.getText().toString();
				submitExplanation = new SubmitExplanation();
				submitExplanation.execute("text");
			}
			break;
		case R.id.btStuClearAbsence:
			etExplaination.setText("");
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

	class GetMissedAttendance extends AsyncTask<String, Void, String>{
		@Override
		protected String doInBackground(String... params) {
			db = new DBHandler();
			JSONArray ja = null;
			postParameters= DBHandler.prepareParams("studentId", studentId);
			try {
				JSONObject jo = null;
				ja = db.executeQuery(DBHandler.GET_STUDENT_ABSENCES, postParameters);
				for(int i = 0; i< ja.length(); i ++){
					jo = ja.getJSONObject(i);
					forMissedAttendancesSpinner.add(jo.getString("date") + "-" + jo.getString("time") + "-" + jo.getString("name"));
					absenceIds.add(jo.getInt("id"));
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			dialog.cancel();
			return null;
		}
	}

	class SubmitExplanation extends AsyncTask<String, Void, String>{
		JSONArray ja = null;
		@Override
		protected String doInBackground(String... params) {
			db = new DBHandler();
			ArrayList<String> keys = new ArrayList<String>();
			keys.add("studentId");
			keys.add("attendanceId");
			keys.add("explaination");
			ArrayList<String> values = new ArrayList<String>();
			values.add(studentId);
			values.add(attendanceId);
			values.add(explaination);
			postParameters = DBHandler.prepareParams(keys, values);
			try {
				ja = db.executeQuery(DBHandler.SUBMIT_EXPLANATION, postParameters);
				
				//showToast("Submitted");
			} catch (Exception e) {
				e.printStackTrace();
			}
			dialog.cancel();
			return null;
		}
		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			etExplaination.setText("");
		}
	}

	public void showAlert(final String title, final String message){
		StudentExplainAbs.this.runOnUiThread(new Runnable() {
			public void run() {
				AlertDialog.Builder builder = new AlertDialog.Builder(StudentExplainAbs.this);
				builder.setTitle(title);
				builder.setMessage(message) 
				.setCancelable(false)
				.setNegativeButton("No", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
					}
				})
				.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						i = new Intent("eu.markmein.STUDENTMENU");
						startActivity(i);
					}
				});                    
				AlertDialog alert = builder.create();
				alert.show();              
			}
		});
	}
}



