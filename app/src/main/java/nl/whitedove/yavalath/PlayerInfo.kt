package nl.whitedove.yavalath

import org.joda.time.DateTime

internal class PlayerInfo(name: String, fcmToken: String, country: String, lastActive : DateTime) {
    var lastActive: DateTime
    var playerState: PlayerState
    var name: String
    var fcmToken: String
    var country: String

    init {
        this.lastActive = lastActive
        this.playerState = PlayerState.Unknown
        this.name = name
        this.fcmToken = fcmToken
        this.country = country
    }
}