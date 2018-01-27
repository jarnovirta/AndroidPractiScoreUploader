package fi.ipscresultservice.androidpractiscoreuploader;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import org.apache.http.protocol.HTTP;

import fi.ipscresultservice.androidpractiscoreuploader.service.FileService;
import fi.ipscresultservice.androidpractiscoreuploader.service.HttpService;
import fi.ipscresultservice.androidpractiscoreuploader.service.TestService;

public class MainActivity extends AppCompatActivity {

	private final int PERMISSIONS_REQUEST_READ_AND_WRITE_SDK = 1;
	private final String TAG = MainActivity.class.getSimpleName();

	private TextView serverAddressTextView;
	private TextView matchNameTextView;
	private TextView infoView;
	private TextView infoViewLabel;
	private TextView errorView;
	private TextView errorViewLabel;
	private RelativeLayout infoViewGroup;
	private ToggleButton toggleUploadServiceButton;

	private final int EDIT_SERVER_ADDRESS_REQUEST_CODE = 1;
	private final int CHOOSE_FILE_REQUEST_CODE = 2;

	private String errorStatusMessage;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		getPermissions();

		serverAddressTextView = findViewById(R.id.server_address);
		matchNameTextView = findViewById(R.id.match_name);

		infoView = findViewById(R.id.info);
		infoViewLabel = findViewById(R.id.info_label);
		infoView.setVisibility(View.GONE);
		infoViewLabel.setVisibility(View.GONE);

		errorView = findViewById(R.id.error);
		errorViewLabel = findViewById(R.id.error_label);
		errorView.setVisibility(View.GONE);
		errorViewLabel.setVisibility(View.GONE);

		infoViewGroup = findViewById(R.id.info_view_group);
		infoViewGroup.setVisibility(View.INVISIBLE);

		toggleUploadServiceButton = findViewById(R.id.toggle_upload_service_button);

		setButtonClickListeners();
		setToggleUploadServiceButtonEnabled();
		loadAppData();
	}

	private void setToggleUploadServiceButtonEnabled() {
		if (FileService.isPractiScoreExportFileUriSet() && HttpService.getServerUrl() != null) {
			toggleUploadServiceButton.setEnabled(true);
		}
		else toggleUploadServiceButton.setEnabled(false);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == EDIT_SERVER_ADDRESS_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
			String serverAddress = data.getStringExtra(Constants.EXTRAS_SERVER_ADDRESS);
			HttpService.setServerUrl(serverAddress);
			saveServerAddress(serverAddress);
			serverAddressTextView.setText(data.getStringExtra(Constants.EXTRAS_SERVER_ADDRESS));
		}
		if (requestCode == CHOOSE_FILE_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
			if (data.getData() != null) {
				FileService.setPractiScoreExportFileUri(data.getData());
				matchNameTextView.setText(FileService.getPractiScoreExportFileMatchname());
			}
		}
		setToggleUploadServiceButtonEnabled();
	}

	@Override
	public void onRequestPermissionsResult(int requestCode,
	                                       @NonNull String permissions[], @NonNull int[] grantResults) {
		switch (requestCode) {
			case PERMISSIONS_REQUEST_READ_AND_WRITE_SDK: {
				// If request is cancelled, the result arrays are empty.
				if (grantResults.length > 0
						&& grantResults[0] == PackageManager.PERMISSION_GRANTED) {
					Log.d(TAG, "App permissions granted");
				} else {
					Log.i(TAG, "App permissions not granted. Exiting.");
					final int ERROR_CODE_PERMISSIONS_NOT_GRANTED = 1;
					System.exit(ERROR_CODE_PERMISSIONS_NOT_GRANTED);
				}
			}
		}
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		matchNameTextView.setText(FileService.getPractiScoreExportFileMatchname());
		serverAddressTextView.setText(HttpService.getServerUrl());
	}

	private void saveServerAddress(String address) {
		SharedPreferences sharedPref = getSharedPreferences("PSUploaderData", Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedPref.edit();
		editor.putString(Constants.APP_DATA_SERVER_ADDRESS, address);
		editor.commit();
	}

	private void loadAppData() {
		SharedPreferences sharedPref = getSharedPreferences("PSUploaderData", Context.MODE_PRIVATE);
		String address = sharedPref.getString(Constants.APP_DATA_SERVER_ADDRESS, null);
		if (address != null) {
			HttpService.setServerUrl(address);
			serverAddressTextView.setText(address);
		}
	}

	private void setButtonClickListeners() {
		final Button editServerAddressButton = findViewById(R.id.edit_server_address_button);
		final Button selectFileButton = findViewById(R.id.select_file_button);
		final MainActivity mainActivity = this;

		editServerAddressButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent i = new Intent(mainActivity, EnterServerAddressActivity.class);
				startActivityForResult(i, EDIT_SERVER_ADDRESS_REQUEST_CODE);
			}
		});
		selectFileButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
				intent.setType("*/*");
				startActivityForResult(intent, CHOOSE_FILE_REQUEST_CODE);
			}
		});

		toggleUploadServiceButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(mainActivity, TestService.class);
//				startService(intent);
			}
		});
	}

	private void getPermissions() {
		if (Build.VERSION.SDK_INT >= 23 &&
				ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED &&
				ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

			ActivityCompat.requestPermissions(this,
					new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE},
					PERMISSIONS_REQUEST_READ_AND_WRITE_SDK);
		}
	}
}
