package fi.ipscresultservice.androidpractiscoreuploader.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.support.v4.app.NotificationCompat;
import android.view.View;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import fi.ipscresultservice.androidpractiscoreuploader.Constants;
import fi.ipscresultservice.androidpractiscoreuploader.R;
import fi.ipscresultservice.androidpractiscoreuploader.UploaderAppContext;
import fi.ipscresultservice.androidpractiscoreuploader.activity.MainActivity;

/**
 *
 * Created by Jarno on 4.2.2018.
 *
 */

public class NotificationService {
	public static Notification getNotification(String notificationText, Constants.NOTIFICATION_TYPE notificationType) {
		Intent resultIntent = new Intent(UploaderAppContext.getAppContext(), MainActivity.class);
		PendingIntent pendingIntent = PendingIntent.getActivity(UploaderAppContext.getAppContext(),
				0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);
		NotificationCompat.Builder notificationBuilder =
				new NotificationCompat.Builder(UploaderAppContext.getAppContext(),
						Constants.NOTIFICATION_CHANNEL_ID)
						.setContentTitle("PractiScore Uploader").setContentText(notificationText)
						.setSmallIcon(R.mipmap.ic_launcher)
						.setLargeIcon(BitmapFactory.decodeResource(UploaderAppContext
										.getAppContext().getResources(),
								R.mipmap.ic_launcher))
						.setPriority(NotificationCompat.PRIORITY_MAX)
						.setContentIntent(pendingIntent)
						.setOngoing(true);
		if (notificationType == Constants.NOTIFICATION_TYPE.LOUD) {
			notificationBuilder.setDefaults(Notification.DEFAULT_SOUND);
		}
		Notification notification = notificationBuilder.build();

		// Remove smaller icon from bottom right of larger icon
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			int smallIconViewId = UploaderAppContext.getAppContext().getResources().getIdentifier("right_icon", "id", android.R.class.getPackage().getName());

			if (smallIconViewId != 0) {
				if (notification.contentIntent != null)
					notification.contentView.setViewVisibility(smallIconViewId, View.INVISIBLE);

				if (notification.headsUpContentView != null)
					notification.headsUpContentView.setViewVisibility(smallIconViewId, View.INVISIBLE);

				if (notification.bigContentView != null)
					notification.bigContentView.setViewVisibility(smallIconViewId, View.INVISIBLE);
			}
		}
		return notification;
	}
	private static void postNotification(String notificationText, Constants.NOTIFICATION_TYPE notificationType) {

		NotificationManager notificationManager =
				(NotificationManager) UploaderAppContext.getAppContext()
						.getSystemService(Context.NOTIFICATION_SERVICE);
		notificationManager.
				notify(Constants.NOTIFICATION_ID.FOREGROUND_SERVICE, getNotification(notificationText, notificationType));
	}


	public static void sendServiceNotifications(String info) {
		sendServiceNotifications(info, Constants.NOTIFICATION_TYPE.LOUD);
	}

	public static void sendServiceNotifications(String info, Constants.NOTIFICATION_TYPE notificationType) {

		ResultReceiver resultReceiver = ResultReceiverService.getFileTrackerResultReceiver();
		Bundle bundle = new Bundle();
		bundle.putString(Constants.DATA_TRANSMISSION_RESULT_MESSAGE_KEY, info);

		DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
		Calendar cal = Calendar.getInstance();
		String timeString = dateFormat.format(cal.getTime());
		bundle.putString(Constants.RESULT_MESSAGE_TIMESTAMP_KEY, timeString);

		resultReceiver.send(1, bundle);

		NotificationService.postNotification(info, notificationType);
	}

}
