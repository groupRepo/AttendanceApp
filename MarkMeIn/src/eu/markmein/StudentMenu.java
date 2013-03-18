package eu.markmein;

import eu.markmein.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class StudentMenu extends Activity implements View.OnClickListener {

	Button btStuViewNotes, btStuCheckAtt, btStuExpAbs, btStuFeedback, btLogout;
	Intent i;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.studnetmenu);
		initialize();
	}

	private void initialize() {
		btStuViewNotes = (Button) findViewById(R.id.btStuViewNotes);
		btStuViewNotes.setOnClickListener(this);
		btStuCheckAtt = (Button) findViewById(R.id.btStuCheckAtt);
		btStuCheckAtt.setOnClickListener(this);
		btStuExpAbs = (Button) findViewById(R.id.btStuExpAbs);
		btStuExpAbs.setOnClickListener(this);
		btStuFeedback = (Button) findViewById(R.id.btStuFeedback);
		btStuFeedback.setOnClickListener(this);
		btLogout = (Button) findViewById(R.id.btLogout);
		btLogout.setOnClickListener(this);		
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.btStuViewNotes:
			i = new Intent("eu.markmein.STUDENTVIEWNOTES");
			startActivity(i);
			break;
		case R.id.btStuCheckAtt:
			i = new Intent("eu.markmein.STUDENTVIEWATT");
			startActivity(i);
			break;
		case R.id.btStuExpAbs:
			i = new Intent("eu.markmein.STUDENTEXPLAINABS");
			startActivity(i);
			break;
		case R.id.btStuFeedback:
			i = new Intent("eu.markmein.STUDENTPOSTFEEDBACK");
			startActivity(i);
			break;
		case R.id.btLogout:
			i = new Intent("eu.markmein.LOGIN");
			startActivity(i);
			break;
		}
	}
}
