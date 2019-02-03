package nl.whitedove.yavalath

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.Window
import android.widget.Button
import android.widget.TextView

internal class AboutDialog(ctx: Context) : Dialog(ctx), View.OnClickListener {

    override fun onCreate(savedInstanceState: Bundle) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.about_dialog)
        val btOk = findViewById<Button>(R.id.btOk)
        btOk.setOnClickListener(this)
        val tvVersionNr = findViewById<TextView>(R.id.tvVersionNr)
        tvVersionNr.text = Helper.getAppVersion()
    }

    override fun onClick(v: View) {
        dismiss()
    }

}

