package nl.whitedove.yavalath

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.app.Dialog
import android.content.BroadcastReceiver
import android.content.Context
import android.location.Location
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.preference.PreferenceManager
import androidx.core.content.ContextCompat
import android.text.Html
import android.text.TextUtils
import android.view.WindowManager
import android.widget.TextView
import android.widget.Toast
import org.joda.time.DateTime
import java.util.*


internal object Helper {

    val DEBUG = false
    private val mRandom = Random()
    var mCurrentBestLocation: Location? = null
    const val ONE_MINUTE = 1000L * 60L
    const val ONE_KM = 1000F

    fun log(log: String) {
        if (DEBUG) {
            println(log)
        }
    }

    fun testInternet(context: Context): Boolean {
        var result = false
        val connectivityManager =
                context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val networkCapabilities = connectivityManager.activeNetwork ?: return false
            val actNw =
                    connectivityManager.getNetworkCapabilities(networkCapabilities) ?: return false
            result = when {
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                else -> false
            }
        } else {
            connectivityManager.run {
                @Suppress("DEPRECATION")
                connectivityManager.activeNetworkInfo?.run {
                    result = when (type) {
                        ConnectivityManager.TYPE_WIFI -> true
                        ConnectivityManager.TYPE_MOBILE -> true
                        ConnectivityManager.TYPE_ETHERNET -> true
                        else -> false
                    }

                }
            }
        }

        if (!result) showMessage(context, context.getString(R.string.NoInternet), ContextCompat.getColor(context, R.color.colorLightRed))
        return result
    }

    fun randomNrInRange(lower: Int, upper: Int): Int {
        return mRandom.nextInt(lower + upper + 1) + lower
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

    fun getGameLevel(context: Context): GameLevel {
        val preferences = PreferenceManager.getDefaultSharedPreferences(context)
        val gameLevel = preferences.getInt("gameLevel", 1)
        return GameLevel.valueOf(gameLevel)
    }

    fun setGameLevel(context: Context, gameLevel: GameLevel) {
        val preferences = PreferenceManager.getDefaultSharedPreferences(context)
        val editor = preferences.edit()
        editor.putInt("gameLevel", gameLevel.value)
        editor.apply()
    }

    fun getGameMode(context: Context): GameMode {
        val preferences = PreferenceManager.getDefaultSharedPreferences(context)
        val gameMode = preferences.getInt("gameMode", 1)
        return GameMode.valueOf(gameMode)
    }

    fun setGameMode(context: Context, gameMode: GameMode) {
        val preferences = PreferenceManager.getDefaultSharedPreferences(context)
        val editor = preferences.edit()
        editor.putInt("gameMode", gameMode.value)
        editor.apply()
    }

    fun getName1(context: Context): String {
        val preferences = PreferenceManager.getDefaultSharedPreferences(context)
        val nick = preferences.getString("nick", "") ?: return ""
        return nick
    }

    fun setName1(context: Context, nick: String) {
        val preferences = PreferenceManager.getDefaultSharedPreferences(context)
        val editor = preferences.edit()
        editor.putString("nick", nick)
        editor.apply()
    }

    fun getName2(context: Context): String {
        val preferences = PreferenceManager.getDefaultSharedPreferences(context)
        val nick2 = preferences.getString("nick2", "")
        if (nick2 == null)
            return ""
        return nick2
    }

    fun setName2(context: Context, nick2: String) {
        val preferences = PreferenceManager.getDefaultSharedPreferences(context)
        val editor = preferences.edit()
        editor.putString("nick2", nick2)
        editor.apply()
    }

    fun showMessage(context: Context, melding: String, color: Int) {
        log(melding)
        val duration = Toast.LENGTH_LONG
        val toast = Toast.makeText(context, melding, duration)
        toast.view.setBackgroundColor(color)
        val text = toast.view.findViewById(android.R.id.message) as TextView
        text.setTextColor(ContextCompat.getColor(context, R.color.colorPrimary))
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

    fun setHtmlText(view: TextView, htmlTxt: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            view.text = Html.fromHtml(htmlTxt, Html.FROM_HTML_MODE_LEGACY)
        }
        else {
            @Suppress("DEPRECATION")
            view.text = Html.fromHtml(htmlTxt)
        }
    }

    fun getLastRemoveHighScoresDate(cxt: Context): DateTime {
        val preferences = PreferenceManager.getDefaultSharedPreferences(cxt)
        val dat = preferences.getLong("RemoveHighScoresDate", DateTime(2000, 1, 1, 0, 0).millis)
        return DateTime(dat)
    }

    fun setLastRemoveHighScoresDate(cxt: Context, date: DateTime) {
        val preferences = PreferenceManager.getDefaultSharedPreferences(cxt)
        val editor = preferences.edit()
        editor.putLong("RemoveHighScoresDate", date.millis)
        editor.apply()
    }
}