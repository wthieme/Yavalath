package nl.whitedove.yavalath

import android.annotation.SuppressLint
import java.util.*

enum class GameLevel(val value: Int) {
    Easy(1), Intermediate(2), Expert(3), Extreme (4);

    companion object {
        @SuppressLint("UseSparseArrays")
        private val map = HashMap<Int, GameLevel>()

        init {
            for (state in GameLevel.values()) {
                map[state.value] = state
            }
        }

        fun valueOf(state: Int): GameLevel {
            return map[state]!!
        }
    }
}