package nl.whitedove.yavalath

import org.joda.time.DateTime

class GameInfo(myName: String, myFcmToken: String, hisName: String, hisToken: String, playesWhite: String) {
    var created: DateTime
    var gameState: GameState
    var fields: ArrayList<Field>
    var myName: String
    var hisName: String
    var myFcmToken: String
    var hisFcmToken: String
    var playesWhite: String
    var playerWhite: String
    var playerBlack: String
    var playerToMove: String
    var winningFields: List<Int> = listOf(0)
    var winner: String

    init {
        this.created = DateTime.now()
        this.gameState = GameState.Unknown
        this.fields = ArrayList()
        this.myName = myName
        this.myFcmToken = myFcmToken
        this.hisName = hisName
        this.hisFcmToken = hisToken
        this.playesWhite = playesWhite
        for (i in 0..60) {
            this.fields.add(Field(i))
        }
        if (playesWhite == myFcmToken) {
            this.playerWhite = myName
            this.playerBlack = hisName
            this.playerToMove = myName
        } else {
            this.playerWhite = hisName
            this.playerBlack = myName
            this.playerToMove = hisName
        }
        this.winner = ""
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

    fun movesPlayed(): Int {
        var nr = 0
        for (f in fields) {
            if (f.fieldState == FieldState.White || f.fieldState == FieldState.Black)
                nr++
        }
        return nr
    }

    fun move(nr: Int, playedByToken: String) {
        val fld = this.fields.get(nr)

        if (playedByToken == this.playesWhite) {
            fld.fieldState = FieldState.White
            this.playerToMove = this.playerBlack
        } else {
            fld.fieldState = FieldState.Black
            this.playerToMove = this.playerWhite
        }

        this.winningFields = testWinner()

        if (this.winningFields.size == 3) {
            if (playedByToken == this.playesWhite) {
                this.winner = this.playerBlack
            } else {
                this.winner = this.playerWhite
            }
        }

        if (winningFields.size == 4) {
            if (playedByToken == this.playesWhite) {
                this.winner = this.playerWhite
            } else {
                this.winner = this.playerBlack
            }
        }
    }

    private fun testWinner(): List<Int> {
        for (g3 in get3()) {

            if (this.fields.get(g3.get(0)).fieldState == FieldState.Black &&
                    this.fields.get(g3.get(1)).fieldState == FieldState.Black &&
                    this.fields.get(g3.get(2)).fieldState == FieldState.Black)
                return listOf(g3.get(0), g3.get(1), g3.get(2))

            if (this.fields.get(g3.get(0)).fieldState == FieldState.White &&
                    this.fields.get(g3.get(1)).fieldState == FieldState.White &&
                    this.fields.get(g3.get(2)).fieldState == FieldState.White)
                return listOf(g3.get(0), g3.get(1), g3.get(2))
        }

        for (g4 in get4()) {

            if (this.fields.get(g4.get(0)).fieldState == FieldState.White &&
                    this.fields.get(g4.get(1)).fieldState == FieldState.White &&
                    this.fields.get(g4.get(2)).fieldState == FieldState.White &&
                    this.fields.get(g4.get(3)).fieldState == FieldState.White)
                return listOf(g4.get(0), g4.get(1), g4.get(2), g4.get(3))

            if (this.fields.get(g4.get(0)).fieldState == FieldState.Black &&
                    this.fields.get(g4.get(1)).fieldState == FieldState.Black &&
                    this.fields.get(g4.get(2)).fieldState == FieldState.Black &&
                    this.fields.get(g4.get(3)).fieldState == FieldState.White)
                return listOf(g4.get(0), g4.get(1), g4.get(2), g4.get(3))
        }

        return listOf(0);
    }
}