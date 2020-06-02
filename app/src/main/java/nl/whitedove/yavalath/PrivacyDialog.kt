package nl.whitedove.yavalath

import android.app.Dialog
import android.content.Context
import android.os.AsyncTask
import android.os.Bundle
import androidx.core.content.ContextCompat
import android.view.View
import android.view.Window
import android.widget.Button
import android.widget.TextView
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.io.IOException
import java.lang.ref.WeakReference

internal class PrivacyDialog(context: Context) : Dialog(context), View.OnClickListener {

    private val mContext = context
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.privacy_dialog)
        val btOk = findViewById<View>(R.id.btOk) as Button
        btOk.setOnClickListener(this)
        getPrivacyTxtInBackground()
    }

    override fun onClick(v: View?) {
        dismiss()
    }

    private fun getPrivacyTxtInBackground() {
        AsyncGetPrivacyTxt(this).execute(mContext.getString(R.string.PrivacuUrl))
    }

    private class AsyncGetPrivacyTxt internal constructor(pd: PrivacyDialog) : AsyncTask<String, Void, String>() {
        private val activityWeakReference: WeakReference<PrivacyDialog> = WeakReference(pd)

        override fun doInBackground(vararg params: String): String {

            var privtxt = ""
            try {
                val client = OkHttpClient()
                val url = params[0]

                val request = Request.Builder()
                        .url(url)
                        .build()

                val response: Response
                response = client.newCall(request).execute()
                if (response.isSuccessful) privtxt = response.body!!.string()
            } catch (ignored: IOException) {

            }
            return privtxt
        }

        override fun onPostExecute(privacyTxt: String) {
            val pd = activityWeakReference.get() ?: return
            pd.toonPrivacyTxt(privacyTxt)
        }
    }

    private fun toonPrivacyTxt(privacyTxt: String) {
        if (privacyTxt == "") {
            Helper.showMessage(mContext,
                    mContext.getString(R.string.ErrorLoadingPrivacy),
                    ContextCompat.getColor(mContext, R.color.colorLightYellow))
        }
        else {
            val startIndex = privacyTxt.indexOf("<h2>")
            val endIndex = privacyTxt.indexOf("</h2>")
            val toBeReplaced = privacyTxt.substring(startIndex + 1, endIndex)
            val showTxt = privacyTxt.replace(toBeReplaced, "")
            val tvPrivacyText = findViewById<View>(R.id.tvPrivacyText) as TextView
            Helper.setHtmlText(tvPrivacyText, showTxt)
        }
    }
}

