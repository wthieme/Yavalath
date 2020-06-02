package nl.whitedove.yavalath

import android.content.Context
import android.media.MediaPlayer
import androidx.preference.PreferenceManager

object SoundPlayer {

    private var mPlayer: MediaPlayer? = null

    internal fun playSound(context: Context, name: String) {

        val preferences = PreferenceManager.getDefaultSharedPreferences(context)
        val soundOn = preferences.getBoolean("settingsound", true)
        if (!soundOn) return

        val id = context.resources.getIdentifier(name, "raw", context.packageName)
        val player = mPlayer
        if (player != null) {
            player.release()
            mPlayer=null
        }

        if (mPlayer == null) {
            mPlayer = MediaPlayer.create(context, id)
        }
        mPlayer!!.start()
    }
}
