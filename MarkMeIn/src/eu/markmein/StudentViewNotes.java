package eu.markmein;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.util.ByteArrayBuffer;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

public class StudentViewNotes extends Activity implements View.OnClickListener{
	Spinner spModule, spNotes;
	Button btGetNotes, btDownload;
	ListView lvNotes;
	Intent i;
	//ArrayAdapter<String> adapter;

	DBHandler db;
	ArrayList<NameValuePair> postParametersA;
	ArrayList<String> forModuleSpinner = new ArrayList<String>();
	ArrayList<String> modulesIds = new ArrayList<String>();
	GetStudentModules getStudentModules;
	String studentId, url, name;
	ArrayList<NameValuePair> postParametersB;
	String moduleOfferingId;
	ArrayList<String> forListViewDisplay = new ArrayList<String>();
	ArrayList<String> urlLinks = new ArrayList<String>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.studentviewnotes);
		initialize();
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.btGetNotes:
			if((spModule.getSelectedItemPosition()) == 0){
				showToast("Select A Module");
			}
			else{
				int index = spModule.getSelectedItemPosition() - 1;
				moduleOfferingId = modulesIds.get(index);
				Log.e("MID", moduleOfferingId);
				GetNotes getNotes = new GetNotes();
				getNotes.execute("text");
				populateSpinner(spNotes, forListViewDisplay);
				spNotes.setVisibility(View.VISIBLE);
			}
			break;
		case R.id.btDownload:
			if((spNotes.getSelectedItemPosition()) == 0){
				showToast("Select A File To Download");
			}
			else{
				int index = spNotes.getSelectedItemPosition() - 1;
				url = urlLinks.get(index);
				name = forListViewDisplay.get(index+1);
				DownloadFile downloadFile = new DownloadFile();
				downloadFile.execute("text");
				//showAlert("Download", "The file has finished downloading\n Return to Main Menu?");

			}
			break;
		}
	}

	private void initialize() {
		studentId = Login.mUserID;
		spModule = (Spinner) findViewById(R.id.spModule);
		spNotes = (Spinner) findViewById(R.id.spNotes);
		spNotes.setVisibility(View.INVISIBLE);
		btGetNotes = (Button) findViewById(R.id.btGetNotes);
		btGetNotes.setOnClickListener(this);
		btDownload = (Button) findViewById(R.id.btDownload);
		btDownload.setOnClickListener(this);
		getStudentModules = new GetStudentModules();
		getStudentModules.execute("tect");
		populateSpinner(spModule, forModuleSpinner);

	}

	private void populateSpinner(Spinner spinnerIn, ArrayList<String> sampleIn) {
		sampleIn.add(0, "Select Module");
		ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, sampleIn);
		spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinnerIn.setAdapter(spinnerArrayAdapter);		
	}

	class GetNotes extends AsyncTask<String, Void, Void>{

		@Override
		protected Void doInBackground(String... params) {
			db = new DBHandler();
			JSONArray ja = null;
			postParametersB = DBHandler.prepareParams("moduleOfferingId", moduleOfferingId);
			Log.e("Working", moduleOfferingId);
			try{
				JSONObject jo = null;
				Log.e("Working", "indfisdnfo");
				Log.e("Working1", moduleOfferingId);
				ja = db.executeQuery(DBHandler.GET_MODULE_NOTES, postParametersB);
				Log.e("Error", ja.toString());
				//forListViewDisplay.removeAll(forListViewDisplay);
				for(int i = 0; i < ja.length(); i++){
					jo = ja.getJSONObject(i);
					Log.e("Error", jo.toString());
					urlLinks.add(jo.getString("link"));
					forListViewDisplay.add(jo.getString("description"));
				}
			}catch(Exception e){
				Log.e("dib Notes", e.toString());
			}
			return null;
		}
	}

	class GetStudentModules extends AsyncTask<String, Void, String>{

		@Override
		protected String doInBackground(String... params) {
			db = new DBHandler();
			JSONArray ja = null;
			postParametersA= DBHandler.prepareParams("studentId",studentId);
			Log.e("Error", "In doInBackground");
			try {
				JSONObject jo = null;
				ja = db.executeQuery(DBHandler.GET_STUDENT_CLASSES, postParametersA);
				Log.e("Error", ja.toString());
				for(int i = 0; i< ja.length(); i ++){
					jo = ja.getJSONObject(i);
					forModuleSpinner.add(jo.getString("code") + "-" + jo.getString("name"));
					modulesIds.add(jo.getString("code"));
				}
			} catch (Exception e) {
				Log.e("Error", "In doInBackground Exception" + e.toString() );
			}
			return null;
		}
	}

	public void showAlert(final String title, final String message){
		StudentViewNotes.this.runOnUiThread(new Runnable() {
			public void run() {
				AlertDialog.Builder builder = new AlertDialog.Builder(StudentViewNotes.this);
				builder.setTitle(title);
				builder.setMessage(message) 
				.setCancelable(false)
				.setNegativeButton("No", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
					}
				})
				.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						i = new Intent("eu.markmein.STUDENTMENU");
						startActivity(i);
					}
				});                    
				AlertDialog alert = builder.create();
				alert.show();              
			}
		});
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

	class DownloadFile extends AsyncTask<String, Void, String>{
		private final int TIMEOUT_CONNECTION = 5000;
		private final int TIMEOUT_SOCKET = 30000;
		@Override
		protected String doInBackground(String... params) {
			try {
				
				File root = android.os.Environment.getExternalStorageDirectory();               
				File dir = new File (root.getAbsolutePath() + "/testing");
				if(dir.exists()==false) {
					dir.mkdirs();
				}
				File file = new File(dir, name);
				
				URL urlLink = new URL(url);
				long startTime = System.currentTimeMillis();
				Log.e("DownloadManager", "download url:" + urlLink);
				
				URLConnection urlConnection = urlLink.openConnection();
				urlConnection.setReadTimeout(TIMEOUT_CONNECTION);
				urlConnection.setConnectTimeout(TIMEOUT_SOCKET);
				
				Log.e("DownloadManager", "open connection");
				InputStream inputStream = urlConnection.getInputStream();
				BufferedInputStream bufferedInputstream = new BufferedInputStream(inputStream, 1024 * 5);
				FileOutputStream fileOutputStream = new FileOutputStream(file);
				Log.e("DownloadManager", "download begining");
				//Read bytes to the Buffer until there is nothing more to read(-1).
				byte[] buffer = new byte[5*1024];
				int len;
				while ((len = bufferedInputstream.read()) != -1) {
					fileOutputStream.write(buffer, 0, len);
				}
				Log.e("DownloadManager", "after for loop");
				fileOutputStream.flush();
				Log.e("DownloadManager", "flush");
				fileOutputStream.close();
				inputStream.close();
				Log.e("Time", "download completed in "+ ((System.currentTimeMillis() - startTime) / 1000)+ " sec");
			} catch (IOException e) {
				Log.d("DownloadManager", "Error: " + e);
			}
			return null;
		}
	}
}
