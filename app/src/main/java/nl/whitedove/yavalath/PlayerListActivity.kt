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
import android.widget.TextView
import kotlinx.android.synthetic.main.player_list_activity.*
import java.util.*


class PlayerListActivity : AppCompatActivity() {
    private var mReceiverInvite: BroadcastReceiver? = null
    private var mReceiverNOk: BroadcastReceiver? = null
    private var mReceiverOk: BroadcastReceiver? = null
    private var mHandler: Handler? = null
    private var mContext: Context = this
    private var mInviteCount: Int = 0
    private var mInvited: String = ""
    private var mInvitedName: String = ""
    private var mInviteTimeout: String = ""
    private var mTimer: Timer? = null

    private val runnableInviting: Runnable = Runnable {
        fcmActive()
        val aantal = Database.TIMEOUT - mInviteCount
        val s = aantal.toString()
        tvInviting.text = String.format(mInvited + " %s", s)
    }

    private val runnableTimeOut: Runnable = Runnable {
        tvInviting.text = mInviteTimeout
        tvInviting.setTextColor(ContextCompat.getColor(mContext, R.color.colorTint))
        mInviteCount = 0
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mContext = this
        setContentView(R.layout.player_list_activity)
        initReceivers()
        initViews()
        initDb()
        getPlayers()
    }

    override fun onResume() {
        super.onResume()
        initReceivers()
        getPlayers()
    }

    public override fun onPause() {
        super.onPause()
        unregBroadcastReceivers()
    }

    override fun onBackPressed() {
        goback()
    }

    private fun goback() {
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        this.startActivity(intent)
        this.overridePendingTransition(R.anim.slide_enter, R.anim.slide_exit)
        this.finish()
    }

    private fun initViews() {
        btnBack.setOnClickListener { goback() }
        val iconFont = FontManager.GetTypeface(this, FontManager.FONTAWESOME_SOLID)
        FontManager.setIconAndText(btnBack,
                iconFont,
                getString(R.string.fa_arrow_left),
                ContextCompat.getColor(this, R.color.colorIcon),
                Typeface.DEFAULT,
                getString(R.string.back),
                ContextCompat.getColor(this, R.color.colorPrimary))
    }

    private fun initDb() {
        Database.setListener(Runnable { getPlayers() })
    }

    private fun fcmActive() {
        Helper.fcmActive(mContext, findViewById<View>(R.id.tvFcmBolt) as TextView)
    }

    private fun getPlayers() {
        fcmActive()
        Database.getPlayers(Runnable { toonPlayerList(Database.mPlayers) })
    }

    private fun gotoGame(playerName: String, playesWhite : String) {
        fcmActive()
        GameHelper.createGame(this, playerName, playesWhite)
        Database.deletePlayer(FcmSender.mMyFcmToken)
        val intent = Intent(this, GameActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        this.startActivity(intent)
        this.overridePendingTransition(R.anim.slide_in, R.anim.slide_out)
        this.finish()
    }

    private fun showYesNoDialog(playerName: String) {
        val title = String.format(getString(R.string.InviteText), playerName)
        val builder = AlertDialog.Builder(this)
        builder.setMessage(title)
                .setCancelable(false)
                .setPositiveButton(getString(R.string.Yes), { _, _ -> answerYes(playerName) })
                .setNegativeButton(getString(R.string.No), { _, _ -> answerNo() })
        val alert = builder.create()
        alert.show()
    }

    private fun answerYes(playerName: String) {
        okInBackground()
        gotoGame(playerName, FcmSender.mHisFcmToken)
    }

    private fun answerOk() {
        getPlayers()
    }

    private fun answerNo() {
        val notAccepted = String.format(getString(R.string.invite_not_accepted), Helper.getName(this))
        nokInBackground(notAccepted)
    }

    private fun initReceivers() {
        if (mReceiverInvite == null) {
            mReceiverInvite = object : BroadcastReceiver() {
                override fun onReceive(context: Context, intent: Intent) {
                    fcmActive()
                    val playername = intent.getStringExtra(FcmNames.Name)
                    showYesNoDialog(playername)
                }
            }
            registerReceiver(mReceiverInvite, IntentFilter(FcmNames.ResponseType.Invite.EnumToString()))
        }

        if (mReceiverOk == null) {
            mReceiverOk = object : BroadcastReceiver() {
                override fun onReceive(context: Context, intent: Intent) {
                    fcmActive()
                    tvInviting.text = ""
                    val timer = mTimer
                    if (timer != null) {
                        timer.cancel()
                        mInviteCount = 0
                    }
                    gotoGame(mInvitedName, FcmSender.mMyFcmToken)
                }
            }
            registerReceiver(mReceiverOk, IntentFilter(FcmNames.ResponseType.Ok.EnumToString()))
        }

        if (mReceiverNOk == null) {
            mReceiverNOk = object : BroadcastReceiver() {
                override fun onReceive(context: Context, intent: Intent) {
                    fcmActive()
                    tvInviting.text = ""
                    val timer = mTimer
                    if (timer != null) {
                        timer.cancel()
                        mInviteCount = 0
                    }

                    val err = intent.getStringExtra(FcmNames.Error)
                    val builder = AlertDialog.Builder(mContext)
                    builder.setMessage(err)
                            .setCancelable(false)
                            .setPositiveButton(getString(R.string.OK)) { _, _ -> answerOk() }

                    val alert = builder.create()
                    alert.show()
                }
            }
            registerReceiver(mReceiverNOk, IntentFilter(FcmNames.ResponseType.Nok.EnumToString()))
        }
    }

    private fun unregBroadcastReceivers() {
        Helper.unRegisterReceiver(mContext, mReceiverInvite)
        Helper.unRegisterReceiver(mContext, mReceiverOk)
        Helper.unRegisterReceiver(mContext, mReceiverNOk)
        mReceiverInvite = null
        mReceiverOk = null
        mReceiverNOk = null
    }

    private fun toonPlayerList(players: ArrayList<PlayerInfo>) {

        if (players.size > 0) {
            lvPlayers.visibility = View.VISIBLE
            tvNoPlayers.visibility = View.GONE
            lvPlayers.adapter = CustomListAdapterPlayers(this, players)
            lvPlayers.setOnItemClickListener { parent, _, position, _ ->
                val player = parent.getItemAtPosition(position) as PlayerInfo
                invitePlayer(player)
            }
        } else {
            tvNoPlayers.visibility = View.VISIBLE
            lvPlayers.visibility = View.GONE
        }
    }

    private fun invitePlayer(player: PlayerInfo) {
        if (mInviteCount != 0) return
        if (!Helper.testInternet(mContext)) return
        if (player.fcmToken == FcmSender.mMyFcmToken) {
            val builder = AlertDialog.Builder(this)
            builder.setMessage(getString(R.string.cant_play_self))
                    .setCancelable(false)
                    .setPositiveButton(getString(R.string.OK)) { _, _ -> answerOk() }
            val alert = builder.create()
            alert.show()
            return
        }

        val myName = Helper.getName(this)
        if (player.name == myName) {
            val builder = AlertDialog.Builder(this)
            builder.setMessage(getString(R.string.cant_play_same_name))
                    .setCancelable(false)
                    .setPositiveButton(getString(R.string.OK)) { _, _ -> answerOk() }
            val alert = builder.create()
            alert.show()
            return
        }

        FcmSender.mHisFcmToken = player.fcmToken
        fcmActive()
        showInviteText(player.name)
        inviteInBackground(Helper.getName(mContext))
    }

    private fun showInviteText(name: String) {
        mInvited = String.format(getString(R.string.Inviting), name)
        mInvitedName = name
        tvInviting.text = mInvited
        mInviteTimeout = String.format(getString(R.string.InviteTimeOut), name)
        tvInviting.setTextColor(ContextCompat.getColor(mContext, R.color.colorPrimary))

        mHandler = Handler()
        mInviteCount = 0

        mTimer = Timer()
        val timer = mTimer
        timer!!.schedule(object : TimerTask() {
            override fun run() {
                mInviteCount++
                updateGUI()
                if (mInviteCount > Database.TIMEOUT) {
                    timedOut()
                    this.cancel()
                }
            }
        }, 0, 1000)
    }

    private fun updateGUI() {
        mHandler!!.post(runnableInviting)
    }

    private fun timedOut() {
        mHandler!!.post(runnableTimeOut)
    }

    private fun inviteInBackground(playerName: String) {
        AsyncInviteInBackgroundTask().execute(playerName)
    }

    private class AsyncInviteInBackgroundTask internal constructor() : AsyncTask<String, Void, Void>() {

        override fun doInBackground(vararg params: String): Void? {
            val playerName = params[0]
            val guid = UUID.randomUUID().toString()
            mGuid = guid
            FcmSender.sendInvite(guid, FcmSender.mHisFcmToken, playerName)
            return null
        }
    }

    private fun okInBackground() {
        AsyncOkInBackgroundTask().execute()
    }

    private class AsyncOkInBackgroundTask internal constructor() : AsyncTask<Void, Void, Void>() {

        override fun doInBackground(vararg params: Void): Void? {
            val guid = mGuid
            FcmSender.sendOk(guid, FcmSender.mHisFcmToken)
            return null
        }
    }

    private fun nokInBackground(notAccepted: String) {
        AsyncNokInBackgroundTask().execute(notAccepted)
    }

    private class AsyncNokInBackgroundTask internal constructor() : AsyncTask<String, Void, Void>() {

        override fun doInBackground(vararg params: String): Void? {
            val result = params[0]
            val guid = mGuid
            FcmSender.sendNok(guid, result, FcmSender.mHisFcmToken)
            return null
        }
    }

    companion object {
        private var mGuid: String = ""
    }
}

