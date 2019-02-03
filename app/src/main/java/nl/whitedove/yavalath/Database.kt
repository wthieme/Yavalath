package nl.whitedove.yavalath

import com.google.firebase.firestore.FirebaseFirestore
import org.joda.time.DateTime
import java.util.*
import kotlin.collections.ArrayList

internal object Database {

    var mPlayers: ArrayList<PlayerInfo> = ArrayList()
    var mPlayer: PlayerInfo? = null
    val PrivacyUrl = "http://wjthieme.com/vampires/privacy"
    val TermsUrl = "http://wjthieme.com/vampires/terms"
    val RulesUrl = "http://wjthieme.com/vampires/rules"
    var TIMEOUT = 60

    internal object Names {
        val players = "players"
        val playerName = "playerName"
        val playerToken = "playerToken"
        val country = "country"
    }

    fun getPlayers(callback: Runnable) {
        val players = ArrayList<PlayerInfo>()
        val db = FirebaseFirestore.getInstance()
        db.collection(Database.Names.players)
                .get()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        for (document in task.result!!) {
                            val doc = document.data
                            val naam = doc[Database.Names.playerName] as String
                            val token = doc[Database.Names.playerToken] as String
                            val country = doc[Database.Names.country] as String
                            players.add(PlayerInfo(naam, token, country))
                        }
                        mPlayers = players
                        RemoveInActivePlayers()
                        callback.run()
                    }
                }
    }

    fun SetListener(callback: Runnable) {
        val db = FirebaseFirestore.getInstance()
        db.collection(Database.Names.players)
                .addSnapshotListener { value, e -> callback.run() }
    }

    fun CreateOrUpdatePlayer(name: String, country: String) {
        val token = FcmSender.mFcmToken
        val db = FirebaseFirestore.getInstance()
        val doc = HashMap<String, Any>()
        doc[Names.playerName] = name
        doc[Names.playerToken] = token
        doc[Names.country] = country
        db.collection(Database.Names.players).document(token).set(doc)
        mPlayer = PlayerInfo(name, token, country)
    }

    fun RemoveInActivePlayers() {
        val playersToRemove: ArrayList<PlayerInfo> = ArrayList()
        for (player in mPlayers) {
            if (player.lastActive.isBefore(DateTime.now().minusMinutes(15)))
                playersToRemove.add(player)
        }
        for (toRemove in playersToRemove) {
            mPlayers.remove(toRemove)
            DeletePlayer(toRemove.fcmToken)
        }
    }

    fun DeletePlayer(token: String) {
        val db = FirebaseFirestore.getInstance()
        db.collection(Database.Names.players).document(token)
                .delete()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                    }
                }
    }
}