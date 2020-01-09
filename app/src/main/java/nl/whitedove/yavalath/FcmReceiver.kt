package nl.whitedove.yavalath

import android.content.Intent
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class FcmReceiver : FirebaseMessagingService() {

    override fun onNewToken(s: String) {
        super.onNewToken(s)
        if (!s.isBlank()) Helper.setFcmToken(this, s)
    }

    override fun onMessageReceived(message: RemoteMessage) {
        val data = message.data

        if (!checkMessage(data)) return

        val responseType = data[FcmNames.Type]
        val rt = FcmNames.ResponseType.stringToEnum(responseType!!)

        if (rt === FcmNames.ResponseType.Invite) {
            val toToken = data[FcmNames.Sender] as String
            val guid = data[FcmNames.UUID] as String
            val playerName = data[FcmNames.Name] as String
            FcmSender.mHisFcmToken = toToken
            receiveInvite(guid, playerName)
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

        if (rt === FcmNames.ResponseType.InviteNOk) {
            val guid = data[FcmNames.UUID] as String
            receiveInviteNOk(guid)
            return
        }

        if (rt === FcmNames.ResponseType.InviteOk) {
            val guid = data[FcmNames.UUID] as String
            receiveInviteOk(guid)
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

        if (rt === FcmNames.ResponseType.ReadyNewGame) {
            val ready = data[FcmNames.Ready] as String
            val isReady = ready == "Y"
            val fromToken = data[FcmNames.Sender] as String
            receiveReadynewGame(isReady, fromToken)
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

    private fun receiveInvite(guid: String, playerName: String) {
        val intent = Intent(FcmNames.ResponseType.Invite.enumToString())
        intent.putExtra(FcmNames.UUID, guid)
        intent.putExtra(FcmNames.Name, playerName)
        sendBroadcast(intent)
    }

    private fun receivePong() {
        val intent = Intent(FcmNames.ResponseType.Pong.enumToString())
        sendBroadcast(intent)
    }

    private fun receiveInviteNOk(guid : String) {
        val intent = Intent(FcmNames.ResponseType.InviteNOk.enumToString())
        intent.putExtra(FcmNames.UUID, guid)
        sendBroadcast(intent)
    }

    private fun receiveInviteOk(guid: String) {
        val intent = Intent(FcmNames.ResponseType.InviteOk.enumToString())
        intent.putExtra(FcmNames.UUID, guid)
        sendBroadcast(intent)
    }

    private fun receiveAbandon() {
        val intent = Intent(FcmNames.ResponseType.Abandon.enumToString())
        sendBroadcast(intent)
    }

    private fun receiveMove(fieldNr: Int, fromToken: String) {
        val game = GameHelper.mGame!!
        game.move(fieldNr, fromToken)
        val intent = Intent(FcmNames.ResponseType.Move.enumToString())
        sendBroadcast(intent)
    }

    private fun receiveReadynewGame(ready: Boolean, fromToken: String) {
        val game = GameHelper.mGame!!
        game.ready(ready, fromToken)
        val intent = Intent(FcmNames.ResponseType.ReadyNewGame.enumToString())
        sendBroadcast(intent)
    }
}
