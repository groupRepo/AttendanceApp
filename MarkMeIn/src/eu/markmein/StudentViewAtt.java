package eu.markmein;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

public class StudentViewAtt extends Activity{

	TextView tvStudentID, tvStudentName, tvStudentCourse, tvDetails;
	Button btStuDetails;
	ArrayList<NameValuePair> postParameters;
	String studentId;
	DBHandler db;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		studentId = Login.mUserID;
		setContentView(R.layout.studentviewatt);
		initialize();
	}

	private void initialize() {
		tvStudentID = (TextView) findViewById(R.id.tvStudentID);
		tvStudentName = (TextView) findViewById(R.id.tvStudentName);
		tvStudentCourse = (TextView) findViewById(R.id.tvStudentCourse);
		tvDetails = (TextView) findViewById(R.id.tvDetails);
		GetStudentInfo getStudentInfo = new GetStudentInfo();
		GetData getData = new GetData();
		getStudentInfo.execute("text");
		getData.execute("text");
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
				ja = db.executeQuery(DBHandler.GET_STUDENT_INFO1, postParameters);
				for(int i = 0; i < ja.length(); i++){
					jo = ja.getJSONObject(i);
					stuModsAtt.add(jo.getString("name"));
					stuModsAtt.add("\t" + jo.getInt("attendance"));
					stuModsAtt.add("\t" + jo.getInt("labAttendance"));
					stuModsAtt.add("\t" + jo.getInt("lectureAttendance"));
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
				tvDetails.append("Overall : " + stuModsAtt.get(i+1) + "%\n");
				tvDetails.append("Lab Att : " + stuModsAtt.get(i+2) + "%\n");
				tvDetails.append("Lect Att: " + stuModsAtt.get(i+3) + "%\n\n");
			}
		}
	}
}
