package fi.ipscresultservice.androidpractiscoreuploader.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
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
import android.widget.ToggleButton;

import fi.ipscresultservice.androidpractiscoreuploader.Constants;
import fi.ipscresultservice.androidpractiscoreuploader.R;
import fi.ipscresultservice.androidpractiscoreuploader.service.AppDataService;
import fi.ipscresultservice.androidpractiscoreuploader.service.FileService;
import fi.ipscresultservice.androidpractiscoreuploader.service.FileTrackerResultReceiver;
import fi.ipscresultservice.androidpractiscoreuploader.service.HttpService;
import fi.ipscresultservice.androidpractiscoreuploader.service.FileTrackerService;
import fi.ipscresultservice.androidpractiscoreuploader.service.NotificationService;
import fi.ipscresultservice.androidpractiscoreuploader.service.PermissionsService;
import fi.ipscresultservice.androidpractiscoreuploader.service.ResultReceiverService;
import fi.ipscresultservice.androidpractiscoreuploader.service.UserService;

public class MainActivity extends AppCompatActivity {
	private final String TAG = MainActivity.class.getSimpleName();

	private Intent trackerServiceIntent;
	private TextView serverAddressTextView;
	private TextView usernameTextView;
	private TextView passwordTextView;

	private TextView matchNameTextView;

	private TextView dataSentTimeTextView;
	private TextView statusTextView;
	private RelativeLayout infoViewGroup;
	private ToggleButton toggleUploadServiceButton;
	private Button exitButton;
	private Button testConnectionButton;
	private Button editConnectionButton;
	private Button selectFileButton;
	private Button sendDataButton;

	private boolean buttonsEnabled = true;

	private final int EDIT_CONNECTION_REQUEST_CODE = 1;
	private final int CHOOSE_FILE_REQUEST_CODE = 2;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		PermissionsService.getPermissions(this);

		serverAddressTextView = findViewById(R.id.server_address);
		usernameTextView = findViewById(R.id.current_username);
		passwordTextView = findViewById(R.id.current_password);

		matchNameTextView = findViewById(R.id.match_name);

		dataSentTimeTextView = findViewById(R.id.last_transmission_time);
		statusTextView = findViewById(R.id.last_transmission_status);

		infoViewGroup = findViewById(R.id.info_view_group);
		infoViewGroup.setVisibility(View.INVISIBLE);

		toggleUploadServiceButton = findViewById(R.id.toggle_upload_service_button);

		setButtonClickListeners();

		AppDataService.loadAppData(this);
		setMatchAndConnectionTextViews();

		ResultReceiverService.setFileTrackerResultReceiver(new FileTrackerResultReceiver(null, this));

		setButtonsEnabled();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == EDIT_CONNECTION_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
			String serverAddress = data.getStringExtra(Constants.EXTRAS_SERVER_ADDRESS);

			HttpService.setServerUrl(serverAddress);

			AppDataService.saveAppData(this);
			serverAddressTextView.setText(data.getStringExtra(Constants.EXTRAS_SERVER_ADDRESS));
			if (UserService.getUsername() != null) usernameTextView.setText(UserService.getUsername());
			if (UserService.getPassword() != null) passwordTextView.setText(UserService.getPassword());
		}
		if (requestCode == CHOOSE_FILE_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
			if (data.getData() != null) {
				FileService.setPractiScoreExportFilePath(data.getData());
				AppDataService.saveAppData(this);
				setMatchAndConnectionTextViews();
			}
		}
		setButtonsEnabled();
	}

	@Override
	public void onRequestPermissionsResult(int requestCode,
	                                       @NonNull String permissions[], @NonNull int[] grantResults) {
		switch (requestCode) {
			case PermissionsService.PERMISSIONS_REQUEST_READ_AND_WRITE_SDK: {
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

	public void setMatchAndConnectionTextViews() {
		if (HttpService.getServerUrl() != null) serverAddressTextView.setText(HttpService.getServerUrl());
		else serverAddressTextView.setText("");
		if (UserService.getUsername() != null) usernameTextView.setText(UserService.getUsername());
		else usernameTextView.setText("");
		if (UserService.getPassword() != null) passwordTextView.setText(UserService.getPassword());
		else passwordTextView.setText("");
		if (FileService.getPractiScoreExportFileMatchname() != null) {
			matchNameTextView.setText(FileService.getPractiScoreExportFileMatchname());
		}
		else matchNameTextView.setText("");
	}

	public void setDataSentTimeTextViewText(String dataSentTime) {
		dataSentTimeTextView.setText(dataSentTime);
	}
	public void setStatusTextView(String statusText) {
		statusTextView.setText(statusText);
	}

	public void setInfoViewGroupVisibility(int visible) {
		infoViewGroup.setVisibility(visible);
	}

	private void setButtonClickListeners() {
		editConnectionButton = findViewById(R.id.edit_server_address_button);
		selectFileButton = findViewById(R.id.select_file_button);
		testConnectionButton = findViewById((R.id.test_connection));
		exitButton = findViewById(R.id.exit_button);
		sendDataButton = findViewById(R.id.send_data_button);

		final MainActivity mainActivity = this;

		editConnectionButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent i = new Intent(mainActivity, EditConnection.class);
				startActivityForResult(i, EDIT_CONNECTION_REQUEST_CODE);
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
		sendDataButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				boolean forceSend = true;
				FileService.checkPractiScoreExportFileModified(forceSend);
			}
		});

		exitButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				NotificationService.clearNotification();
				mainActivity.finish();
			}
		});
	}


	private void startFileTrackerService() {
		trackerServiceIntent = new Intent(MainActivity.this, FileTrackerService.class);
		trackerServiceIntent.setAction(Constants.ACTION.STARTFOREGROUND_ACTION);

		startService(trackerServiceIntent);

	}

	private void stopFileTrackerService() {
		Intent stopIntent = new Intent(MainActivity.this, FileTrackerService.class);
		stopIntent.setAction(Constants.ACTION.STOPFOREGROUND_ACTION);
		startService(stopIntent);
	}

	private void setButtonsEnabled() {
		boolean sendDataToServerButtonsEnabled;
		if (FileService.isPractiScoreExportFileUriSet() && HttpService.getServerUrl() != null) {
			sendDataToServerButtonsEnabled = true;
		} else {
			sendDataToServerButtonsEnabled = false;
		}
		toggleUploadServiceButton.setEnabled(sendDataToServerButtonsEnabled);
		sendDataButton.setEnabled(sendDataToServerButtonsEnabled == true && buttonsEnabled == true);
		exitButton.setEnabled(buttonsEnabled);
		testConnectionButton.setEnabled(buttonsEnabled);
		editConnectionButton.setEnabled(buttonsEnabled);
		selectFileButton.setEnabled(buttonsEnabled);

	}

}

