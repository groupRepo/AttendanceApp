package eu.markmein;

import java.io.IOException;
import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.json.JSONArray;
import org.json.JSONException;
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

	DBHandler db;
	GetLecturersModules getLectMods;
	GetData getData;

	int a,b,c,d,e,f,g, x,y,z;
	String h,i;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.staffmodulerecords);
		initialize();
		getLectMods = new GetLecturersModules();
		getData = new GetData();
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
		ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, sampleIn);
		spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinnerIn.setAdapter(spinnerArrayAdapter);		
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.btGetRecords:
			//int index = spModule.getSelectedItemPosition();
			//String code = modulesIds.get(index);
			getData.execute("text");
			break;
		}
	}

	class GetLecturersModules extends AsyncTask<String, Void, String>{
		@Override
		protected String doInBackground(String... params) {
			db = new DBHandler();
			JSONArray ja = null;
			postParameters= DBHandler.prepareParams("lecturerId","S00012345");
			Log.e("Error", "In doInBackground");
			try {
				JSONObject jo = null;
				ja = db.executeQuery(DBHandler.GET_LECTURERES_CLASSES, postParameters);
				Log.e("Error", ja.toString());
				for(int i = 0; i< ja.length(); i ++){
					jo = ja.getJSONObject(i);
					forModuleSpinner.add(jo.getString("code") + "-" + jo.getString("name"));
					modulesIds.add(jo.getString("code"));
				}
			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return null;
		}
	}

	class GetData extends AsyncTask<String, Void, String>{

		int a,b,c,d,e,f,g,x,y,z;
		String h,i;
		@Override
		protected String doInBackground(String... params) {
			Log.e("Error1", "In 2nd dib 1");
			db = new DBHandler();
			JSONArray ja = null;
			postParameters = DBHandler.prepareParams("code", "CRN8081");
			Log.e("Error2", "In 2nd dib 1");
			try{
				//JSONObject jo = null;
				Log.e("Error3", "In 2nd dib 1");
				ja = db.executeQuery(DBHandler.GET_MODULE_ATTENDANCE_RECORDS, postParameters);
				Log.e("Error1", ja.toString());
				//jo = ja.getJSONObject(0);
				
				a = ja.getJSONObject(0).getInt("overall");
				b = ja.getJSONObject(1).getInt("lab");
				c = ja.getJSONObject(2).getInt("lecture");
				
				h = ja.getJSONObject(3).getString("best");
				i = ja.getJSONObject(4).getString("worst");

			} catch (ClientProtocolException e) {
				Log.e("Catch1", e.toString());
			} catch (IOException e) {
				Log.e("Catch2", e.toString());
			} catch (JSONException e) {
				Log.e("Catch3", e.toString());
			}
			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			
			tvStaffModRecs.setText("");
			tvStaffModRecs.append("\nLecture Attendance:\t\t\t" + a + "%\n\n");
			tvStaffModRecs.append("Laboratory Attendance:\t" + b + "%\n\n");
			tvStaffModRecs.append("Overall Attendance:\t\t\t" + c + "%\n\n");

			tvStaffModRecs.append("\nStudent\n\n");
			tvStaffModRecs.append("Best Attendance:\t\t" + h + "\n\n");
			tvStaffModRecs.append("Worst Attendance:\t" + i + "\n\n");
		}
	}
}