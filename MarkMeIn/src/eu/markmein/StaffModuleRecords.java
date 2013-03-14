package eu.markmein;

import java.io.IOException;
import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import eu.markmein.R;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

public class StaffModuleRecords extends Activity implements View.OnClickListener{

	Spinner spDept, spModule;
	TextView tvStaffModRecs;
	Button btGetRecords;
	ArrayList<NameValuePair> postParameters;
	ArrayList<String> forDeptSpinner = new ArrayList<String>();
	
	DBHandler db;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.staffmodulerecords);
		initialize();
		populateSpinner(spDept, forDeptSpinner);
	}

	private void initialize() {
		spDept = (Spinner) findViewById(R.id.spDept);
		spModule = (Spinner) findViewById(R.id.spModule);
		tvStaffModRecs = (TextView) findViewById(R.id.tvStaffModRec);
		btGetRecords = (Button) findViewById(R.id.btGetRecords);
		btGetRecords.setOnClickListener(this);
		db = new DBHandler();
		JSONArray ja = null;
		postParameters= DBHandler.prepareParams("lecturerId","S00012345");
		
		try {
			JSONObject jo = null;
			ja = db.executeQuery(DBHandler.GET_MODULE_OFFERING_TRDATA, postParameters);
			for(int i = 0; i< ja.length(); i ++){
				jo = ja.getJSONObject(0);
				forDeptSpinner.add(jo.toString());
			}
			
			
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
	}
	
	private void populateSpinner(Spinner spinnerIn, ArrayList<String> sampleIn) {
		sampleIn.add(0, "Nothing Selected");
		ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, sampleIn);
		spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinnerIn.setAdapter(spinnerArrayAdapter);		
	}
	
	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.btGetRecords:
			break;
		}
	}

}
