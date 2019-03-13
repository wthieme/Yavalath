package nl.whitedove.yavalath

import org.joda.time.DateTime

class GameInfo(var myName: String, var myFcmToken: String, var hisName: String, hisToken: String, var playesWhite: String, var gameMode: GameMode, var gameLevel: GameLevel) {
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
    var score: Int
    var computerSimulation: Boolean
    var pointsWhite: Int
    var pointsBlack: Int

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
        this.score = 0
        this.computerSimulation = false
        this.pointsWhite = 0
        this.pointsBlack = 0
    }

    fun movesPlayed(): Int {
        return fields.count { f -> f.fieldState == FieldState.White || f.fieldState == FieldState.Black }
    }

    fun myMove(): Boolean {
        when {
            this.gameMode == GameMode.HumanVsHumanInternet -> return (this.playerToMove == this.myName)
            this.gameMode == GameMode.HumanVsHumanLocal -> return true
            this.gameMode == GameMode.HumanVsComputer -> return (this.playerToMove == this.myName)
        }
        return true
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
    }

    private fun testGameEnd(): GameState {
        val gameState = GameState.Running
        this.winningFields3 = ArrayList()
        this.winningFields4 = ArrayList()
        this.winningFields5 = ArrayList()

        val boardFull = !this.fields.any { f -> f.fieldState == FieldState.Empty }

        for (g5 in Board.list5) {
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

        for (g4 in Board.list4) {
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

        for (g3 in Board.list3) {
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
        val points45 = 3 * this.winningFields5.size + this.winningFields4.size
        val points3 = this.winningFields3.size

        if (boardFull) {
            if (!this.computerSimulation) {
                GameHelper.mPointsWhite++
                GameHelper.mPointsBlack++
                this.pointsBlack = 1
                this.pointsWhite = 1
            }
            return GameState.DrawBoardFull
        }

        if (points3 == 0 && points45 == 0) {
            return GameState.Running
        }

        if (points3 == points45) {
            if (!this.computerSimulation) {
                GameHelper.mPointsWhite += points3
                GameHelper.mPointsBlack += points3
                this.pointsBlack = points3
                this.pointsWhite = points3
            }
            return GameState.DrawByWinAndLose
        }

        if (this.fields[lastMove].fieldState == FieldState.White && points3 > points45) {
            if (!this.computerSimulation) {
                GameHelper.mPointsBlack += points3
                GameHelper.mPointsWhite += points45
                this.pointsBlack = points3
                this.pointsWhite = points45
            }
            this.winner = this.playerBlack
            return GameState.BlackWins
        }

        if (this.fields[lastMove].fieldState == FieldState.Black && points3 > points45) {
            if (!this.computerSimulation) {
                GameHelper.mPointsWhite += points3
                GameHelper.mPointsBlack += points45
                this.pointsWhite = points3
                this.pointsBlack = points45
            }
            this.winner = this.playerWhite
            return GameState.WhiteWins
        }

        if (this.fields[lastMove].fieldState == FieldState.White && points45 > points3) {
            if (!this.computerSimulation) {
                GameHelper.mPointsWhite += points45
                GameHelper.mPointsBlack += points3
                this.pointsWhite = points45
                this.pointsBlack = points3
            }
            this.winner = this.playerWhite
            return GameState.WhiteWins
        }

        if (this.fields[lastMove].fieldState == FieldState.Black && points45 > points3) {
            if (!this.computerSimulation) {
                GameHelper.mPointsBlack += points45
                GameHelper.mPointsWhite += points3
                this.pointsBlack = points45
                this.pointsWhite = points3
            }
            this.winner = this.playerBlack
            return GameState.BlackWins
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

    fun boardScore(color: FieldState): Int {
        var score = 0
        for (g4 in Board.list4) {
            val fieldStates = listOf(this.fields[g4[0]].fieldState, this.fields[g4[1]].fieldState, this.fields[g4[2]].fieldState, this.fields[g4[3]].fieldState)
            val aantalEmpty = fieldStates.count { f -> f == FieldState.Empty }
            val aantalOk = fieldStates.count { f -> f == color }
            val aantalNOk = fieldStates.count { f -> f != color && f != FieldState.Empty }
            if (aantalEmpty == 1 && aantalOk == 3) {
                score += 4
            }
            if (aantalEmpty == 2 && aantalOk == 2) {
                score += 1
            }
            if (aantalEmpty == 1 && aantalNOk == 3) {
                score -= 4
            }
            if (aantalEmpty == 2 && aantalNOk == 2) {
                score -= 1
            }
        }
        return score
    }

    fun winInOne(compColor: FieldState): Int {
        val lastMove = this.lastMove
        val listMoves = listOf(0, 1, 2, 3)
        for (g4 in Board.list4) {
            val fieldStates = listOf(this.fields[g4[0]].fieldState, this.fields[g4[1]].fieldState, this.fields[g4[2]].fieldState, this.fields[g4[3]].fieldState)
            if (g4.contains(lastMove)) {
                val aantalCompColor = fieldStates.count { f -> f == compColor }
                val aantalNotCompColor = fieldStates.count { f -> f != compColor && f != FieldState.Empty }
                if (aantalCompColor == 3 && aantalNotCompColor == 0) {
                    for (listMove in listMoves) {
                        if (fieldStates[listMove] == FieldState.Empty) {
                            this.move(this.fields[g4[listMove]].nr, this.myFcmToken)
                            val state = this.gameState
                            this.fields[g4[listMove]].fieldState = FieldState.Empty
                            this.gameState = GameState.Running
                            this.lastMove = lastMove
                            if ((state == GameState.BlackWins && compColor == FieldState.Black) ||
                                    (state == GameState.WhiteWins && compColor == FieldState.White)) {
                                return 60
                            }
                        }
                    }
                }
            }
        }
        return 0
    }

    fun loseInTwo(compColor: FieldState): Int {
        val lastMove = this.lastMove
        val listMoves = listOf(Pair(0, 1), Pair(0, 2), Pair(1, 2), Pair(2, 1), Pair(3, 1), Pair(3, 2))
        for (g4 in Board.list4) {
            val fieldStates = listOf(this.fields[g4[0]].fieldState, this.fields[g4[1]].fieldState, this.fields[g4[2]].fieldState, this.fields[g4[3]].fieldState)
            val aantalCompColor = fieldStates.count { f -> f == compColor }
            val aantalNotCompColor = fieldStates.count { f -> f != compColor && f != FieldState.Empty }
            if (aantalCompColor == 0 && aantalNotCompColor == 2) {
                for (listMove in listMoves) {
                    if (fieldStates[listMove.first] == FieldState.Empty && fieldStates[listMove.second] == FieldState.Empty) {
                        // Put my (human) stone on the first field and then his (computer) stone on the second field
                        this.move(this.fields[g4[listMove.first]].nr, this.myFcmToken)
                        // Second move only if the game is still running after the first move
                        var state = this.gameState
                        if (state == GameState.Running) {
                            // Only do the second move if the game is still running
                            this.move(this.fields[g4[listMove.second]].nr, this.hisFcmToken)
                            state = this.gameState
                        }
                        this.fields[g4[listMove.first]].fieldState = FieldState.Empty
                        this.fields[g4[listMove.second]].fieldState = FieldState.Empty
                        this.gameState = GameState.Running
                        this.lastMove = lastMove
                        if ((state == GameState.BlackWins && compColor == FieldState.White) ||
                                (state == GameState.WhiteWins && compColor == FieldState.Black)) {
                            return -25
                        }
                    }
                }
            }
        }
        return 0
    }

    fun possibleWinInTwo(compColor: FieldState): Int {
        val lastMove = this.lastMove
        val listMoves = listOf(Pair(0, 1), Pair(0, 2), Pair(1, 2), Pair(2, 1), Pair(3, 1), Pair(3, 2))
        for (g4 in Board.list4) {
            val fieldStates = listOf(this.fields[g4[0]].fieldState, this.fields[g4[1]].fieldState, this.fields[g4[2]].fieldState, this.fields[g4[3]].fieldState)
            val aantalCompColor = fieldStates.count { f -> f == compColor }
            val aantalNotCompColor = fieldStates.count { f -> f != compColor && f != FieldState.Empty }
            if (aantalCompColor == 2 && aantalNotCompColor == 0) {
                for (listMove in listMoves) {
                    if (fieldStates[listMove.first] == FieldState.Empty && fieldStates[listMove.second] == FieldState.Empty) {
                        // Put my (human) stone on the first field and then his (computer) stone on the second field
                        this.move(this.fields[g4[listMove.first]].nr, this.hisFcmToken)
                        var state = this.gameState
                        if (state == GameState.Running) {
                            // Only do the second move if the game is still running
                            this.move(this.fields[g4[listMove.second]].nr, this.myFcmToken)
                            state = this.gameState
                        }
                        this.fields[g4[listMove.first]].fieldState = FieldState.Empty
                        this.fields[g4[listMove.second]].fieldState = FieldState.Empty
                        this.gameState = GameState.Running
                        this.lastMove = lastMove
                        if ((state == GameState.BlackWins && compColor == FieldState.Black) ||
                                (state == GameState.WhiteWins && compColor == FieldState.White)) {
                            return 15
                        }
                    }
                }
            }
        }
        return 0
    }

    fun winBy2RowsOf4(compColor: FieldState): Int {
        var returnVal = 0
        val factor = 6
        for (g7 in Board.list7) {
            val fieldStates = listOf(this.fields[g7[0]].fieldState, this.fields[g7[1]].fieldState, this.fields[g7[2]].fieldState,
                    this.fields[g7[3]].fieldState, this.fields[g7[4]].fieldState, this.fields[g7[5]].fieldState, this.fields[g7[6]].fieldState)

            if (g7.contains(this.lastMove)) {
                if (fieldStates[0] == compColor &&
                        fieldStates[1] == compColor &&
                        fieldStates[2] == compColor &&
                        fieldStates[3] == compColor &&
                        fieldStates[4] == compColor &&
                        fieldStates[5] == FieldState.Empty &&
                        fieldStates[6] == FieldState.Empty) {
                    return 50
                }

                val aantalCompColor = fieldStates.count { f -> f == compColor }
                val aantalNotCompColor = fieldStates.count { f -> f != compColor && f != FieldState.Empty }
                if (aantalCompColor > 1 && aantalNotCompColor == 0 &&
                        fieldStates[4] == FieldState.Empty &&
                        fieldStates[5] == FieldState.Empty &&
                        fieldStates[6] == FieldState.Empty &&
                        factor * aantalCompColor > returnVal) {
                    returnVal = factor * aantalCompColor
                }
            }
        }
        return returnVal
    }

    fun loseBy2RowsOf4(compColor: FieldState): Int {
        val humanColor = if (compColor == FieldState.White) FieldState.Black else FieldState.White
        for (g7 in Board.list7) {
            val fieldStates = listOf(this.fields[g7[0]].fieldState, this.fields[g7[1]].fieldState, this.fields[g7[2]].fieldState,
                    this.fields[g7[3]].fieldState, this.fields[g7[4]].fieldState, this.fields[g7[5]].fieldState, this.fields[g7[6]].fieldState)

            if (g7.contains(this.lastMove)) {
                if (fieldStates[0] == humanColor &&
                        fieldStates[1] == humanColor &&
                        fieldStates[2] == humanColor &&
                        fieldStates[3] == humanColor &&
                        fieldStates[4] == compColor &&
                        fieldStates[5] == FieldState.Empty &&
                        fieldStates[6] == FieldState.Empty) {
                    return 40
                }

                if (fieldStates[0] == humanColor &&
                        fieldStates[1] == humanColor &&
                        fieldStates[2] == humanColor &&
                        fieldStates[3] == humanColor &&
                        fieldStates[4] == FieldState.Empty &&
                        fieldStates[5] == compColor &&
                        fieldStates[6] == FieldState.Empty) {
                    return 35
                }

                if (fieldStates[0] == humanColor &&
                        fieldStates[1] == humanColor &&
                        fieldStates[2] == humanColor &&
                        fieldStates[3] == humanColor &&
                        fieldStates[4] == FieldState.Empty &&
                        fieldStates[5] == FieldState.Empty &&
                        fieldStates[6] == compColor) {
                    return 35
                }
            }
        }
        return 0
    }
}