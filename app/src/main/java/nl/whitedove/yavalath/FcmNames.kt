package nl.whitedove.yavalath

import android.annotation.SuppressLint
import java.util.*

internal object FcmNames {

    val To = "to"
    val Sender = "sender"
    val Type = "type"
    val UUID = "uuid"
    val Data = "data"
    val Priority = "priority"
    val High = "high"
    val Environment = "environment"
    val AppVersion = "appVersion"
    val Error = "error"
    val Name = "name"
    val FcmToken = "fcmToken"
    val FieldNr = "fieldNr"

    internal enum class ResponseType private constructor(private val value: String) {
        Ping("ping"), Pong("pong"), Ok("OK"), Nok("notOK"), Invite("invite"), Abandon("abandon"), Move("move");

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
