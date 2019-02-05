package nl.whitedove.yavalath

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Typeface
import android.os.Bundle
import android.os.Handler
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.TextView
import kotlinx.android.synthetic.main.player_list_activity.*
import java.util.*

class PlayerListActivity : AppCompatActivity() {
    private var mReceiverNOk: BroadcastReceiver? = null
    private var mReceiverOk: BroadcastReceiver? = null
    private var mHandler: Handler? = null
    private var mContext: Context = this
    private var mInviteCount: Int = 0

    private val runnableInviting: Runnable = Runnable {
        fcmActive()
        val invite = String.format(getString(R.string.Inviting), "INVITE")
        tvInviting.text = invite
    }

    private val runnableTimeOut: Runnable = Runnable {
        tvInviting.text = getString(R.string.InviteTimeOut)
        tvInviting.setTextColor(ContextCompat.getColor(mContext, R.color.colorTint))
        mJoinGuid = null
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

    private fun initReceivers() {
        if (mReceiverNOk == null) {
            mReceiverNOk = object : BroadcastReceiver() {
                override fun onReceive(context: Context, intent: Intent) {
                    fcmActive()
                    tvInviting.text = ""
                    val err = intent.getStringExtra(FcmNames.Error)
                    Helper.alert(context, err)
                    getPlayers()
                }
            }
            registerReceiver(mReceiverNOk, IntentFilter(FcmNames.ResponseType.Nok.EnumToString()))
        }

        if (mReceiverOk == null) {
            mReceiverOk = object : BroadcastReceiver() {
                override fun onReceive(context: Context, intent: Intent) {
                    fcmActive()
                    gotoGame()
                }
            }
            registerReceiver(mReceiverOk, IntentFilter(FcmNames.ResponseType.Ok.EnumToString()))
        }
    }

    private fun gotoGame()
    {

    }

    private fun unregBroadcastReceivers() {
        Helper.unRegisterReceiver(mContext, mReceiverNOk)
        Helper.unRegisterReceiver(mContext, mReceiverOk)
        mReceiverNOk = null
        mReceiverOk = null
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
        if (!Helper.testInternet(mContext)) return
        Helper.setHostToken(mContext, player.fcmToken)
        fcmActive()
        showInviteText()
    }

    private fun showInviteText() {

        tvInviting.text = getString(R.string.Inviting)
        tvInviting.setTextColor(ContextCompat.getColor(mContext, R.color.colorPrimary))

        mHandler = Handler()
        mInviteCount = 0

        val myTimer = Timer()
        myTimer.schedule(object : TimerTask() {
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

    companion object {
        private var mJoinGuid: String? = null
    }
}

