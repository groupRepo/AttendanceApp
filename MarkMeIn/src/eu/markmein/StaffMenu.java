package eu.markmein;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class StaffMenu extends Activity implements View.OnClickListener {

	Button btTakePic, btViewAtt, btViewAbs, btViewFeed, btLogout;
	final static int cameraData = 0;
	Intent i;
	String ID;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.staffmenu);
		initialize();
	}

	private void initialize() {
		btTakePic = (Button) findViewById(R.id.btTakeAtt);
		btTakePic.setOnClickListener(this);
		btViewAtt = (Button) findViewById(R.id.btViewAtt);
		btViewAtt.setOnClickListener(this);
		btViewAbs = (Button) findViewById(R.id.btViewAbs);
		btViewAbs.setOnClickListener(this);
		btViewFeed = (Button) findViewById(R.id.btViewFeedback);
		btViewFeed.setOnClickListener(this);
		btLogout = (Button) findViewById(R.id.btLogout);
		btLogout.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.btTakeAtt:
			i = new Intent("eu.markmein.STAFFTAKEATT");
			startActivity(i);
			break;
		case R.id.btViewAtt:
			i = new Intent("eu.markmein.STAFFVIEWATT");
			startActivity(i);
			break;
		case R.id.btViewAbs:
			i = new Intent("eu.markmein.STAFFVIEWABS");
			startActivity(i);
			break;
		case R.id.btViewFeedback:
			i = new Intent("eu.markmein.STAFFVIEWFEEDBACK");
			startActivity(i);
			break;
		case R.id.btLogout:
			i = new Intent(this, Login.class);
			i.putExtra("finish", true);
			i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(i);
			finish();
			break;
		}
	}
}
