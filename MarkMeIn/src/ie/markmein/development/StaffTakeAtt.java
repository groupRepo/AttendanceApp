package ie.markmein.development;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class StaffTakeAtt extends Activity implements View.OnClickListener {

	private Button btTakePic, btProcess;
	private ImageView iv;
	private TextView tvStatus;

	private Socket client;

	private PrintWriter printwriter;
	private BufferedInputStream bufferedInputStream;
	private OutputStream outputStream;

	SendImageToServer sendImageToServer;
	Intent i;
	final static int cameraData = 0;
	Bitmap bmp;

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
		tvStatus = (TextView) findViewById(R.id.tvStatus);
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
			bmp = (Bitmap) extras.get("data");
			iv.setImageBitmap(bmp);
		}
	}

	public class SendImageToServer extends AsyncTask<String, Void, String>{

		@Override
		protected String doInBackground(String... params) {
			Log.e("Error", "In doInBackground 1" );
			//tvStatus.setText(":"+ Environment.getExternalStorageDirectory().getPath().toString());

			Socket sock;
			try {
				sock = new Socket("172.20.10.7", 2221); 
				
				Log.e("Error", "In doInBackground 2" );
				
				OutputStream os = sock.getOutputStream();
				ObjectOutputStream dOutStream = new ObjectOutputStream(os);
				dOutStream.writeUTF("CCC");
				dOutStream.writeInt(3);
				
				byte arr[] = new byte[3];
				for(byte b : arr){
					char r = 'A';
					b = (byte) r;
				}
				dOutStream.write(arr, 0, arr.length);
				dOutStream.close();
/*
				// sendfile
				File myFile = new File (Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) +"0.jpg"); 
				Log.e("Error", "In doInBackground 3" + myFile.getAbsolutePath());
				tvStatus.setText(""+myFile.length() + " :"+ Environment.getExternalStorageDirectory().getPath().toString());
				Log.e("Error", "In doInBackground 4" );
				byte [] mybytearray  = new byte [(int)myFile.length()];
				Log.e("Error", "In doInBackground 5" + myFile.length() );
				FileInputStream fis = new FileInputStream(myFile);
				Log.e("Error", "In doInBackground 6" );
				BufferedInputStream bis = new BufferedInputStream(fis);
				Log.e("Error", "In doInBackground 7" );
				bis.read(mybytearray,0,mybytearray.length);
				Log.e("Error", "In doInBackground 8" );
				OutputStream os = sock.getOutputStream();
				Log.e("Error", "In doInBackground 9" );
				ObjectOutputStream dOutStream = new ObjectOutputStream(os);
				Log.e("Error", "In doInBackground 10" );
				tvStatus.setText("Sending...");
				Log.e("Error", "In doInBackground 11" );
				dOutStream.writeUTF("DDD");
				Log.e("Error", "In doInBackground 12" );
				dOutStream.writeInt((int) myFile.length());
				Log.e("Error", "In doInBackground 13" );
				dOutStream.write(mybytearray,0,mybytearray.length);
				Log.e("Error", "In doInBackground 14" );
				dOutStream.flush();
				Log.e("Error", "In doInBackground 15" );
				InputStream is = sock.getInputStream();
				Log.e("Error", "In doInBackground 16" );
				DataInputStream dis = new DataInputStream(is);
				Log.e("Error", "In doInBackground 17" );
				sock.shutdownOutput();
				Log.e("Error", "In doInBackground 18" );
				tvStatus.setText("Sent");
*/
				//  String ret = dis.readUTF();
				//  System.out.println(ret);
				sock.close();
			} catch (UnknownHostException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			catch (Exception e) {
				// TODO Auto-generated catch block
				Log.e("Error", "In doInBackground 21" + e.toString() );
			}
			return null;
		}

	}
}
