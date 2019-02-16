package nl.whitedove.yavalath

import android.annotation.SuppressLint
import java.util.*

internal object FcmNames {

    const val To = "to"
    const val Sender = "sender"
    const val Type = "type"
    const val UUID = "uuid"
    const val Data = "data"
    const val Priority = "priority"
    const val High = "high"
    const val Error = "error"
    const val Name = "name"
    const val FcmToken = "fcmToken"
    const val FieldNr = "fieldNr"
    const val Ready = "ready"

    internal enum class ResponseType(private val value: String) {
        Ping("ping"), Pong("pong"), InviteOk("inviteOk"), InviteNOk("inviteNOk"),
        Invite("invite"), Abandon("abandon"), Move("move"), ReadyNewGame("readyNewGame");

        fun enumToString(): String {
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

            fun stringToEnum(responseType: String): ResponseType? {
                return map[responseType]
            }
        }
    }
}
