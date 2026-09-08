# SmartTask - Smart Task & Productivity Manager

SmartTask is a modern, offline-first Android productivity application designed to help users organize, schedule, and track their daily tasks efficiently. Built using modern Android development practices, Jetpack Compose, Material 3, and Clean Architecture principles.

---

## App Description

SmartTask is an intuitive productivity manager built to operate completely offline. It provides users with a distraction-free environment to create tasks, organize them by priorities and categories, set up local reminders, and track progress over time.

### Core Value Proposition

* **Offline First**: All user data remains local, secure, and accessible instantly without requiring an internet connection.
* **Modern Material 3 Design**: Features a custom-built executive theme with support for Light and Dark modes.
* **Reliable Task Scheduling**: Utilizes Android WorkManager for offline task reminders and recurring schedules.
* **Fast Navigation**: Smooth onboarding and screen transitions built with Jetpack Navigation Compose.

---

## Key Features

### Task Management
* **Create, Edit, and Delete Tasks**: Manage daily tasks with titles, descriptions, due dates, and priorities.
* **Task Prioritization**: Categorize tasks by High, Medium, and Low priorities with visual indicator badges.
* **Subtasks and Checklists**: Break down complex tasks into smaller actionable items.

### Productivity Tools
* **Offline Local Persistence**: Instant data retrieval and storage powered by Room Database.
* **Preferences Storage**: User theme selection and onboarding state saved via Jetpack DataStore.
* **Background Reminders**: Scheduled local notifications using WorkManager.

### User Interface
* **Jetpack Compose & Material 3**: Declarative UI components with clean typography and adaptive layout support.
* **Custom Animated Splash Screen**: Live checkmark vector animation on app startup.
* **Light and Dark Themes**: Fully tailored color palettes for both bright and dark environments.

---

## Architecture and Tech Stack

SmartTask follows Modern Android Development (MAD) recommendations and Clean Architecture principles:

* **Language**: 100% Kotlin
* **UI Framework**: Jetpack Compose with Material 3
* **Architecture Pattern**: MVVM / Clean Architecture (Presentation, Domain, Data layers)
* **Dependency Injection**: Hilt
* **Local Database**: Room Database (with KSP code generation)
* **Preferences**: Jetpack DataStore Preferences
* **Background Processing**: WorkManager & Hilt Work
* **Navigation**: Jetpack Navigation Compose
* **Asynchronous Programming**: Kotlin Coroutines & Flow
* **Image Loading**: Coil
* **Logging**: Timber
* **Splash Screen**: Jetpack Core SplashScreen with Animated Vector Drawables

---

## Project Structure

```
SmartTask/
├── app/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/madi/smarttask/
│   │   │   │   ├── core/
│   │   │   │   │   ├── domain/        # Domain models and shared business logic
│   │   │   │   │   ├── presentation/  # MainActivity, Theme, Navigation, Base Components
│   │   │   │   │   └── util/          # Utilities and Constants
│   │   │   │   ├── di/                # Hilt Dependency Injection Modules
│   │   │   │   ├── feature_onboarding/ # Onboarding feature screen and components
│   │   │   │   └── SmartTaskApplication.kt
│   │   │   └── res/                   # Drawables, Animators, Layouts, Values
│   └── build.gradle.kts
├── gradle/
│   └── libs.versions.toml             # Centralized Gradle Version Catalog
├── build.gradle.kts
└── settings.gradle.kts
```

---

## Prerequisites and Requirements

* **Android Studio**: Ladybug / 2024.2.1 or newer
* **JDK Version**: Java 11 or Java 17
* **Minimum SDK**: Android 10 (API level 29)
* **Target SDK**: Android 15 (API level 35/37)

---

## Getting Started

1. **Clone the Repository**:
   ```bash
   git clone https://github.com/your-username/SmartTask.git
   cd SmartTask
   ```

2. **Open in Android Studio**:
   Open Android Studio and select `Open an existing project`, then navigate to the cloned `SmartTask` directory.

3. **Build the Project**:
   Run a Gradle Sync and build using:
   ```bash
   ./gradlew assembleDebug
   ```

4. **Run on Device / Emulator**:
   Select your connected device or emulator (API 29+) and press `Run`.

---

## License

This project is licensed under the MIT License.
