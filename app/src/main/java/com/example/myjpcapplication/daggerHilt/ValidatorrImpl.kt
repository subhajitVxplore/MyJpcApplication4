package com.example.myjpcapplication.daggerHilt

import android.util.Patterns
import javax.inject.Inject

class ValidatorrImpl @Inject constructor():ValidatorrUtil {
    override fun isEmailValid(email: String): Boolean =
        Patterns.EMAIL_ADDRESS.matcher(email).matches()
}