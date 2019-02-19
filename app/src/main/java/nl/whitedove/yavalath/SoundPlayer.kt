package nl.whitedove.yavalath

import android.content.Context
import android.media.MediaPlayer

object SoundPlayer {

    private var mPlayer: MediaPlayer? = null

    internal fun playSound(context: Context, name: String) {
        val id = context.resources.getIdentifier(name, "raw", context.packageName)
        if (mPlayer == null) {
            mPlayer = MediaPlayer.create(context, id)
        }
        if (mPlayer!!.isPlaying) mPlayer!!.stop()
        mPlayer!!.start()
    }
}
