package nl.whitedove.yavalath


fun computerMove(currentGame: GameInfo) {
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
                var up = 24
                when {
                    newgame.ring1(emptyField.nr) -> up = 15
                    newgame.ring2(emptyField.nr) -> up = 18
                    newgame.ring3(emptyField.nr) -> up = 21
                }
                newgame.score = Helper.randomNrInRange(1, up)
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
                var up = 24
                when {
                    newgame.ring1(emptyField.nr) -> up = 15
                    newgame.ring2(emptyField.nr) -> up = 18
                    newgame.ring3(emptyField.nr) -> up = 21
                }
                newgame.score = Helper.randomNrInRange(1, up)
            }
        }
        games.add(newgame)
    }
    // find the highest scoring move and apply that move

    val bestGame = games.toList().sortedByDescending { g -> g.score }.first()
    val bestFieldNr = bestGame.lastMove
    currentGame.move(bestFieldNr, currentGame.hisFcmToken)
}