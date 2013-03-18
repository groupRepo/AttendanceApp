package eu.markmein;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

public class StudentExplainAbs extends Activity implements View.OnClickListener {
	Spinner spMissedAttendance;
	EditText etReason;
	Button btSubmit, btClear, btMainMenu;

	ArrayList<NameValuePair> postParameters;
	ArrayList<String> forMissedAttendancesSpinner = new ArrayList<String>();
	ArrayList<String> absenceIds = new ArrayList<String>();

	String code, studentId, date, explaination, attendanceId, time;
	
	//id name code date time
	
	//id studentId

	GetMissedAttendance getMissedAttendance;
	SubmitExplanation submitExplanation;

	DBHandler db;

	Intent i;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.studentexplainabs);
		initialize();
	}

	private void initialize() {
		studentId = Login.mUserID;
		spMissedAttendance = (Spinner) findViewById(R.id.spMissedAttendance);
		etReason = (EditText) findViewById(R.id.etReason);
		btSubmit = (Button) findViewById(R.id.btStuSubmitAbsence);
		btSubmit.setOnClickListener(this);
		btClear = (Button) findViewById(R.id.btStuClearAbsence);
		btClear.setOnClickListener(this);
		btMainMenu = (Button) findViewById(R.id.btMainMenu);
		btMainMenu.setOnClickListener(this);
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

	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.btStuSubmitAbsence:
			int index = spMissedAttendance.getSelectedItemPosition() - 1;
			attendanceId = absenceIds.get(index);
			explaination = etReason.getText().toString();
			submitExplanation = new SubmitExplanation();
			submitExplanation.execute("text");
			break;
		case R.id.btStuClearAbsence:
			etReason.setText("");
			break;
		case R.id.btMainMenu:
			i = new Intent("eu.markmein.STUDENTMENU");
			startActivity(i);
			break;
		}
	}

	class GetMissedAttendance extends AsyncTask<String, Void, String>{

		@Override
		protected String doInBackground(String... params) {
			db = new DBHandler();
			JSONArray ja = null;
			postParameters= DBHandler.prepareParams("studentId", studentId);
			Log.e("Error", "In doInBackground");
			try {
				JSONObject jo = null;
				ja = db.executeQuery(DBHandler.GET_STUDENT_ABSENCES, postParameters);
				Log.e("Error", ja.toString());
				for(int i = 0; i< ja.length(); i ++){
					jo = ja.getJSONObject(i);
					forMissedAttendancesSpinner.add(jo.getString("date") + "-" + jo.getString("time") + "-" + jo.getString("name"));
					absenceIds.add(jo.getString("id"));
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}
	}

	class SubmitExplanation extends AsyncTask<String, Void, String>{

		@Override
		protected String doInBackground(String... params) {
			db = new DBHandler();
			JSONArray ja = null;
			postParameters = DBHandler.prepareParams("studentId", studentId);
			postParameters = DBHandler.prepareParams("attendanceId", attendanceId);
			postParameters = DBHandler.prepareParams("explaination", explaination);
			Log.e("Error", "In doInBackground");
			try {
				ja = db.executeQuery(DBHandler.SUBMIT_EXPLANATION, postParameters);
				Log.e("Error", ja.toString());
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}
	}

	public void showAlert(){
		StudentExplainAbs.this.runOnUiThread(new Runnable() {
			public void run() {
				AlertDialog.Builder builder = new AlertDialog.Builder(StudentExplainAbs.this);
				builder.setTitle("Error.");
				builder.setMessage("Fields must be selected.") 
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



