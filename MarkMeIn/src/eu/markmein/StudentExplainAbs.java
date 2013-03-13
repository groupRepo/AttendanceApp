package eu.markmein;

import ie.markmein.development.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

public class StudentExplainAbs extends Activity implements View.OnClickListener, OnItemSelectedListener {
	Spinner spModule;
	EditText etReason;
	Button btSubmit, btClear, btMainMenu;
	ArrayAdapter<String> adapter;
	String[] modules = {"Network Security", "System Programming", "Advanced Object Programming"	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.studentexplainabs);
		initialize();
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.btStuSubmitAbsence:
			break;
		case R.id.btStuClearAbsence:
			break;
		case R.id.btMainMenu:
			break;
		}
	}

	private void initialize() {
		adapter = new ArrayAdapter<String>(StudentExplainAbs.this, android.R.layout.simple_spinner_item, modules);

		spModule = (Spinner) findViewById(R.id.spModule);
		spModule.setAdapter(adapter);
		spModule.setOnItemSelectedListener((OnItemSelectedListener) this);

		etReason = (EditText) findViewById(R.id.etReason);
		btSubmit = (Button) findViewById(R.id.btStuSubmitAbsence);
		btSubmit.setOnClickListener(this);
		btClear = (Button) findViewById(R.id.btStuClearAbsence);
		btClear.setOnClickListener(this);
		btMainMenu = (Button) findViewById(R.id.btMainMenu);
		btMainMenu.setOnClickListener(this);
	}

	@Override
	public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		int position = spModule.getSelectedItemPosition();
		adapter.getItem(position).toString();

	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		showAlert();
	}

	
	public void showAlert(){
		StudentExplainAbs.this.runOnUiThread(new Runnable() {
			public void run() {
				AlertDialog.Builder builder = new AlertDialog.Builder(StudentExplainAbs.this);
				builder.setTitle("Error.");
				builder.setMessage("Fields must be selected.") 
				.setCancelable(false)
				.setPositiveButton("OK", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						//intent = new Intent("ie.markmein.development.LOGIN");
						//startActivity(intent);
					}
				});                    
				AlertDialog alert = builder.create();
				alert.show();              
			}
		});
	}
}



