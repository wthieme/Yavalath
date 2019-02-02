package nl.whitedove.yavalath

import org.joda.time.DateTime

internal class Game {
    var started : DateTime? = null
    var gameState: GameState = GameState.Unknown
    var fields: List<Field> = ArrayList()
    var myName: String = ""
    var hisName: String = ""
    var myFcmToken: String = ""
    var hisFcmToken: String = ""
}