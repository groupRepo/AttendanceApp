package eu.markmein;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import eu.markmein.StaffModuleRecords.GetLecturersModules;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;

public class StaffTakeAtt extends Activity implements View.OnClickListener {
	private Button btTakePic, btProcess;
	private ImageView iv;
	private ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
	private Spinner spModule, spLabLect;
	DBHandler db;

	SendImageToServer sendImageToServer;
	Intent i;
	
	ArrayList<NameValuePair> postParameters;
	ArrayList<String> sample = new ArrayList<String>();
	ArrayList<String> forModuleSpinner = new ArrayList<String>();
	ArrayList<String> modulesIds = new ArrayList<String>();
	
	String moduleSelection = null;
	GetLecturersModules getLectMods;
	String lecturerId;
	
	final static int cameraData = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.stafftakeatt);
		lecturerId = Login.mUserID;
		initialize();
		getLectMods = new GetLecturersModules();
		getLectMods.execute("text");
		populateSpinner(spModule, forModuleSpinner);
		populateSpinner(spLabLect, sample);
	}

	private void initialize() {
		sample.add("Lecture");
		sample.add("Laboratory");
		
		btTakePic = (Button) findViewById(R.id.btTakePic);
		btTakePic.setOnClickListener(this);
		btProcess = (Button) findViewById(R.id.btProcess);
		btProcess.setOnClickListener(this);
		iv = (ImageView) findViewById(R.id.ivPic);
		spModule = (Spinner) findViewById(R.id.spModule);
		spLabLect = (Spinner) findViewById(R.id.spLabLect);
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
		case R.id.btTakePic:
			i = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
			startActivityForResult(i, cameraData);
			break;
		case R.id.btProcess:
			sendImageToServer = new SendImageToServer();
			sendImageToServer.execute("text");
		}		
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode == RESULT_OK){
			Bundle extras = data.getExtras();
			Bitmap bmp = (Bitmap) extras.get("data");
			iv.setImageBitmap(bmp);
			bmp.compress(CompressFormat.JPEG, 100, outputStream);
		}
	}

	public class SendImageToServer extends AsyncTask<String, Void, String>{
		@SuppressWarnings("unchecked")
		@Override
		protected String doInBackground(String... params) {
			Socket sock;
			try {
				sock = new Socket("www.markmein.eu", 2221); 
				
				byte [] mybytearray  = outputStream.toByteArray();
				outputStream = new ByteArrayOutputStream();
				OutputStream os = sock.getOutputStream();
				ObjectOutputStream dOutStream = new ObjectOutputStream(os);
				dOutStream.writeUTF("CRN8081");
				dOutStream.writeInt(mybytearray.length);
				dOutStream.write(mybytearray,0,mybytearray.length);
				dOutStream.flush();
				
				sock.shutdownOutput();
				
				InputStream is = sock.getInputStream();
				ObjectInputStream ois = new ObjectInputStream(is);
				ArrayList<String> present = new ArrayList<String>();
				present = (ArrayList<String>) ois.readObject();
				sock.close();

				i = new Intent("eu.markmein.STAFFLISTATT");
				i.putStringArrayListExtra("list", present);
				startActivity(i);

			} catch (UnknownHostException e) {
				Log.e("Error", "In doInBackground Exception" + e.toString() );
			} catch (IOException e) {
				Log.e("Error", "In doInBackground Exception" + e.toString() );			}
			catch (Exception e) {
				Log.e("Error", "In doInBackground Exception" + e.toString() );
			}
			return null;
		}
	}
	
	class GetLecturersModules extends AsyncTask<String, Void, String>{

		@Override
		protected String doInBackground(String... params) {
			db = new DBHandler();
			JSONArray ja = null;
			postParameters= DBHandler.prepareParams("lecturerId",lecturerId);
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
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}

	}
}
