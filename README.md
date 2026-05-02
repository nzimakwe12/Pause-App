# Pause App

Stop the notification overload. Pause collects non-essential notifications and delivers them as a daily digest at times you choose.

## Features

- **Notification Batching**: Automatically intercepts and batches non-essential notifications
- **Daily Digest**: Receive a summary of all notifications at your preferred time
- **Quick Settings Tile**: Toggle Pause on/off directly from your Quick Settings
- **Home Widget**: Monitor Pause status with a home screen widget
- **Monetization**: AdMob integration with native ads and interstitial ads
- **In-App Purchases**: One-time $0.20 purchase to remove ads
- **Material Design 3**: Modern, intuitive user interface

## AdMob Configuration

This app includes AdMob integration with the following ad unit:
- **App ID**: ca-app-pub-5374221916375781~8498128651
- **Native Ads**: Displayed in the digest screen
- **Interstitial Ads**: Displayed on app exit

## Getting Started

### Download the APK

1. Go to the **Releases** section of this repository
2. Download the latest `Pause-App.apk` file
3. Transfer to your Android phone
4. Open the file and tap "Install"
5. Grant the required permissions

### First Launch

1. Open Pause
2. Go to Settings → Apps & Notifications → Special app access → Notification access
3. Find "Pause" and toggle it ON
4. Return to the Pause app
5. Customize your digest time in Settings
6. Toggle Pause ON

## Building from Source

If you want to build the app yourself:

```bash
./gradlew assembleDebug
```

The APK will be generated at: `app/build/outputs/apk/debug/app-debug.apk`

## Requirements

- Android 8.0 (API 26) or higher
- Notification Listener permission
- Internet connection (for ads)

## Tech Stack

- **Language**: Kotlin
- **UI Framework**: Jetpack Compose
- **Database**: Room
- **Background Jobs**: WorkManager
- **Widgets**: Glance
- **Ads**: Google Mobile Ads SDK
- **Billing**: Google Play Billing Library

## Architecture

The app follows Clean Architecture principles with:
- **Data Layer**: Room database, DAOs, Repository
- **Service Layer**: NotificationListenerService, QuickSettingsTileService
- **ViewModel Layer**: MVVM pattern with LiveData
- **UI Layer**: Jetpack Compose screens

## Permissions

- `NOTIFICATION_LISTENER_SERVICE`: To intercept notifications
- `POST_NOTIFICATIONS`: To display digest notifications
- `INTERNET`: For AdMob ads
- `ACCESS_NETWORK_STATE`: For ad network detection

## License

This project is proprietary. All rights reserved.

## Support

For issues or feature requests, please open an issue in this repository.
