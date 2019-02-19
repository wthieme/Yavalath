package nl.whitedove.yavalath

import android.content.Context

internal object GameHelper {

    var mGame: GameInfo? = null
    var mPointsWhite = 0
    var mPointsBlack = 0

    fun createGame(context: Context, playerName: String, playesWhite: String) {
        mGame = GameInfo(Helper.getName(context), FcmSender.mMyFcmToken, playerName, FcmSender.mHisFcmToken, playesWhite)
        mPointsBlack = 0
        mPointsWhite = 0
    }
}
