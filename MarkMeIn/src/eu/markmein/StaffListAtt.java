package eu.markmein;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class StaffListAtt extends Activity implements View.OnClickListener {

	TextView tvAttendees;
	Button confirm, retake;
	Intent i;
	ArrayList<String> list;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.stafflistatt);
		initialize();
	}
	
	private void initialize() {
		tvAttendees = (TextView) findViewById(R.id.tvListOfAttendees);
		confirm = (Button) findViewById(R.id.btConfirm);
		retake = (Button) findViewById(R.id.btRetake);
		list = getIntent().getExtras().getStringArrayList("list");
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
			
			break;
		case R.id.btRetake:
			
			break;
		}
	}

}
