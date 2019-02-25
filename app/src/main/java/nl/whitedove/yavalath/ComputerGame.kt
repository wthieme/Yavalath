package nl.whitedove.yavalath

fun computerMove(currentGame: GameInfo) {
    val best = bestGames(currentGame)
    val bestGame = best.first()
    val bestFieldNr = bestGame.lastMove
    currentGame.move(bestFieldNr, currentGame.hisFcmToken)
    return
}

private fun bestGames(currentGame: GameInfo): List<GameInfo> {
    val emptyFields = currentGame.fields.filter { f -> f.fieldState == FieldState.Empty }
    val computerHasWhite = currentGame.playesWhite == currentGame.hisFcmToken
    val games = ArrayList<GameInfo>()
    for (emptyField in emptyFields) {
        val newgame = GameInfo(currentGame.myName, currentGame.myFcmToken, currentGame.hisName, currentGame.hisFcmToken, currentGame.playesWhite, currentGame.gameMode, currentGame.gameLevel)
        newgame.computerSimulation = true
        for (field in currentGame.fields.filter { f -> f.fieldState != FieldState.Empty }) {
            newgame.fields[field.nr].fieldState = field.fieldState
        }

        // Put my stone on this empty field and check if I win, this field scores 100
        newgame.move(emptyField.nr, newgame.hisFcmToken)
        when {
            newgame.gameState == GameState.WhiteWins -> {
                newgame.score = if (computerHasWhite) 100 else -100
            }
            newgame.gameState == GameState.BlackWins -> {
                newgame.score = if (computerHasWhite) -100 else 100
            }
            newgame.gameState == GameState.DrawByWinAndLose -> {
                newgame.score = 0
            }
            newgame.gameState == GameState.DrawBoardFull -> {
                newgame.score = 0
            }
            newgame.gameState == GameState.Running -> {
                var up = 5
                when {
                    newgame.ring1(emptyField.nr) -> up = 4
                    newgame.ring2(emptyField.nr) -> up = 3
                    newgame.ring3(emptyField.nr) -> up = 2
                }
                newgame.score = Helper.randomNrInRange(1, up)
                if (currentGame.gameLevel == GameLevel.Intermediate) {
                    val bonus = newgame.boardScore(if (computerHasWhite) FieldState.White else FieldState.Black)
                    newgame.score += bonus
                }
            }
        }

        if (newgame.score == 100) {
            // We win, so break out of the loop
            games.add(newgame)
            break
        }

        // Put his stone on this empty field and check if he wins, this field scores 50
        newgame.move(emptyField.nr, newgame.myFcmToken)
        when {
            newgame.gameState == GameState.WhiteWins -> {
                newgame.score = if (computerHasWhite) -50 else 50
            }
            newgame.gameState == GameState.BlackWins -> {
                newgame.score = if (computerHasWhite) 50 else -50
            }
            newgame.gameState == GameState.DrawByWinAndLose -> {
                newgame.score = 0
            }
            newgame.gameState == GameState.DrawBoardFull -> {
                newgame.score = 0
            }
            newgame.gameState == GameState.Running -> {
            }
        }
        games.add(newgame)
    }
    return games.toList().sortedByDescending { g -> g.score }
}
