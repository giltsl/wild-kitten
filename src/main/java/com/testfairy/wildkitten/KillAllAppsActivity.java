package com.testfairy.wildkitten;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import java.util.List;

public class KillAllAppsActivity extends Activity {

	static private final String OWN_PACKAGE = "com.testfairy.wildkitten";

	@Override
	protected void onResume() {
		super.onResume();

		try {
			ActivityManager am = (ActivityManager)getSystemService(Context.ACTIVITY_SERVICE);

			final List<ActivityManager.RunningAppProcessInfo> runningProcesses = am.getRunningAppProcesses();
			for (ActivityManager.RunningAppProcessInfo runningProcess: runningProcesses) {

				String packageName = runningProcess.processName;
				if (!packageName.equals(OWN_PACKAGE) && !packageName.equals("com.sec.android.app.twlauncher")) {
					Log.d(Config.TAG, "Killing process: " + packageName + " (pid " + runningProcess.pid + ")");
					am.restartPackage(packageName);
					am.killBackgroundProcesses(packageName);
				}
			}

		} catch (Exception e) {
			Log.e(Config.TAG, "Exception", e);
		}

		leaveThisPlanet();
	}

	private void leaveThisPlanet() {
		// leave app
		Intent homeIntent = new Intent(Intent.ACTION_MAIN);
		homeIntent.addCategory(Intent.CATEGORY_HOME);
		homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(homeIntent);
		System.exit(0);
	}
}
