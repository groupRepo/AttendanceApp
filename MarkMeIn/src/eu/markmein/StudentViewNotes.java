package eu.markmein;

import ie.markmein.development.R;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

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
