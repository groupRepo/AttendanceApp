package eu.markmein;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
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

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		lecturerId = Login.mUserID;
		setContentView(R.layout.staffstudentrecords);
		initialize();
		getStudents = new GetStudents();
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
		sampleIn.add(0, "Select Student");
		ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, sampleIn);
		spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinnerIn.setAdapter(spinnerArrayAdapter);		
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

	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.btStuDetails:
			if(spStudents.getSelectedItemPosition() == 0){
				showToast("Select A Student");
			}else{
				GetData getData = new GetData();
				GetStudentInfo getStudentInfo = new GetStudentInfo();
				int index = spStudents.getSelectedItemPosition() - 1;
				studentId = studentIDs.get(index);
				getStudentInfo.execute("text");
				getData.execute("text");
			}
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
		String a;
		int b,c,d;
		@Override
		protected String doInBackground(String... params) {
			db = new DBHandler();
			JSONArray ja = null;
			postParameters= DBHandler.prepareParams("studentId", studentId);
			try{
				JSONObject jo = null;
				try{
					ja = db.executeQuery(DBHandler.GET_STUDENT_INFO1, postParameters);
					for(int i = 0; i < ja.length(); i++){
						jo = ja.getJSONObject(i);
						try{
							a = jo.getString("name");
							stuModsAtt.add(a);
						}catch(Exception e){
							Log.e("boo", e.toString());
						}
						try{
							b = jo.getInt("attendance");
							stuModsAtt.add(b + "%");
						}catch(Exception e){
							b = 0;
							stuModsAtt.add(b + "%");
						}
						try{
							c = jo.getInt("labAttendance");
							stuModsAtt.add(c + "%");
						}catch(Exception e){
							c = 0;
							stuModsAtt.add(c + "%");
						}
						try{
							d = jo.getInt("lectureAttendance");
							stuModsAtt.add(d + "%");
						}catch(Exception e){
							d = 0;
							stuModsAtt.add(d + "%");
						}
					}
				}catch(Exception e){
					Log.e("Boo", e.toString());
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
			tvDetails.append("\nAttendance Statistics\n\n");
			for(int i = 0; i< stuModsAtt.size(); i+=4){
				tvDetails.append("Module: " + stuModsAtt.get(i) + "\n");
				tvDetails.append("\tOverall : " + stuModsAtt.get(i+1) + "\n");
				tvDetails.append("\tLab Att : " + stuModsAtt.get(i+2) + "\n");
				tvDetails.append("\tLect Att: " + stuModsAtt.get(i+3) + "\n\n");
			}
		}
	}
}
