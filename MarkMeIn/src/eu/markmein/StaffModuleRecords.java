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

public class StaffModuleRecords extends Activity implements View.OnClickListener{

	Spinner spModule;
	TextView tvStaffModRecs;
	Button btGetRecords;

	ArrayList<NameValuePair> postParameters;
	ArrayList<String> forModuleSpinner = new ArrayList<String>();
	ArrayList<String> modulesIds = new ArrayList<String>();

	String code;
	String lecturerId;

	DBHandler db;
	GetLecturersModules getLectMods;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		lecturerId = Login.mUserID;
		setContentView(R.layout.staffmodulerecords);
		initialize();
		getLectMods = new GetLecturersModules();
		getLectMods.execute("text");
		populateSpinner(spModule, forModuleSpinner);
	}

	private void initialize() {
		spModule = (Spinner) findViewById(R.id.spModule);
		tvStaffModRecs = (TextView) findViewById(R.id.tvStaffModRec);
		btGetRecords = (Button) findViewById(R.id.btGetRecords);
		btGetRecords.setOnClickListener(this);
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
		case R.id.btGetRecords:
			GetData getData = new GetData();
			int index = spModule.getSelectedItemPosition() - 1;
			code = modulesIds.get(index);
			getData.execute("text");
			break;
		}
	}

	class GetLecturersModules extends AsyncTask<String, Void, String>{
		@Override
		protected String doInBackground(String... params) {
			db = new DBHandler();
			JSONArray ja = null;
			postParameters= DBHandler.prepareParams("lecturerId", lecturerId);
			try {
				JSONObject jo = null;
				ja = db.executeQuery(DBHandler.GET_LECTURERES_CLASSES, postParameters);
				for(int i = 0; i < ja.length(); i ++){
					jo = ja.getJSONObject(i);
					forModuleSpinner.add(jo.getString("code") + "-" + jo.getString("name"));
					modulesIds.add(jo.getString("code"));
				}
			}catch(Exception e){
				Log.e("dib", e.toString());
			}
			return null;
		}
	}

	class GetData extends AsyncTask<String, Void, String>{
		int a,b,c;
		String h,i;
		@Override
		protected String doInBackground(String... params) {
			db = new DBHandler();
			JSONArray ja = null;
			postParameters = DBHandler.prepareParams("code", code);
			try{
				ja = db.executeQuery(DBHandler.GET_MODULE_ATTENDANCE_RECORDS, postParameters);
				a = ja.getJSONObject(0).getInt("overall");
				b = ja.getJSONObject(1).getInt("lab");
				c = ja.getJSONObject(2).getInt("lecture");
				h = ja.getJSONObject(3).getString("best");
				i = ja.getJSONObject(4).getString("worst");
			}catch(Exception e){
				Log.e("dib", e.toString());
			}
			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			tvStaffModRecs.setText("");
			tvStaffModRecs.append("\nLecture Attendance:\t\t\t" + a + "%\n\n");
			tvStaffModRecs.append("Laboratory Attendance:\t\t" + b + "%\n\n");
			tvStaffModRecs.append("Overall Attendance:\t\t\t" + c + "%\n\n");
			tvStaffModRecs.append("\nStudent\n\n");
			tvStaffModRecs.append("Best Attendance:\t\t" + h + "\n\n");
			tvStaffModRecs.append("Worst Attendance:\t\t" + i + "\n\n");
		}
	}
}