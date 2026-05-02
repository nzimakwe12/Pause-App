# Pause App - Build & Installation Guide

## Quick Start (Easiest Method)

### Prerequisites
- Android Studio 2024.1 or later
- Android SDK 34
- Java 17 or later

### Step-by-Step

1. **Extract the Project**
   ```bash
   unzip pause_app.zip
   cd pause_app
   ```

2. **Open in Android Studio**
   - Launch Android Studio
   - Click "Open" and select the `pause_app` folder
   - Wait for Gradle sync to complete (first time takes 2-3 minutes)

3. **Build the APK**
   - Go to `Build` → `Build Bundle(s) / APK(s)` → `Build APK(s)`
   - Select "Debug" when prompted
   - Wait for the build to complete
   - The APK will be generated at: `app/build/outputs/apk/debug/app-debug.apk`

4. **Install on Your Device**
   - Connect your Android device (Android 8.0+)
   - Click `Run` → `Run 'app'`
   - Or manually install using ADB:
     ```bash
     adb install app/build/outputs/apk/debug/app-debug.apk
     ```

## Command Line Build (Advanced)

If you prefer building from the command line:

```bash
cd pause_app
./gradlew clean assembleDebug
```

The APK will be generated at: `app/build/outputs/apk/debug/app-debug.apk`

## First Launch Setup

When you first open the Pause app:

1. **Grant Notification Listener Permission**
   - The app will prompt you to enable "Notification Listener"
   - Go to Settings → Apps & notifications → Special app access → Notification access
   - Toggle "Pause" ON

2. **Customize Settings**
   - Set your preferred digest times (default: 8 AM, 1 PM, 6 PM)
   - Add VIP contacts (optional)
   - Toggle Pause ON to start batching notifications

3. **Test It Out**
   - Open Pause and toggle it ON
   - Send yourself test notifications from other apps
   - Wait for the next digest time to see them batched

## Troubleshooting

### "Android Gradle plugin requires Java 17" Error

**Solution:** Set Java 17 as your JDK
- In Android Studio: `File` → `Project Structure` → `SDK Location`
- Set JDK location to Java 17
- Or set environment variable: `export JAVA_HOME=/path/to/java17`

### Gradle Sync Fails

**Solution:**
- Click `File` → `Sync Now`
- Or run: `./gradlew --refresh-dependencies`
- If still failing, try: `./gradlew clean`

### Build Fails with "Plugin not found"

**Solution:**
- Click `File` → `Invalidate Caches` → `Invalidate and Restart`
- Wait for Android Studio to restart and resync

### APK Won't Install

**Solution:**
- Ensure device is on Android 8.0 or higher
- Uninstall any previous version: `adb uninstall com.pause.app`
- Try again: `adb install app/build/outputs/apk/debug/app-debug.apk`

### App Crashes on Launch

**Solution:**
- Check logcat for errors: `adb logcat | grep pause`
- Ensure all permissions are granted
- Try clearing app data: `adb shell pm clear com.pause.app`

## Release Build (For Google Play)

To create a production-ready APK:

```bash
./gradlew clean assembleRelease
```

You'll need to:
1. Create a keystore file for signing
2. Configure signing in `app/build.gradle.kts`
3. Upload to Google Play Console

See: https://developer.android.com/studio/publish/app-signing

## App Features

✅ Smart notification batching
✅ Daily digest summaries (customizable times)
✅ VIP contact management (bypass Pause)
✅ Quick settings tile (toggle from notification shade)
✅ Home screen widget (1x1 quick toggle)
✅ AdMob integration (ads in digest)
✅ Google Play Billing ($0.20 support)
✅ Notification insights & trends

## Permissions Explained

The app requests these permissions:

| Permission | Purpose |
|-----------|---------|
| Notification Listener | Intercept and batch notifications |
| Read Contacts | Identify VIP contacts |
| Vibrate | Notification feedback |
| Internet | AdMob ads & Google Play Billing |
| Post Notifications | Show digest notifications (Android 13+) |

Grant these when prompted on first launch.

## Project Structure

```
pause_app/
├── app/
│   ├── src/main/
│   │   ├── java/com/pause/app/
│   │   │   ├── data/          (Room database, entities, DAOs)
│   │   │   ├── service/       (Notification listener, tile service)
│   │   │   ├── ui/            (Screens and components)
│   │   │   ├── viewmodel/     (ViewModels)
│   │   │   ├── worker/        (WorkManager for digest scheduling)
│   │   │   ├── billing/       (Google Play Billing)
│   │   │   ├── ads/           (AdMob integration)
│   │   │   ├── widget/        (Home screen widget)
│   │   │   └── util/          (Utilities and extensions)
│   │   └── res/               (Resources, strings, layouts)
│   └── build.gradle.kts       (App dependencies & configuration)
├── build.gradle.kts           (Project configuration)
├── settings.gradle.kts        (Module settings)
└── gradle.properties          (Gradle configuration)
```

## Architecture

The app follows **Clean Architecture** with **MVVM** pattern:

- **Data Layer:** Room database, DAOs, Repository
- **Domain Layer:** Business logic (notification batching, scheduling)
- **UI Layer:** Jetpack Compose screens, ViewModels
- **Services:** NotificationListenerService, WorkManager, Billing

## Next Steps

1. **Build the APK** using the steps above
2. **Install on your device**
3. **Grant permissions** when prompted
4. **Customize settings** to your preference
5. **Test the app** by toggling Pause ON/OFF
6. **Submit to Google Play** (optional, for release)

## Support & Feedback

For issues or feature requests:
- Check the troubleshooting section above
- Review Android Studio logs for error details
- Visit: https://pause-app.com/support

---

**Happy building! 🎉**
