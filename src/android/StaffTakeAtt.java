package ie.markmein.development;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
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

			Handler handler = new Handler(Looper.getMainLooper());
			handler.post(new Runnable(){

				@Override
				public void run() {

					try {

						Socket sock = new Socket("192.168.1.213", 1149); 
						//File myFile = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM + "0.jpg"); 
						//byte [] mybytearray  = new byte [(int)myFile.length()];
						//FileInputStream fis = new FileInputStream(myFile);
						//BufferedInputStream bis = new BufferedInputStream(fis);
						//bis.read(mybytearray,0,mybytearray.length);

						OutputStream os = sock.getOutputStream();
						Log.e("Error", "In doInBackground 3" );
						DataOutputStream dOutStream = new DataOutputStream(os);
						//dOutStream.writeInt((int) myFile.length());
						Log.e("Error", "In doInBackground 4" );
						dOutStream.writeUTF("DUPA");
						//dOutStream.write(mybytearray,0,mybytearray.length);
						//dOutStream.flush();
						//InputStream is = sock.getInputStream();
						//DataInputStream dis = new DataInputStream(is);
						//sock.shutdownOutput();
						//String ret = dis.readUTF();
						Log.e("Error", "In doInBackground 5" );
						tvStatus.setText("Processing for sending 3");
						sock.close();


						/*tvStatus.setText("Processing for sending 2");
					client = new Socket("localhost", 9999);
					tvStatus.setText("Processing for sending 3");
					//ByteArrayOutputStream baos = new ByteArrayOutputStream();
					tvStatus.setText("Processing for sending 4");
					//bmp.compress(CompressFormat.JPEG, 100, baos);
					tvStatus.setText("Processing for sending 5");
					//byte[] imageByteArray = baos.toByteArray();
					tvStatus.setText("Processing for sending 6");*/

						/*fileInputStream = new FileInputStream(file);
					bufferedInputStream = new BufferedInputStream(fileInputStream);
					bufferedInputStream.read(imageByteArray, 0, imageByteArray.length);*/

						/*outputStream = client.getOutputStream();
					tvStatus.setText("Processing for sending 7");
					outputStream.write(imageByteArray, 0, imageByteArray.length);
					tvStatus.setText("Processing for sending 8");
					outputStream.flush();
					tvStatus.setText("Processing for sending 9");
					bufferedInputStream.close();
					tvStatus.setText("Processing for sending 10");
					outputStream.close();
					tvStatus.setText("Processing for sending 11");
					client.shutdownOutput();
					tvStatus.setText("Processing for sending 12");*/
					}catch(Exception e){
						tvStatus.setText("Error sending 1" + e.toString());

						Log.e("Error", "File sending failed " + e.toString());
					}
				}});
			//sendImageToServer = new SendImageToServer();
			//sendImageToServer.execute("send");
			break;
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

				// sock;
				Log.e("Error", "In doInBackground 2" );
				try {

					Socket sock = new Socket("192.168.1.213", 1149); 
					//File myFile = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM + "0.jpg"); 
					//byte [] mybytearray  = new byte [(int)myFile.length()];
					//FileInputStream fis = new FileInputStream(myFile);
					//BufferedInputStream bis = new BufferedInputStream(fis);
					//bis.read(mybytearray,0,mybytearray.length);

					OutputStream os = sock.getOutputStream();
					Log.e("Error", "In doInBackground 3" );
					DataOutputStream dOutStream = new DataOutputStream(os);
					//dOutStream.writeInt((int) myFile.length());
					Log.e("Error", "In doInBackground 4" );
					dOutStream.writeUTF("DUPA");
					//dOutStream.write(mybytearray,0,mybytearray.length);
					//dOutStream.flush();
					//InputStream is = sock.getInputStream();
					//DataInputStream dis = new DataInputStream(is);
					//sock.shutdownOutput();
					//String ret = dis.readUTF();
					Log.e("Error", "In doInBackground 5" );
					tvStatus.setText("Processing for sending 3");
					sock.close();


					/*tvStatus.setText("Processing for sending 2");
				client = new Socket("localhost", 9999);
				tvStatus.setText("Processing for sending 3");
				//ByteArrayOutputStream baos = new ByteArrayOutputStream();
				tvStatus.setText("Processing for sending 4");
				//bmp.compress(CompressFormat.JPEG, 100, baos);
				tvStatus.setText("Processing for sending 5");
				//byte[] imageByteArray = baos.toByteArray();
				tvStatus.setText("Processing for sending 6");*/

					/*fileInputStream = new FileInputStream(file);
				bufferedInputStream = new BufferedInputStream(fileInputStream);
				bufferedInputStream.read(imageByteArray, 0, imageByteArray.length);*/

					/*outputStream = client.getOutputStream();
				tvStatus.setText("Processing for sending 7");
				outputStream.write(imageByteArray, 0, imageByteArray.length);
				tvStatus.setText("Processing for sending 8");
				outputStream.flush();
				tvStatus.setText("Processing for sending 9");
				bufferedInputStream.close();
				tvStatus.setText("Processing for sending 10");
				outputStream.close();
				tvStatus.setText("Processing for sending 11");
				client.shutdownOutput();
				tvStatus.setText("Processing for sending 12");*/
				}catch(Exception e){
					tvStatus.setText("Error sending 1" + e.toString());

					Log.e("Error", "File sending failed " + e.toString());
				}
				return null;
			}

		}
	}
