package nl.whitedove.yavalath

import android.content.Context

internal object GameHelper {

    var mGame: GameInfo? = null

    fun createGame(context: Context, playerName: String, playesWhite : String) {
        mGame = GameInfo(Helper.getName(context), FcmSender.mMyFcmToken, playerName, FcmSender.mHisFcmToken,playesWhite)
    }
}
