package eu.markmein;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class StaffViewAbs  extends Activity implements View.OnClickListener {

	Spinner spStudents;
	TextView tvAbsences;
	Button btGetAbsences, btMainMenu;

	ArrayList<NameValuePair> postParameters;
	ArrayList<String> forStudentSpinner = new ArrayList<String>();
	ArrayList<String> studentIDs = new ArrayList<String>();

	DBHandler db;
	GetStudents getStudents;

	ProgressDialog dialog;
	
	String lecturerId;
	String studentId;

	Intent i;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		dialog = ProgressDialog.show(StaffViewAbs.this, "", "Loading...", true);
		setContentView(R.layout.staffviewabs);
		lecturerId = Login.mUserID;
		initialize();
		getStudents = new GetStudents();
		getStudents.execute("text");
		populateSpinner(spStudents, forStudentSpinner);
	}

	private void populateSpinner(Spinner spinnerIn, ArrayList<String> sampleIn) {
		sampleIn.add(0, "Select a Student");
		ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, sampleIn);
		spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinnerIn.setAdapter(spinnerArrayAdapter);		
	}

	private void initialize() {
		spStudents = (Spinner) findViewById(R.id.spStudents);
		btGetAbsences = (Button) findViewById(R.id.btGetAbsences);
		btGetAbsences.setOnClickListener(this);
		btMainMenu = (Button) findViewById(R.id.btMainMenu);
		btMainMenu.setOnClickListener(this);
		tvAbsences = (TextView) findViewById(R.id.tvAbsences);
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.btGetAbsences:
			if(spStudents.getSelectedItemPosition() == 0){
				showToast("Select A Student");
			}else{
				dialog = ProgressDialog.show(StaffViewAbs.this, "", "Retrieving Information", true);
				int index = spStudents.getSelectedItemPosition() - 1;
				studentId = studentIDs.get(index);
				GetStudentAbsences getStudentAbsences = new GetStudentAbsences();
				getStudentAbsences.execute("text");
			}
			break;
		case R.id.btMainMenu:
			i = new Intent("eu.markmein.STAFFMENU");
			startActivity(i);
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

	public class GetStudents extends AsyncTask<String, Void, String>{
		@Override
		protected String doInBackground(String... params) {
			db = new DBHandler();
			JSONArray ja = null;
			postParameters= DBHandler.prepareParams("lecturerId", lecturerId);
			try{
				JSONObject jo = null;
				ja = db.executeQuery(DBHandler.GET_STUDENTS_ID_NAME, postParameters);
				for(int i = 0; i < ja.length(); i++){
					jo = ja.getJSONObject(i);
					forStudentSpinner.add(jo.getString("id") + " - " + jo.getString("name"));
					studentIDs.add(jo.getString("id"));
				}
			}catch(Exception e){
				Log.e("dib0", e.toString());
			}
			dialog.cancel();
			return null;
		}
	}

	public class GetStudentAbsences extends AsyncTask<String, Void, String>{
		ArrayList<String> excuses = new ArrayList<String>();
		@Override
		protected String doInBackground(String... params) {
			db = new DBHandler();
			JSONArray ja = null;
			ArrayList<String> keys = new ArrayList<String>();
			keys.add("lecturerId");
			keys.add("studentId");
			
			ArrayList<String> values = new ArrayList<String>();
			values.add(lecturerId);
			values.add(studentId);
			
			postParameters= DBHandler.prepareParams(keys, values);
			try{
				JSONObject jo = null;
				ja = db.executeQuery(DBHandler.GET_A_STUDENT_ABSENCES, postParameters);
				for(int i = 0; i < ja.length(); i++){
					jo = ja.getJSONObject(i);
					excuses.add(jo.getString("date") + " - " + jo.getString("time"));
					excuses.add(jo.getString("code") + " - " + jo.getString("name"));
					excuses.add(jo.getString("explaination"));
				}
			}catch(Exception e){
				Log.e("dib0", e.toString());
			}
			dialog.cancel();
			return null;
		}
		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			tvAbsences.setText("");
			tvAbsences.append("\nAbsence Explanations for" + studentId + "\n\n");
			for(int i = 0; i < excuses.size(); i++){
				tvAbsences.append(excuses.get(i) + "\n\n");
				//tvAbsences.append("\t" + excuses.get(i+1) + "\n");
				//tvAbsences.append("\t" + excuses.get(i+2) + "\n");
			}
		}
	}
}
