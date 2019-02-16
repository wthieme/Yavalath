package nl.whitedove.yavalath

import android.content.Context
import android.media.MediaPlayer

object SoundPlayer {

    private var mPlayer: MediaPlayer? = null

    internal fun playSound(context: Context, name: String) {
        if (mPlayer != null && mPlayer!!.isPlaying) mPlayer!!.stop()
        val id = context.resources.getIdentifier(name, "raw", context.packageName)
        mPlayer = MediaPlayer.create(context, id)
        mPlayer!!.start()
    }
}
