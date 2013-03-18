package eu.markmein;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import eu.markmein.StaffStudentRecords.GetStudents;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

public class StaffViewAbs  extends Activity implements View.OnClickListener {

	Spinner spStudents;
	TextView tvAbsences;
	Button btGetAbsences, btMainMenu;

	ArrayList<NameValuePair> postParameters;
	ArrayList<String> forStudentSpinner = new ArrayList<String>();
	ArrayList<String> studentIDs = new ArrayList<String>();

	DBHandler db;
	GetStudents getStudents;
	GetStudentAbsences getStudentAbsences;

	String lecturerId;
	String studentId;

	Intent i;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
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
			int index = spStudents.getSelectedItemPosition() - 1;
			studentId = studentIDs.get(index);
			getStudentAbsences = new GetStudentAbsences();
			getStudentAbsences.execute("text");
			break;
		case R.id.btMainMenu:
			i = new Intent("eu.markmein.STAFFMENU");
			startActivity(i);
			break;
		}
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
			return null;
		}
	}

	public class GetStudentAbsences extends AsyncTask<String, Void, String>{
		ArrayList<String> excuses = new ArrayList<String>();
		@Override
		protected String doInBackground(String... params) {
			db = new DBHandler();
			JSONArray ja = null;
			postParameters= DBHandler.prepareParams("lecturerId", lecturerId);
			postParameters= DBHandler.prepareParams("studentId", studentId);
			try{
				JSONObject jo = null;
				ja = db.executeQuery(DBHandler.GET_STUDENT_ABSENCES, postParameters);
				for(int i = 0; i < ja.length(); i++){
					jo = ja.getJSONObject(i);
					/*
					 * Do the parsing!
					 */
					excuses.add(lecturerId/* the parsesd data is to go here*/);
				}
			}catch(Exception e){
				Log.e("dib0", e.toString());
			}
			return null;
		}
		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			tvAbsences.setText("");
			tvAbsences.append("\nAbsence Explanations for" + studentId + "\n\n");
			for(int i = 0; i< excuses.size(); i++){
				tvAbsences.append(excuses.get(i) + "\n");
			}
		}
	}
}
