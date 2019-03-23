package nl.whitedove.yavalath

import android.content.Context
import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.TextView
import kotlinx.android.synthetic.main.highscore_activity.*
import java.util.*


class HighScoreActivity : AppCompatActivity() {
    private var mContext: Context = this

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mContext = this
        setContentView(R.layout.highscore_activity)
        initViews()
        initDb()
        getHighScores()
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
        val iconFont = FontManager.getTypeface(this, FontManager.FONTAWESOME_SOLID)
        FontManager.setIconAndText(btnBack,
                iconFont,
                getString(R.string.fa_arrow_left),
                ContextCompat.getColor(this, R.color.colorIcon),
                Typeface.DEFAULT,
                getString(R.string.back),
                ContextCompat.getColor(this, R.color.colorPrimary))

        FontManager.markAsIconContainer(tvEasyGold,iconFont)
        FontManager.markAsIconContainer(tvEasySilver,iconFont)
        FontManager.markAsIconContainer(tvEasyBronze,iconFont)
        FontManager.markAsIconContainer(tvIntermediateGold,iconFont)
        FontManager.markAsIconContainer(tvIntermediateSilver,iconFont)
        FontManager.markAsIconContainer(tvIntermediateBronze,iconFont)
        FontManager.markAsIconContainer(tvExpertGold,iconFont)
        FontManager.markAsIconContainer(tvExpertSilver,iconFont)
        FontManager.markAsIconContainer(tvExpertBronze,iconFont)
        FontManager.markAsIconContainer(tvExtremeGold,iconFont)
        FontManager.markAsIconContainer(tvExtremeSilver,iconFont)
        FontManager.markAsIconContainer(tvExtremeBronze,iconFont)
    }

    private fun initDb() {
        Database.setListenerOnHighScores(Runnable { getHighScores() })
    }

    private fun fcmActive() {
        Helper.fcmActive(mContext, findViewById<View>(R.id.tvFcmBolt) as TextView)
    }

    private fun getHighScores() {
        fcmActive()
        val score = 1000L * 60L * 60L * 24L
        Database.getHighScoreForLevel(GameLevel.Easy, score, Runnable { toonHighScores(GameLevel.Easy, Database.mHighScoreForLevel[0]) })
        Database.getHighScoreForLevel(GameLevel.Intermediate, score, Runnable { toonHighScores(GameLevel.Intermediate, Database.mHighScoreForLevel[1]) })
        Database.getHighScoreForLevel(GameLevel.Expert, score, Runnable { toonHighScores(GameLevel.Expert, Database.mHighScoreForLevel[2]) })
        Database.getHighScoreForLevel(GameLevel.Extreme, score, Runnable { toonHighScores(GameLevel.Extreme, Database.mHighScoreForLevel[3]) })
    }

    private fun toonHighScores(level: GameLevel, scores: ArrayList<HighScore>) {

        when (level) {
            GameLevel.Easy -> {
                if (scores.size>0) {
                    tvEasyGoldScore.text = GameHelper.scoreToString(scores[0].score)
                    tvEasyGoldWinner.text = scores[0].playerName
                }
                if (scores.size>1) {
                    tvEasySilverScore.text = GameHelper.scoreToString(scores[1].score)
                    tvEasySilverWinner.text = scores[1].playerName
                }
                if (scores.size>2){
                    tvEasyBronzeScore.text = GameHelper.scoreToString(scores[2].score)
                    tvEasyBronzeWinner.text= scores[2].playerName
                }
            }
            GameLevel.Intermediate -> {
                if (scores.size>0) {
                    tvIntermediateGoldScore.text = GameHelper.scoreToString(scores[0].score)
                    tvIntermediateGoldWinner.text = scores[0].playerName
                }
                if (scores.size>1) {
                    tvIntermediateSilverScore.text = GameHelper.scoreToString(scores[1].score)
                    tvIntermediateSilverWinner.text = scores[1].playerName
                }
                if (scores.size>2){
                    tvIntermediateBronzeScore.text = GameHelper.scoreToString(scores[2].score)
                    tvIntermediateBronzeWinner.text= scores[2].playerName
                }
            }
            GameLevel.Expert -> {
                if (scores.size>0) {
                    tvExpertGoldScore.text = GameHelper.scoreToString(scores[0].score)
                    tvExpertGoldWinner.text = scores[0].playerName
                }
                if (scores.size>1) {
                    tvExpertSilverScore.text = GameHelper.scoreToString(scores[1].score)
                    tvExpertSilverWinner.text = scores[1].playerName
                }
                if (scores.size>2){
                    tvExpertBronzeScore.text = GameHelper.scoreToString(scores[2].score)
                    tvExpertBronzeWinner.text= scores[2].playerName
                }
            }
            GameLevel.Extreme -> {
                if (scores.size>0) {
                    tvExtremeGoldScore.text = GameHelper.scoreToString(scores[0].score)
                    tvExtremeGoldWinner.text = scores[0].playerName
                }
                if (scores.size>1) {
                    tvExtremeSilverScore.text = GameHelper.scoreToString(scores[1].score)
                    tvExtremeSilverWinner.text = scores[1].playerName
                }
                if (scores.size>2){
                    tvExtremeBronzeScore.text = GameHelper.scoreToString(scores[2].score)
                    tvExtremeBronzeWinner.text= scores[2].playerName
                }
            }
        }
    }
}

