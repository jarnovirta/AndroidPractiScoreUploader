package fi.ipscresultservice.androidpractiscoreuploader.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import fi.ipscresultservice.androidpractiscoreuploader.Constants;
import fi.ipscresultservice.androidpractiscoreuploader.R;
import fi.ipscresultservice.androidpractiscoreuploader.UploaderAppContext;
import fi.ipscresultservice.androidpractiscoreuploader.service.HttpService;
import fi.ipscresultservice.androidpractiscoreuploader.service.UserService;

/**
 * Created by Jarno on 26.1.2018.
 *
 */

public class EditConnection extends AppCompatActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.edit_connection);
		Button okButton = findViewById(R.id.url_ok_button);
		Button cancelButton = findViewById(R.id.url_cancel_button);
		final EditText usernameEditText = findViewById(R.id.username_edit_text);
		final EditText passwordEditText = findViewById(R.id.password_edit_text);
		final EditText serverAddressEditText = findViewById(R.id.url_edit_text);
		if (HttpService.getServerUrl() != null) serverAddressEditText.setText(HttpService.getServerUrl());
		if (UserService.getUsername() != null) usernameEditText.setText(UserService.getUsername());
		if (UserService.getPassword() != null) passwordEditText.setText(UserService.getPassword());

		okButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				if (usernameEditText.getText() != null) UserService.setUsername(usernameEditText.getText().toString());
				if (passwordEditText.getText() != null) UserService.setPassword(passwordEditText.getText().toString());
				String url = serverAddressEditText.getText().toString();

				if (!Patterns.WEB_URL.matcher(url).matches()) {
					Toast.makeText(UploaderAppContext.getAppContext(), "Invalid address",
							Toast.LENGTH_SHORT).show();
					return;
				}
				String testUrl = url.toLowerCase();
				if (testUrl.length() >= 8 && testUrl.substring(0, 8).equals("https://")) {
					Toast.makeText(UploaderAppContext.getAppContext(), "No HTTPS available",
							Toast.LENGTH_SHORT).show();
					return;
				}
				if (testUrl.length() < 7) {
					url = "http://" + url;
				}

				else if (!testUrl.substring(0, 7).equals("http://")) {
					url = "http://" + url;
				}

				Intent output = new Intent();
				output.putExtra(Constants.EXTRAS_SERVER_ADDRESS,
						url);
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
