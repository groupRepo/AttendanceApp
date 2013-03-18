package eu.markmein;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

public class StaffStudentRecords extends Activity implements View.OnClickListener{

	Spinner spStudents;
	TextView tvStudentID, tvStudentName, tvStudentCourse, tvDetails;
	Button btStuDetails;

	ArrayList<NameValuePair> postParameters;
	ArrayList<String> forStudentSpinner = new ArrayList<String>();
	ArrayList<String> studentIDs = new ArrayList<String>();

	String studentId;
	String lecturerId;

	DBHandler db;
	GetStudents getStudents;
	GetStudentInfo getStudentInfo;
	GetData getData;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		lecturerId = Login.mUserID;
		setContentView(R.layout.staffstudentrecords);
		initialize();
		getStudents = new GetStudents();
		getStudentInfo = new GetStudentInfo();
		getData = new GetData();
		getStudents.execute("text");
		populateSpinner(spStudents, forStudentSpinner);
	}

	private void initialize() {
		spStudents = (Spinner) findViewById(R.id.spStudents);
		tvStudentID = (TextView) findViewById(R.id.tvStudentID);
		tvStudentName = (TextView) findViewById(R.id.tvStudentName);
		tvStudentCourse = (TextView) findViewById(R.id.tvStudentCourse);
		tvDetails = (TextView) findViewById(R.id.tvDetails);
		btStuDetails = (Button) findViewById(R.id.btStuDetails);
		btStuDetails.setOnClickListener(this);	
	}

	private void populateSpinner(Spinner spinnerIn, ArrayList<String> sampleIn) {
		sampleIn.add(0, "Select Item");
		ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, sampleIn);
		spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinnerIn.setAdapter(spinnerArrayAdapter);		
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.btStuDetails:
			int index = spStudents.getSelectedItemPosition() - 1;
			studentId = studentIDs.get(index);
			getStudentInfo.execute("text");
			getData.execute("text");
			//getData.cancel(isDestroyed());
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

	public class GetStudentInfo extends AsyncTask<String, Void, String>{
		String id, name, course;
		@Override
		protected String doInBackground(String... params) {
			db = new DBHandler();
			JSONArray ja = null;
			postParameters= DBHandler.prepareParams("studentId", studentId);
			try{
				//JSONObject jo = null;
				ja = db.executeQuery(DBHandler.GET_STUDENT_INFO, postParameters);
				id = ja.getJSONObject(0).getString("id");
				name = ja.getJSONObject(0).getString("name");
				course = ja.getJSONObject(0).getString("courseName");				
			}catch(Exception e){
				Log.e("dib1", e.toString());
			}
			return null;
		}
		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			tvStudentID.setText("");
			tvStudentID.append(id);
			tvStudentName.setText("");
			tvStudentName.append(name);
			tvStudentCourse.setText("");
			tvStudentCourse.append(course);
		}
	}

	public class GetData extends AsyncTask<String, Void, String>{
		ArrayList<String> stuModsAtt = new ArrayList<String>();
		@Override
		protected String doInBackground(String... params) {
			db = new DBHandler();
			JSONArray ja = null;
			postParameters= DBHandler.prepareParams("studentId", studentId);
			try{
				JSONObject jo = null;
				ja = db.executeQuery(DBHandler.GET_STUDENT_MODULES_ATTENDANCES, postParameters);
				for(int i = 0; i < ja.length(); i++){
					jo = ja.getJSONObject(i);
					stuModsAtt.add(jo.getInt("attendance" ) + "%\t- " +jo.getString("name"));
				}
			}catch(Exception e){
				Log.e("dib2", e.toString());
			}
			return null;
		}
		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			tvDetails.setText("");
			tvDetails.append("\n%\t\t - Module\n\n");
			for(int i = 0; i< stuModsAtt.size(); i++){
				tvDetails.append(stuModsAtt.get(i) + "\n");
			}
		}
	}
}
