package com.testfairy.wildkitten;

import android.app.*;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.net.ConnectivityManager;
import android.telephony.TelephonyManager;
import android.os.IBinder;
import android.util.Log;
import android.os.Build;
import android.os.PowerManager;
import org.apache.http.*;
import org.apache.http.client.methods.*;
import org.apache.http.impl.client.DefaultHttpClient;
import java.lang.reflect.*;
import java.util.*;

public class KittenService extends IntentService {

	static PowerManager.WakeLock wakeLock = null;

	public KittenService(String name) {
		super(name);
	}

	public KittenService() {
		super("KittenService");
	}

	@Override
	public IBinder onBind(Intent intent) {
		Log.v(Config.TAG, "onBind with intent " + intent);
		return null;
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		Log.v(Config.TAG, "onHandleIntent " + intent);
		Log.d(Config.TAG, "Extra: " + Arrays.toString(intent.getExtras().keySet().toArray()));

		try {
			if (intent.hasExtra("killAllApps")) {
				killAllApps();
			} else if (intent.hasExtra("enableWifi")) {
				enableWifi();
			} else if (intent.hasExtra("disableWifi")) {
				disableWifi();
			} else if (intent.hasExtra("enableNetwork")) {
				enableNetwork();
			} else if (intent.hasExtra("disableNetwork")) {
				disableNetwork();
			} else if (intent.hasExtra("disableKeyguard")) {
				disableKeyguard();
			} else if (intent.hasExtra("checkNetwork")) {
				checkNetwork();
			} else if (intent.hasExtra("acquireWakeLock")) {
				acquireWakeLock();
			} else if (intent.hasExtra("releaseWakeLock")) {
				releaseWakeLock();
			} else if (intent.hasExtra("version")) {
				version();
			}
		} catch (Exception e) {
			Log.e(Config.TAG, "onHandleIntent failed with exception", e);
		}
	}

	private void version() {
		Log.v(Config.TAG, "Version " + Config.VERSION);
	}

	/**
	 * Check network, see if device can access some websites using port 80. This
	 * does not rely on ICMP or the ping command.
	 *
	 * If successful, logs "Network is up", otherwise logs "Network is down". Note that
	 * in either case, checkNetwork() will also log "WildKitten completed successfully".
	 *
	 * @return
	 */
	private void checkNetwork() {
		String[] urls = new String[] {
			"http://www.google.com/",
			"http://www.facebook.com/",
			"http://www.youtube.com/",
			"http://www.amazon.com/",
			"http://en.wikipedia.org/",
		};

		for (String url: urls) {
			Log.d(Config.TAG, "Checking if network is available by fetching: " + url);

			try {
				DefaultHttpClient client = new DefaultHttpClient();
				HttpGet httpGet = new HttpGet(url);
				HttpResponse response = client.execute(httpGet);
				if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
					Log.d(Config.TAG, "Successfully downloaded a page from the internet, network works well");
					Log.d(Config.TAG, "Network is up");
					success();
					return;
				}
			} catch (Exception e) {
				// ignored
			}
		}


		Log.d(Config.TAG, "Network is down");
		success();
	}

	/**
	 * Kill all apps running on this device.
	 *
	 * @return
	 */
	private void killAllApps() {
		Intent i = new Intent(this, KillAllAppsActivity.class);
		i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(i);
		success();
	}

	/**
	 * Enable WIFI on this device.
	 *
	 * @return
	 */
	private void disableWifi() {
		WifiManager wifiManager = (WifiManager)this.getSystemService(Context.WIFI_SERVICE);
		wifiManager.setWifiEnabled(false);
		success();
	}

	/**
	 * Enable WIFI on this device.
	 *
	 * @return
	 */
	private void enableWifi() {
		WifiManager wifiManager = (WifiManager)this.getSystemService(Context.WIFI_SERVICE);
		wifiManager.setWifiEnabled(true);
		success();
	}

	/**
	 * Disable 3G network on this device.
	 *
	 * @return
	 */
	private void disableNetwork() throws Exception {
		if (Build.VERSION.SDK_INT <= 9) {
			setMobileDataEnabledOld(false);
		} else {
			setMobileDataEnabledNew(false);
		}

		success();
	}

	/**
	 * Enable 3G network on this device.
	 *
	 * @return
	 */
	private void enableNetwork() throws Exception {
		if (Build.VERSION.SDK_INT <= 9) {
			setMobileDataEnabledOld(true);
		} else {
			setMobileDataEnabledNew(true);
		}

		success();
	}

	/**
	 * Enable or disable data network for Android OS 2.3 and above
	 *
	 * See http://stackoverflow.com/a/12535246/31515
	 *
	 * @return
	 */
	private void setMobileDataEnabledNew(boolean enabled) throws Exception {
		ConnectivityManager conman = (ConnectivityManager)this.getSystemService(Context.CONNECTIVITY_SERVICE);
		Class conmanClass = Class.forName(conman.getClass().getName());
		Field iConnectivityManagerField = conmanClass.getDeclaredField("mService");
		iConnectivityManagerField.setAccessible(true);
		Object iConnectivityManager = iConnectivityManagerField.get(conman);
		Class iConnectivityManagerClass = Class.forName(iConnectivityManager.getClass().getName());
		Method setMobileDataEnabledMethod = iConnectivityManagerClass.getDeclaredMethod("setMobileDataEnabled", Boolean.TYPE);
		setMobileDataEnabledMethod.setAccessible(true);
		setMobileDataEnabledMethod.invoke(iConnectivityManager, enabled);
	}

	/**
	 * Enable or disable data network for Android OS 2.2 and below
	 *
	 * See http://stackoverflow.com/a/12535246/31515
         *
         * @return
         */
	private void setMobileDataEnabledOld(boolean enabled) throws Exception {
		TelephonyManager telephonyManager = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
		Class telephonyManagerClass = Class.forName(telephonyManager.getClass().getName());
		Method getITelephonyMethod = telephonyManagerClass.getDeclaredMethod("getITelephony");
		getITelephonyMethod.setAccessible(true);
		Object ITelephonyStub = getITelephonyMethod.invoke(telephonyManager);
		Class ITelephonyClass = Class.forName(ITelephonyStub.getClass().getName());
		Method dataConnSwitchmethod = ITelephonyClass .getDeclaredMethod(enabled ? "enableDataConnectivity" : "disableDataConnectivity");
		dataConnSwitchmethod.setAccessible(true);
		dataConnSwitchmethod.invoke(ITelephonyStub);
	}

	/**
	 * Disable keyguard, which prevents automatic tests to run on emulator or device.
	 *
	 * See http://developer.android.com/tools/testing/activity_testing.html#UnlockDevice
	 *
	 * @return
	 */
	private void disableKeyguard() {
		KeyguardManager keyguardManager = (KeyguardManager)this.getSystemService(Context.KEYGUARD_SERVICE);
		KeyguardManager.KeyguardLock lock = keyguardManager.newKeyguardLock("activity_classname");
		lock.disableKeyguard();
		success();
	}

	/**
	 * Acquire a wake lock. This will turn on the device (if not on) and set keyboard and screen to full brightness.
	 *
	 * Call releaseWakeLock to turn the device screen off.
	 *
	 * @return
	 */
	private void acquireWakeLock() {
		if (wakeLock == null) {
			Context context = getApplicationContext();
			PowerManager pm = (PowerManager)context.getSystemService(Context.POWER_SERVICE);
			wakeLock = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP, Config.TAG);
			wakeLock.acquire();
			Log.v(Config.TAG, "Wakelock acquired at " + wakeLock);
		}

		success();
	}

	/**
	 * Release wake lock if previous acquired with acquireWakeLock.
	 *
	 * @return
	 */
	private void releaseWakeLock() {
		if (wakeLock != null) {
			wakeLock.release();
			wakeLock = null;
		}

		success();
	}

	/**
	 * Print out a success message, for a developer to know exactly when WildKitten
	 * has completed its work.
	 *
	 * @return
	 */
	private void success() {
		Log.v(Config.TAG, "WildKitten completed successfully");
	}
}
