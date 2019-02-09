package nl.whitedove.yavalath

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.AsyncTask
import android.os.Bundle
import android.os.Handler
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.game_activity.*
import org.joda.time.DateTime
import java.util.*

class GameActivity : AppCompatActivity() {
    private var mContext: Context = this
    private var mReceiverAbandon: BroadcastReceiver? = null
    private var mReceiverPong: BroadcastReceiver? = null
    private var mHandler: Handler? = null
    private var mTimer: Timer? = null
    private var mLastPong = DateTime.now()

    private val runnableOffline: Runnable = Runnable {
        val iconFont = FontManager.GetTypeface(this, FontManager.FONTAWESOME_SOLID)
        FontManager.markAsIconContainer(tvOnline, iconFont)
        val col = ContextCompat.getColor(this, R.color.colorRed)
        tvOnline.setTextColor(col)
        showWaitOrQuitDialog()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.game_activity)
        setOnline()
        initTimer()
        initViews()
        initReceivers()
        toonGameData()
    }

    public override fun onPause() {
        super.onPause()
        unregBroadcastReceivers()
    }

    override fun onBackPressed() {
        goback()
    }

    private fun goback() {
        showQuitDialog()
    }

    private fun initViews() {
    }

    private fun initTimer() {
        mLastPong = DateTime.now()
        mHandler = Handler()
        mTimer = Timer()
        val timer = mTimer
        timer!!.schedule(object : TimerTask() {
            override fun run() {
                if (mLastPong.plusSeconds(15).isBeforeNow) {
                    setOffline()
                    this.cancel()
                }
                pingInBackground()
            }
        }, 0, 10000)
    }

    private fun setOffline() {
        mHandler!!.post(runnableOffline)
    }

    private fun toonGameData() {
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

    private fun showQuitDialog() {
        val title = getString(R.string.AbandonText)
        val builder = AlertDialog.Builder(this)
        builder.setMessage(title)
                .setCancelable(false)
                .setPositiveButton(getString(R.string.Yes)) { _, _ -> answerYes() }
                .setNegativeButton(getString(R.string.No)) { _, _ -> answerNo() }
        val alert = builder.create()
        alert.show()
    }

    private fun answerYes() {
        abandonInBackground()
        gotoMain()
    }

    private fun answerNo() {
    }

    private fun showWaitOrQuitDialog() {
        val game = GameHelper.mGame!!
        val title = String.format(getString(R.string.OfflineText), game.hisName)
        val builder = AlertDialog.Builder(this)
        builder.setMessage(title)
                .setCancelable(false)
                .setPositiveButton(getString(R.string.Wait)) { _, _ -> answerWait() }
                .setNegativeButton(getString(R.string.Quit)) { _, _ -> answerQuit() }
        val alert = builder.create()
        alert.show()
    }

    private fun answerWait() {
        initTimer()
    }

    private fun answerQuit() {
        abandonInBackground()
        gotoMain()
    }

    private fun quitMessage() {
        val game = GameHelper.mGame!!
        val builder = AlertDialog.Builder(this)
        builder.setMessage(String.format(getString(R.string.left_game), game.hisName))
                .setCancelable(false)
                .setPositiveButton(getString(R.string.OK)) { _, _ -> gotoMain() }
        val alert = builder.create()
        alert.show()
    }

    private fun gotoMain() {
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        this.startActivity(intent)
        this.overridePendingTransition(R.anim.slide_enter, R.anim.slide_exit)
        this.finish()
    }

    private fun initReceivers() {
        if (mReceiverAbandon == null) {
            mReceiverAbandon = object : BroadcastReceiver() {
                override fun onReceive(context: Context, intent: Intent) {
                    fcmActive()
                    quitMessage()
                }
            }
            registerReceiver(mReceiverAbandon, IntentFilter(FcmNames.ResponseType.Abandon.EnumToString()))
        }

        if (mReceiverPong == null) {
            mReceiverPong = object : BroadcastReceiver() {
                override fun onReceive(context: Context, intent: Intent) {
                    mLastPong = DateTime.now()
                    fcmActive()
                    setOnline()
                }
            }
            registerReceiver(mReceiverPong, IntentFilter(FcmNames.ResponseType.Pong.EnumToString()))
        }
    }

    private fun setOnline() {
        val iconFont = FontManager.GetTypeface(this, FontManager.FONTAWESOME_SOLID)
        FontManager.markAsIconContainer(tvOnline, iconFont)
        val col = ContextCompat.getColor(this, R.color.colorGreen)
        tvOnline.setTextColor(col)
    }

    private fun unregBroadcastReceivers() {
        Helper.unRegisterReceiver(mContext, mReceiverAbandon)
        Helper.unRegisterReceiver(mContext, mReceiverPong)
        mReceiverAbandon = null
        mReceiverPong = null
    }

    private fun abandonInBackground() {
        fcmActive()
        AsyncAbandonInBackgroundTask().execute()
    }

    private class AsyncAbandonInBackgroundTask internal constructor() : AsyncTask<Void, Void, Void>() {

        override fun doInBackground(vararg params: Void): Void? {
            FcmSender.sendAbandon(FcmSender.mHisFcmToken)
            return null
        }
    }

    private fun pingInBackground() {
        AsyncPingInBackgroundTask().execute()
    }

    private class AsyncPingInBackgroundTask internal constructor() : AsyncTask<Void, Void, Void>() {

        override fun doInBackground(vararg params: Void): Void? {
            val guid = UUID.randomUUID().toString()
            FcmSender.sendPing(guid, FcmSender.mHisFcmToken)
            return null
        }
    }
}
