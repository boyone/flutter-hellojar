package com.example.hellojar

import androidx.annotation.NonNull
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import io.flutter.embedding.android.FlutterActivity
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.plugin.common.MethodChannel

import example.myflutterx.Greeting

class MainActivity: FlutterActivity() {
    private val CHANNEL = "example.myflutter/hellojar"

    override fun configureFlutterEngine(@NonNull flutterEngine: FlutterEngine) {
    super.configureFlutterEngine(flutterEngine)
    MethodChannel(flutterEngine.dartExecutor.binaryMessenger, CHANNEL).setMethodCallHandler {
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
  }

  private fun greeting(): String {
      var greeting = Greeting()
      return greeting.message()
  }
}

