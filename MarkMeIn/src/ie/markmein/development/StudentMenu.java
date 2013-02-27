package ie.markmein.development;

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
			i = new Intent("ie.markmein.development.STUDENTVIEWNOTES");
			startActivity(i);
			break;
		case R.id.btStuCheckAtt:
			i = new Intent("ie.markmein.development.STUDENTCHECKATT");
			startActivity(i);
			break;
		case R.id.btStuExpAbs:
			i = new Intent("ie.markmein.development.STUDENTEXPABS");
			startActivity(i);
			break;
		case R.id.btStuFeedback:
			i = new Intent("ie.markmein.development.STUDENTFEEDBACK");
			startActivity(i);
			break;
		case R.id.btLogout:
			i = new Intent("ie.markmein.development.LOGIN");
			startActivity(i);
			break;
		}
	}
}
