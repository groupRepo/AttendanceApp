package eu.markmein;

import java.io.IOException;
import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.json.JSONArray;
import org.json.JSONException;

import eu.markmein.R;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class StaffLabRecords extends Activity implements View.OnClickListener{
	Button btStaffMenu;
	TextView tvBestLabAtt, tvWorstLabAtt;
	DBHandler db;
	ArrayList<NameValuePair> postParameters;
	
	Intent i;
	
	String lecturerId;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.stafflabrecords);
		lecturerId = Login.mUserID;
		initialize();
		GetData getData = new GetData();
		getData.execute("text");
	}

	private void initialize() {
		tvBestLabAtt = (TextView) findViewById(R.id.tvBestLabAtt);
		tvWorstLabAtt = (TextView) findViewById(R.id.tvWorstLabAtt);
		btStaffMenu = (Button) findViewById(R.id.btMainMenu);
		btStaffMenu.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.btMainMenu:
			i = new Intent("eu.markmein.STAFFMENU");
			startActivity(i);
			break;
		}
	}

	class GetData extends AsyncTask<String, Void, String>{
		String a,b,d,e;
		int c,f;
		@Override
		protected String doInBackground(String... params) {
			db = new DBHandler();
			JSONArray ja = null;
			Log.e("lecturerId", lecturerId);
			postParameters = DBHandler.prepareParams("lecturerId", lecturerId);
			try{
				ja = db.executeQuery(DBHandler.GET_BEST_WORST_LAB_RECORDS, postParameters);
				a = ja.getJSONObject(0).getString("code");
				b = ja.getJSONObject(0).getString("name");
				c = ja.getJSONObject(0).getInt("attendance");
				d = ja.getJSONObject(1).getString("code");
				e = ja.getJSONObject(1).getString("name");
				f = ja.getJSONObject(1).getInt("attendance");
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
			tvBestLabAtt.setText("");
			tvBestLabAtt.append(a + " - " + b + " - " + c);
			tvWorstLabAtt.setText("");
			tvWorstLabAtt.append(d + " - " + e + " - " + f);
		}
	}
}
