package nl.whitedove.yavalath

import org.joda.time.DateTime

internal class PlayerInfo(name: String, fcmToken: String, country: String) {
    var lastActive: DateTime
    var playerState: PlayerState
    var name: String
    var fcmToken: String
    var country: String

    init {
        this.lastActive = DateTime.now()
        this.playerState = PlayerState.Unknown
        this.name = name
        this.fcmToken = fcmToken
        this.country = country
    }
}