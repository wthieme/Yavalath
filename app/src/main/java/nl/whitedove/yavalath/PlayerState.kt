package nl.whitedove.yavalath

import android.annotation.SuppressLint
import java.util.HashMap

enum class PlayerState(val value: Int) {
    Unknown(1), InLobby(2), InGame(3);

    companion object {
        @SuppressLint("UseSparseArrays")
        private val map = HashMap<Int, PlayerState>()

        init {
            for (state in PlayerState.values()) {
                map[state.value] = state
            }
        }

        fun valueOf(state: Int): PlayerState {
            return map[state] ?: Unknown
        }
    }
}