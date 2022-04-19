# hellojar

1. Create Project

   ```sh
   flutter create hellojar
   ```

2. Create the Flutter platform client

   All channel names used in a single app must be `unique`; prefix the channel name with a unique ‘domain prefix’, for example: `example.myflutter/hellojar`.

   - Import the following line to `main.dart`

   ```dart
   import 'dart:async';
   import 'package:flutter/material.dart';
   import 'package:flutter/services.dart';
   ```

   - Add following lines to `_MyHomePageState`

   ```dart
   class _MyHomePageState extends State<MyHomePage> {
      static const platform = MethodChannel('example.myflutter/hellojar');

      String _message = 'Unknown';
      int _counter = 0;

      Future<void> _greeting() async {
        String message;
        try {
          final String result = await platform.invokeMethod('greeting');
          message = result;
        } on PlatformException catch (e) {
          message = "Failed to get greeting message: '${e.message}'.";
        }

        setState(() {
          _message = message;
          _counter++;
        });
      }
   }
   ```

   - Change implementation in `Widget build(BuildContext context)`

     - Replace `Text('$_counter')` with

       ```dart
       Text('$_message: $_counter')
       ```

     - Replace `onPressed: _incrementCounter` with

       ```dart
       onPressed: _greeting,
       ```

3. Add an Android platform-specific implementation

   - Add CHANNEL to MainActivity class

   ```kotlin
   private val CHANNEL = "example.myflutter/hellojar"
   ```

   - Inside the `configureFlutterEngine()` method, create a `MethodChannel` and call `setMethodCallHandler()`.

   ```kotlin
   import androidx.annotation.NonNull
   import io.flutter.embedding.android.FlutterActivity
   import io.flutter.embedding.engine.FlutterEngine
   import io.flutter.plugin.common.MethodChannel

   class MainActivity: FlutterActivity() {
   private val CHANNEL = "example.myflutter/hellojar"

   override fun configureFlutterEngine(@NonNull flutterEngine: FlutterEngine) {
       super.configureFlutterEngine(flutterEngine)
       MethodChannel(flutterEngine.dartExecutor.binaryMessenger, CHANNEL).setMethodCallHandler {
       call, result ->
       // Note: this method is invoked on the main thread.
       // TODO
       }
   }
   }
   ```

   - Add the following method in the `MainActivity` class.

   ```kotlin
   private fun greeting(): String {
      var time = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(LocalDateTime.now())
      return "Hello, World $time"
   }
   ```

   ```kotlin
   import java.time.LocalDateTime
   import java.time.format.DateTimeFormatter
   ```

   - Complete the `setMethodCallHandler()` method

   ```kotlin
   setMethodCallHandler {
     call, result ->
     if (call.method == "greeting") {
       val message = greeting()

       if (message != null) {
         result.success(message)
       } else {
         result.error("NOT FOUND", "Greeting message not found.", null)
       }
     } else {
       result.notImplemented()
     }
   }
   ```

4. Use External jar

   - Create directory call `libs` under android/app directory

   ```sh
   mkdir android/app/libs
   ```

   - Copy ext-libs/greeting-1.0-SNAPSHOT.jar to android/app/libs

   ```sh
   cp ext-libs/greeting-1.0-SNAPSHOT.jar android/app/libs
   ```

   - Add dependencies in android/app/build.gradle

   ```gradle
   dependencies {
       ...
       implementation(files('libs/greeting-1.0-SNAPSHOT.jar'))
   }
   ```

   - Import Greeting

   ```kotlin
   import example.myflutterx.Greeting
   ```

   - Replace MainActivity.greeting() with

   ```kotlin
   private fun greeting(): String {
      var greeting = Greeting()
      return greeting.message()
   }
   ```
