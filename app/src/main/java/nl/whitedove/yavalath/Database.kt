package nl.whitedove.yavalath

import android.util.Pair

import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.QuerySnapshot

import org.joda.time.DateTime
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

import java.text.Collator
import java.util.ArrayList
import java.util.Collections
import java.util.Comparator
import java.util.HashMap
import java.util.Random

internal object Database {

    var mGames: ArrayList<GameInfo> = ArrayList()
    var mGame: GameInfo? = null
    var mOffset: Long? = null
    var mDiscussionEndTime = DateTime.now()
    val PrivacyUrl = "http://wjthieme.com/vampires/privacy"
    val TermsUrl = "http://wjthieme.com/vampires/terms"
    val RulesUrl = "http://wjthieme.com/vampires/rules"
    val MAX_PLAYERS = 5
    val TIMEOUT = 10
    private var mImgNum = 0
    private val mRandom = Random()

    internal object Names {
        val HostedGame = "hostedGame"
        val Games = "games"
        val GameName = "gameName"
        val HostToken = "hostToken"
        val NumPlayersString = "numPlayersString"
        val PasswordProtected = "passwordProtected"
    }

    fun RandomNr(): Int {
        if (mImgNum == 0) mImgNum = mRandom.nextInt(4) + 1
        return mImgNum
    }

    fun GetGames(callback: Runnable) {
        val games = ArrayList<GameInfo>()
        val db = FirebaseFirestore.getInstance()
        db.collection(Database.Names.Games)
                .get()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        for (document in task.result!!) {
                            val doc = document.data
                            val naam = doc[Database.Names.GameName] as String?
                            val token = doc[Database.Names.HostToken] as String?
                            val numPlayers = doc[Database.Names.NumPlayersString] as String?
                            val isPassword = doc[Database.Names.PasswordProtected] as Boolean?

                            if (naam != null && token != null)
                                games.add(GameInfo(naam, token))
                        }
                        mGames = games
                        callback.run()
                    }
                }
    }

    fun SetListener(callback: Runnable) {
        val db = FirebaseFirestore.getInstance()
        db.collection(Database.Names.Games)
                .addSnapshotListener { value, e -> callback.run() }
    }

    fun CreateGame(name: String, passcode: String) {
        val token = FcmSender.mFcmToken
        val db = FirebaseFirestore.getInstance()
        val doc = HashMap<String, Any>()
        doc[Names.GameName] = name
        doc[Names.HostToken] = token
        db.collection(Database.Names.Games).document(token).set(doc)
    }
}