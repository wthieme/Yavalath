package nl.whitedove.yavalath

import okhttp3.*
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException
import java.util.*

internal object FcmSender {

    private const val mUrl = "https://fcm.googleapis.com/fcm/send"
    private const val mFcmKey = "key=AAAAySj3n-E:APA91bG7nqxcjHqa-VWxDXjafXYd9LW6EhNXn23Ztc6gCXyFTBW7LPBKoOXuDPSeP5flxRC9tYOT6Z_g6mvWT6pBNpNoY2QmlEVMLOKEJDEzt6XUIE83Cm0U2eZSNjvoAzXgMSJeckeP"
    private val JSON = MediaType.parse("application/json; charset=utf-8")
    var mHisFcmToken = ""
    var mMyFcmToken = ""

    @Throws(JSONException::class)
    private fun makePayLoad(serverToken: String, responseType: FcmNames.ResponseType, data: HashMap<*, *>?): RequestBody {
        val json = JSONObject()
        json.put(FcmNames.To, serverToken)
        json.put(FcmNames.Priority, FcmNames.High)
        val jsonData = JSONObject(data)
        jsonData.put(FcmNames.Sender, mMyFcmToken)
        jsonData.put(FcmNames.Type, responseType.EnumToString())
        jsonData.put(FcmNames.Environment, Helper.getAndroidVersion())
        jsonData.put(FcmNames.AppVersion, Helper.getAppVersion())
        json.put(FcmNames.Data, jsonData)

        val js = json.toString()

        return FormBody.create(JSON, js)
    }

    private fun sendMessage(responseType: FcmNames.ResponseType, data: HashMap<*, *>, toToken: String) {
        var body: RequestBody? = null
        try {
            body = makePayLoad(toToken, responseType, data)
        } catch (ignored: JSONException) {
        }

        try {
            val client = OkHttpClient()
            assert(body != null)
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
        val data = HashMap<String, String>()
        data[FcmNames.UUID] = guid
        FcmSender.sendMessage(FcmNames.ResponseType.Ping, data, toToken)
    }

    fun sendAbandon(toToken: String) {
        val data = HashMap<String, String>()
        FcmSender.sendMessage(FcmNames.ResponseType.Abandon, data, toToken)
    }

    fun sendInvite(guid: String, toToken: String, name: String) {
        val data = HashMap<String, String>()
        data[FcmNames.UUID] = guid
        data[FcmNames.Name] = name
        data[FcmNames.FcmToken] = mMyFcmToken
        FcmSender.sendMessage(FcmNames.ResponseType.Invite, data, toToken)
    }

    fun sendOk(guid: String, ToToken: String) {
        val data = HashMap<String, String>()
        data[FcmNames.UUID] = guid
        FcmSender.sendMessage(FcmNames.ResponseType.Ok, data, ToToken)
    }

    fun sendPong(toToken: String, pongData: String) {
        val data = HashMap<String, String>()
        data[FcmNames.UUID] = pongData
        FcmSender.sendMessage(FcmNames.ResponseType.Pong, data, toToken)
    }

    fun sendNok(guid: String, result: String, toToken: String) {
        val data = HashMap<String, String>()
        data[FcmNames.UUID] = guid
        data[FcmNames.Error] = result
        FcmSender.sendMessage(FcmNames.ResponseType.Nok, data, toToken)
    }
}
