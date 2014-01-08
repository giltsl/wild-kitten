package com.testfairy.wildkitten;

import android.app.*;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.os.IBinder;
import android.util.Log;

import java.util.List;

public class KittenService extends IntentService {

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

		if (intent.hasExtra("killAllApps")) {
			killAllApps();
		} else if (intent.hasExtra("enableWifi")) {
			enableWifi();
		} else if (intent.hasExtra("disableWifi")) {
			disableWifi();
		} else if (intent.hasExtra("disableKeyguard")) {
			disableKeyguard();
		} else if (intent.hasExtra("version")) {
			version();
		}
	}

	private void version() {
		Log.v(Config.TAG, "Version " + Config.VERSION);
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
	 * Disable WIFI on this device.
	 *
	 * @return
	 */
	private void enableWifi() {
		WifiManager wifiManager = (WifiManager)this.getSystemService(Context.WIFI_SERVICE);
		wifiManager.setWifiEnabled(true);
		success();
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
	 * Print out a success message, for a developer to know exactly when WildKitten
	 * has completed its work.
	 *
	 * @return
	 */
	private void success() {
		Log.v(Config.TAG, "WildKitten completed successfully");
	}
}
