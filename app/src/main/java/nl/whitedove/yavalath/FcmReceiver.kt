package nl.whitedove.yavalath

import android.content.Intent
import android.util.Log

import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

import org.joda.time.DateTime

class FcmReceiver : FirebaseMessagingService() {

    override fun onNewToken(s: String?) {
        super.onNewToken(s)
        if (s!=null) Helper.setFcmToken(this, s)
    }

    override fun onMessageReceived(message: RemoteMessage?) {
        val data = message!!.data

        if (!CheckMessage(data)) return

        val responseType = data[FcmNames.Type]
        val rt = FcmNames.ResponseType.StringToEnum(responseType!!)

        if (rt === FcmNames.ResponseType.Pong) {
            val fromToken = data[FcmNames.Sender]
            ReceivePong(fromToken)
            return
        }

        if (rt === FcmNames.ResponseType.Ping) {
            val pongData = data[FcmNames.UUID]
            val toToken = data[FcmNames.Sender]
            FcmSender.SendPong(toToken!!, pongData!!)
            return
        }

        if (rt === FcmNames.ResponseType.Nok) {
            val err = data[FcmNames.Error]
            ReceiveNok(err)
            return
        }

        if (rt === FcmNames.ResponseType.Ok) {
            val guid = data[FcmNames.UUID]
            ReceiveOk(guid)
            return
        }
    }

    private fun CheckMessage(data: Map<String, String>?): Boolean {

        val messageOk: Boolean
        if (data == null || data.size == 0 || data.isEmpty()) return false
        val from = data[FcmNames.Sender]
        messageOk = from == FcmSender.mHostToken
        return messageOk
    }

    private fun ReceivePong(fromToken: String?) {
        val intent = Intent(FcmNames.ResponseType.GameInfo.EnumToString())
        sendBroadcast(intent)
    }

    private fun ReceiveNok(error: String?) {
        val intent = Intent(FcmNames.ResponseType.Nok.EnumToString())
        intent.putExtra(FcmNames.Error, error)
        sendBroadcast(intent)
    }

    private fun ReceiveOk(guid: String?) {
        val intent = Intent(FcmNames.ResponseType.Ok.EnumToString())
        intent.putExtra(FcmNames.UUID, guid)
        sendBroadcast(intent)
    }

    private fun Paused() {
        val intent = Intent(FcmNames.ResponseType.Pause.EnumToString())
        sendBroadcast(intent)
    }

    private fun Resumed() {
        val intent = Intent(FcmNames.ResponseType.UnPause.EnumToString())
        sendBroadcast(intent)
    }

    private fun Kicked(reason: String?) {
        val intent = Intent(FcmNames.ResponseType.GameDisband.EnumToString())
        intent.putExtra(FcmNames.Error, reason)
        sendBroadcast(intent)
    }
}
