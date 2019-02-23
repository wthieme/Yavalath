package nl.whitedove.yavalath

import android.annotation.SuppressLint
import java.util.*

enum class GameMode(val value: Int) {
    HumanVsHumanInternet(1), HumanVsHumanLocal(2), HumanVsComputer(3);

    companion object {
        @SuppressLint("UseSparseArrays")
        private val map = HashMap<Int, GameMode>()

        init {
            for (state in GameMode.values()) {
                map[state.value] = state
            }
        }

        fun valueOf(state: Int): GameMode {
            return map[state]!!
        }
    }
}