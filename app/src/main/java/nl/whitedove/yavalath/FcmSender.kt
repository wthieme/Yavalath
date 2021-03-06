package nl.whitedove.yavalath

import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException
import java.util.*

internal object FcmSender {

    private const val mUrl = "https://fcm.googleapis.com/fcm/send"
    private const val mFcmKey = "key=AAAAySj3n-E:APA91bG7nqxcjHqa-VWxDXjafXYd9LW6EhNXn23Ztc6gCXyFTBW7LPBKoOXuDPSeP5flxRC9tYOT6Z_g6mvWT6pBNpNoY2QmlEVMLOKEJDEzt6XUIE83Cm0U2eZSNjvoAzXgMSJeckeP"
    private val JSON = "application/json; charset=utf-8".toMediaTypeOrNull()
    var mHisFcmToken = ""
    var mMyFcmToken = ""

    @Throws(JSONException::class)
    private fun makePayLoad(serverToken: String, responseType: FcmNames.ResponseType, data: HashMap<String, Any>): RequestBody {
        val json = JSONObject()
        json.put(FcmNames.To, serverToken)
        json.put(FcmNames.Priority, FcmNames.High)
        val jsonData = JSONObject(data as Map<*, *>)
        jsonData.put(FcmNames.Sender, mMyFcmToken)
        jsonData.put(FcmNames.Type, responseType.enumToString())
        json.put(FcmNames.Data, jsonData)

        val js = json.toString()

        return js.toRequestBody(JSON)
    }

    private fun sendMessage(responseType: FcmNames.ResponseType, data: HashMap<String, Any>, toToken: String) {
        var body: RequestBody? = null
        try {
            body = makePayLoad(toToken, responseType, data)
        } catch (ignored: JSONException) {
        }

        try {
            val client = OkHttpClient()
            val request = Request.Builder()
                    .url(mUrl)
                    .addHeader("Authorization", mFcmKey)
                    .addHeader("Content-Type", "application/json; charset=UTF-8")
                    .post(body!!)
                    .build()

            client.newCall(request).execute()

        } catch (ignored: IOException) {
        }
    }

    fun sendPing(guid: String, toToken: String) {
        val data = HashMap<String, Any>()
        data[FcmNames.UUID] = guid
        sendMessage(FcmNames.ResponseType.Ping, data, toToken)
    }

    fun sendAbandon(toToken: String) {
        val data = HashMap<String, Any>()
        data[FcmNames.UUID] = UUID.randomUUID().toString()
        sendMessage(FcmNames.ResponseType.Abandon, data, toToken)
    }

    fun sendInvite(guid: String, toToken: String, name: String) {
        val data = HashMap<String, Any>()
        data[FcmNames.UUID] = guid
        data[FcmNames.Name] = name
        sendMessage(FcmNames.ResponseType.Invite, data, toToken)
    }

    fun sendInviteOk(guid: String, ToToken: String) {
        val data = HashMap<String, Any>()
        data[FcmNames.UUID] = guid
        sendMessage(FcmNames.ResponseType.InviteOk, data, ToToken)
    }

    fun sendPong(toToken: String, pongData: String) {
        val data = HashMap<String, Any>()
        data[FcmNames.UUID] = pongData
        sendMessage(FcmNames.ResponseType.Pong, data, toToken)
    }

    fun sendInviteNok(guid: String, toToken: String) {
        val data = HashMap<String, Any>()
        data[FcmNames.UUID] = guid
        sendMessage(FcmNames.ResponseType.InviteNOk, data, toToken)
    }

    fun sendMove(fieldNr: Int, toToken: String) {
        val data = HashMap<String, Any>()
        data[FcmNames.UUID] = UUID.randomUUID().toString()
        data[FcmNames.FieldNr] = fieldNr
        sendMessage(FcmNames.ResponseType.Move, data, toToken)
    }

    fun sendReadyNewGame(ready: String, toToken: String) {
        val data = HashMap<String, Any>()
        data[FcmNames.UUID] = UUID.randomUUID().toString()
        data[FcmNames.Ready] = ready
        sendMessage(FcmNames.ResponseType.ReadyNewGame, data, toToken)
    }

}
