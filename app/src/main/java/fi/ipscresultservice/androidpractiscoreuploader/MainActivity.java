package fi.ipscresultservice.androidpractiscoreuploader;

import android.Manifest;
import android.app.Notification;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;
import android.os.ResultReceiver;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import fi.ipscresultservice.androidpractiscoreuploader.service.FileService;
import fi.ipscresultservice.androidpractiscoreuploader.service.HttpService;
import fi.ipscresultservice.androidpractiscoreuploader.service.FileChangeTrackerService;

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
	private Button exitButton;

	private final int EDIT_SERVER_ADDRESS_REQUEST_CODE = 1;
	private final int CHOOSE_FILE_REQUEST_CODE = 2;

	private final int EXIT_CODE_OK = 0;
	private final int EXIT_CODE_PERMISSIONS_NOT_GRANTED = 1;

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
		exitButton = findViewById(R.id.exit_button);
		setButtonClickListeners();
		setToggleUploadServiceButtonEnabled();
		loadAppData();
	}

	private void setToggleUploadServiceButtonEnabled() {

		// TODO: REMOVE COMMENT OUT
//		if (FileService.isPractiScoreExportFileUriSet() && HttpService.getServerUrl() != null) {
//			toggleUploadServiceButton.setEnabled(true);
//		}
//		else toggleUploadServiceButton.setEnabled(false);
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
					finish();
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
				boolean buttonsEnabled = true;
				if (toggleUploadServiceButton.isChecked()) {
					buttonsEnabled = false;
					Log.d(TAG, "MainActivity in main thread. Thread: " + Thread.currentThread().getName());
//					startFileTrackerService();
					Intent startIntent = new Intent(MainActivity.this, FileChangeTrackerService.class);
					startIntent.setAction(Constants.ACTION.STARTFOREGROUND_ACTION);
					startService(startIntent);
				}
				else {
					Intent stopIntent = new Intent(MainActivity.this, FileChangeTrackerService.class);
					stopIntent.setAction(Constants.ACTION.STOPFOREGROUND_ACTION);
					startService(stopIntent);
				}
				editServerAddressButton.setEnabled(buttonsEnabled);
				selectFileButton.setEnabled(buttonsEnabled);
				exitButton.setEnabled(buttonsEnabled);

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
				ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED &&
				ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

			ActivityCompat.requestPermissions(this,
					new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE},
					PERMISSIONS_REQUEST_READ_AND_WRITE_SDK);
		}
	}

	private void startFileTrackerService() {
		Log.d(TAG, "Starting file tracker service");
		ResultReceiver fileTrackerResultReceiver = new FileTrackerResultReceiver(null);
		Intent intent = new Intent(this, FileChangeTrackerService.class);
		intent.putExtra(Constants.EXTRAS_RESULT_RECEIVER_KEY, fileTrackerResultReceiver);
		startService(intent);

	}

	private void stopFileTrackerService() {
		Log.d(TAG, "Stopping file tracker service");
		Intent intent = new Intent(this, FileChangeTrackerService.class);
		stopService(intent);
	}
	private class FileTrackerResultReceiver extends ResultReceiver {

		public FileTrackerResultReceiver(Handler handler) {
			super(handler);
		}

		@Override
		protected void onReceiveResult(int resultCode, Bundle resultData) {
			super.onReceiveResult(resultCode, resultData);
			Log.d(TAG, "Received " + resultData.getString("result"));
		}
	}
	private Notification buildForegroundNotification() {
		NotificationCompat.Builder b = new NotificationCompat.Builder(this);
		b.setOngoing(true)
				.setContentTitle("Uploader")
				.setContentText("Content text")
				.setSmallIcon(android.R.drawable.stat_sys_download)
				.setTicker("Ticker");

		return(b.build());
	}
}
