package nl.whitedove.yavalath

import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.game_activity.*

class GameActivity : AppCompatActivity() {
    private var mContext: Context = this

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.game_activity)
        initViews()
        toonGameData()
    }

    private fun initViews() {
    }

    private fun toonGameData() {
        val myName = Helper.getName(this)
        val game = GameHelper.mGame!!
        val movesPlayed = game.movesPlayed()
        if (FcmSender.mMyFcmToken == game.playesWhite) {
            tvPlayerWhite.text = game.myName
            tvPlayerBlack.text = game.hisName
            if (movesPlayed % 2 == 0) {
                tvToMove.text = String.format(getString(R.string.to_move), game.myName)
            } else {
                tvToMove.text = String.format(getString(R.string.to_move), game.hisName)
            }
        } else {
            tvPlayerWhite.text = game.hisName
            tvPlayerBlack.text = game.myName
            if (movesPlayed % 2 == 0) {
                tvToMove.text = String.format(getString(R.string.to_move), game.hisName)
            } else {
                tvToMove.text = String.format(getString(R.string.to_move), game.myName)
            }
        }
    }

    private fun fcmActive() {
        Helper.fcmActive(mContext, tvFcmBolt)
    }
}
