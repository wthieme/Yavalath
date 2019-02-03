package nl.whitedove.yavalath

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.util.Pair
import android.view.View
import android.view.Window
import android.widget.Button
import android.widget.RelativeLayout
import android.widget.TextView

import java.util.ArrayList

import android.widget.RelativeLayout.TRUE

internal class PlayerInfoDialog(ctx: Context, private val mPlayer: Player, private val mIsHost: Boolean) : Dialog(ctx) {

    override fun onCreate(savedInstanceState: Bundle) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.player_info_dialog)
        val btClose = findViewById(R.id.btClose)
        btClose.setOnClickListener(View.OnClickListener { dismiss() })

        val btKick = findViewById(R.id.btKick)
        if (mIsHost && !mPlayer.getIsHost()) {
            btKick.setVisibility(View.VISIBLE)
            btKick.setOnClickListener(View.OnClickListener {
                Database.LeaveGame(mPlayer.getToken())
                val tokens = ArrayList<String>()
                tokens.add(mPlayer.getToken())

                FcmSender.SendGameDisband().execute(Pair.create(tokens, "You have been kicked!"))
                dismiss()
            })

        } else {
            btKick.setVisibility(View.GONE)
            val lp = btClose.getLayoutParams() as RelativeLayout.LayoutParams
            lp.addRule(RelativeLayout.CENTER_HORIZONTAL, TRUE)
            btClose.setLayoutParams(lp)
        }

        val tvPlayerName = findViewById(R.id.tvPlayerName)
        tvPlayerName.setText(mPlayer.getName())

        val tvOSVersion = findViewById(R.id.tvOSVersion)
        val tvOSVersionNr = findViewById(R.id.tvOSVersionNr)

        val os = mPlayer.getEnvironment().split(" ")
        if (os.size == 2) {
            tvOSVersion.setText(os[0])
            tvOSVersionNr.setText(os[1])
        } else {
            tvOSVersion.setText(mPlayer.getEnvironment())
            tvOSVersionNr.setText("")
        }

        val tvVersionNr = findViewById<TextView>(R.id.tvVersionNr)
        tvVersionNr.setText(mPlayer.getAppVersion())
    }
}

