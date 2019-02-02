package nl.whitedove.yavalath

import android.annotation.SuppressLint

import java.util.HashMap

internal object FcmNames {

    val To = "to"
    val Sender = "sender"
    val Type = "type"
    val UUID = "uuid"
    val Data = "data"
    val latency = "latency"
    val Priority = "priority"
    val UserName = "userName"
    val High = "high"
    val Environment = "environment"
    val AppVersion = "appVersion"
    val Error = "error"
    val PassCode = "passCode"
    val GameInfo = "gameInfo"
    val Name = "name"
    val State = "state"
    val PassHash = "passHash"
    val IsPasswordProtected = "IsPasswordProtected"
    val Players = "players"
    val FcmToken = "fcmToken"
    val IsHost = "isHost"
    val IsInGame = "isInGame"
    val Gender = "gender"
    val HostToken = "hostToken"
    val Id = "id"

    internal enum class ResponseType private constructor(private val value: String) {
        Ping("ping?"), Pong("ping!"), Ok("OK"), JoinGame("joinGame"), LeaveGame("leaveGame"),
        Nok("notOK"), GameDisband("gameDisband"), Pause("pause"), UnPause("unpause"),
        GameInfo("gameInfo"), comingFromBackground("comingFromBackground"), goingInBackground("goingInBackground"),
        RequestGameInfo("requestGameInfo"), ReadyToStart("readyToStart");

        fun EnumToString(): String {
            return value
        }

        companion object {

            @SuppressLint("UseSparseArrays")
            private val map = HashMap<String, ResponseType>()

            init {
                for (responseType in ResponseType.values()) {
                    map[responseType.value] = responseType
                }
            }

            fun StringToEnum(responseType: String): ResponseType? {
                return map[responseType]
            }
        }
    }
}
