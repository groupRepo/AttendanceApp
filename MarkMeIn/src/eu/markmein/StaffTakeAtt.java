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

public class StaffTakeAtt extends Activity implements View.OnClickListener, OnItemSelectedListener {
	private Button btTakePic, btProcess;
	private ImageView iv;
	private ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
	private Spinner spModule;

	SendImageToServer sendImageToServer;
	Intent i;
	
	ArrayList<String> sample = new ArrayList<String>();
	ArrayList<String> forModuleSpinner = new ArrayList<String>();
	
	String moduleSelection = null;
	
	final static int cameraData = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.stafftakeatt);
		initialize();
		populateSpinner(spModule, sample);
	}

	private void initialize() {
		btTakePic = (Button) findViewById(R.id.btTakePic);
		btTakePic.setOnClickListener(this);
		btProcess = (Button) findViewById(R.id.btProcess);
		btProcess.setOnClickListener(this);
		iv = (ImageView) findViewById(R.id.ivPic);
		spModule = (Spinner) findViewById(R.id.spModule);
		spModule.setOnItemSelectedListener(this);
		sample.add("Group Project");
		sample.add("System Programming");
		sample.add("Network Security");
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

	@Override
	public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
			long arg3) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub
		
	}
}
