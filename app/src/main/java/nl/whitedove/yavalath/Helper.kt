package nl.whitedove.yavalath

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.app.Dialog
import android.content.BroadcastReceiver
import android.content.Context
import android.location.Location
import android.net.ConnectivityManager
import android.os.Build
import android.preference.PreferenceManager
import android.support.v4.content.ContextCompat
import android.view.WindowManager
import android.widget.TextView
import android.widget.Toast
import java.util.*
import android.text.TextUtils



internal object Helper {

    val DEBUG = false
    private val mRandom = Random()
    var mCurrentBestLocation: Location? = null
    const val ONE_MINUTE = 1000L * 60L
    const val ONE_KM = 1000F

    fun log(log: String) {
        if (Helper.DEBUG) {
            println(log)
        }
    }

    fun testInternet(ctx: Context): Boolean {
        val result: Boolean
        val cm = ctx.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val netInfo = cm.activeNetworkInfo
        result = netInfo != null && netInfo.isConnected
        if (!result) Helper.showMessage(ctx, ctx.getString(R.string.NoInternet))
        return result
    }

    fun randomNrInRange(lower: Int, upper: Int): Int {
        return mRandom.nextInt(lower + upper + 1) + lower
    }

    fun tryParseInt(value: String): Boolean {
        return try {
            Integer.parseInt(value)
            true
        } catch (e: NumberFormatException) {
            false
        }
    }

    fun getFcmToken(cxt: Context): String {
        val preferences = PreferenceManager.getDefaultSharedPreferences(cxt)
        return preferences.getString(FcmNames.FcmToken, "")!!
    }

    fun setFcmToken(cxt: Context, token: String) {
        val preferences = PreferenceManager.getDefaultSharedPreferences(cxt)
        val editor = preferences.edit()
        editor.putString(FcmNames.FcmToken, token)
        editor.apply()
        FcmSender.mMyFcmToken = token
    }

    fun getName(cxt: Context): String {
        val preferences = PreferenceManager.getDefaultSharedPreferences(cxt)
        val nick = preferences.getString("nick", "")
        if (nick == null)
            return ""
        return nick
    }

    fun setName(cxt: Context, nick: String) {
        val preferences = PreferenceManager.getDefaultSharedPreferences(cxt)
        val editor = preferences.edit()
        editor.putString("nick", nick)
        editor.apply()
    }

    fun showMessage(cxt: Context, melding: String) {
        Helper.log(melding)
        val duration = Toast.LENGTH_SHORT
        val toast = Toast.makeText(cxt, melding, duration)
        toast.view.setBackgroundColor(ContextCompat.getColor(cxt, R.color.colorDisabled))
        val text = toast.view.findViewById(android.R.id.message) as TextView
        text.setTextColor(ContextCompat.getColor(cxt, R.color.colorWhite))
        toast.show()
    }

    fun getAppVersion(): String {
        return BuildConfig.VERSION_NAME
    }

    fun unRegisterReceiver(context: Context, receiver: BroadcastReceiver?) {
        if (receiver != null) {
            try {
                context.unregisterReceiver(receiver)
            } catch (ignored: Exception) {
            }

        }
    }

    fun getAndroidVersion(): String {
        return "Android " + Build.VERSION.RELEASE
    }

    fun getDeviceName(): String {
        val manufacturer = Build.MANUFACTURER
        val model = Build.MODEL
        return if (model.startsWith(manufacturer)) {
            capitalize(model)
        } else capitalize(manufacturer) + " " + model
    }

    private fun capitalize(str: String): String {
        if (TextUtils.isEmpty(str)) {
            return str
        }
        val arr = str.toCharArray()
        var capitalizeNext = true

        val phrase = StringBuilder()
        for (c in arr) {
            if (capitalizeNext && Character.isLetter(c)) {
                phrase.append(Character.toUpperCase(c))
                capitalizeNext = false
                continue
            } else if (Character.isWhitespace(c)) {
                capitalizeNext = true
            }
            phrase.append(c)
        }

        return phrase.toString()
    }

    fun showDialog(d: Dialog, maximize: Boolean) {
        val lp = WindowManager.LayoutParams()
        lp.copyFrom(d.window!!.attributes)
        if (maximize) {
            lp.height = WindowManager.LayoutParams.MATCH_PARENT
            lp.width = WindowManager.LayoutParams.MATCH_PARENT
        } else {
            lp.height = WindowManager.LayoutParams.WRAP_CONTENT
            lp.width = WindowManager.LayoutParams.WRAP_CONTENT
        }
        d.show()
        d.window!!.attributes = lp
    }

    fun fcmActive(context: Context, tvFcmBolt: TextView) {
        val iconFont = FontManager.getTypeface(context, FontManager.FONTAWESOME_SOLID)
        FontManager.markAsIconContainer(tvFcmBolt, iconFont)
        tvFcmBolt.animate().alpha(1.0f).setDuration(50)
                .setListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator) {
                        tvFcmBolt.animate().alpha(0.0f).setDuration(100).startDelay = 50
                    }
                })
    }

}