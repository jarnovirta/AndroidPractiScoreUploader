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

public class TestService extends Service {

	// constant
	public static final long NOTIFY_INTERVAL = 2 * 1000; // 10 seconds

	// run on another Thread to avoid crash
	private Handler mHandler = new Handler();
	// timer handling
	private Timer mTimer = null;

	@Override
	public IBinder onBind(Intent intent) {
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
		return Service.START_NOT_STICKY;
	}

	class TimeDisplayTimerTask extends TimerTask {
		@Override
		public void run() {
			Log.d("TestService", "Test service scheduled task");
		}
	}
}
