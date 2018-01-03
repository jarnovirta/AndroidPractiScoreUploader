package fi.ipscresultservice.androidpractiscoreuploader.service;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Jarno on 31.12.2017.
 */

// TODO: Make this a foreground service by calling startForegournd()
	// https://stackoverflow.com/a/9696962/4070597
public class TestService extends Service {

	// constant
	public static final long NOTIFY_INTERVAL = 2 * 1000; // 10 seconds

	// run on another Thread to avoid crash
	private Handler mHandler = new Handler();
	// timer handling
	private Timer mTimer = null;

	@Override
	public IBinder onBind(Intent intent) {
		Log.d("TestService", "Test service bind called");
		return null;
	}

	@Override
	public int  onStartCommand(Intent intent, int flags, int startId) {
		Log.d("TestService", "Test service on create called");
		// cancel if already existed
		if(mTimer != null) {
			mTimer.cancel();
		} else {
			// recreate new
			mTimer = new Timer();
		}
		// schedule task
		mTimer.scheduleAtFixedRate(new TimeDisplayTimerTask(), 0, NOTIFY_INTERVAL);
		return Service.START_STICKY;
	}

	class TimeDisplayTimerTask extends TimerTask {
		@Override
		public void run() {
			FileService.checkPractiScoreExportFileChange();
		}
	}
}
