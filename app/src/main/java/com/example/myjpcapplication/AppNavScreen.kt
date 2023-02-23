package com.example.myjpcapplication

/**
 * title can also come from StringRes
 *
 * enum class Screen(@StringRes title: Int) { ... }
 */
enum class AppNavScreen(val title: String) {
    //LoginScreen(title = "Screenn A/{userId}/{userName}"), // start screen
    LoginScreen(title = "Screenn A"),
    RegistrationScreen(title = "Screenn B"),
    DashboardScreen(title = "Scrreen C"),
}
