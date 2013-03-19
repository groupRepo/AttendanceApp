package eu.markmein;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

public class StudentViewNotes extends Activity implements View.OnClickListener{
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.studentviewnotes);
		initialize();
	}
	
	private void initialize() {
		
	}

	@Override
	public void onClick(View v) {
		
	}
}
