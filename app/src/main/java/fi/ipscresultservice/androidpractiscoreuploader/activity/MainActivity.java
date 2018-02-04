package fi.ipscresultservice.androidpractiscoreuploader.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;
import android.os.ResultReceiver;
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
import android.widget.Toast;
import android.widget.ToggleButton;

import fi.ipscresultservice.androidpractiscoreuploader.Constants;
import fi.ipscresultservice.androidpractiscoreuploader.R;
import fi.ipscresultservice.androidpractiscoreuploader.service.FileService;
import fi.ipscresultservice.androidpractiscoreuploader.service.HttpService;
import fi.ipscresultservice.androidpractiscoreuploader.service.FileTrackerService;
import fi.ipscresultservice.androidpractiscoreuploader.service.ResultReceiverService;

public class MainActivity extends AppCompatActivity {

	private final int PERMISSIONS_REQUEST_READ_AND_WRITE_SDK = 1;
	private final String TAG = MainActivity.class.getSimpleName();

	private TextView serverAddressTextView;
	private TextView matchNameTextView;

	private TextView dataSentTimeTextView;
	private TextView statusTextView;
	private RelativeLayout infoViewGroup;
	private ToggleButton toggleUploadServiceButton;
	private Button exitButton;
	private Button testConnectionButton;
	private Button editServerAddressButton;
	private Button selectFileButton;

	private boolean buttonsEnabled = true;

	private final int EDIT_SERVER_ADDRESS_REQUEST_CODE = 1;
	private final int CHOOSE_FILE_REQUEST_CODE = 2;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		getPermissions();

		serverAddressTextView = findViewById(R.id.server_address);
		matchNameTextView = findViewById(R.id.match_name);

		dataSentTimeTextView = findViewById(R.id.last_transmission_time);
		statusTextView = findViewById(R.id.last_transmission_status);

		infoViewGroup = findViewById(R.id.info_view_group);
		infoViewGroup.setVisibility(View.INVISIBLE);

		toggleUploadServiceButton = findViewById(R.id.toggle_upload_service_button);
		exitButton = findViewById(R.id.exit_button);
		setButtonClickListeners();
		setButtonsEnabled();
		loadAppData();
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
				FileService.setPractiScoreExportFilePath(data.getData());
				matchNameTextView.setText(FileService.getPractiScoreExportFileMatchname());
			}
		}
		setButtonsEnabled();
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
					finish();
				}
			}
		}
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		outState.putBoolean(Constants.MAIN_ACTIVITY_BUTTONS_STATE, buttonsEnabled);
		outState.putInt(Constants.INFO_VIEW_GROUP_VISIBILITY, infoViewGroup.getVisibility());
		outState.putString(Constants.DATA_SENT_TIME_TEXT_VIEW_STATE, dataSentTimeTextView.getText().toString());
		outState.putString(Constants.STATUS_TEXT_VIEW_STATE, statusTextView.getText().toString());
		super.onSaveInstanceState(outState);
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		matchNameTextView.setText(FileService.getPractiScoreExportFileMatchname());
		serverAddressTextView.setText(HttpService.getServerUrl());
		buttonsEnabled = savedInstanceState.getBoolean(Constants.MAIN_ACTIVITY_BUTTONS_STATE);
		setButtonsEnabled();
		infoViewGroup.setVisibility(savedInstanceState.getInt(Constants.INFO_VIEW_GROUP_VISIBILITY));
		dataSentTimeTextView.setText(savedInstanceState.getString(Constants.DATA_SENT_TIME_TEXT_VIEW_STATE));
		statusTextView.setText(savedInstanceState.getString(Constants.STATUS_TEXT_VIEW_STATE));
		super.onRestoreInstanceState(savedInstanceState);

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
		editServerAddressButton = findViewById(R.id.edit_server_address_button);
		selectFileButton = findViewById(R.id.select_file_button);
		testConnectionButton = findViewById((R.id.test_connection));
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
		testConnectionButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				HttpService.testConnection();

			}
		});

		toggleUploadServiceButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				buttonsEnabled = true;

				if (toggleUploadServiceButton.isChecked()) {
					buttonsEnabled = false;
					startFileTrackerService();
				} else {
					stopFileTrackerService();
				}
				setButtonsEnabled();

			}
		});
		exitButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				mainActivity.finish();
			}
		});
	}

	private void getPermissions() {
		if (Build.VERSION.SDK_INT >= 23 &&
				ContextCompat.checkSelfPermission(this,
						Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED &&
				ContextCompat.checkSelfPermission(this,
						Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

			ActivityCompat.requestPermissions(this,
					new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
							Manifest.permission.READ_EXTERNAL_STORAGE},
					PERMISSIONS_REQUEST_READ_AND_WRITE_SDK);
		}
	}

	private void startFileTrackerService() {
		Intent startIntent = new Intent(MainActivity.this, FileTrackerService.class);
		startIntent.setAction(Constants.ACTION.STARTFOREGROUND_ACTION);
		ResultReceiverService.setFileTrackerResultReceiver(new FileTrackerResultReceiver(null));
		startService(startIntent);

	}

	private void stopFileTrackerService() {
		Intent stopIntent = new Intent(MainActivity.this, FileTrackerService.class);
		stopIntent.setAction(Constants.ACTION.STOPFOREGROUND_ACTION);
		startService(stopIntent);
	}

	private class FileTrackerResultReceiver extends ResultReceiver {

		public FileTrackerResultReceiver(Handler handler) {
			super(handler);
		}

		@Override
		protected void onReceiveResult(int resultCode, final Bundle resultData) {
			super.onReceiveResult(resultCode, resultData);
			if (resultCode == Constants.DATA_TRAMSMISSION_RESULT_KEY) {
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						String resultMessage = resultData.getString(Constants.DATA_TRANSMISSION_RESULT_MESSAGE_KEY);
						String dataSentTime = resultData.getString(Constants.DATA_TRANSMISSION_TIME_KEY);
						infoViewGroup.setVisibility(View.VISIBLE);

						if (dataSentTime != null && dataSentTime.length() > 0)
							dataSentTimeTextView.setText(dataSentTime);
						if (resultMessage != null && resultMessage.length() > 0)
							statusTextView.setText(resultMessage);
					}
				});
			}
		}
	}

	private void setButtonsEnabled() {
		boolean toggleUploadServiceButtonEnabled;
		if (FileService.isPractiScoreExportFileUriSet() && HttpService.getServerUrl() != null) {
			toggleUploadServiceButtonEnabled = true;

		} else toggleUploadServiceButtonEnabled = false;
		toggleUploadServiceButton.setEnabled(toggleUploadServiceButtonEnabled);
		exitButton.setEnabled(buttonsEnabled);
		testConnectionButton.setEnabled(buttonsEnabled);
		editServerAddressButton.setEnabled(buttonsEnabled);
		selectFileButton.setEnabled(buttonsEnabled);

	}
}

