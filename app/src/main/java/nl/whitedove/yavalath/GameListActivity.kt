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
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.util.Pair
import android.view.View
import android.widget.AdapterView
import android.widget.TextView
import kotlinx.android.synthetic.main.players_list_activity.*
import java.util.*

class GameListActivity : AppCompatActivity() {
    private var mReceiverNOk: BroadcastReceiver? = null
    private var mReceiverOk: BroadcastReceiver? = null
    private var mHandler: Handler? = null
    private var mContext: Context = this
    private var mInviteCount: Int = 0

    internal val runnableInviting: Runnable = Runnable {
        FcmActive()
        val invite = String.format(getString(R.string.Inviting), "INVITE")
        tvInviting.setText(invite)
    }

    internal val runnableTimeOut: Runnable = Runnable {
        tvInviting.setText(getString(R.string.InviteTimeOut))
        tvInviting.setTextColor(ContextCompat.getColor(mContext!!, R.color.colorTint))
        mJoinGuid = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d(TAG, "OnCreate")
        super.onCreate(savedInstanceState)
        mContext = this
        setContentView(R.layout.players_list_activity)
        InitReceivers()
        InitViews()
        InitDb()
        getPlayers()
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume")
        InitReceivers()
        getPlayers()
    }

    public override fun onPause() {
        super.onPause()
        Log.d(TAG, "onPause")
        UnregBroadcastReceivers()
    }

    override fun onBackPressed() {
        goback()
    }

    fun goback() {
        Log.d(TAG, "onBackPressed")
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        this.startActivity(intent)
        this.overridePendingTransition(R.anim.slide_enter, R.anim.slide_exit)
        this.finish()
    }

    private fun InitViews() {
        btnBack.setOnClickListener(View.OnClickListener { goback() })
        val iconFont = FontManager.GetTypeface(this, FontManager.FONTAWESOME_SOLID)
        FontManager.SetIconAndText(btnBack,
                iconFont,
                getString(R.string.fa_arrow_left),
                ContextCompat.getColor(this, R.color.colorIcon),
                Typeface.DEFAULT,
                getString(R.string.back),
                ContextCompat.getColor(this, R.color.colorPrimary))
    }

    private fun InitDb() {
        Database.SetListener(Runnable { getPlayers() })
    }

    private fun FcmActive() {
        Helper.fcmActive(mContext, findViewById<View>(R.id.tvFcmBolt) as TextView)
    }

    private fun getPlayers() {
        FcmActive()
        Database.getPlayers(Runnable { toonPlayerList(Database.mPlayers) })
    }

    private fun InitReceivers() {
        if (mReceiverNOk == null) {
            mReceiverNOk = object : BroadcastReceiver() {
                override fun onReceive(context: Context, intent: Intent) {
                    FcmActive()
                    tvInviting.setText("")
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
                    FcmActive()
                    gotoGame()
                }
            }
            registerReceiver(mReceiverOk, IntentFilter(FcmNames.ResponseType.Ok.EnumToString()))
        }
    }

    private fun UnregBroadcastReceivers() {
        Helper.unRegisterReceiver(mContext, mReceiverNOk)
        Helper.unRegisterReceiver(mContext, mReceiverOk)
        mReceiverNOk = null
        mReceiverOk = null
    }

    private fun toonPlayerList(players: ArrayList<PlayerInfo>) {

        if (players.size > 0) {
            lvPlayers.setVisibility(View.VISIBLE)
            tvNoPlayers.visibility = View.GONE
            lvPlayers.setAdapter(CustomListAdapterGames(this, players))
            lvPlayers.setOnItemClickListener(AdapterView.OnItemClickListener { parent, view, position, id ->
                val player = parent.getItemAtPosition(position) as PlayerInfo
                invitePlayer(player)
            })
        } else {
            tvNoPlayers.visibility = View.VISIBLE
            lvPlayers.setVisibility(View.GONE)
        }
    }

    private fun invitePlayer(player: PlayerInfo) {
        if (!Helper.testInternet(mContext)) return
        Helper.setHostToken(mContext, player.fcmToken)
        FcmActive()
        showInviteText()
    }

    private fun showInviteText() {

        tvInviting.setText(getString(R.string.Inviting))
        tvInviting.setTextColor(ContextCompat.getColor(mContext!!, R.color.colorPrimary))

        mHandler = Handler()
        mInviteCount = 0

        val myTimer = Timer()
        myTimer.schedule(object : TimerTask() {
            override fun run() {
                mInviteCount++
                UpdateGUI()
                if (mInviteCount > Database.TIMEOUT) {
                    TimedOut()
                    this.cancel()
                }
            }
        }, 0, 1000)
    }

    private fun UpdateGUI() {
        mHandler!!.post(runnableInviting)
    }

    private fun TimedOut() {
        mHandler!!.post(runnableTimeOut)
    }

    private fun JoinGameInBackground(game: GameInfo) {
        AsyncJoinInBackground().execute(Pair.create(mContext, game))
    }

    private class AsyncJoinInBackground : AsyncTask<Pair<Context, GameInfo>, Void, Void>() {
        @SafeVarargs
        override fun doInBackground(vararg params: Pair<Context, GameInfo>): Void? {
            val context = params[0].first
            val game = params[0].second
            val data = HashMap<String, String>()
            data[FcmNames.UserName] = Helper.GetName(context)
            data[FcmNames.Gender] = Helper.GetGender(context).EnumToString()
            data[FcmNames.PassCode] = game.getPassCode()
            mJoinGuid = UUID.randomUUID().toString()
            data[FcmNames.UUID] = mJoinGuid
            FcmSender.SendMessage(FcmNames.ResponseType.Game, data, game.getHostToken())
            return null
        }
    }

    companion object {
        private var mJoinGuid: String? = null
    }
}

