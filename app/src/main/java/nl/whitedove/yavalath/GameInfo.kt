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

    private fun get7(): List<List<Int>> {
        val vertDown = listOf(
                listOf(0, 1, 19, 21, 6, 12, 13), listOf(1, 2, 20, 22, 7, 13, 14), listOf(2, 3, 21, 23, 8, 14, 15), listOf(3, 4, 22, 24, 9, 15, 16),
                listOf(5, 6, 27, 29, 12, 19, 20), listOf(6, 7, 28, 30, 13, 20, 21), listOf(7, 8, 29, 31, 14, 21, 22), listOf(8, 9, 30, 32, 15, 22, 23), listOf(9, 10, 31, 33, 16, 23, 24),
                listOf(11, 12, 35, 37, 19, 27, 28), listOf(12, 13, 36, 38, 20, 28, 29), listOf(13, 14, 37, 39, 21, 29, 30), listOf(14, 15, 38, 40, 22, 30, 31), listOf(15, 16, 39, 41, 23, 31, 32), listOf(16, 17, 40, 42, 24, 32, 33),
                listOf(19, 20, 43, 45, 28, 36, 37), listOf(20, 21, 44, 46, 29, 37, 38), listOf(21, 22, 45, 47, 30, 38, 39), listOf(22, 23, 46, 48, 31, 39, 40), listOf(23, 24, 47, 49, 32, 40, 41),
                listOf(28, 29, 50, 52, 37, 44, 45), listOf(29, 30, 51, 53, 38, 45, 46), listOf(30, 31, 52, 54, 39, 46, 47), listOf(31, 32, 53, 55, 40, 47, 48),
                listOf(37, 38, 56, 58, 45, 51, 52), listOf(38, 39, 57, 59, 46, 52, 53), listOf(39, 40, 58, 60, 47, 53, 54))

        val vertUp = vertDown.map { it.map { 60 - it } }

        val rightDown = listOf(
                listOf(4, 10, 7, 22, 9, 8, 15), listOf(10, 17, 14, 31, 16, 15, 23), listOf(17, 25, 22, 40, 24, 23, 32), listOf(25, 34, 31, 48, 33, 32, 41),
                listOf(3, 9, 6, 21, 8, 7, 14), listOf(9, 16, 13, 30, 15, 14, 22), listOf(16, 24, 21, 39, 23, 22, 31), listOf(24, 33, 30, 47, 32, 31, 40), listOf(33, 42, 39, 54, 41, 40, 48),
                listOf(2, 8, 5, 20, 7, 6, 13), listOf(8, 15, 12, 29, 14, 13, 21), listOf(15, 23, 20, 38, 22, 21, 30), listOf(23, 32, 29, 46, 31, 30, 39), listOf(32, 41, 38, 53, 40, 39, 47), listOf(41, 49, 46, 59, 48, 47, 54),
                listOf(7, 14, 11, 28, 13, 12, 20), listOf(14, 22, 19, 37, 21, 20, 29), listOf(22, 31, 28, 45, 30, 29, 38), listOf(31, 40, 37, 52, 39, 38, 46), listOf(40, 48, 45, 58, 47, 46, 53),
                listOf(13, 21, 18, 36, 20, 19, 28), listOf(21, 30, 27, 44, 29, 28, 37), listOf(30, 39, 36, 51, 38, 37, 45), listOf(39, 47, 44, 57, 46, 45, 52),
                listOf(20, 29, 26, 43, 28, 27, 36), listOf(29, 38, 35, 50, 37, 36, 44), listOf(38, 46, 43, 56, 45, 44, 51))

        val rightUp = rightDown.map { it.map { 60 - it } }

        val leftDown = listOf(
                listOf(26, 18, 44, 29, 27, 36, 28), listOf(18, 11, 37, 21, 19, 28, 20), listOf(11, 5, 29, 14, 12, 20, 13), listOf(5, 0, 21, 8, 6, 13, 7),
                listOf(35, 27, 45, 30, 36, 44, 48), listOf(27, 19, 45, 30, 28, 37, 29), listOf(19, 12, 38, 22, 20, 29, 21), listOf(12, 6, 30, 15, 13, 21, 14), listOf(6, 1, 22, 9, 7, 14, 8),
                listOf(43, 36, 57, 46, 44, 51, 45), listOf(36, 28, 46, 31, 37, 45, 49), listOf(28, 20, 46, 31, 29, 38, 30), listOf(20, 13, 39, 23, 21, 30, 22), listOf(13, 7, 31, 16, 14, 22, 15), listOf(7, 2, 23, 10, 8, 15, 9),
                listOf(44, 37, 58, 47, 45, 52, 46), listOf(37, 29, 47, 32, 28, 46, 50), listOf(29, 21, 47, 32, 30, 39, 31), listOf(21, 14, 40, 24, 22, 31, 23), listOf(14, 8, 32, 17, 15, 23, 16),
                listOf(45, 38, 59, 48, 46, 53, 47), listOf(38, 30, 48, 33, 29, 47, 51), listOf(30, 22, 48, 33, 31, 40, 32), listOf(22, 15, 41, 25, 23, 32, 24),
                listOf(46, 39, 60, 49, 47, 54, 48), listOf(39, 31, 49, 34, 30, 48, 52), listOf(31, 23, 49, 34, 32, 41, 33))

        val leftUp = leftDown.map { it.map { 60 - it } }

        val veeDown = listOf(
                listOf(0, 1, 14, 21, 3, 2, 8), listOf(1, 2, 15, 22, 4, 3, 9),
                listOf(5, 6, 21, 29, 8, 7, 14), listOf(6, 7, 22, 30, 9, 8, 15), listOf(7, 8, 23, 31, 10, 9, 16),
                listOf(11, 12, 29, 37, 14, 13, 21), listOf(12, 13, 30, 38, 15, 14, 22), listOf(13, 14, 31, 39, 16, 15, 23), listOf(14, 15, 32, 40, 17, 16, 24),
                listOf(18, 19, 37, 44, 21, 20, 29), listOf(19, 20, 38, 45, 22, 21, 30), listOf(20, 21, 39, 46, 23, 22, 31), listOf(21, 22, 40, 47, 24, 23, 32), listOf(22, 23, 41, 48, 25, 24, 33),
                listOf(26, 27, 44, 50, 29, 28, 37), listOf(27, 28, 45, 51, 30, 29, 38), listOf(28, 29, 46, 52, 31, 30, 39), listOf(29, 30, 47, 53, 32, 31, 40), listOf(30, 31, 48, 54, 33, 32, 41), listOf(31, 32, 49, 55, 34, 33, 42),
                listOf(35, 36, 51, 56, 38, 37, 45), listOf(36, 37, 52, 57, 39, 38, 46), listOf(37, 38, 53, 58, 40, 39, 47), listOf(38, 39, 54, 59, 41, 40, 48), listOf(39, 40, 55, 60, 42, 41, 49))

        val veeUp = veeDown.map { it.map { 60 - it } }

        val rightVeeDown = listOf(
                listOf(4, 10, 23, 22, 25, 17, 24), listOf(10, 17, 32, 31, 34, 25, 33),
                listOf(3, 9, 22, 21, 24, 16, 23), listOf(9, 16, 31, 30, 33, 24, 32), listOf(16, 24, 40, 39, 42, 33, 41),
                listOf(2, 8, 21, 20, 23, 15, 22), listOf(8, 15, 30, 29, 32, 23, 31), listOf(15, 23, 39, 38, 41, 32, 40), listOf(23, 32, 47, 46, 49, 41, 48),
                listOf(1, 7, 20, 19, 22, 14, 21), listOf(7, 14, 29, 28, 31, 22, 30), listOf(14, 22, 38, 37, 40, 31, 39), listOf(22, 31, 46, 45, 48, 40, 47), listOf(31, 40, 53, 52, 55, 48, 54),
                listOf(0, 6, 19, 18, 21, 13, 20), listOf(6, 13, 28, 27, 30, 21, 29), listOf(13, 21, 37, 36, 39, 30, 38), listOf(21, 30, 45, 44, 47, 39, 46), listOf(30, 39, 52, 51, 54, 47, 53), listOf(39, 47, 58, 57, 60, 54, 59),
                listOf(5, 12, 27, 26, 29, 20, 28), listOf(12, 20, 36, 35, 38, 29, 37), listOf(20, 29, 44, 43, 46, 38, 45), listOf(29, 38, 51, 50, 53, 46, 52), listOf(38, 46, 57, 56, 59, 53, 58))

        val rightVeeUp = rightVeeDown.map { it.map { 60 - it } }

        val leftVeeDown = listOf(
                listOf(26, 18, 20, 29, 5, 11, 12), listOf(18, 11, 13, 21, 0, 5, 6),
                listOf(35, 27, 29, 38, 12, 19, 20), listOf(27, 19, 21, 30, 6, 12, 13), listOf(19, 12, 14, 22, 1, 6, 7),
                listOf(43, 36, 38, 46, 20, 28, 29), listOf(36, 28, 30, 39, 13, 20, 21), listOf(28, 20, 22, 31, 7, 13, 14), listOf(20, 13, 15, 23, 2, 7, 8),
                listOf(50, 44, 46, 53, 29, 37, 38), listOf(44, 37, 39, 47, 21, 29, 30), listOf(37, 29, 31, 40, 14, 21, 22), listOf(29, 21, 23, 32, 8, 14, 15), listOf(21, 14, 16, 24, 3, 8, 9),
                listOf(56, 51, 53, 59, 38, 45, 46), listOf(51, 45, 46, 54, 30, 38, 39), listOf(45, 38, 40, 48, 22, 30, 31), listOf(38, 30, 32, 41, 15, 22, 23), listOf(30, 22, 24, 33, 9, 15, 16), listOf(22, 15, 17, 25, 4, 9, 10),
                listOf(57, 52, 54, 60, 39, 46, 47), listOf(52, 46, 47, 55, 31, 39, 40), listOf(46, 39, 41, 49, 23, 31, 32), listOf(39, 31, 33, 42, 16, 23, 24), listOf(31, 23, 25, 34, 10, 16, 17))

        val leftVeeUp = leftVeeDown.map { it.map { 60 - it } }

        val wideDown = listOf(
                listOf(0, 1, 16, 24, 3, 2, 9), listOf(1, 2, 17, 25, 4, 3, 10),
                listOf(5, 6, 23, 32, 8, 7, 15), listOf(6, 7, 24, 34, 9, 8, 16), listOf(7, 8, 25, 34, 10, 9, 17),
                listOf(11, 12, 31, 40, 14, 13, 22), listOf(12, 13, 32, 41, 15, 14, 23), listOf(13, 14, 33, 42, 16, 15, 24),
                listOf(18, 19, 39, 47, 21, 20, 30), listOf(19, 20, 40, 48, 22, 21, 31), listOf(20, 21, 41, 49, 23, 22, 32),
                listOf(26, 27, 46, 53, 29, 28, 38), listOf(27, 28, 47, 54, 30, 29, 39), listOf(28, 29, 48, 55, 31, 30, 40),
                listOf(35, 36, 53, 59, 38, 37, 46), listOf(36, 37, 54, 60, 39, 38, 47))

        val wideUp = wideDown.map { it.map { 60 - it } }

        // TODO hier gebleven

        val line = listOf(
                listOf(11, 12, 16, 17, 14, 13, 15),
                listOf(18, 19, 23, 24, 21, 20, 22), listOf(19, 20, 24, 25, 22, 21, 23),
                listOf(26, 27, 31, 32, 29, 28, 30), listOf(27, 28, 32, 33, 30, 29, 31), listOf(28, 29, 33, 34, 31, 30, 32),
                listOf(35, 36, 40, 41, 38, 37, 39), listOf(36, 37, 41, 42, 39, 38, 40),
                listOf(43, 44, 48, 49, 46, 45, 47))

        val rightLine = listOf(
                listOf(2, 8, 41, 49, 23, 15, 32),
                listOf(1, 7, 40, 48, 22, 14, 31), listOf(7, 14, 48, 55, 31, 22, 40),
                listOf(0, 6, 39, 47, 21, 13, 30), listOf(6, 13, 47, 54, 30, 21, 39), listOf(13, 21, 54, 60, 39, 30, 47),
                listOf(5, 12, 46, 53, 29, 20, 38), listOf(12, 20, 53, 59, 38, 29, 46),
                listOf(11, 19, 52, 58, 37, 28, 45))

        val leftLine = listOf(
                listOf(43, 36, 7, 2, 20, 28, 13),
                listOf(50, 44, 14, 8, 29, 37, 21), listOf(44, 37, 8, 3, 21, 29, 14),
                listOf(56, 51, 22, 15, 38, 45, 30), listOf(51, 45, 15, 9, 30, 38, 22), listOf(45, 38, 9, 4, 22, 30, 15),
                listOf(57, 52, 23, 16, 39, 46, 31), listOf(52, 46, 16, 10, 31, 39, 23),
                listOf(58, 53, 24, 17, 40, 47, 32))

        val samen = vertDown + vertUp + rightDown + rightUp + leftDown + leftUp + veeDown + veeUp + rightVeeDown + rightVeeUp + leftVeeDown + leftVeeUp + line + rightLine + leftLine + wideDown + wideUp
        return samen
    }

    fun ring1(fieldNr: Int): Boolean {
        val ring = listOf(0, 1, 2, 3, 4, 10, 17, 25, 34, 42, 49, 55, 60, 59, 58, 57, 56, 50, 43, 35, 26, 18, 11, 5)
        return ring.contains(fieldNr)
    }

    fun ring2(fieldNr: Int): Boolean {
        val ring = listOf(6, 7, 8, 9, 16, 24, 33, 41, 48, 54, 53, 52, 51, 44, 36, 27, 19, 12)
        return ring.contains(fieldNr)
    }

    fun ring3(fieldNr: Int): Boolean {
        val ring = listOf(13, 14, 15, 23, 32, 40, 47, 46, 45, 37, 28, 20)
        return ring.contains(fieldNr)
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

        for (g5 in this.get5()) {
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

        for (g4 in this.get4()) {
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

        for (g3 in this.get3()) {
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
        for (g4 in this.get4()) {
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
        for (g4 in this.get4()) {
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
        for (g4 in this.get4()) {
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
        for (g4 in this.get4()) {
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
        for (g7 in this.get7()) {
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
        val lastMove = this.lastMove
        for (g7 in this.get7()) {
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