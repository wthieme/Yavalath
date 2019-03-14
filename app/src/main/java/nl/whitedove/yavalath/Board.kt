package nl.whitedove.yavalath

internal object Board {

    val list3: List<List<Int>>
    val list4: List<List<Int>>
    val list5: List<List<Int>>
    val list7: List<List<Int>>
    val ring1: List<Int>
    val ring2: List<Int>
    val ring3: List<Int>

    init {
        list3 = get3()
        list4 = get4()
        list5 = get5()
        list7 = get7()
        ring1 = getR1()
        ring2 = getR2()
        ring3 = getR3()
    }

    private fun rotate(fieldNr: Int): Int {
        when (fieldNr) {
            0 -> return 4
            1 -> return 10
            2 -> return 17
            3 -> return 25
            4 -> return 34
            5 -> return 3
            6 -> return 9
            7 -> return 16
            8 -> return 24
            9 -> return 33
            10 -> return 42
            11 -> return 2
            12 -> return 8
            13 -> return 15
            14 -> return 23
            15 -> return 32
            16 -> return 41
            17 -> return 49
            18 -> return 1
            19 -> return 7
            20 -> return 14
            21 -> return 22
            22 -> return 31
            23 -> return 40
            24 -> return 48
            25 -> return 55
            26 -> return 0
            27 -> return 6
            28 -> return 13
            29 -> return 21
            30 -> return 30
            31 -> return 39
            32 -> return 47
            33 -> return 54
            34 -> return 60
            35 -> return 5
            36 -> return 12
            37 -> return 20
            38 -> return 29
            39 -> return 38
            40 -> return 46
            41 -> return 53
            42 -> return 59
            43 -> return 11
            44 -> return 19
            45 -> return 28
            46 -> return 37
            47 -> return 45
            48 -> return 52
            49 -> return 58
            50 -> return 18
            51 -> return 27
            52 -> return 36
            53 -> return 44
            54 -> return 51
            55 -> return 57
            56 -> return 26
            57 -> return 35
            58 -> return 43
            59 -> return 50
            60 -> return 56
        }
        return -1
    }

    private fun herhaal(l: List<Int>, n: Int): List<List<Int>> {
        var lreturn = listOf(l)
        for (i in 1..n - 1) {
            lreturn = lreturn + listOf(l.map { it + i })
        }
        return lreturn
    }

    private fun get3(): List<List<Int>> {
        val l1 = Board.herhaal(listOf(0, 1, 2), 3) +
                Board.herhaal(listOf(5, 6, 7), 4) +
                Board.herhaal(listOf(11, 12, 13), 5) +
                Board.herhaal(listOf(18, 19, 20), 6) +
                Board.herhaal(listOf(26, 27, 28), 7) +
                Board.herhaal(listOf(35, 36, 37), 6) +
                Board.herhaal(listOf(43, 44, 45), 5) +
                Board.herhaal(listOf(50, 51, 52), 4) +
                Board.herhaal(listOf(56, 57, 58), 3)

        val l2 = l1.map { i1 -> i1.map { rotate(it) } }
        val l3 = l2.map { i1 -> i1.map { rotate(it) } }
        return l1 + l2 + l3
    }

    private fun get4(): List<List<Int>> {
        val l1 = Board.herhaal(listOf(0, 1, 2, 3), 2) +
                Board.herhaal(listOf(5, 6, 7, 8), 3) +
                Board.herhaal(listOf(11, 12, 13, 14), 4) +
                Board.herhaal(listOf(18, 19, 20, 21), 5) +
                Board.herhaal(listOf(26, 27, 28, 29), 6) +
                Board.herhaal(listOf(35, 36, 37, 38), 5) +
                Board.herhaal(listOf(43, 44, 45, 46), 4) +
                Board.herhaal(listOf(50, 51, 52, 53), 3) +
                Board.herhaal(listOf(56, 57, 58, 59), 2)
        val l2 = l1.map { i1 -> i1.map { rotate(it) } }
        val l3 = l2.map { i1 -> i1.map { rotate(it) } }
        return l1 + l2 + l3
    }

    private fun get5(): List<List<Int>> {
        val l1 = Board.herhaal(listOf(0, 1, 2, 3, 4), 1) +
                Board.herhaal(listOf(5, 6, 7, 8, 9), 2) +
                Board.herhaal(listOf(11, 12, 13, 14, 15), 3) +
                Board.herhaal(listOf(18, 19, 20, 21, 22), 4) +
                Board.herhaal(listOf(26, 27, 28, 29, 30), 5) +
                Board.herhaal(listOf(35, 36, 37, 38, 39), 4) +
                Board.herhaal(listOf(43, 44, 45, 46, 47), 3) +
                Board.herhaal(listOf(50, 51, 52, 53, 54), 2) +
                Board.herhaal(listOf(56, 57, 58, 59, 60), 1)

        val l2 = l1.map { i1 -> i1.map { rotate(it) } }
        val l3 = l2.map { i1 -> i1.map { rotate(it) } }
        return l1 + l2 + l3
    }

    private fun get7(): List<List<Int>> {
        val lx1 = Board.herhaal(listOf(0, 1, 19, 21, 6, 12, 13), 4) +
                Board.herhaal(listOf(5, 6, 27, 29, 12, 19, 20), 5) +
                Board.herhaal(listOf(11, 12, 35, 37, 19, 27, 28), 6) +
                Board.herhaal(listOf(19, 20, 43, 45, 28, 36, 37), 5) +
                Board.herhaal(listOf(28, 29, 50, 52, 37, 44, 45), 4) +
                Board.herhaal(listOf(37, 38, 56, 58, 45, 51, 52), 3)

        val lx2 = lx1.map { i1 -> i1.map { rotate(it) } }
        val lx3 = lx2.map { i1 -> i1.map { rotate(it) } }
        val lx4 = lx3.map { i1 -> i1.map { rotate(it) } }
        val lx5 = lx4.map { i1 -> i1.map { rotate(it) } }
        val lx6 = lx5.map { i1 -> i1.map { rotate(it) } }

        val lv1 = Board.herhaal(listOf(0, 1, 14, 21, 3, 2, 8), 2) +
                Board.herhaal(listOf(5, 6, 21, 29, 8, 7, 14), 3) +
                Board.herhaal(listOf(11, 12, 29, 37, 14, 13, 21), 4) +
                Board.herhaal(listOf(18, 19, 37, 44, 21, 20, 29), 5) +
                Board.herhaal(listOf(26, 27, 44, 50, 29, 28, 37), 6) +
                Board.herhaal(listOf(35, 36, 51, 56, 38, 37, 45), 5)

        val lv2 = lv1.map { i1 -> i1.map { rotate(it) } }
        val lv3 = lv2.map { i1 -> i1.map { rotate(it) } }
        val lv4 = lv3.map { i1 -> i1.map { rotate(it) } }
        val lv5 = lv4.map { i1 -> i1.map { rotate(it) } }
        val lv6 = lv5.map { i1 -> i1.map { rotate(it) } }

        val lvr1 = Board.herhaal(listOf(0, 1, 8, 21, 3, 2, 14), 2) +
                Board.herhaal(listOf(5, 6, 14, 29, 8, 7, 21), 3) +
                Board.herhaal(listOf(11, 12, 21, 37, 14, 13, 29), 4) +
                Board.herhaal(listOf(18, 19, 29, 44, 21, 20, 37), 5) +
                Board.herhaal(listOf(26, 27, 37, 50, 29, 28, 44), 6) +
                Board.herhaal(listOf(35, 36, 45, 56, 38, 37, 51), 5)

        val lvr2 = lvr1.map { i1 -> i1.map { rotate(it) } }
        val lvr3 = lvr2.map { i1 -> i1.map { rotate(it) } }
        val lvr4 = lvr3.map { i1 -> i1.map { rotate(it) } }
        val lvr5 = lvr4.map { i1 -> i1.map { rotate(it) } }
        val lvr6 = lvr5.map { i1 -> i1.map { rotate(it) } }

        val lvl1 = Board.herhaal(listOf(0, 13, 18, 19, 21, 6, 20), 5) +
                Board.herhaal(listOf(5, 20, 26, 27, 29, 12, 18), 6) +
                Board.herhaal(listOf(12, 29, 35, 36, 38, 20, 37), 5) +
                Board.herhaal(listOf(20, 38, 43, 44, 46, 29, 45), 4) +
                Board.herhaal(listOf(29, 46, 50, 51, 53, 38, 52), 3) +
                Board.herhaal(listOf(38, 53, 56, 57, 59, 46, 58), 2)

        val lvl2 = lvl1.map { i1 -> i1.map { rotate(it) } }
        val lvl3 = lvl2.map { i1 -> i1.map { rotate(it) } }
        val lvl4 = lvl3.map { i1 -> i1.map { rotate(it) } }
        val lvl5 = lvl4.map { i1 -> i1.map { rotate(it) } }
        val lvl6 = lvl5.map { i1 -> i1.map { rotate(it) } }

        val lw1 = Board.herhaal(listOf(0, 1, 16, 24, 3, 2, 9), 2) +
                Board.herhaal(listOf(5, 6, 23, 32, 8, 7, 15), 3) +
                Board.herhaal(listOf(11, 12, 31, 40, 14, 13, 22), 3) +
                Board.herhaal(listOf(18, 19, 39, 47, 21, 20, 30), 3) +
                Board.herhaal(listOf(26, 27, 46, 53, 29, 28, 38), 3) +
                Board.herhaal(listOf(35, 36, 53, 59, 38, 37, 46), 2)

        val lw2 = lw1.map { i1 -> i1.map { rotate(it) } }
        val lw3 = lw2.map { i1 -> i1.map { rotate(it) } }
        val lw4 = lw3.map { i1 -> i1.map { rotate(it) } }
        val lw5 = lw4.map { i1 -> i1.map { rotate(it) } }
        val lw6 = lw5.map { i1 -> i1.map { rotate(it) } }

        val lwr1 = Board.herhaal(listOf(0, 1, 9, 24, 3, 2, 16), 2) +
                Board.herhaal(listOf(5, 6, 15, 32, 8, 7, 23), 3) +
                Board.herhaal(listOf(11, 12, 22, 40, 14, 13, 31), 3) +
                Board.herhaal(listOf(18, 19, 30, 47, 21, 20, 39), 3) +
                Board.herhaal(listOf(26, 27, 38, 53, 29, 28, 46), 3) +
                Board.herhaal(listOf(35, 36, 46, 59, 38, 37, 53), 2)

        val lwr2 = lwr1.map { i1 -> i1.map { rotate(it) } }
        val lwr3 = lwr2.map { i1 -> i1.map { rotate(it) } }
        val lwr4 = lwr3.map { i1 -> i1.map { rotate(it) } }
        val lwr5 = lwr4.map { i1 -> i1.map { rotate(it) } }
        val lwr6 = lwr5.map { i1 -> i1.map { rotate(it) } }

        val lr1 = Board.herhaal(listOf(0, 6, 20, 23, 21, 13, 22), 3) +
                Board.herhaal(listOf(5, 12, 28, 31, 29, 20, 30), 4) +
                Board.herhaal(listOf(11, 19, 36, 39, 37, 28, 38), 4) +
                Board.herhaal(listOf(18, 27, 43, 46, 44, 36, 45), 4) +
                Board.herhaal(listOf(27, 36, 50, 53, 51, 44, 52), 3) +
                Board.herhaal(listOf(36, 44, 56, 59, 57, 51, 58), 2)

        val lr2 = lr1.map { i1 -> i1.map { rotate(it) } }
        val lr3 = lr2.map { i1 -> i1.map { rotate(it) } }
        val lr4 = lr3.map { i1 -> i1.map { rotate(it) } }
        val lr5 = lr4.map { i1 -> i1.map { rotate(it) } }
        val lr6 = lr5.map { i1 -> i1.map { rotate(it) } }

        val ll1 = Board.herhaal(listOf(0, 6, 19, 22, 21, 13, 20), 4) +
                Board.herhaal(listOf(5, 12, 27, 30, 29, 20, 28), 5) +
                Board.herhaal(listOf(11, 19, 35, 38, 37, 28, 36), 5) +
                Board.herhaal(listOf(19, 28, 43, 46, 45, 37, 44), 4) +
                Board.herhaal(listOf(28, 37, 50, 53, 52, 45, 51), 3) +
                Board.herhaal(listOf(37, 45, 56, 59, 58, 52, 57), 2)

        val ll2 = ll1.map { i1 -> i1.map { rotate(it) } }
        val ll3 = ll2.map { i1 -> i1.map { rotate(it) } }
        val ll4 = ll3.map { i1 -> i1.map { rotate(it) } }
        val ll5 = ll4.map { i1 -> i1.map { rotate(it) } }
        val ll6 = ll5.map { i1 -> i1.map { rotate(it) } }

        val rl1 = Board.herhaal(listOf(2, 7, 18, 21, 20, 13, 19), 3) +
                Board.herhaal(listOf(7, 13, 26, 29, 28, 20, 27), 4) +
                Board.herhaal(listOf(14, 21, 35, 38, 37, 29, 36), 4) +
                Board.herhaal(listOf(22, 30, 43, 46, 45, 38, 44), 4) +
                Board.herhaal(listOf(31, 39, 50, 53, 52, 46, 51), 3) +
                Board.herhaal(listOf(40, 47, 56, 59, 58, 53, 57), 2)

        val rl2 = rl1.map { i1 -> i1.map { rotate(it) } }
        val rl3 = rl2.map { i1 -> i1.map { rotate(it) } }
        val rl4 = rl3.map { i1 -> i1.map { rotate(it) } }
        val rl5 = rl4.map { i1 -> i1.map { rotate(it) } }
        val rl6 = rl5.map { i1 -> i1.map { rotate(it) } }

        val rr1 = Board.herhaal(listOf(1, 6, 18, 21, 19, 12, 20), 4) +
                Board.herhaal(listOf(6, 12, 26, 29, 27, 19, 28), 5) +
                Board.herhaal(listOf(13, 20, 35, 38, 36, 28, 37), 5) +
                Board.herhaal(listOf(21, 29, 43, 46, 44, 37, 45), 4) +
                Board.herhaal(listOf(30, 38, 50, 53, 51, 45, 52), 3) +
                Board.herhaal(listOf(39, 46, 56, 59, 57, 52, 58), 2)

        val rr2 = rr1.map { i1 -> i1.map { rotate(it) } }
        val rr3 = rr2.map { i1 -> i1.map { rotate(it) } }
        val rr4 = rr3.map { i1 -> i1.map { rotate(it) } }
        val rr5 = rr4.map { i1 -> i1.map { rotate(it) } }
        val rr6 = rr5.map { i1 -> i1.map { rotate(it) } }

        val xx1 = Board.herhaal(listOf(0, 7, 21, 28, 13, 6, 20), 4) +
                Board.herhaal(listOf(5, 13, 29, 36, 20, 12, 28), 5) +
                Board.herhaal(listOf(11, 20, 37, 43, 28, 19, 36), 6) +
                Board.herhaal(listOf(19, 29, 45, 50, 37, 28, 44), 5) +
                Board.herhaal(listOf(28, 38, 52, 56, 45, 37, 51), 4)

        val xx2 = xx1.map { i1 -> i1.map { rotate(it) } }
        val xx3 = xx2.map { i1 -> i1.map { rotate(it) } }
        val xx4 = xx3.map { i1 -> i1.map { rotate(it) } }
        val xx5 = xx4.map { i1 -> i1.map { rotate(it) } }
        val xx6 = xx5.map { i1 -> i1.map { rotate(it) } }

        val ls1 = Board.herhaal(listOf(11, 12, 16, 17, 14, 13, 15), 1) +
                Board.herhaal(listOf(18, 19, 23, 24, 21, 20, 22), 2) +
                Board.herhaal(listOf(26, 27, 31, 32, 29, 28, 30), 3) +
                Board.herhaal(listOf(35, 36, 40, 41, 38, 37, 39), 2) +
                Board.herhaal(listOf(43, 44, 48, 49, 46, 45, 47), 1)

        val ls2 = ls1.map { i1 -> i1.map { rotate(it) } }
        val ls3 = ls2.map { i1 -> i1.map { rotate(it) } }
        return lx1 + lx2 + lx3 + lx4 + lx5 + lx6 +
                lv1 + lv2 + lv3 + lv4 + lv5 + lv6 +
                lvl1 + lvl2 + lvl3 + lvl4 + lvl5 + lvl6 +
                lvr1 + lvr2 + lvr3 + lvr4 + lvr5 + lvr6 +
                lw1 + lw2 + lw3 + lw4 + lw5 + lw6 +
                lwr1 + lwr2 + lwr3 + lwr4 + lwr5 + lwr6 +
                lr1 + lr2 + lr3 + lr4 + lr5 + lr6 +
                ll1 + ll2 + ll3 + ll4 + ll5 + ll6 +
                rl1 + rl2 + rl3 + rl4 + rl5 + rl6 +
                rr1 + rr2 + rr3 + rr4 + rr5 + rr6 +
                xx1 + xx2 + xx3 + xx4 + xx5 + xx6 +
                ls1 + ls2 + ls3
    }

    private fun getR1(): List<Int> {
        val r1 = listOf(0, 1, 2, 3, 4)
        val r2 = r1.map { rotate(it) }
        val r3 = r2.map { rotate(it) }
        val r4 = r3.map { rotate(it) }
        val r5 = r4.map { rotate(it) }
        val r6 = r5.map { rotate(it) }
        return r1 + r2 + r3 + r4 + r5 + r6
    }

    private fun getR2(): List<Int> {
        val r1 = listOf(6, 7, 8, 9)
        val r2 = r1.map { rotate(it) }
        val r3 = r2.map { rotate(it) }
        val r4 = r3.map { rotate(it) }
        val r5 = r4.map { rotate(it) }
        val r6 = r5.map { rotate(it) }
        return r1 + r2 + r3 + r4 + r5 + r6
    }

    private fun getR3(): List<Int> {
        val r1 = listOf(13, 14, 15)
        val r2 = r1.map { rotate(it) }
        val r3 = r2.map { rotate(it) }
        val r4 = r3.map { rotate(it) }
        val r5 = r4.map { rotate(it) }
        val r6 = r5.map { rotate(it) }
        return r1 + r2 + r3 + r4 + r5 + r6
    }
}
