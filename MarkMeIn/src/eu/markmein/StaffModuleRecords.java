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

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
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
		ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, sampleIn);
		spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinnerIn.setAdapter(spinnerArrayAdapter);		
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.btGetRecords:
			int index = spModule.getSelectedItemPosition();
			String code = modulesIds.get(index);
			
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
}
