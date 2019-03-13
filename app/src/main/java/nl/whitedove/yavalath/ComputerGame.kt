package nl.whitedove.yavalath

internal  object ComputerGame {
    fun move(currentGame: GameInfo) {
        val bestGame = bestGame(currentGame)
        val bestFieldNr = bestGame.lastMove
        currentGame.move(bestFieldNr, currentGame.hisFcmToken)
        return
    }

    private fun bestGame(currentGame: GameInfo): GameInfo {
        val emptyFields = currentGame.fields.filter { f -> f.fieldState == FieldState.Empty }
        val computerHasWhite = currentGame.playesWhite == currentGame.hisFcmToken
        val compColor = if (computerHasWhite) FieldState.White else FieldState.Black
        val games = ArrayList<GameInfo>()
        for (emptyField in emptyFields) {
            val newgame = GameInfo(currentGame.myName, currentGame.myFcmToken, currentGame.hisName, currentGame.hisFcmToken, currentGame.playesWhite, currentGame.gameMode, currentGame.gameLevel)
            newgame.computerSimulation = true
            for (field in currentGame.fields.filter { f -> f.fieldState != FieldState.Empty }) {
                newgame.fields[field.nr].fieldState = field.fieldState
            }

            var myScore = 0
            // Put my stone on this empty field and check if I win
            newgame.move(emptyField.nr, newgame.hisFcmToken)
            when {
                newgame.gameState == GameState.WhiteWins -> {
                    myScore = if (computerHasWhite) 250 else -250
                }
                newgame.gameState == GameState.BlackWins -> {
                    myScore = if (computerHasWhite) -250 else 250
                }
                newgame.gameState == GameState.DrawByWinAndLose -> {
                    myScore = 0
                }
                newgame.gameState == GameState.DrawBoardFull -> {
                    myScore = 0
                }
                newgame.gameState == GameState.Running -> {
                    var up = 5
                    when {
                        Board.ring3.contains(emptyField.nr) -> up = 4
                        Board.ring2.contains(emptyField.nr) -> up = 3
                        Board.ring1.contains(emptyField.nr) -> up = 2
                    }
                    myScore = Helper.randomNrInRange(1, up)
                    if (currentGame.gameLevel.value >= GameLevel.Intermediate.value) {
                        myScore += newgame.winInOne(compColor)
                        myScore += newgame.boardScore(compColor)
                    }
                    if (currentGame.gameLevel.value >= GameLevel.Expert.value) {
                        myScore += newgame.possibleWinInTwo(compColor)
                        myScore += newgame.loseInTwo(compColor)
                    }
                    if (currentGame.gameLevel.value >= GameLevel.Extreme.value) {
                        val winScore = newgame.winBy2RowsOf4(compColor)
                        val loseScore = newgame.loseBy2RowsOf4(compColor)
                        myScore += if (winScore > loseScore) winScore else loseScore
                    }
                }
            }

            if (myScore == 250) {
                // We win, so break out of the loop
                newgame.score = myScore
                games.add(newgame)
                break
            }

            var hisScore = 0
            // Put his stone on this empty field and check if he wins
            newgame.move(emptyField.nr, newgame.myFcmToken)
            when {
                newgame.gameState == GameState.WhiteWins -> {
                    hisScore = if (computerHasWhite) -250 else 250
                }
                newgame.gameState == GameState.BlackWins -> {
                    hisScore = if (computerHasWhite) 250 else -250
                }
                newgame.gameState == GameState.DrawByWinAndLose -> {
                    hisScore = 0
                }
                newgame.gameState == GameState.DrawBoardFull -> {
                    hisScore = 0
                }
                newgame.gameState == GameState.Running -> {
                    hisScore = 0
                }
            }
            // Don't play the losing move
            if (myScore == -250) {
                newgame.score = 0
            } else {
                newgame.score = if (myScore >= hisScore) myScore else hisScore
            }
            games.add(newgame)
        }

        val bestGames = games.toList().sortedByDescending { g -> g.score }
        val bestScore = bestGames.first().score
        val equalBestGames = bestGames.filter { g -> g.score == bestScore }
        val randomnr = Helper.randomNrInRange(0, equalBestGames.count() - 1)
        return bestGames[randomnr]
    }
}