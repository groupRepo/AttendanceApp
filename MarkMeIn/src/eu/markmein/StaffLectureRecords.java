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

public class StaffLectureRecords extends Activity implements View.OnClickListener{
	Button btStaffMenu;
	TextView tvBestLectAtt, tvWorstLectAtt;
	DBHandler db;
	ArrayList<NameValuePair> postParameters;
	GetData getData;
	Intent i;

	String lecturerId;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.stafflecturerecords);
		lecturerId = Login.mUserID;
		initialize();
		getData = new GetData();
		getData.execute("text");
	}

	private void initialize() {
		tvBestLectAtt = (TextView) findViewById(R.id.tvBestLectAtt);
		tvWorstLectAtt = (TextView) findViewById(R.id.tvWorstLectAtt);
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
				ja = db.executeQuery(DBHandler.GET_BEST_WORST_LECTURE_RECORDS, postParameters);
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
			tvBestLectAtt.setText("");
			tvBestLectAtt.append(a + " - " + b + " - " + c);
			tvWorstLectAtt.setText("");
			tvWorstLectAtt.append(d + " - " + e + " - " + f);
		}
	}
}
