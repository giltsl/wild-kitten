Wild Kitten
-----------

Wild Kitten is a helper for Android developers for performing certain actions only
available via an Android application. Such actions including kill all running processes,
turning on and off WIFI network or disabling the keyguard lock.

Wild Kitten was designed to work from command line using adb. There is no requirement
for a root permission on the device, only for an apk to be installed.

Installation
------------

Compile Wild Kitten or download a pre-compiled binary from:
https://www.testfairy.com/downloads/WildKitten-debug-unaligned.apk

To install Wild Kitten on your device or emulator, run
`adb install -r WildKitten-debug-unsigned.apk`

To perform a specific action, run
`adb shell am startservice -n com.testfairy.wildkitten/.KittenService --esn ACTION`

For example, to turn off WIFI, run
`adb shell am startservice -n com.testfairy.wildkitten/.KittenService --esn disableWifi`

Actions
-------

Here is a list of supported actions:

* enableWifi        - enable WIFI, connects to last network
* disableWifi       - disable WIFI and disconnect from network
* enableNetwork     - enable 3G network
* disableNetwork    - disable 3G network
* checkNetwork      - check if network is accessible (either 3G or WIFI)
* killAllApps       - kills all running apps (other than system and home app)
* disableKeyguard   - disable keyguard lock screen
* version           - return version of Wild Kitten

Result
------

Every action completes with the log "WildKitten completed successfully". If you care for the
order of execution, if would like to block until WildKitten has completed, clear logcat, run
the action wanted, and wait until logcat contains the string above.

Codename: Wild Kitten
---------------------

Wild Kitten was chosen randomly using name-generator service at:
http://online-generator.com/name-generator/project-name-generator.php

Really, it was either that or "Serious Moose" :)

Compiling
---------

Wild Kitten was developed using Gradle, to build from command-line, simply run
`gradlew packageDebug`

You might to set ANDROID_HOME environment variable beforehand, for example (with Android Studio on OSX)
`export ANDROID_HOME=/Applications/Android\ Studio.app/sdk`

Uninstalling
------------

Always remeber that Wild Kitten is for testing purposes only. To uninstall Wild Kitten from your test device, simple run:
`adb uninstall com.testfairy.wildkitten`

If you removed used disableKeyguard, the device will restore the default keyguard lock.

Contact
-------

Wild Kitten was developed as part of our automatic tests at TestFairy (https://www.testfairy.com).
For every build, we run hundreds of tests, some on real devices and some emulators. Our product
requires that we support all API levels, OEM devices, serious memory limitations and other quirks.

We are releasing Wild Kitten to the general public, we all deserve better products!

For support (/moral), feature requests, bugs and general talk, feel free to contact Gil Megidish at
`gil@testfairy.com`

TestFairy Ltd.


https://www.testfairy.com


https://github.com/testfairy/


