package eu.markmein;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class StaffViewAtt extends Activity implements View.OnClickListener {

	Button btModule, btLecture, btLab, btStudent;
	Intent i;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.staffviewatt);
		initialize();
	}

	private void initialize() {
		btModule = (Button) findViewById(R.id.btModuleRecords);
		btModule.setOnClickListener(this);
		btLecture = (Button) findViewById(R.id.btLectureRecords);
		btLecture.setOnClickListener(this);
		btLab = (Button) findViewById(R.id.btLabRecords);
		btLab.setOnClickListener(this);
		btStudent = (Button) findViewById(R.id.btStudentRecords);
		btStudent.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.btModuleRecords:
			i = new Intent("eu.markmein.");
			startActivity(i);
			break;
		case R.id.btLectureRecords:
			i = new Intent("eu.markmein.");
			startActivity(i);
			break;
		case R.id.btLabRecords:
			i = new Intent("eu.markmein.");
			startActivity(i);
			break;
		case R.id.btStudentRecords:
			i = new Intent("eu.markmein.");
			startActivity(i);
			break;
		}
	}

}
