# Farmers Pest Detection Android Application

Native Android application built in Java for farmers to scan pest photos, obtain instant crop damage analysis, and receive recommended pesticide treatments.

## Key Features

- **Material Design 3**: Clean agricultural theme (`#1E5631`, `#4C9A2A`), large tap targets (minimum 48dp), high-contrast outdoor sunlight readability.
- **Bilingual & RTL**: Full support for English (`values/strings.xml`) and Urdu (`values-ur/strings.xml`) with automatic Right-to-Left (RTL) layout direction switching.
- **CameraX Integration**: Live camera preview, flash toggle, and alternative gallery photo picker.
- **Lottie Animations**: Smooth scanning leaf loading animation (`scan_leaf_anim.json`).
- **Offline Mode & Room Database**: Caches past scans locally in SQLite via Room database (`AppDatabase`). If offline, queue images and display clear offline status banner.
- **Encrypted Local Storage**: JWT tokens and session data stored securely via `EncryptedSharedPreferences`.
- **WhatsApp & SMS Sharing**: Share detection results directly with fellow farmers or agricultural experts.

## Technology Stack

- **Language**: Java 17 / 8
- **Min SDK**: 24 (Android 7.0+)
- **Target SDK**: 34 (Android 14)
- **Architecture**: Material 3 + ViewBinding + Room + Retrofit + OkHttp + Glide + Lottie + CameraX

## Project Structure

```
app/
├── app/src/main/
│   ├── AndroidManifest.xml
│   ├── java/com/pestdetect/app/
│   │   ├── data/ (api, db, models)
│   │   ├── ui/ (splash, onboarding, language, auth, main, camera, preview, analyzing, result, history, profile, settings, adapters)
│   │   └── utils/ (LocaleHelper, EncryptedSessionManager, NetworkUtils, Constants)
│   └── res/
│       ├── layout/
│       ├── values/ (colors, strings, themes)
│       ├── values-ur/ (Urdu strings)
│       ├── drawable/
│       └── raw/ (lottie animation)
└── build.gradle
```

## How to Build & Run

1. **Open Project**: Launch Android Studio and select "Open an Existing Project", choosing the `/app` folder.
2. **Build Project**: Run `./gradlew assembleDebug` or click **Build -> Make Project** in Android Studio.
3. **Run on Emulator / Device**:
   - Ensure backend server is running on `localhost:5000` (or `10.0.2.2:5000` for Android Emulator).
   - Press **Run 'app'** in Android Studio.
