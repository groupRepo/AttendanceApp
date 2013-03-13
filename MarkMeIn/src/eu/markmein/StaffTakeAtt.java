package eu.markmein;

import ie.markmein.development.R;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class StaffTakeAtt extends Activity implements View.OnClickListener {

	private Button btTakePic, btProcess;
	private ImageView iv;
	private ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

	SendImageToServer sendImageToServer;
	Intent i;
	final static int cameraData = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.stafftakeatt);
		initialize();
	}

	private void initialize() {
		btTakePic = (Button) findViewById(R.id.btTakePic);
		btTakePic.setOnClickListener(this);
		btProcess = (Button) findViewById(R.id.btProcess);
		btProcess.setOnClickListener(this);
		iv = (ImageView) findViewById(R.id.ivPic);
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
			sendImageToServer.execute("tect");
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
				InputStream is = sock.getInputStream();
				DataInputStream dis = new DataInputStream(is);
				sock.shutdownOutput();
				//  String ret = dis.readUTF();
				//  System.out.println(ret);
				sock.close();
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
}
