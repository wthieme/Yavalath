package nl.whitedove.yavalath

import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import org.joda.time.DateTime
import org.joda.time.format.ISODateTimeFormat
import java.util.*
import kotlin.collections.ArrayList

internal object Database {
    val fmt = ISODateTimeFormat.dateTime()
    var mPlayers: ArrayList<PlayerInfo> = ArrayList()
    private var mPlayer: PlayerInfo? = null
    var TIMEOUT = 30
    var mHighScoreForLevel: Array<ArrayList<HighScore>> = Array(4) { ArrayList<HighScore>() }
    var mHighScoreForPlayerAndLevel: HighScore? = null

    internal object PlayerNames {
        var collectionName = "players"
        const val playerName = "playerName"
        const val playerToken = "playerToken"
        const val country = "country"
        const val lastActive = "lastActive"
        const val device = "device"
        const val platform = "platform"
        const val timestamp = "timestamp"
    }

    internal object HighScoreNames {
        var collectionName = "highscores"
        const val level = "level"
        const val playerName = "playerName"
        const val score = "score"
        const val achievedDate = "achievedDate"
    }

    private fun getPlayersCollectionName(): String {
        if (Helper.DEBUG)
            return "DEV" + Database.PlayerNames.collectionName
        else
            return Database.PlayerNames.collectionName
    }

    private fun getHighscoresCollectionName(): String {
        if (Helper.DEBUG)
            return "DEV" + Database.HighScoreNames.collectionName
        else
            return Database.HighScoreNames.collectionName
    }

    fun getPlayers(callback: Runnable) {
        val players = ArrayList<PlayerInfo>()
        val db = FirebaseFirestore.getInstance()
        db.collection(getPlayersCollectionName())
                .get()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        for (document in task.result!!) {
                            val doc = document.data
                            val naam = doc[Database.PlayerNames.playerName] as String
                            val token = doc[Database.PlayerNames.playerToken] as String
                            val country = doc[Database.PlayerNames.country] as String
                            var device = ""
                            if (doc[Database.PlayerNames.device] != null) {
                                device = doc[Database.PlayerNames.device] as String
                            }
                            var platform = ""
                            if (doc[Database.PlayerNames.platform] != null) {
                                platform = doc[Database.PlayerNames.platform] as String
                            }
                            val lastActive = fmt.parseDateTime(doc[Database.PlayerNames.lastActive] as String)
                            players.add(PlayerInfo(naam, token, country, lastActive, device, platform))
                        }
                        val sorted = players.sortedWith(compareBy({ it.country.toLowerCase() }, { it.name.toLowerCase() }))
                        mPlayers = ArrayList()
                        mPlayers.addAll(sorted)
                        removeInActivePlayers()
                        callback.run()
                    }
                }
    }

    fun setListenerOnPlayers(callback: Runnable) {
        val db = FirebaseFirestore.getInstance()
        db.collection(getPlayersCollectionName())
                .addSnapshotListener { _, _ -> callback.run() }
    }

    fun setListenerOnHighScores(callback: Runnable) {
        val db = FirebaseFirestore.getInstance()
        db.collection(getHighscoresCollectionName())
                .addSnapshotListener { _, _ -> callback.run() }
    }

    fun createOrUpdatePlayer(name: String, country: String) {
        val token = FcmSender.mMyFcmToken
        val db = FirebaseFirestore.getInstance()
        val doc = HashMap<String, Any>()
        doc[PlayerNames.playerName] = name
        doc[PlayerNames.playerToken] = token
        doc[PlayerNames.country] = country
        val device = Helper.getDeviceName()
        doc[PlayerNames.device] = device
        val platform = Helper.getAndroidVersion()
        doc[PlayerNames.platform] = platform
        doc[PlayerNames.lastActive] = DateTime.now().toString()
        doc[PlayerNames.timestamp] = FieldValue.serverTimestamp()
        db.collection(getPlayersCollectionName()).document(token).set(doc)
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
        db.collection(getPlayersCollectionName()).document(token)
                .delete()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                    }
                }
    }

    fun getHighScoreForLevel(level: GameLevel, myScore: Long, callback: Runnable) {
        val highScores = ArrayList<HighScore>()
        val db = FirebaseFirestore.getInstance()
        db.collection(getHighscoresCollectionName())
                .whereEqualTo(HighScoreNames.level, level.toString())
                .whereLessThan(HighScoreNames.score, myScore)
                .orderBy(HighScoreNames.score, Query.Direction.ASCENDING)
                .limit(3)
                .get()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        for (document in task.result!!) {
                            val doc = document.data
                            val playerName = doc[Database.HighScoreNames.playerName] as String
                            val score = doc[Database.HighScoreNames.score] as Long
                            val achievedDate = fmt.parseDateTime(doc[Database.HighScoreNames.achievedDate] as String)
                            highScores.add(HighScore(playerName, score, level, achievedDate))
                        }
                        mHighScoreForLevel[level.value - 1] = ArrayList()
                        mHighScoreForLevel[level.value - 1].addAll(highScores)
                        callback.run()
                    }
                }
    }

    fun getHighScoreForPlayerAndLevel(playerName: String, level: GameLevel, callback: Runnable) {
        val db = FirebaseFirestore.getInstance()
        db.collection(getHighscoresCollectionName())
                .whereEqualTo(HighScoreNames.playerName, playerName)
                .whereEqualTo(HighScoreNames.level, level.toString())
                .orderBy(HighScoreNames.score, Query.Direction.ASCENDING)
                .limit(1)
                .get()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val results = task.result!!
                        if (!results.isEmpty) {
                            val document = results.first();
                            val doc = document.data
                            val score = doc[Database.HighScoreNames.score] as Long
                            val achievedDate = fmt.parseDateTime(doc[Database.HighScoreNames.achievedDate] as String)
                            mHighScoreForPlayerAndLevel = HighScore(playerName, score, level, achievedDate)
                        } else {
                            mHighScoreForPlayerAndLevel = null
                        }
                        callback.run()
                    }
                }
    }

    fun addHighScore(highScore: HighScore) {
        val db = FirebaseFirestore.getInstance()
        val doc = HashMap<String, Any>()
        doc[HighScoreNames.playerName] = highScore.playerName
        doc[HighScoreNames.level] = highScore.level
        doc[HighScoreNames.score] = highScore.score
        doc[HighScoreNames.achievedDate] = highScore.achievedDate.toString()
        db.collection(getHighscoresCollectionName()).document().set(doc)
    }

    fun deleteOldHighScoresForPlayerAndLevel(playerName: String, myScore: Long, level: GameLevel) {
        val toDelete = ArrayList<String>()
        val db = FirebaseFirestore.getInstance()
        db.collection(getHighscoresCollectionName())
                .whereEqualTo(HighScoreNames.playerName, playerName)
                .whereEqualTo(HighScoreNames.level, level.toString())
                .whereGreaterThan(HighScoreNames.score, myScore)
                .get()
                .addOnCompleteListener { task ->
                    for (document in task.result!!) {
                        toDelete.add(document.id)
                    }
                    deleteOldScores(toDelete)
                }
    }

    fun deleteOldScores(toDelete: List<String>) {
        val db = FirebaseFirestore.getInstance()
        for (toDel in toDelete) {
            db.collection(getHighscoresCollectionName()).document(toDel)
                    .delete()
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                        }
                    }
        }
    }
}
