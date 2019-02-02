package nl.whitedove.yavalath

import android.annotation.SuppressLint
import java.util.HashMap

enum class FieldState(val value: Int) {
    Empty(1), White(2), Black(3);


    companion object {
        @SuppressLint("UseSparseArrays")
        private val map = HashMap<Int, FieldState>()

        init {
            for (state in FieldState.values()) {
                map[state.value] = state
            }
        }

        fun valueOf(state: Int): FieldState {
            return map[state] ?: Empty
        }
    }
}
