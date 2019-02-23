package nl.whitedove.yavalath

import android.content.Context

internal object GameHelper {

    var mGame: GameInfo? = null
    var mPointsWhite = 0
    var mPointsBlack = 0

    fun createGame(context: Context, playerName: String, playesWhite: String) {
        mGame = GameInfo(Helper.getName1(context), FcmSender.mMyFcmToken, playerName, FcmSender.mHisFcmToken, playesWhite, GameMode.HumanVsHumanInternet, GameLevel.Easy)
        mPointsBlack = 0
        mPointsWhite = 0
    }
}
