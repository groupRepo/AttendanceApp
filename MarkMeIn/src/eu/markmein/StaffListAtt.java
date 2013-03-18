package eu.markmein;

import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class StaffListAtt extends Activity implements View.OnClickListener {

	TextView tvAttendees;
	Button confirm, retake;
	Intent i;
	
	SendListToServer slts;
	
	//data sent to the server
	
	ArrayList<String> list = null;
	String moduleOfferingId, type;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.stafflistatt);
		initialize();
	}
	
	private void initialize() {
		type = "S";
		tvAttendees = (TextView) findViewById(R.id.tvListOfAttendees);
		confirm = (Button) findViewById(R.id.btConfirm);
		confirm.setOnClickListener(this);
		retake = (Button) findViewById(R.id.btRetake);
		retake.setOnClickListener(this);
		list = getIntent().getExtras().getStringArrayList("list");
		moduleOfferingId = list.get(0);
		list.remove(0);
		tvAttendees.setText("");
		tvAttendees.append("No. of Attendees: " + list.size() + "\n\n");
		for(int i = 0; i < list.size(); i++){
			tvAttendees.append(list.get(i));
		}
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.btConfirm:
			slts = new SendListToServer();
			slts.execute("text");
			break;
		case R.id.btRetake:
			i = new Intent("eu.markmein.STAFFTAKEATT");
			startActivity(i);
			break;
		}
	}
	
	public class SendListToServer extends AsyncTask<String, Void, String>{

		protected String doInBackground(String... params) {
			Socket sock;
			try {
				sock = new Socket("www.markmein.eu", 2222); 
				OutputStream os = sock.getOutputStream();
				ObjectOutputStream oos = new ObjectOutputStream(os);
				oos.writeUTF(type);
				oos.writeUTF(moduleOfferingId);
				oos.writeObject(list);
				sock.close();
			} catch (Exception e) {
				Log.e("Error", "In doInBackground Exception" + e.toString() );
			}
			return null;
		}
	}
}
