package com.example.myjpcapplication

sealed class BottomNavigationFiles(var title:String, var icon:Int, var screen_route:String){
    object Home : BottomNavigationFiles("Home", R.drawable.ic_launcher_foreground,"home")
    object Message: BottomNavigationFiles("Message",R.drawable.ic_launcher_foreground,"message")
    object Notification: BottomNavigationFiles("Notification",R.drawable.ic_launcher_foreground,"notification")
    object Profile: BottomNavigationFiles("Profile",R.drawable.ic_launcher_foreground,"profile")
}
