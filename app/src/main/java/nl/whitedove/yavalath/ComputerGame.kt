package nl.whitedove.yavalath

fun computerMove(currentGame: GameInfo) {
    val bestGame = bestGame(currentGame)
    val bestFieldNr = bestGame.lastMove
    currentGame.move(bestFieldNr, currentGame.hisFcmToken)
    return
}

private fun bestGame(currentGame: GameInfo): GameInfo {
    val emptyFields = currentGame.fields.filter { f -> f.fieldState == FieldState.Empty }
    val computerHasWhite = currentGame.playesWhite == currentGame.hisFcmToken
    val games = ArrayList<GameInfo>()
    for (emptyField in emptyFields) {
        val newgame = GameInfo(currentGame.myName, currentGame.myFcmToken, currentGame.hisName, currentGame.hisFcmToken, currentGame.playesWhite, currentGame.gameMode, currentGame.gameLevel)
        newgame.computerSimulation = true
        for (field in currentGame.fields.filter { f -> f.fieldState != FieldState.Empty }) {
            newgame.fields[field.nr].fieldState = field.fieldState
        }

        var myScore = 0
        // Put my stone on this empty field and check if I win, this field scores 100
        newgame.move(emptyField.nr, newgame.hisFcmToken)
        when {
            newgame.gameState == GameState.WhiteWins -> {
                myScore = if (computerHasWhite) 100 else -100
            }
            newgame.gameState == GameState.BlackWins -> {
                myScore = if (computerHasWhite) -100 else 100
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
                    newgame.ring1(emptyField.nr) -> up = 4
                    newgame.ring2(emptyField.nr) -> up = 3
                    newgame.ring3(emptyField.nr) -> up = 2
                }
                myScore = Helper.randomNrInRange(1, up)

                if (currentGame.gameLevel == GameLevel.Intermediate || currentGame.gameLevel == GameLevel.Expert) {
                    myScore += newgame.winInOne(if (computerHasWhite) FieldState.White else FieldState.Black)
                    myScore += newgame.boardScore(if (computerHasWhite) FieldState.White else FieldState.Black)
                }
                if (currentGame.gameLevel == GameLevel.Expert) {
                    myScore += newgame.possibleWinInTwo(if (computerHasWhite) FieldState.White else FieldState.Black)
                    myScore += newgame.loseInTwo(if (computerHasWhite) FieldState.White else FieldState.Black)
                }
            }
        }

        if (myScore == 100) {
            // We win, so break out of the loop
            newgame.score = myScore
            games.add(newgame)
            break
        }

        var hisScore = 0
        // Put his stone on this empty field and check if he wins, this field scores 50
        newgame.move(emptyField.nr, newgame.myFcmToken)
        when {
            newgame.gameState == GameState.WhiteWins -> {
                hisScore = if (computerHasWhite) -100 else 100
            }
            newgame.gameState == GameState.BlackWins -> {
                hisScore = if (computerHasWhite) 100 else -100
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
        if (myScore == -100 && hisScore == 100) {
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

