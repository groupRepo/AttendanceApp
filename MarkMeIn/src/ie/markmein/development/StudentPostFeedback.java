package ie.markmein.development;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

public class StudentPostFeedback extends Activity implements View.OnClickListener{

	ArrayAdapter<String> adapter;
	Spinner spAtTag, spHashTag;
	Button btSubmitTweet, btClearTweet, btMainMenu;
//test
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.studentpostfeedback);
		initialize();
	}

	private void initialize() {
		adapter = new ArrayAdapter<String>(StudentPostFeedback.this, android.R.layout.simple_spinner_item );
		spAtTag = (Spinner) findViewById(R.id.spAtTag);
		spAtTag.setOnItemClickListener((OnItemClickListener) this);
		
		btSubmitTweet = (Button) findViewById(R.id.btSubmitTweet);
		btSubmitTweet.setOnClickListener(this);
		btClearTweet = (Button) findViewById(R.id.btClearTweet);
		btClearTweet.setOnClickListener(this);
		btMainMenu = (Button) findViewById(R.id.btMainMenu);
		btMainMenu.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {

	}
}
