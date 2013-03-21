package eu.markmein;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

public class StaffTakeAtt extends Activity implements View.OnClickListener {
	private Button btTakePic, btProcess;
	private ImageView iv;
	private ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
	private Spinner spModule, spLabLect;
	DBHandler db;

	SendImageToServer sendImageToServer;
	Intent i;

	String code;
	
	boolean hasPicBeenTaken = false;

	ProgressDialog dialog;

	ArrayList<NameValuePair> postParameters;
	ArrayList<String> sample = new ArrayList<String>();

	ArrayList<String> forModuleSpinner = new ArrayList<String>();
	ArrayList<String> modulesIds = new ArrayList<String>();
	GetLecturersModules getLectMods;
	String lecturerId;
	View focusView = null;
	final static int cameraData = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		dialog = ProgressDialog.show(StaffTakeAtt.this, "", "Loading...", true);
		setContentView(R.layout.stafftakeatt);
		lecturerId = Login.mUserID;
		initialize();
		getLectMods = new GetLecturersModules();
		getLectMods.execute("text");
		populateSpinner("Select a Module", spModule, forModuleSpinner);
		populateSpinner("Select a Classtype", spLabLect, sample);
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

	private void populateSpinner(String firstItem, Spinner spinnerIn, ArrayList<String> sampleIn) {
		sampleIn.add(0, firstItem);
		ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, sampleIn);
		spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinnerIn.setAdapter(spinnerArrayAdapter);		
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

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.btTakePic:
			if((spModule.getSelectedItemPosition() == 0) || (spLabLect.getSelectedItemPosition() == 0) ){
				showToast("Ensure Module And Class Type Are Selected");
			}else{
				hasPicBeenTaken = true;
				i = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
				startActivityForResult(i, cameraData);
			}
			break;
		case R.id.btProcess:
			if(hasPicBeenTaken == true){
				dialog = ProgressDialog.show(StaffTakeAtt.this, "Info", "Processing Image", true);
				sendImageToServer = new SendImageToServer();
				sendImageToServer.execute("text");
			}
			else{
				showToast("Capture Class Attendance First!!");
			}
			
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

				int index = spModule.getSelectedItemPosition() - 1;
				code = modulesIds.get(index);

				//do something 
				File f = null;
				File[] a = null;

				String make = android.os.Build.MANUFACTURER;

				if(make.startsWith("HTC")){
					f = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM + "/100MEDIA" );
				}
				else if(make.startsWith("samsung")){
					f = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM + "/Camera");	
				}
				a = f.listFiles();

				String path = null;
				long lastMod = Long.MIN_VALUE;
				for(File ff : a){
					if(ff.lastModified() > lastMod){
						lastMod = ff.lastModified();
						path = ff.getAbsolutePath();
					}
				}

				f = new File(path);

				byte [] mybytearray = new byte [(int) f.length()];
				FileInputStream fis = new FileInputStream(f);
				BufferedInputStream bis = new BufferedInputStream(fis);
				bis.read(mybytearray,0,mybytearray.length);

				outputStream = new ByteArrayOutputStream();
				OutputStream os = sock.getOutputStream();
				ObjectOutputStream dOutStream = new ObjectOutputStream(os);
				//send code
				dOutStream.writeUTF(code);
				//send length of the file
				dOutStream.writeInt(mybytearray.length);
				//send file
				dOutStream.write(mybytearray,0,mybytearray.length);
				dOutStream.flush();
				bis.close();
				sock.shutdownOutput();

				InputStream is = sock.getInputStream();
				ObjectInputStream ois = new ObjectInputStream(is);
				ArrayList<String> present = new ArrayList<String>();
				present = (ArrayList<String>) ois.readObject();
				present.add(0, code);
				sock.close();

				i = new Intent("eu.markmein.STAFFLISTATT");
				i.putStringArrayListExtra("list", present);
				startActivity(i);

			} catch (Exception e) {
				Log.e("Error", "In doInBackground Exception" + e.toString() );
			}
			dialog.cancel();
			return null;
		}
	}

	class GetLecturersModules extends AsyncTask<String, Void, String>{

		@Override
		protected String doInBackground(String... params) {
			db = new DBHandler();
			JSONArray ja = null;
			postParameters= DBHandler.prepareParams("lecturerId",lecturerId);
			try {
				JSONObject jo = null;
				ja = db.executeQuery(DBHandler.GET_LECTURERES_CLASSES, postParameters);
				for(int i = 0; i< ja.length(); i ++){
					jo = ja.getJSONObject(i);
					forModuleSpinner.add(jo.getString("code") + "-" + jo.getString("name"));
					modulesIds.add(jo.getString("code"));
				}
			} catch (Exception e) {
				Log.e("Error", "In doInBackground Exception" + e.toString() );
			}
			dialog.cancel();
			return null;
		}
	}
}