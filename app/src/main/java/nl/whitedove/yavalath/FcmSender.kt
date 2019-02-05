package nl.whitedove.yavalath

import android.os.AsyncTask
import android.util.Pair

import org.joda.time.DateTime
import org.json.JSONException
import org.json.JSONObject

import java.io.IOException
import java.util.ArrayList
import java.util.HashMap
import java.util.UUID

import okhttp3.FormBody
import okhttp3.MediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody

internal object FcmSender {

    private val mUrl = "https://fcm.googleapis.com/fcm/send"
    private val mFcmKey = "key=AAAAySj3n-E:APA91bG7nqxcjHqa-VWxDXjafXYd9LW6EhNXn23Ztc6gCXyFTBW7LPBKoOXuDPSeP5flxRC9tYOT6Z_g6mvWT6pBNpNoY2QmlEVMLOKEJDEzt6XUIE83Cm0U2eZSNjvoAzXgMSJeckeP"
    private val JSON = MediaType.parse("application/json; charset=utf-8")
    var mLastSend = DateTime.now()
    var mHostToken = ""
    var mFcmToken = ""

    @Throws(JSONException::class)
    private fun makePayLoad(serverToken: String, responseType: FcmNames.ResponseType, data: HashMap<*, *>?): RequestBody {
        val json = JSONObject()
        json.put(FcmNames.To, serverToken)
        json.put(FcmNames.Priority, FcmNames.High)
        val jsonData = JSONObject(data)
        jsonData.put(FcmNames.Sender, mFcmToken)
        jsonData.put(FcmNames.Type, responseType.EnumToString())
        jsonData.put(FcmNames.Environment, Helper.getAndroidVersion())
        jsonData.put(FcmNames.AppVersion, Helper.getAppVersion())
        json.put(FcmNames.Data, jsonData)

        val js = json.toString()

        return FormBody.create(JSON, js)
    }

    fun SendMessageToHost(responseType: FcmNames.ResponseType, data: HashMap<*, *>) {
        SendMessage(responseType, data, null)
    }

    fun SendMessage(responseType: FcmNames.ResponseType, data: HashMap<*, *>, toToken: String?) {

        val serverToken = toToken ?: mHostToken
        var body: RequestBody? = null
        try {
            body = makePayLoad(serverToken, responseType, data)
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

    internal class SendSyncPing : AsyncTask<String, Void, Void>() {
        override fun doInBackground(vararg params: String): Void? {
            val toToken = params[0]
            val data = HashMap<String, String>()
            FcmSender.SendMessage(FcmNames.ResponseType.Ping, data, toToken)
            return null
        }
    }

    fun SendOk(guid: String, ToToken: String) {
        val data = HashMap<String, String>()
        data[FcmNames.UUID] = guid
        FcmSender.SendMessage(FcmNames.ResponseType.Ok, data, ToToken)
    }

    fun SendPong(toToken: String, pongData: String) {
        val data = HashMap<String, String>()
        data[FcmNames.UUID] = pongData
        FcmSender.SendMessage(FcmNames.ResponseType.Pong, data, toToken)
    }

    fun SendNok(guid: String, result: String, toToken: String) {
        val data = HashMap<String, String>()
        data[FcmNames.UUID] = guid
        data[FcmNames.Error] = result
        FcmSender.SendMessage(FcmNames.ResponseType.Nok, data, toToken)
    }
}
