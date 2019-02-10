package nl.whitedove.yavalath

import android.content.Intent
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class FcmReceiver : FirebaseMessagingService() {

    override fun onNewToken(s: String?) {
        super.onNewToken(s)
        if (s != null) Helper.setFcmToken(this, s)
    }

    override fun onMessageReceived(message: RemoteMessage?) {
        val data = message!!.data

        if (!checkMessage(data)) return

        val responseType = data[FcmNames.Type]
        val rt = FcmNames.ResponseType.StringToEnum(responseType!!)

        if (rt === FcmNames.ResponseType.Invite) {
            val toToken = data[FcmNames.Sender] as String
            val playerName = data[FcmNames.Name] as String
            FcmSender.mHisFcmToken = toToken
            receiveInvite(playerName)
            return
        }

        if (!checkSender(data)) return

        if (rt === FcmNames.ResponseType.Pong) {
            receivePong()
            return
        }

        if (rt === FcmNames.ResponseType.Ping) {
            val guid = data[FcmNames.UUID] as String
            val toToken = data[FcmNames.Sender] as String
            FcmSender.sendPong(toToken, guid)
            return
        }

        if (rt === FcmNames.ResponseType.Nok) {
            val err = data[FcmNames.Error] as String
            receiveNok(err)
            return
        }

        if (rt === FcmNames.ResponseType.Ok) {
            val guid = data[FcmNames.UUID] as String
            receiveOk(guid)
            return
        }

        if (rt === FcmNames.ResponseType.Abandon) {
            receiveAbandon()
            return
        }

        if (rt === FcmNames.ResponseType.Move) {
            val fieldS = data[FcmNames.FieldNr] as String
            val fieldNr = fieldS.toInt()
            val fromToken = data[FcmNames.Sender] as String
            receiveMove(fieldNr, fromToken)
            return
        }
    }

    private fun checkMessage(data: Map<String, String>): Boolean {
        if (data.isEmpty()) return false
        val responseType = data[FcmNames.Type]
        return !responseType.isNullOrEmpty()
    }

    private fun checkSender(data: Map<String, String>): Boolean {

        val messageOk: Boolean
        if (data.isEmpty()) return false
        val from = data[FcmNames.Sender]
        messageOk = from == FcmSender.mHisFcmToken
        return messageOk
    }

    private fun receiveInvite(playerName: String) {
        val intent = Intent(FcmNames.ResponseType.Invite.EnumToString())
        intent.putExtra(FcmNames.Name, playerName)
        sendBroadcast(intent)
    }

    private fun receivePong() {
        val intent = Intent(FcmNames.ResponseType.Pong.EnumToString())
        sendBroadcast(intent)
    }

    private fun receiveNok(error: String) {
        val intent = Intent(FcmNames.ResponseType.Nok.EnumToString())
        intent.putExtra(FcmNames.Error, error)
        sendBroadcast(intent)
    }

    private fun receiveOk(guid: String) {
        val intent = Intent(FcmNames.ResponseType.Ok.EnumToString())
        intent.putExtra(FcmNames.UUID, guid)
        sendBroadcast(intent)
    }

    private fun receiveAbandon() {
        val intent = Intent(FcmNames.ResponseType.Abandon.EnumToString())
        sendBroadcast(intent)
    }

    private fun receiveMove(fieldNr: Int, fromToken: String) {
        val game = GameHelper.mGame!!
        game.move(fieldNr, fromToken)
        val intent = Intent(FcmNames.ResponseType.Move.EnumToString())
        sendBroadcast(intent)
    }
}
