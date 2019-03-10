package nl.whitedove.yavalath

import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import org.joda.time.DateTime
import org.joda.time.format.ISODateTimeFormat
import java.util.*
import kotlin.collections.ArrayList

internal object Database {
    var mPlayers: ArrayList<PlayerInfo> = ArrayList()
    private var mPlayer: PlayerInfo? = null
    var TIMEOUT = 30

    internal object Names {
        var collectionName = "players"
        const val playerName = "playerName"
        const val playerToken = "playerToken"
        const val country = "country"
        const val lastActive = "lastActive"
        const val device = "device"
        const val platform = "platform"
        const val timestamp = "timestamp"
    }

    private fun getCollectionName(): String {
        if (Helper.DEBUG)
            return "DEV" + Database.Names.collectionName
        else
            return Database.Names.collectionName
    }

    fun getPlayers(callback: Runnable) {
        val players = ArrayList<PlayerInfo>()
        val db = FirebaseFirestore.getInstance()
        db.collection(getCollectionName())
                .get()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        for (document in task.result!!) {
                            val doc = document.data
                            val naam = doc[Database.Names.playerName] as String
                            val token = doc[Database.Names.playerToken] as String
                            val country = doc[Database.Names.country] as String
                            var device = ""
                            if (doc[Database.Names.device] != null) {
                                device = doc[Database.Names.device] as String
                            }
                            var platform = ""
                            if (doc[Database.Names.platform] != null) {
                                platform = doc[Database.Names.platform] as String
                            }
                            val fmt = ISODateTimeFormat.dateTime()
                            val lastActive = fmt.parseDateTime(doc[Database.Names.lastActive] as String)
                            players.add(PlayerInfo(naam, token, country, lastActive, device, platform))
                        }
                        val sorted = players.sortedWith(compareBy({ it.country.toLowerCase() }, { it.name.toLowerCase() }))
                        mPlayers = ArrayList()
                        for (p in sorted)
                            mPlayers.add(p)

                        removeInActivePlayers()
                        callback.run()
                    }
                }
    }

    fun setListener(callback: Runnable) {
        val db = FirebaseFirestore.getInstance()
        db.collection(getCollectionName())
                .addSnapshotListener { _, _ -> callback.run() }
    }

    fun createOrUpdatePlayer(name: String, country: String) {
        val token = FcmSender.mMyFcmToken
        val db = FirebaseFirestore.getInstance()
        val doc = HashMap<String, Any>()
        doc[Names.playerName] = name
        doc[Names.playerToken] = token
        doc[Names.country] = country
        val device = Helper.getDeviceName()
        doc[Names.device] = device
        val platform = Helper.getAndroidVersion()
        doc[Names.platform] = platform
        doc[Names.lastActive] = DateTime.now().toString()
        doc[Names.timestamp] = FieldValue.serverTimestamp()
        db.collection(getCollectionName()).document(token).set(doc)
        mPlayer = PlayerInfo(name, token, country, DateTime.now(), device, platform)
    }

    private fun removeInActivePlayers() {
        val playersToRemove: ArrayList<PlayerInfo> = ArrayList()
        for (player in mPlayers) {
            if (player.lastActive.isBefore(DateTime.now().minusMinutes(15)))
                playersToRemove.add(player)
        }
        for (toRemove in playersToRemove) {
            mPlayers.remove(toRemove)
            deletePlayer(toRemove.fcmToken)
        }
    }

    fun deletePlayer(token: String) {
        val db = FirebaseFirestore.getInstance()
        db.collection(getCollectionName()).document(token)
                .delete()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                    }
                }
    }
}