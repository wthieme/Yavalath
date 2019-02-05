package nl.whitedove.yavalath

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.Window
import android.webkit.WebView
import android.widget.Button

internal class TermsDialog(private val mContext: Context) : Dialog(mContext), View.OnClickListener {

    override fun onCreate(savedInstanceState: Bundle) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.terms_dialog)
        val btOk = findViewById<Button>(R.id.btOk)
        btOk.setOnClickListener(this)
        GetTermsTxt()
    }

    override fun onClick(v: View) {
        dismiss()
    }

    private fun GetTermsTxt() {
        if (!Helper.testInternet(mContext)) return
        val wvTerms = findViewById<WebView>(R.id.wvTerms)
        wvTerms.loadUrl(Database.TermsUrl)
    }
}

