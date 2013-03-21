package eu.markmein;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
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
	
	ProgressDialog dialog;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		studentId = Login.mUserID;
		setContentView(R.layout.studentviewatt);
		initialize();
	}

	private void initialize() {
		dialog = ProgressDialog.show(StudentViewAtt.this, "", "loading", true);
		tvStudentID = (TextView) findViewById(R.id.tvStudentID);
		tvStudentName = (TextView) findViewById(R.id.tvStudentName);
		tvStudentCourse = (TextView) findViewById(R.id.tvStudentCourse);
		tvDetails = (TextView) findViewById(R.id.tvDetailsStudent);
		GetStudentInfo getStudentInfo = new GetStudentInfo();
		GetData getData = new GetData();
		getStudentInfo.execute("text");
		getData.execute("text");
		//dialog.cancel();
	}

	public class GetStudentInfo extends AsyncTask<String, Void, String>{
		String id, name, course;
		@Override
		protected String doInBackground(String... params) {
			db = new DBHandler();
			JSONArray ja = null;
			postParameters= DBHandler.prepareParams("studentId", studentId);
			try{
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
			dialog.cancel();
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
