package nl.whitedove.yavalath

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Typeface
import android.os.AsyncTask
import android.os.Bundle
import android.os.Handler
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.ImageView
import android.widget.Switch
import android.widget.TextView
import com.devs.vectorchildfinder.VectorChildFinder
import kotlinx.android.synthetic.main.game_activity.*
import org.joda.time.DateTime
import org.joda.time.Period
import java.util.*


class GameActivity : AppCompatActivity() {
    private var mContext: Context = this
    private var mReceiverAbandon: BroadcastReceiver? = null
    private var mReceiverPong: BroadcastReceiver? = null
    private var mReceiverMove: BroadcastReceiver? = null
    private var mReceiverReady: BroadcastReceiver? = null
    private var mHandler: Handler? = null
    private var mTimer: Timer? = null
    private var mLastPong = DateTime.now()
    private var mGameHandler: Handler? = null
    private var mGameTimer: Timer? = null

    private val runnableOffline: Runnable = Runnable {
        val iconFont = FontManager.getTypeface(this, FontManager.FONTAWESOME_SOLID)
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
        val hours = Math.abs(period.hours).toString().padStart(2, '0')
        if (hours == "00") {
            tvTime.text = String.format(getString(R.string.timems), minutes, seconds)
        } else {
            tvTime.text = String.format(getString(R.string.timehms), hours, minutes, seconds)
        }
    }

    private val runnablePing: Runnable = Runnable {
        pingInBackground()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.game_activity)
        setOnline()
        initTimer()
        initReceivers()
        initGameTimer()
        showGameData()
    }

    override fun onDestroy() {
        super.onDestroy()
        unregBroadcastReceivers()
    }

    public override fun onPause() {
        super.onPause()
        mHandler = null
        mGameHandler = null
    }

    public override fun onResume() {
        super.onResume()
        initTimer()
        initGameTimer()
    }

    private fun unregBroadcastReceivers() {
        Helper.unRegisterReceiver(mContext, mReceiverAbandon)
        Helper.unRegisterReceiver(mContext, mReceiverPong)
        Helper.unRegisterReceiver(mContext, mReceiverMove)
        Helper.unRegisterReceiver(mContext, mReceiverReady)
        mReceiverAbandon = null
        mReceiverPong = null
        mReceiverMove = null
        mReceiverReady = null
    }

    override fun onBackPressed() {
        goback()
    }

    private fun goback() {
        showQuitDialog()
    }

    private fun initGameTimer() {
        mGameHandler = Handler()
        mGameTimer = Timer()
        val timer = mGameTimer
        timer!!.schedule(object : TimerTask() {
            override fun run() {
                val game = GameHelper.mGame!!
                if (game.gameState == GameState.Running)
                    updateGUI()
                else
                    this.cancel()
            }
        }, 0, 1000)
    }

    private fun updateGUI() {
        if (mGameHandler == null) return
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
        if (mHandler == null) return
        mHandler!!.post(runnableOffline)
    }

    private fun pinging() {
        if (mHandler == null) return
        mHandler!!.post(runnablePing)
    }

    private fun showGameData() {
        val game = GameHelper.mGame!!
        val res = this.resources
        val packname = this.packageName
        val iconFont = FontManager.getTypeface(this, FontManager.FONTAWESOME_SOLID)
        val movesPlayed = game.movesPlayed()
        val myMove = game.myMove()

        tvPlayerWhite.text = String.format(getString(R.string.PlayerWhite), game.playerWhite, GameHelper.mPointsWhite.toString())
        tvPlayerBlack.text = String.format(getString(R.string.PlayerBlack), game.playerBlack, GameHelper.mPointsBlack.toString())

        if (movesPlayed == 0)
            tvNrMoves.text = ""
        else
            tvNrMoves.text = movesPlayed.toString()

        if (game.gameState == GameState.DrawBoardFull || game.gameState == GameState.DrawByWinAndLose) {
            tvReady.visibility = View.VISIBLE
            swReady.visibility = View.VISIBLE
            game.whiteReady = false
            game.blackReady = false
            tvToMove.text = getString(R.string.draw)
            initReadySwitch(swReady, false)
        }
        if (game.gameState == GameState.Running) {
            tvReady.visibility = View.GONE
            swReady.visibility = View.GONE
            tvToMove.text = String.format(getString(R.string.to_move), game.playerToMove)
        }

        if (game.gameState == GameState.WhiteWins || game.gameState == GameState.BlackWins) {
            tvReady.visibility = View.VISIBLE
            swReady.visibility = View.VISIBLE
            game.whiteReady = false
            game.blackReady = false
            tvToMove.text = String.format(getString(R.string.wins), game.winner)
            initReadySwitch(swReady, false)
        }

        btnSend.visibility = View.GONE
        btnSend.setOnClickListener(null)

        for (i in 0..60) {
            val name = "fld" + Integer.toString(i)
            val id = res.getIdentifier(name, "id", packname)
            val fldview = findViewById<View>(id)
            val tvStone = fldview.findViewById<TextView>(R.id.tvStone)
            val ivHexagon = fldview.findViewById<(ImageView)>(R.id.ivHexagon)
            val vector = VectorChildFinder(this, R.drawable.hexagon, ivHexagon)
            val pathHexagon = vector.findPathByName("path_hexagon")
            pathHexagon.fillColor = ContextCompat.getColor(mContext, R.color.colorLightYellow)
            FontManager.markAsIconContainer(tvStone, iconFont)
            val field = game.fields[i]
            tvStone.visibility = View.VISIBLE

            fldview.setOnClickListener(null)

            if (field.fieldState == FieldState.Empty) {
                tvStone.visibility = View.GONE
                if (myMove && game.gameState == GameState.Running) {
                    fldview.setOnClickListener { fieldClick(i) }
                }
            } else {
                if (field.fieldState == FieldState.White) {
                    tvStone.setTextColor(ContextCompat.getColor(mContext, R.color.colorWhite))
                } else if (field.fieldState == FieldState.Black) {
                    tvStone.setTextColor(ContextCompat.getColor(mContext, R.color.colorBlack))
                }
            }
        }

        if (game.lastMove != -1) {
            val name = "fld" + Integer.toString(game.lastMove)
            val id = res.getIdentifier(name, "id", packname)
            val fldview = findViewById<View>(id)
            val ivHexagon = fldview.findViewById<(ImageView)>(R.id.ivHexagon)
            val vector = VectorChildFinder(this, R.drawable.hexagon, ivHexagon)
            val pathHexagon = vector.findPathByName("path_hexagon")
            pathHexagon.fillColor = ContextCompat.getColor(mContext, R.color.colorSelected)
        }

        for (winningRows in game.winningFields5) {
            for (fldNr in winningRows) {
                val name = "fld" + Integer.toString(fldNr)
                val id = res.getIdentifier(name, "id", packname)
                val fldview = findViewById<View>(id)
                val ivHexagon = fldview.findViewById<(ImageView)>(R.id.ivHexagon)
                val vector = VectorChildFinder(this, R.drawable.hexagon, ivHexagon)
                val pathHexagon = vector.findPathByName("path_hexagon")
                pathHexagon.fillColor = ContextCompat.getColor(mContext, R.color.colorLightGreen)
            }
        }

        for (winningRows in game.winningFields4) {
            for (fldNr in winningRows) {
                val name = "fld" + Integer.toString(fldNr)
                val id = res.getIdentifier(name, "id", packname)
                val fldview = findViewById<View>(id)
                val ivHexagon = fldview.findViewById<(ImageView)>(R.id.ivHexagon)
                val vector = VectorChildFinder(this, R.drawable.hexagon, ivHexagon)
                val pathHexagon = vector.findPathByName("path_hexagon")
                pathHexagon.fillColor = ContextCompat.getColor(mContext, R.color.colorLightGreen)
            }
        }

        for (winningRows in game.winningFields3) {
            for (fldNr in winningRows) {
                val name = "fld" + Integer.toString(fldNr)
                val id = res.getIdentifier(name, "id", packname)
                val fldview = findViewById<View>(id)
                val ivHexagon = fldview.findViewById<(ImageView)>(R.id.ivHexagon)
                val vector = VectorChildFinder(this, R.drawable.hexagon, ivHexagon)
                val pathHexagon = vector.findPathByName("path_hexagon")
                pathHexagon.fillColor = ContextCompat.getColor(mContext, R.color.colorLightRed)
            }
        }
    }

    private fun initReadySwitch(swReady: Switch, checked: Boolean) {
        swReady.setOnCheckedChangeListener(null)
        swReady.isChecked = checked
        swReady.setOnCheckedChangeListener { _, isChecked -> ready(isChecked) }
    }

    private fun checkNewGame() {
        val game = GameHelper.mGame!!
        if (!game.whiteReady || !game.blackReady) {
            return
        }

        val token = if (game.playesWhite == game.myFcmToken) {
            game.hisFcmToken
        } else {
            game.myFcmToken
        }

        GameHelper.mGame = GameInfo(game.myName, game.myFcmToken, game.hisName, game.hisFcmToken, token)
        val pointsWhite = GameHelper.mPointsWhite
        val pointsBlack = GameHelper.mPointsBlack
        GameHelper.mPointsWhite = pointsBlack
        GameHelper.mPointsBlack = pointsWhite
        initGameTimer()
        initTimer()
        showGameData()
    }

    private fun ready(isReady: Boolean) {
        val game = GameHelper.mGame!!
        game.ready(isReady, FcmSender.mMyFcmToken)
        if (isReady) {
            checkNewGame()
        }
        startReadyInBackground(isReady)
    }

    private fun fieldClick(fieldNr: Int) {
        val iconFont = FontManager.getTypeface(this, FontManager.FONTAWESOME_SOLID)
        val game = GameHelper.mGame!!
        game.lastMove = fieldNr
        val res = this.resources
        val packname = this.packageName
        val myMove = game.myMove()

        for (i in 0..60) {
            val name = "fld" + Integer.toString(i)
            val id = res.getIdentifier(name, "id", packname)
            val fldview = findViewById<View>(id)
            val tvStone = fldview.findViewById<TextView>(R.id.tvStone)
            val ivHexagon = fldview.findViewById<(ImageView)>(R.id.ivHexagon)
            val vector = VectorChildFinder(this, R.drawable.hexagon, ivHexagon)
            val pathHexagon = vector.findPathByName("path_hexagon")
            pathHexagon.fillColor = ContextCompat.getColor(mContext, R.color.colorLightYellow)

            val field = game.fields[i]
            if (field.fieldState == FieldState.Empty) {
                tvStone.visibility = View.GONE
            }
        }

        val name = "fld" + Integer.toString(fieldNr)
        val id = res.getIdentifier(name, "id", packname)
        val fldview = findViewById<View>(id)
        val tvStone = fldview.findViewById<TextView>(R.id.tvStone)
        tvStone.visibility = View.VISIBLE
        if (game.playerWhite == game.myName) {
            tvStone.setTextColor(ContextCompat.getColor(mContext, R.color.colorWhite))
        } else {
            tvStone.setTextColor(ContextCompat.getColor(mContext, R.color.colorBlack))
        }

        val ivHexagon = fldview.findViewById<(ImageView)>(R.id.ivHexagon)
        val vector = VectorChildFinder(this, R.drawable.hexagon, ivHexagon)
        val pathHexagon = vector.findPathByName("path_hexagon")
        pathHexagon.fillColor = ContextCompat.getColor(mContext, R.color.colorSelected)

        if (myMove && game.gameState == GameState.Running) {
            btnSend.visibility = View.VISIBLE
            FontManager.setIconAndText(btnSend,
                    iconFont,
                    getString(R.string.fa_share_square),
                    ContextCompat.getColor(this, R.color.colorIcon),
                    Typeface.DEFAULT,
                    getString(R.string.send),
                    ContextCompat.getColor(this, R.color.colorPrimary))
            btnSend.setOnClickListener { sendMove() }
        } else {
            btnSend.visibility = View.GONE
            btnSend.setOnClickListener(null)
        }
    }

    private fun sendMove() {
        val game = GameHelper.mGame!!
        val fieldNr = game.lastMove
        game.move(fieldNr, FcmSender.mMyFcmToken)
        showGameData()
        moveInBackground(fieldNr)
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
            registerReceiver(mReceiverAbandon, IntentFilter(FcmNames.ResponseType.Abandon.enumToString()))
        }

        if (mReceiverPong == null) {
            mReceiverPong = object : BroadcastReceiver() {
                override fun onReceive(context: Context, intent: Intent) {
                    mLastPong = DateTime.now()
                    fcmActive()
                    setOnline()
                }
            }
            registerReceiver(mReceiverPong, IntentFilter(FcmNames.ResponseType.Pong.enumToString()))
        }

        if (mReceiverMove == null) {
            mReceiverMove = object : BroadcastReceiver() {
                override fun onReceive(context: Context, intent: Intent) {
                    fcmActive()
                    SoundPlayer.playSound(mContext, "sonar")
                    setOnline()
                    showGameData()
                }
            }
            registerReceiver(mReceiverMove, IntentFilter(FcmNames.ResponseType.Move.enumToString()))
        }

        if (mReceiverReady == null) {
            mReceiverReady = object : BroadcastReceiver() {
                override fun onReceive(context: Context, intent: Intent) {
                    fcmActive()
                    setOnline()
                    checkNewGame()
                }
            }
            registerReceiver(mReceiverReady, IntentFilter(FcmNames.ResponseType.ReadyNewGame.enumToString()))
        }
    }

    private fun setOnline() {
        val iconFont = FontManager.getTypeface(this, FontManager.FONTAWESOME_SOLID)
        FontManager.markAsIconContainer(tvOnline, iconFont)
        val col = ContextCompat.getColor(this, R.color.colorGreen)
        tvOnline.setTextColor(col)
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

    private fun startReadyInBackground(isReady: Boolean) {
        fcmActive()
        AsyncReadyInBackgroundTask().execute(isReady)
    }

    private class AsyncReadyInBackgroundTask internal constructor() : AsyncTask<Boolean, Void, Void>() {

        override fun doInBackground(vararg params: Boolean?): Void? {
            val isReady = params[0]!!
            val ready = if (isReady) "Y" else "N"
            FcmSender.sendReadyNewGame(ready, FcmSender.mHisFcmToken)
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
