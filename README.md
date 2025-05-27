# Feel 🌈

A modern Android application built with Jetpack Compose and Material 3, designed to help users track and manage their emotions.

## 🌟 Features

- **Modern UI**: Built entirely with Jetpack Compose and Material 3 design system
- **Real-time Weather Integration**: Location-based weather updates
- **Emotion Tracking**: Track and monitor your emotional well-being
- **Beautiful Animations**: Smooth transitions and engaging user interactions
- **Offline Support**: Access your data even without internet connection

## 🛠️ Tech Stack

- **UI Framework**: Jetpack Compose with Material 3
- **Architecture**: MVVM with Clean Architecture
- **Language**: Kotlin
- **Networking**: Retrofit with OkHttp
- **Concurrency**: Kotlin Coroutines
- **Image Loading**: Coil
- **Location Services**: Google Play Services Location
- **Navigation**: Jetpack Navigation Compose

## 🚀 Getting Started

### Prerequisites

- Android Studio Arctic Fox or later
- JDK 11 or later
- Android SDK with minimum API level 31 (Android 12)

### Building the Project

1. Clone the repository
2. Open the project in Android Studio
3. Sync the project with Gradle files
4. Run the app on an emulator or physical device

## 📱 Supported Android Versions

- Minimum SDK: 31 (Android 12)
- Target SDK: 35 (Android 15)

## 🏗️ Project Structure

```
app/
├── src/
│   ├── main/
│   │   ├── java/           # Kotlin source files
│   │   ├── res/           # Resources
│   │   └── assets/        # Static assets
│   ├── test/              # Unit tests
│   └── androidTest/       # Instrumentation tests
├── build.gradle.kts       # App-level Gradle build file
└── proguard-rules.pro    # ProGuard rules
```

## 🔑 Configuration

The application uses the debug keystore for development builds. For production releases, please configure your own signing keys in the `signingConfigs` block of the app's `build.gradle.kts`.

## 📝 Version History

- Current Version: 1.5 (Build 2)
- Minimum Android Version: Android 12 (API 31)

## 🤝 Contributing

Contributions are welcome! Please feel free to submit a Pull Request.

## 📄 License

This project is licensed under the terms of the MIT license.

---

Made with ❤️ using Jetpack Compose
