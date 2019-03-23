package nl.whitedove.yavalath

import android.content.Context
import android.preference.PreferenceManager
import org.joda.time.DateTime
import org.joda.time.Period
import java.util.*

internal object GameHelper {
    var mGame: GameInfo? = null
    var mPointsWhite = 0
    var mPointsBlack = 0

    fun createGame(context: Context, playerName: String, playesWhite: String) {
        mGame = GameInfo(Helper.getName1(context), FcmSender.mMyFcmToken, playerName, FcmSender.mHisFcmToken, playesWhite, GameMode.HumanVsHumanInternet, GameLevel.Easy)
        mPointsBlack = 0
        mPointsWhite = 0
    }

    fun registerWin(context: Context, playerName: String, level: GameLevel) {

        // Read the current wins
        val preferences = PreferenceManager.getDefaultSharedPreferences(context)
        val key = "wins$level"
        val current = preferences.getString(key, "")
        val wins = ArrayList<DateTime>()
        if (!current.isNullOrEmpty()) {
            val winsAsListStrings = current.split(",")
            wins.addAll(winsAsListStrings.map { DateTime(it.trim().toLong()) })
        }
        wins.add(DateTime.now())
        if (wins.size > 3) {
            wins.removeAt(0)
        }

        // Save back to preference
        val editor = preferences.edit()
        editor.putString(key, wins.map { it.millis }.joinToString())
        editor.apply()

        // Check for highScore
        if (wins.size == 3) {
            val period = Period(wins[0], wins[2])
            val score = wins[2].millis - wins[0].millis
            // A high score must be less the 24 hours
            if (period.hours < 24) {
                Database.getHighScoreForPlayerAndLevel(playerName, score, level, Runnable { processScoreForPlayerAndLevel(context, playerName, score, level) })
                Database.getHighScoreForLevel(level, score, Runnable { processScoreForLevel(context, level) })
            }
        }
    }

    private fun processScoreForPlayerAndLevel(context: Context, playerName: String, score: Long, level: GameLevel) {
        val highScoreCount = Database.mHighScoreForPlayerAndLevel.size
        if (highScoreCount<3) {
            // New top3 score for player
            Database.addHighScore(HighScore(playerName, score, level, DateTime.now()))
        }
    }

    fun scoreToString(score: Long): String {
        val period = Period(score)
        if (period.hours == 0) {
            return String.format("%s:%s",
                    period.minutes.toString().padStart(2, '0'),
                    period.seconds.toString().padStart(2, '0'))
        } else {
            return String.format("%s:%s:%s",
                    period.hours.toString().padStart(2, '0'),
                    period.minutes.toString().padStart(2, '0'),
                    period.seconds.toString().padStart(2, '0'))
        }
    }

    private fun processScoreForLevel(context: Context, level: GameLevel) {
        val highScoreCount = Database.mHighScoreForLevel[level.value-1].size
        if (highScoreCount >= 3) {
            return
        }
        val medal = when (highScoreCount) {
            0 -> context.getString(R.string.gold)
            1 -> context.getString(R.string.silver)
            2 -> context.getString(R.string.bronze)
            else -> ""
        }

        val levelString = when (level) {
            GameLevel.Easy -> context.getString(R.string.Easy)
            GameLevel.Intermediate -> context.getString(R.string.Intermediate)
            GameLevel.Expert -> context.getString(R.string.Expert)
            GameLevel.Extreme -> context.getString(R.string.Extreme)
        }

        Helper.showMessage(context,
                String.format(context.getString(R.string.medal), medal, levelString),
                context.getColor(R.color.colorLightGreen))
    }
}