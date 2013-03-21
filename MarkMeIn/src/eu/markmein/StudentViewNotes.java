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
			try{
				JSONObject jo = null;
				ja = db.executeQuery(DBHandler.GET_MODULE_NOTES, postParametersB);
				for(int i = 0; i < ja.length(); i++){
					jo = ja.getJSONObject(i);
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
			try {
				JSONObject jo = null;
				ja = db.executeQuery(DBHandler.GET_STUDENT_CLASSES, postParametersA);
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
				name = url.substring(url.lastIndexOf("/")+1, url.length());
				File file = new File(dir, name);
				
				URL urlLink = new URL(url);
				URLConnection urlConnection = urlLink.openConnection();
				urlConnection.setReadTimeout(TIMEOUT_CONNECTION);
				urlConnection.setConnectTimeout(TIMEOUT_SOCKET);
				int size = urlConnection.getContentLength();
				
				InputStream inputStream = urlConnection.getInputStream();
				BufferedInputStream bufferedInputstream = new BufferedInputStream(inputStream);
				FileOutputStream fileOutputStream = new FileOutputStream(file);
				
				byte[] buffer = new byte[size +1];
				
				int currentByte = 0;
				int bytesRead = bufferedInputstream.read(buffer, 0,buffer.length);
				currentByte = bytesRead;
				System.out.println("Bytes read in first go.: " + currentByte);
				do{
					bytesRead = bufferedInputstream.read(buffer, currentByte, (buffer.length-currentByte));
					if(bytesRead >= 0){
						currentByte += bytesRead;
					}
				}while(bytesRead > 0);
				fileOutputStream.write(buffer, 0, buffer.length);
				fileOutputStream.flush();
				fileOutputStream.close();
				inputStream.close();
			} catch (IOException e) {
				Log.d("DownloadManager", "Error: " + e);
			}
			return null;
		}
	}
}
