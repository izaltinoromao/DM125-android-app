# MyTasks

MyTasks is an Android application for task organization developed in Kotlin. The app allows users to create, save, and view tasks with customizable notification settings and date formatting. It also offers integrated authentication with Google Sign-In.

## Features
- **Task Creation**: Create tasks with specific details and save them for later access.
- **Notifications**: Enable or disable daily reminder notifications for tasks.
- **Date Format Configuration**: Choose your preferred date format for display in task cards.
- **Google Authentication**: Securely log in using Google Sign-In.
- **Registration and Login with Firebase**: Email and password authentication for account creation and login using Firebase Authentication.

## Requirements
- **Minimum SDK**: 26
- **Target SDK**: 34
- **Tools**: Android Studio with Kotlin support
- **Dependencies**: Configured in `build.gradle` with libraries for Firebase and Google Sign-In

## Initial Setup

1. **Firebase Configuration**
    - Access the Firebase Console and create a new project.
    - Add an Android app to the project by entering your project's `applicationId` (in this case, `com.izaltino.mytasks`).
    - Download the `google-services.json` file and place it in the `app` directory of your project in Android Studio.
    - In the Firebase Console, enable Email and Password authentication and Google Sign-In.

2. **Google Sign-In Configuration**
    - In the Firebase Console, navigate to **Project Settings > Service Accounts** and copy the client_id for the Web client.
    - Add the client_id to the `strings.xml` file for easy configuration:
      ```xml
      <!-- In res/values/strings.xml -->
      <string name="default_web_client_id">YOUR_CLIENT_ID_HERE</string>
      ```
    - In your app's code, the client_id can then be accessed as a string resource:
      ```kotlin
      val client_id = getString(R.string.default_web_client_id)
      ```

3. **Sync and Rebuild the Project**
    - After adding dependencies and configuring environment variables, sync Gradle and rebuild the project.

4. **Running the App**
    - Connect to Firebase Authentication via Google login or email/password registration.
    - Manage notification settings and date format configurations through the app's settings.
    - Create and organize your tasks, which will be displayed in order of creation or priority.

## Dependencies
The main dependencies for the project are located in the `build.gradle` file:
- Firebase Authentication
- Google Sign-In API
- Retrofit for network requests
- Material Design Components for UI design

## Retrofit Configuration
The base URL for the server is directly set in the `RetrofitService` class. Currently, it is configured to `http://192.168.18.101:8080/`.

```kotlin
class RetrofitService {

    private var taskRepository: TaskRepository

    init {
        val retrofit = Retrofit.Builder()
                .baseUrl("http://192.168.18.101:8080/") // Base URL directly set here
                .client(configureClient())
                .addConverterFactory(configureConverter())
                .build()

        taskRepository = retrofit.create(TaskRepository::class.java)
    }

    // ...
}
