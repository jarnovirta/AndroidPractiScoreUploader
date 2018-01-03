package fi.ipscresultservice.androidpractiscoreuploader;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import fi.ipscresultservice.androidpractiscoreuploader.practiscorefileparser.PractiScoreFileParser;
import fi.ipscresultservice.androidpractiscoreuploader.service.FileService;
import fi.ipscresultservice.androidpractiscoreuploader.service.HttpService;
import fi.ipscresultservice.androidpractiscoreuploader.service.TestService;

public class MainActivity extends AppCompatActivity {

	private int MY_PERMISSIONS_REQUEST_READ_AND_WRITE_SDK;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		if (Build.VERSION.SDK_INT >= 23 &&
				ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED &&
				ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

			ActivityCompat.requestPermissions(this,
					new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE},
					MY_PERMISSIONS_REQUEST_READ_AND_WRITE_SDK);
		}


		final EditText etext3 = (EditText) findViewById(R.id.editText3);
		final Button serverAddressOkButton = findViewById(R.id.serverAddressOkButton);
		final Button toggleServiceButton = findViewById(R.id.toggleService);
		serverAddressOkButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				HttpService.setServerAddress(etext3.getText().toString());
			}
		});
		final MainActivity mainActivity = this;


		toggleServiceButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Log.d("onCreate", "*** STARTING TEST SERVICE ***");
				Intent intent = new Intent(mainActivity, TestService.class);
				startService(intent);
			}
		});


		// String matchScore = PractiScoreFileParser.readMatchScoreData(FileService.getPractiScoreExportFile());

		// Log.d("onCreate", "*** SENDING TEST jSON ***");
		// HttpService.sendMatchScore(matchScore);

	}
}
