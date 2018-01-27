package fi.ipscresultservice.androidpractiscoreuploader.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.ResultReceiver;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

import fi.ipscresultservice.androidpractiscoreuploader.Constants;
import fi.ipscresultservice.androidpractiscoreuploader.MainActivity;
import fi.ipscresultservice.androidpractiscoreuploader.R;
import fi.ipscresultservice.androidpractiscoreuploader.UploaderApp;


/**
 * Created by Jarno on 31.12.2017.
 */

public class TestService extends Service {

	private final String TAG = TestService.class.getSimpleName();

	private ResultReceiver resultReceiver;
	// constant
	public static final long CHECK_FILE_MODIFIED_INTERVAL = 2 * 1000;

	// timer handling
	private Timer mTimer = null;

	@Override
	public IBinder onBind(Intent intent) {
		Log.d(TAG, "Test service bind called");
		return null;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
			Log.i(TAG, "Received Start Foreground Intent ");
			resultReceiver = intent.getParcelableExtra(Constants.EXTRAS_RESULT_RECEIVER_KEY);

//			Bundle bundle = new Bundle();
//			bundle.putString("result", "Test result");
//			resultReceiver.send(1, bundle);
//			bundle = new Bundle();
//			bundle.putString("result", "Second test result");
//			resultReceiver.send(1, bundle);

			if (intent.getAction().equals(Constants.ACTION.STARTFOREGROUND_ACTION)) {
				Log.i(TAG, "Received Start Foreground Intent ");

				initChannels(UploaderApp.getAppContext());
				RemoteViews notificationView = new RemoteViews(this.getPackageName(),R.layout.notification);
				NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(UploaderApp.getAppContext(), "default");
				Notification notification = notificationBuilder
						.setContentTitle("nkDroid Music Player")
						.setTicker("nkDroid Music Player")
						.setContentText("nkDroid Music")
						.setContent(notificationView)
						.setOngoing(true).build();
				startForeground(Constants.NOTIFICATION_ID.FOREGROUND_SERVICE,
						notification);

				// cancel if already existed
				if(mTimer != null) {
					mTimer.cancel();
				} else {
					// recreate new
					mTimer = new Timer();
				}
				// schedule task
				mTimer.scheduleAtFixedRate(new TimeDisplayTimerTask(), 0, CHECK_FILE_MODIFIED_INTERVAL);
			}

			else if (intent.getAction().equals(Constants.ACTION.STOPFOREGROUND_ACTION)) {
				Log.i(TAG, "Received Stop Foreground Intent");
				stopForeground(true);
				stopSelf();
			}
			return START_STICKY;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		Log.d(TAG, "Test service destroy called. Cancelling timer." + Thread.currentThread().getName());
		if (mTimer != null) mTimer.cancel();
	}

	class TimeDisplayTimerTask extends TimerTask {
		private final String TAG = TimeDisplayTimerTask.class.getSimpleName();
		private int counter = 0;
		@Override
		public void run() {
			Log.d(TAG, "Timer task " + counter++ + " Thread: " + Thread.currentThread().getName());
		}
	}

	public void initChannels(Context context) {
		if (Build.VERSION.SDK_INT < 26) {
			return;
		}
		NotificationManager notificationManager =
				(NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		NotificationChannel channel = new NotificationChannel("default",
				"Channel name",
				NotificationManager.IMPORTANCE_DEFAULT);
		channel.setDescription("Channel description");
		notificationManager.createNotificationChannel(channel);
	}
}
