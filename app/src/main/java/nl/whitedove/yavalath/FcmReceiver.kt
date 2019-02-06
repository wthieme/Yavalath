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

        if (!checkMessage(data)) return

        val responseType = data[FcmNames.Type]
        val rt = FcmNames.ResponseType.StringToEnum(responseType!!)

        if (rt === FcmNames.ResponseType.Invite) {
            val toToken = data[FcmNames.Sender]
            val playerName = data[FcmNames.Name]
            FcmSender.mHostToken = toToken!!
            receiveInvite(playerName)
            return
        }

        if (!checkSender(data)) return
        if (rt === FcmNames.ResponseType.Pong) {
            val fromToken = data[FcmNames.Sender]
            receivePong(fromToken)
            return
        }

        if (rt === FcmNames.ResponseType.Ping) {
            val pongData = data[FcmNames.UUID]
            val toToken = data[FcmNames.Sender]
            FcmSender.sendPong(toToken!!, pongData!!)
            return
        }

        if (rt === FcmNames.ResponseType.Nok) {
            val err = data[FcmNames.Error]
            receiveNok(err)
            return
        }

        if (rt === FcmNames.ResponseType.Ok) {
            val guid = data[FcmNames.UUID]
            receiveOk(guid)
            return
        }
    }

    private fun checkMessage(data: Map<String, String>?): Boolean {
        if (data == null || data.size == 0 || data.isEmpty()) return false
        val responseType = data[FcmNames.Type]
        return !responseType.isNullOrEmpty()
    }

    private fun checkSender(data: Map<String, String>?): Boolean {

        val messageOk: Boolean
        if (data == null || data.size == 0 || data.isEmpty()) return false
        val from = data[FcmNames.Sender]
        messageOk = from == FcmSender.mHostToken
        return messageOk
    }

    private fun receiveInvite(playerName: String?) {
        val intent = Intent(FcmNames.ResponseType.Invite.EnumToString())
        intent.putExtra(FcmNames.Name, playerName)
        sendBroadcast(intent)
    }

    private fun receivePong(fromToken: String?) {
        val intent = Intent(FcmNames.ResponseType.Pong.EnumToString())
        sendBroadcast(intent)
    }

    private fun receiveNok(error: String?) {
        val intent = Intent(FcmNames.ResponseType.Nok.EnumToString())
        intent.putExtra(FcmNames.Error, error)
        sendBroadcast(intent)
    }

    private fun receiveOk(guid: String?) {
        val intent = Intent(FcmNames.ResponseType.Ok.EnumToString())
        intent.putExtra(FcmNames.UUID, guid)
        sendBroadcast(intent)
    }
}
