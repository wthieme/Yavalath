package nl.whitedove.yavalath

import org.joda.time.DateTime

class GameInfo(var myName: String, var myFcmToken: String, var hisName: String, hisToken: String, var playesWhite: String) {
    var created: DateTime = DateTime.now()
    var fields: ArrayList<Field> = ArrayList()
    var hisFcmToken: String = hisToken
    var playerWhite: String
    var playerBlack: String
    var playerToMove: String
    var winningFields3: ArrayList<List<Int>> = ArrayList()
    var winningFields4: ArrayList<List<Int>> = ArrayList()
    var winningFields5: ArrayList<List<Int>> = ArrayList()
    var winner: String
    var lastMove: Int
    var whiteReady: Boolean
    var blackReady: Boolean
    var gameState: GameState

    init {
        for (i in 0..60) {
            this.fields.add(Field(i))
        }
        if (this.playesWhite == this.myFcmToken) {
            this.playerWhite = this.myName
            this.playerBlack = this.hisName
            this.playerToMove = this.myName
        } else {
            this.playerWhite = this.hisName
            this.playerBlack = this.myName
            this.playerToMove = this.hisName
        }
        this.winner = ""
        this.lastMove = -1
        this.whiteReady = true
        this.blackReady = true
        this.gameState = GameState.Running
    }

    private fun get3(): List<List<Int>> {
        return listOf(
                listOf(0, 1, 2), listOf(1, 2, 3), listOf(2, 3, 4),
                listOf(5, 6, 7), listOf(6, 7, 8), listOf(7, 8, 9), listOf(8, 9, 10),
                listOf(11, 12, 13), listOf(12, 13, 14), listOf(13, 14, 15), listOf(14, 15, 16), listOf(15, 16, 17),
                listOf(18, 19, 20), listOf(19, 20, 21), listOf(20, 21, 22), listOf(21, 22, 23), listOf(22, 23, 24), listOf(23, 24, 25),
                listOf(26, 27, 28), listOf(27, 28, 29), listOf(28, 29, 30), listOf(29, 30, 31), listOf(30, 31, 32), listOf(31, 32, 33), listOf(32, 33, 34),
                listOf(35, 36, 37), listOf(36, 37, 38), listOf(37, 38, 39), listOf(38, 39, 40), listOf(39, 40, 41), listOf(40, 41, 42),
                listOf(43, 44, 45), listOf(44, 45, 46), listOf(45, 46, 47), listOf(46, 47, 48), listOf(47, 48, 49),
                listOf(50, 51, 52), listOf(51, 52, 53), listOf(52, 53, 54), listOf(53, 54, 55),
                listOf(56, 57, 58), listOf(57, 58, 59), listOf(58, 59, 60),
                listOf(0, 6, 13), listOf(1, 7, 14), listOf(2, 8, 15), listOf(3, 9, 16), listOf(4, 10, 17),
                listOf(5, 12, 20), listOf(6, 13, 21), listOf(7, 14, 22), listOf(8, 15, 23), listOf(9, 16, 24), listOf(10, 17, 25),
                listOf(11, 19, 28), listOf(12, 20, 29), listOf(13, 21, 30), listOf(14, 22, 31), listOf(15, 23, 32), listOf(16, 24, 33), listOf(17, 25, 34),
                listOf(18, 27, 36), listOf(19, 28, 37), listOf(20, 29, 38), listOf(21, 30, 39), listOf(22, 31, 40), listOf(23, 32, 41), listOf(24, 33, 42),
                listOf(26, 35, 43), listOf(27, 36, 44), listOf(28, 37, 45), listOf(29, 38, 46), listOf(30, 39, 47), listOf(31, 40, 48), listOf(32, 41, 49),
                listOf(35, 43, 50), listOf(36, 44, 51), listOf(37, 45, 52), listOf(38, 46, 53), listOf(39, 47, 54), listOf(40, 48, 55),
                listOf(43, 50, 56), listOf(44, 51, 57), listOf(45, 52, 58), listOf(46, 53, 59), listOf(47, 54, 60),
                listOf(0, 5, 11), listOf(1, 6, 12), listOf(2, 7, 13), listOf(3, 8, 14), listOf(4, 9, 15),
                listOf(5, 11, 18), listOf(6, 12, 19), listOf(7, 13, 20), listOf(8, 14, 21), listOf(9, 15, 22), listOf(10, 16, 23),
                listOf(11, 18, 26), listOf(12, 19, 27), listOf(13, 20, 28), listOf(14, 21, 29), listOf(15, 22, 30), listOf(16, 23, 31), listOf(17, 24, 32),
                listOf(19, 27, 35), listOf(20, 28, 36), listOf(21, 29, 37), listOf(22, 30, 38), listOf(23, 31, 39), listOf(24, 32, 40), listOf(25, 33, 41),
                listOf(28, 36, 43), listOf(29, 37, 44), listOf(30, 38, 45), listOf(31, 39, 46), listOf(32, 40, 47), listOf(33, 41, 48), listOf(34, 42, 49),
                listOf(37, 44, 50), listOf(38, 45, 51), listOf(39, 46, 52), listOf(40, 47, 53), listOf(41, 48, 54), listOf(42, 49, 55),
                listOf(45, 51, 56), listOf(46, 52, 57), listOf(47, 53, 58), listOf(48, 54, 59), listOf(49, 55, 60)
        )
    }

    private fun get4(): List<List<Int>> {
        return listOf(
                listOf(0, 1, 2, 3), listOf(1, 2, 3, 4),
                listOf(5, 6, 7, 8), listOf(6, 7, 8, 9), listOf(7, 8, 9, 10),
                listOf(11, 12, 13, 14), listOf(12, 13, 14, 15), listOf(13, 14, 15, 16), listOf(14, 15, 16, 17),
                listOf(18, 19, 20, 21), listOf(19, 20, 21, 22), listOf(20, 21, 22, 23), listOf(21, 22, 23, 24), listOf(22, 23, 24, 25),
                listOf(26, 27, 28, 29), listOf(27, 28, 29, 30), listOf(28, 29, 30, 31), listOf(29, 30, 31, 32), listOf(30, 31, 32, 33), listOf(31, 32, 33, 34),
                listOf(35, 36, 37, 38), listOf(36, 37, 38, 39), listOf(37, 38, 39, 40), listOf(38, 39, 40, 41), listOf(39, 40, 41, 42),
                listOf(43, 44, 45, 46), listOf(44, 45, 46, 47), listOf(45, 46, 47, 48), listOf(46, 47, 48, 49),
                listOf(50, 51, 52, 53), listOf(51, 52, 53, 54), listOf(52, 53, 54, 55),
                listOf(56, 57, 58, 59), listOf(57, 58, 59, 60),
                listOf(0, 6, 13, 21), listOf(1, 7, 14, 22), listOf(2, 8, 15, 23), listOf(3, 9, 16, 24), listOf(4, 10, 17, 25),
                listOf(5, 12, 20, 29), listOf(6, 13, 21, 30), listOf(7, 14, 22, 31), listOf(8, 15, 23, 32), listOf(9, 16, 24, 33), listOf(10, 17, 25, 34),
                listOf(11, 19, 28, 37), listOf(12, 20, 29, 38), listOf(13, 21, 30, 39), listOf(14, 22, 31, 40), listOf(15, 23, 32, 41), listOf(16, 24, 33, 42),
                listOf(18, 27, 36, 44), listOf(19, 28, 37, 45), listOf(20, 29, 38, 46), listOf(21, 30, 39, 47), listOf(22, 31, 40, 48), listOf(23, 32, 41, 49),
                listOf(26, 35, 43, 50), listOf(27, 36, 44, 51), listOf(28, 37, 45, 52), listOf(29, 38, 46, 53), listOf(30, 39, 47, 54), listOf(31, 40, 48, 55),
                listOf(35, 43, 50, 56), listOf(36, 44, 51, 57), listOf(37, 45, 52, 58), listOf(38, 46, 53, 59), listOf(39, 47, 54, 60),
                listOf(0, 5, 11, 18), listOf(1, 6, 12, 19), listOf(2, 7, 13, 20), listOf(3, 8, 14, 21), listOf(4, 9, 15, 22),
                listOf(5, 11, 18, 26), listOf(6, 12, 19, 27), listOf(7, 13, 20, 28), listOf(8, 14, 21, 29), listOf(9, 15, 22, 30), listOf(10, 16, 23, 31),
                listOf(12, 19, 27, 35), listOf(13, 20, 28, 36), listOf(14, 21, 29, 37), listOf(15, 22, 30, 38), listOf(16, 23, 31, 39), listOf(17, 24, 32, 40),
                listOf(20, 28, 36, 43), listOf(21, 29, 37, 44), listOf(22, 30, 38, 45), listOf(23, 31, 39, 46), listOf(24, 32, 40, 47), listOf(25, 33, 41, 48),
                listOf(29, 37, 44, 50), listOf(30, 38, 45, 51), listOf(31, 39, 46, 52), listOf(32, 40, 47, 53), listOf(33, 41, 48, 54), listOf(34, 42, 49, 55),
                listOf(38, 45, 51, 56), listOf(39, 46, 52, 57), listOf(40, 47, 53, 58), listOf(41, 48, 54, 59), listOf(42, 49, 55, 60)
        )
    }

    private fun get5(): List<List<Int>> {
        return listOf(
                listOf(0, 1, 2, 3, 4),
                listOf(5, 6, 7, 8, 9), listOf(6, 7, 8, 9, 10),
                listOf(11, 12, 13, 14, 15), listOf(12, 13, 14, 15, 16), listOf(13, 14, 15, 16, 17),
                listOf(18, 19, 20, 21, 22), listOf(19, 20, 21, 22, 23), listOf(20, 21, 22, 23, 24), listOf(21, 22, 23, 24, 25),
                listOf(26, 27, 28, 29, 30), listOf(27, 28, 29, 30, 31), listOf(28, 29, 30, 31, 32), listOf(29, 30, 31, 32, 33), listOf(30, 31, 32, 33, 34),
                listOf(35, 36, 37, 38, 39), listOf(36, 37, 38, 39, 40), listOf(37, 38, 39, 40, 41), listOf(38, 39, 40, 41, 42),
                listOf(43, 44, 45, 46, 47), listOf(44, 45, 46, 47, 48), listOf(45, 46, 47, 48, 49),
                listOf(50, 51, 52, 53, 54), listOf(51, 52, 53, 54, 55),
                listOf(56, 57, 58, 59, 60),
                listOf(0, 6, 13, 21, 30), listOf(1, 7, 14, 22, 31), listOf(2, 8, 15, 23, 32), listOf(3, 9, 16, 24, 33), listOf(4, 10, 17, 25, 34),
                listOf(5, 12, 20, 29, 38), listOf(6, 13, 21, 30, 39), listOf(7, 14, 22, 31, 40), listOf(8, 15, 23, 32, 41), listOf(9, 16, 24, 33, 42),
                listOf(11, 19, 28, 37, 45), listOf(12, 20, 29, 38, 46), listOf(13, 21, 30, 39, 47), listOf(14, 22, 31, 40, 48), listOf(15, 23, 32, 41, 49),
                listOf(18, 27, 36, 44, 51), listOf(19, 28, 37, 45, 52), listOf(20, 29, 38, 46, 53), listOf(21, 30, 39, 47, 54), listOf(22, 31, 40, 48, 55),
                listOf(26, 35, 43, 50, 56), listOf(27, 36, 44, 51, 57), listOf(28, 37, 45, 52, 58), listOf(29, 38, 46, 53, 59), listOf(30, 39, 47, 54, 60),
                listOf(0, 5, 11, 18, 26), listOf(1, 6, 12, 19, 27), listOf(2, 7, 13, 20, 28), listOf(3, 8, 14, 21, 29), listOf(4, 9, 15, 22, 30),
                listOf(6, 12, 19, 27, 35), listOf(7, 13, 20, 28, 36), listOf(8, 14, 21, 29, 37), listOf(9, 15, 22, 30, 38), listOf(10, 16, 23, 31, 39),
                listOf(13, 20, 28, 36, 43), listOf(14, 21, 29, 37, 44), listOf(15, 22, 30, 38, 45), listOf(16, 23, 31, 39, 46), listOf(17, 24, 32, 40, 47),
                listOf(21, 29, 37, 44, 50), listOf(22, 30, 38, 45, 51), listOf(23, 31, 39, 46, 52), listOf(24, 32, 40, 47, 53), listOf(25, 33, 41, 48, 54),
                listOf(30, 38, 45, 51, 56), listOf(31, 39, 46, 52, 57), listOf(32, 40, 47, 53, 58), listOf(33, 41, 48, 54, 59), listOf(34, 42, 49, 55, 60)
        )
    }

    fun movesPlayed(): Int {
        var nr = 0
        for (f in fields) {
            if (f.fieldState == FieldState.White || f.fieldState == FieldState.Black)
                nr++
        }
        return nr
    }

    fun myMove(): Boolean {
        return (this.playerToMove == this.myName)
    }

    fun ready(ready: Boolean, byToken: String) {
        if (byToken == this.playesWhite) {
            this.whiteReady = ready
        } else {
            this.blackReady = ready
        }
    }

    fun move(nr: Int, playedByToken: String) {
        this.lastMove = nr
        val fld = this.fields[nr]

        if (playedByToken == this.playesWhite) {
            fld.fieldState = FieldState.White
            this.playerToMove = this.playerBlack
        } else {
            fld.fieldState = FieldState.Black
            this.playerToMove = this.playerWhite
        }

        this.gameState = testGameEnd()

        if (this.gameState == GameState.BlackWins) {
            this.winner = this.playerBlack
            if (this.winningFields3.size > (this.winningFields4.size + this.winningFields5.size)) {
                GameHelper.mPointsBlack += this.winningFields3.size
                GameHelper.mPointsWhite += this.winningFields4.size + this.winningFields5.size
            } else {
                GameHelper.mPointsWhite += this.winningFields3.size
                GameHelper.mPointsBlack += this.winningFields4.size + this.winningFields5.size
            }
        }

        if (this.gameState == GameState.WhiteWins) {
            this.winner = this.playerWhite
            if (this.winningFields3.size > (this.winningFields4.size + this.winningFields5.size)) {
                GameHelper.mPointsWhite += this.winningFields3.size
                GameHelper.mPointsBlack += this.winningFields4.size + this.winningFields5.size
            } else {
                GameHelper.mPointsBlack += this.winningFields3.size
                GameHelper.mPointsWhite += this.winningFields4.size + this.winningFields5.size
            }
        }

        if (this.gameState == GameState.DrawBoardFull || this.gameState == GameState.DrawByWinAndLose) {
            GameHelper.mPointsWhite++
            GameHelper.mPointsBlack++
        }
    }

    private fun testGameEnd(): GameState {
        val gameState = GameState.Running

        var boardFull = true
        for (field in this.fields) {
            if (field.fieldState == FieldState.Empty) {
                boardFull = false
                break
            }
        }

        for (g5 in get5()) {
            if (this.fields[g5[0]].fieldState == FieldState.White &&
                    this.fields[g5[1]].fieldState == FieldState.White &&
                    this.fields[g5[2]].fieldState == FieldState.White &&
                    this.fields[g5[3]].fieldState == FieldState.White &&
                    this.fields[g5[4]].fieldState == FieldState.White) {
                this.winningFields5.add(listOf(g5[0], g5[1], g5[2], g5[3], g5[4]))
            }

            if (this.fields[g5[0]].fieldState == FieldState.Black &&
                    this.fields[g5[1]].fieldState == FieldState.Black &&
                    this.fields[g5[2]].fieldState == FieldState.Black &&
                    this.fields[g5[3]].fieldState == FieldState.Black &&
                    this.fields[g5[4]].fieldState == FieldState.Black) {
                this.winningFields5.add(listOf(g5[0], g5[1], g5[2], g5[3], g5[4]))
            }
        }

        for (g4 in get4()) {
            if (this.fields[g4[0]].fieldState == FieldState.White &&
                    this.fields[g4[1]].fieldState == FieldState.White &&
                    this.fields[g4[2]].fieldState == FieldState.White &&
                    this.fields[g4[3]].fieldState == FieldState.White &&
                    this.isOwnRow(this.winningFields5, listOf(g4[0], g4[1], g4[2], g4[3]))) {
                this.winningFields4.add(listOf(g4[0], g4[1], g4[2], g4[3]))
            }

            if (this.fields[g4[0]].fieldState == FieldState.Black &&
                    this.fields[g4[1]].fieldState == FieldState.Black &&
                    this.fields[g4[2]].fieldState == FieldState.Black &&
                    this.fields[g4[3]].fieldState == FieldState.Black &&
                    this.isOwnRow(this.winningFields5, listOf(g4[0], g4[1], g4[2], g4[3]))) {
                this.winningFields4.add(listOf(g4[0], g4[1], g4[2], g4[3]))
            }
        }

        for (g3 in get3()) {
            if (this.fields[g3[0]].fieldState == FieldState.Black &&
                    this.fields[g3[1]].fieldState == FieldState.Black &&
                    this.fields[g3[2]].fieldState == FieldState.Black &&
                    this.isOwnRow(this.winningFields5, listOf(g3[0], g3[1], g3[2])) &&
                    this.isOwnRow(this.winningFields4, listOf(g3[0], g3[1], g3[2]))) {
                this.winningFields3.add(listOf(g3[0], g3[1], g3[2]))
            }

            if (this.fields[g3[0]].fieldState == FieldState.White &&
                    this.fields[g3[1]].fieldState == FieldState.White &&
                    this.fields[g3[2]].fieldState == FieldState.White &&
                    this.isOwnRow(this.winningFields5, listOf(g3[0], g3[1], g3[2])) &&
                    this.isOwnRow(this.winningFields4, listOf(g3[0], g3[1], g3[2]))) {
                this.winningFields3.add(listOf(g3[0], g3[1], g3[2]))
            }
        }

        if (this.winningFields3.size > 0 && (this.winningFields3.size == winningFields4.size + this.winningFields5.size)) {
            return GameState.DrawByWinAndLose
        }

        if (this.fields[lastMove].fieldState == FieldState.White &&
                this.winningFields3.size > (this.winningFields4.size + this.winningFields5.size)) {
            return GameState.BlackWins
        }

        if (this.fields[lastMove].fieldState == FieldState.Black &&
                this.winningFields3.size > (this.winningFields4.size + this.winningFields5.size)) {
            return GameState.WhiteWins
        }

        if (this.fields[lastMove].fieldState == FieldState.White &&
                (this.winningFields4.size + this.winningFields5.size) > this.winningFields3.size) {
            return GameState.WhiteWins
        }

        if (this.fields[lastMove].fieldState == FieldState.Black &&
                (this.winningFields4.size + this.winningFields5.size) > this.winningFields3.size) {
            return GameState.BlackWins
        }

        if (boardFull) {
            return GameState.DrawBoardFull
        }
        return gameState
    }

    private fun isOwnRow(winningFields: List<List<Int>>, testFields: List<Int>): Boolean {
        for (winningField in winningFields) {
            if (winningField.containsAll(testFields)) {
                return false
            }
        }
        return true
    }
}