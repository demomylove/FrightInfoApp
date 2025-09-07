package com.flightinfo.app.utils

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri

object DeepLinkHelper {
    fun openCheckIn(context: Context, url: String?): Boolean {
        return openUrl(context, url)
    }

    fun openBoardingPass(context: Context, url: String?): Boolean {
        return openUrl(context, url)
    }

    private fun openUrl(context: Context, url: String?): Boolean {
        if (url.isNullOrBlank()) return false
        return try {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)
            true
        } catch (_: ActivityNotFoundException) {
            false
        }
    }
}
