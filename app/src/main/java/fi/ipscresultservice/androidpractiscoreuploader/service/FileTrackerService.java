package fi.ipscresultservice.androidpractiscoreuploader.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.os.ResultReceiver;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import java.util.Timer;
import java.util.TimerTask;

import javax.xml.transform.Result;

import fi.ipscresultservice.androidpractiscoreuploader.Constants;
import fi.ipscresultservice.androidpractiscoreuploader.UploaderAppContext;


/**
 * Created by Jarno on 31.12.2017.
 */

@SuppressWarnings("ConstantConditions")
public class FileTrackerService extends Service {

	private final String TAG = FileTrackerService.class.getSimpleName();


	private Timer checkFileTimer = null;

	@Override
	public IBinder onBind(Intent intent) {
		Log.d(TAG, "Test service bind called");
		return null;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
			Log.i(TAG, "Received Start Foreground Intent ");

			FileService.setLastModifiedToZero();

			if (intent.getAction().equals(Constants.ACTION.STARTFOREGROUND_ACTION)) {
				Log.i(TAG, "Received Start Foreground Intent ");

				initChannels(UploaderAppContext.getAppContext());
				NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(UploaderAppContext.getAppContext(), "default");
				Notification notification = notificationBuilder
						.setOngoing(true).build();
				startForeground(Constants.NOTIFICATION_ID.FOREGROUND_SERVICE,
						notification);

				// cancel if already existed
				if(checkFileTimer != null) {
					checkFileTimer.cancel();
				} else {
					// recreate new
					checkFileTimer = new Timer();
				}
				// schedule task
				checkFileTimer.scheduleAtFixedRate(new FileModifiedTimerTask(), 0, Constants.CHECK_FILE_MODIFIED_INTERVAL);
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
		if (checkFileTimer != null) checkFileTimer.cancel();
	}

	class FileModifiedTimerTask extends TimerTask {
		private final String TAG = FileModifiedTimerTask.class.getSimpleName();
		private int counter = 0;
		@Override
		public void run() {
			Log.d(TAG, "Timer task calling check file changed");
			FileService.checkPractiScoreExportFileModified();
		}
	}


	public void initChannels(Context context) {
		if (Build.VERSION.SDK_INT < 26) {
			return;
		}
		NotificationManager notificationManager =
				(NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		NotificationChannel channel = new NotificationChannel("default",
				"PractiScore Uploader",
				NotificationManager.IMPORTANCE_HIGH);
		channel.setDescription("PractiScore Uploader Service Running");
		notificationManager.createNotificationChannel(channel);
	}

}
