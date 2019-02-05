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

internal object Helper {

    private val DEBUG = false
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

    fun tryParseInt(value: String): Boolean {
        return try {
            Integer.parseInt(value)
            true
        } catch (e: NumberFormatException) {
            false
        }

    }

    fun showMessage(cxt: Context, melding: String) {
        Helper.log(melding)
        val duration = Toast.LENGTH_SHORT
        val toast = Toast.makeText(cxt, melding, duration)
        toast.view.setBackgroundColor(ContextCompat.getColor(cxt, R.color.colorPrimary))
        val text = toast.view.findViewById(android.R.id.message) as TextView
        text.setTextColor(ContextCompat.getColor(cxt, R.color.colorAccent))
        toast.show()
    }

    fun getFcmToken(cxt: Context): String {
        val preferences = PreferenceManager.getDefaultSharedPreferences(cxt)
        return preferences.getString(FcmNames.FcmToken, "")
    }

    fun setFcmToken(cxt: Context, token: String) {
        val preferences = PreferenceManager.getDefaultSharedPreferences(cxt)
        val editor = preferences.edit()
        editor.putString(FcmNames.FcmToken, token)
        editor.apply()
        FcmSender.mFcmToken = token
    }

    fun getHostToken(cxt: Context): String? {
        val preferences = PreferenceManager.getDefaultSharedPreferences(cxt)
        return preferences.getString(FcmNames.HostToken, "")
    }

    fun setHostToken(cxt: Context, token: String) {
        val preferences = PreferenceManager.getDefaultSharedPreferences(cxt)
        val editor = preferences.edit()
        editor.putString(FcmNames.HostToken, token)
        editor.apply()
        FcmSender.mHostToken = token
    }

    fun getName(cxt: Context): String {
        val preferences = PreferenceManager.getDefaultSharedPreferences(cxt)
        val nick =  preferences.getString("nick", "")
        if (nick==null)
            return ""
        return nick
    }

    fun setName(cxt: Context, nick: String) {
        val preferences = PreferenceManager.getDefaultSharedPreferences(cxt)
        val editor = preferences.edit()
        editor.putString("nick", nick)
        editor.apply()
    }

    fun showMessage(cxt: Context, melding: String, isLong: Boolean) {
        Helper.log(melding)
        val duration = if (isLong) Toast.LENGTH_LONG else Toast.LENGTH_SHORT
        val toast = Toast.makeText(cxt, melding, duration)
        toast.show()
    }


    fun alert(cxt: Context, melding: String) {
        Helper.showMessage(cxt, melding, true)
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

    fun showDialog(d: Dialog) {
        showDialog(d, true, false)
    }

    fun showDialog(d: Dialog, maximize: Boolean?) {
        showDialog(d, maximize!!, false)
    }

    fun showDialog(d: Dialog, maximize: Boolean, forceKeyboard: Boolean?) {
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

        if (forceKeyboard!!) {
            val dialogWindow = d.window
            dialogWindow!!.attributes = lp
            dialogWindow.clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM)
            dialogWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE)
        }
    }

    fun fcmActive(context: Context, tvFcmBolt: TextView) {
        val iconFont = FontManager.GetTypeface(context, FontManager.FONTAWESOME_SOLID)
        FontManager.markAsIconContainer(tvFcmBolt, iconFont)
        tvFcmBolt.animate().alpha(1.0f).setDuration(50)
                .setListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator) {
                        tvFcmBolt.animate().alpha(0.0f).setDuration(100).startDelay = 50
                    }
                })
    }

}