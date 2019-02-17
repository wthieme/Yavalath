package nl.whitedove.yavalath

import android.annotation.SuppressLint
import java.util.HashMap

enum class GameState(val value: Int) {
   Running(1), WhiteWins(2), BlackWins(3), DrawBoardFull(4), DrawBy3And4 (5);

    companion object {
        @SuppressLint("UseSparseArrays")
        private val map = HashMap<Int, GameState>()

        init {
            for (state in GameState.values()) {
                map[state.value] = state
            }
        }

        fun valueOf(state: Int): GameState {
            return map[state]!!
        }
    }
}