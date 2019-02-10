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
import org.joda.time.Period
import java.util.*

class GameActivity : AppCompatActivity() {
    private var mContext: Context = this
    private var mReceiverAbandon: BroadcastReceiver? = null
    private var mReceiverPong: BroadcastReceiver? = null
    private var mReceiverMove: BroadcastReceiver? = null
    private var mHandler: Handler? = null
    private var mTimer: Timer? = null
    private var mLastPong = DateTime.now()
    private var mGameHandler: Handler? = null
    private var mGameTimer: Timer? = null

    private val runnableOffline: Runnable = Runnable {
        val iconFont = FontManager.GetTypeface(this, FontManager.FONTAWESOME_SOLID)
        FontManager.markAsIconContainer(tvOnline, iconFont)
        val col = ContextCompat.getColor(this, R.color.colorRed)
        tvOnline.setTextColor(col)
        showWaitOrQuitDialog()
    }

    private val runnableUpdateGUI: Runnable = Runnable {
        val game = GameHelper.mGame!!
        val now = DateTime.now()
        val period = Period(game.created, now)
        val seconds = Math.abs(period.seconds).toString().padStart(2, '0')
        val minutes = Math.abs(period.minutes).toString().padStart(2, '0')
        tvTime.text = String.format(getString(R.string.time), minutes, seconds)
    }

    private val runnablePing: Runnable = Runnable {
        pingInBackground()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.game_activity)
        setOnline()
        initTimer()
        initViews()
        initReceivers()
        initGameTimer()
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

    private fun initGameTimer() {
        mGameHandler = Handler()
        mGameTimer = Timer()
        val timer = mGameTimer
        timer!!.schedule(object : TimerTask() {
            override fun run() {
                val game = GameHelper.mGame!!
                if (game.winner.isEmpty())
                    UpdateGUI()
                else
                    this.cancel()
            }
        }, 0, 1000)
    }

    private fun UpdateGUI() {
        mGameHandler!!.post(runnableUpdateGUI)
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
                pinging()
            }
        }, 0, 10000)
    }

    private fun setOffline() {
        mHandler!!.post(runnableOffline)
    }

    private fun pinging() {
        mHandler!!.post(runnablePing)
    }

    private fun toonGameData() {
        val game = GameHelper.mGame!!
        tvPlayerWhite.text = game.playerWhite
        tvPlayerBlack.text = game.playerBlack

        val movesPlayed = game.movesPlayed()
        if (movesPlayed == 0)
            tvNrMoves.text = ""
        else
            tvNrMoves.text = movesPlayed.toString()

        val winner = game.winner
        if (winner.isEmpty()) {
            tvToMove.text = String.format(getString(R.string.to_move), game.playerToMove)
        } else {
            tvToMove.text = String.format(getString(R.string.wins), winner)
            if (game.winningFields.size == 3) {
                for (field in game.winningFields) {
                    // TODO highlight the fields with red
                }
            } else {
                for (field in game.winningFields) {
                    // TODO highlight the fields with green
                }
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

        if (mReceiverMove == null) {
            mReceiverMove = object : BroadcastReceiver() {
                override fun onReceive(context: Context, intent: Intent) {
                    fcmActive()
                    setOnline()
                    toonGameData()
                }
            }
            registerReceiver(mReceiverMove, IntentFilter(FcmNames.ResponseType.Move.EnumToString()))
        }
    }

    private fun move(fieldNr: Int) {
        val game = GameHelper.mGame!!
        game.move(fieldNr, FcmSender.mMyFcmToken)
        moveInBackground(fieldNr)
        toonGameData()
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
        Helper.unRegisterReceiver(mContext, mReceiverMove)
        mReceiverAbandon = null
        mReceiverPong = null
        mReceiverMove = null
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
        fcmActive()
        AsyncPingInBackgroundTask().execute()
    }

    private class AsyncPingInBackgroundTask internal constructor() : AsyncTask<Void, Void, Void>() {

        override fun doInBackground(vararg params: Void): Void? {
            val guid = UUID.randomUUID().toString()
            FcmSender.sendPing(guid, FcmSender.mHisFcmToken)
            return null
        }
    }

    private fun moveInBackground(fieldNr: Int) {
        fcmActive()
        AsyncMoveInBackgroundTask().execute(fieldNr)
    }

    private class AsyncMoveInBackgroundTask internal constructor() : AsyncTask<Int, Void, Void>() {

        override fun doInBackground(vararg params: Int?): Void? {
            val fieldNr = params[0]!!
            FcmSender.sendMove(fieldNr, FcmSender.mHisFcmToken)
            return null
        }
    }
}
