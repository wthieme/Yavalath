package nl.whitedove.yavalath

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.Window
import android.widget.Button

internal class RulesDialog(ctx: Context) : Dialog(ctx), View.OnClickListener {

    override fun onCreate(savedInstanceState: Bundle) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.rules_dialog)
        val btOk = findViewById<Button>(R.id.btOk)
        btOk.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        dismiss()
    }
}

