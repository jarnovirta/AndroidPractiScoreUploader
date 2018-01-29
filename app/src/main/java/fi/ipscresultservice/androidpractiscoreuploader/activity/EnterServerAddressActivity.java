package fi.ipscresultservice.androidpractiscoreuploader.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import fi.ipscresultservice.androidpractiscoreuploader.Constants;
import fi.ipscresultservice.androidpractiscoreuploader.R;

/**
 * Created by Jarno on 26.1.2018.
 *
 */

public class EnterServerAddressActivity extends AppCompatActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_enter_server_address);

		Button okButton = findViewById(R.id.url_ok_button);
		Button cancelButton = findViewById(R.id.url_cancel_button);
		final EditText serverAddressEditText = findViewById(R.id.url_edit_text);

		okButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent output = new Intent();
				output.putExtra(Constants.EXTRAS_SERVER_ADDRESS,
						serverAddressEditText.getText().toString());
				setResult(RESULT_OK, output);
				finish();
			}
		});
		cancelButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				setResult(RESULT_CANCELED);
				finish();
			}
		});
	}

}
