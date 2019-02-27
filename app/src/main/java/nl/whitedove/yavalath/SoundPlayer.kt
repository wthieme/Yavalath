package nl.whitedove.yavalath

import android.content.Context
import android.media.MediaPlayer

object SoundPlayer {

    private var mPlayer: MediaPlayer? = null

    internal fun playSound(context: Context, name: String) {
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
