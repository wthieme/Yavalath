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

    private fun get3(): List<List<Int>> {
        val l1 = listOf(
                listOf(0, 1, 2), listOf(1, 2, 3), listOf(2, 3, 4),
                listOf(5, 6, 7), listOf(6, 7, 8), listOf(7, 8, 9), listOf(8, 9, 10),
                listOf(11, 12, 13), listOf(12, 13, 14), listOf(13, 14, 15), listOf(14, 15, 16), listOf(15, 16, 17),
                listOf(18, 19, 20), listOf(19, 20, 21), listOf(20, 21, 22), listOf(21, 22, 23), listOf(22, 23, 24), listOf(23, 24, 25),
                listOf(26, 27, 28), listOf(27, 28, 29), listOf(28, 29, 30), listOf(29, 30, 31), listOf(30, 31, 32), listOf(31, 32, 33), listOf(32, 33, 34),
                listOf(35, 36, 37), listOf(36, 37, 38), listOf(37, 38, 39), listOf(38, 39, 40), listOf(39, 40, 41), listOf(40, 41, 42),
                listOf(43, 44, 45), listOf(44, 45, 46), listOf(45, 46, 47), listOf(46, 47, 48), listOf(47, 48, 49),
                listOf(50, 51, 52), listOf(51, 52, 53), listOf(52, 53, 54), listOf(53, 54, 55),
                listOf(56, 57, 58), listOf(57, 58, 59), listOf(58, 59, 60))
        val l2 = l1.map { i1 -> i1.map { rotate(it) } }
        val l3 = l2.map { i1 -> i1.map { rotate(it) } }
        return l1 + l2 + l3
    }

    private fun get4(): List<List<Int>> {
        val l1 = listOf(
                listOf(0, 1, 2, 3), listOf(1, 2, 3, 4),
                listOf(5, 6, 7, 8), listOf(6, 7, 8, 9), listOf(7, 8, 9, 10),
                listOf(11, 12, 13, 14), listOf(12, 13, 14, 15), listOf(13, 14, 15, 16), listOf(14, 15, 16, 17),
                listOf(18, 19, 20, 21), listOf(19, 20, 21, 22), listOf(20, 21, 22, 23), listOf(21, 22, 23, 24), listOf(22, 23, 24, 25),
                listOf(26, 27, 28, 29), listOf(27, 28, 29, 30), listOf(28, 29, 30, 31), listOf(29, 30, 31, 32), listOf(30, 31, 32, 33), listOf(31, 32, 33, 34),
                listOf(35, 36, 37, 38), listOf(36, 37, 38, 39), listOf(37, 38, 39, 40), listOf(38, 39, 40, 41), listOf(39, 40, 41, 42),
                listOf(43, 44, 45, 46), listOf(44, 45, 46, 47), listOf(45, 46, 47, 48), listOf(46, 47, 48, 49),
                listOf(50, 51, 52, 53), listOf(51, 52, 53, 54), listOf(52, 53, 54, 55),
                listOf(56, 57, 58, 59), listOf(57, 58, 59, 60))
        val l2 = l1.map { i1 -> i1.map { rotate(it) } }
        val l3 = l2.map { i1 -> i1.map { rotate(it) } }
        return l1 + l2 + l3
    }

    private fun get5(): List<List<Int>> {
        val l1 = listOf(
                listOf(0, 1, 2, 3, 4),
                listOf(5, 6, 7, 8, 9), listOf(6, 7, 8, 9, 10),
                listOf(11, 12, 13, 14, 15), listOf(12, 13, 14, 15, 16), listOf(13, 14, 15, 16, 17),
                listOf(18, 19, 20, 21, 22), listOf(19, 20, 21, 22, 23), listOf(20, 21, 22, 23, 24), listOf(21, 22, 23, 24, 25),
                listOf(26, 27, 28, 29, 30), listOf(27, 28, 29, 30, 31), listOf(28, 29, 30, 31, 32), listOf(29, 30, 31, 32, 33), listOf(30, 31, 32, 33, 34),
                listOf(35, 36, 37, 38, 39), listOf(36, 37, 38, 39, 40), listOf(37, 38, 39, 40, 41), listOf(38, 39, 40, 41, 42),
                listOf(43, 44, 45, 46, 47), listOf(44, 45, 46, 47, 48), listOf(45, 46, 47, 48, 49),
                listOf(50, 51, 52, 53, 54), listOf(51, 52, 53, 54, 55),
                listOf(56, 57, 58, 59, 60))

        val l2 = l1.map { i1 -> i1.map { rotate(it) } }
        val l3 = l2.map { i1 -> i1.map { rotate(it) } }
        return l1 + l2 + l3
    }

    private fun get7(): List<List<Int>> {
        val lx1 = listOf(
                listOf(0, 1, 19, 21, 6, 12, 13), listOf(1, 2, 20, 22, 7, 13, 14), listOf(2, 3, 21, 23, 8, 14, 15), listOf(3, 4, 22, 24, 9, 15, 16),
                listOf(5, 6, 27, 29, 12, 19, 20), listOf(6, 7, 28, 30, 13, 20, 21), listOf(7, 8, 29, 31, 14, 21, 22), listOf(8, 9, 30, 32, 15, 22, 23), listOf(9, 10, 31, 33, 16, 23, 24),
                listOf(11, 12, 35, 37, 19, 27, 28), listOf(12, 13, 36, 38, 20, 28, 29), listOf(13, 14, 37, 39, 21, 29, 30), listOf(14, 15, 38, 40, 22, 30, 31), listOf(15, 16, 39, 41, 23, 31, 32), listOf(16, 17, 40, 42, 24, 32, 33),
                listOf(19, 20, 43, 45, 28, 36, 37), listOf(20, 21, 44, 46, 29, 37, 38), listOf(21, 22, 45, 47, 30, 38, 39), listOf(22, 23, 46, 48, 31, 39, 40), listOf(23, 24, 47, 49, 32, 40, 41),
                listOf(28, 29, 50, 52, 37, 44, 45), listOf(29, 30, 51, 53, 38, 45, 46), listOf(30, 31, 52, 54, 39, 46, 47), listOf(31, 32, 53, 55, 40, 47, 48),
                listOf(37, 38, 56, 58, 45, 51, 52), listOf(38, 39, 57, 59, 46, 52, 53), listOf(39, 40, 58, 60, 47, 53, 54))

        val lx2 = lx1.map { i1 -> i1.map { rotate(it) } }
        val lx3 = lx2.map { i1 -> i1.map { rotate(it) } }
        val lx4 = lx3.map { i1 -> i1.map { rotate(it) } }
        val lx5 = lx4.map { i1 -> i1.map { rotate(it) } }
        val lx6 = lx5.map { i1 -> i1.map { rotate(it) } }

        val lv1 = listOf(
                listOf(0, 1, 14, 21, 3, 2, 8), listOf(1, 2, 15, 22, 4, 3, 9),
                listOf(5, 6, 21, 29, 8, 7, 14), listOf(6, 7, 22, 30, 9, 8, 15), listOf(7, 8, 23, 31, 10, 9, 16),
                listOf(11, 12, 29, 37, 14, 13, 21), listOf(12, 13, 30, 38, 15, 14, 22), listOf(13, 14, 31, 39, 16, 15, 23), listOf(14, 15, 32, 40, 17, 16, 24),
                listOf(18, 19, 37, 44, 21, 20, 29), listOf(19, 20, 38, 45, 22, 21, 30), listOf(20, 21, 39, 46, 23, 22, 31), listOf(21, 22, 40, 47, 24, 23, 32), listOf(22, 23, 41, 48, 25, 24, 33),
                listOf(26, 27, 44, 50, 29, 28, 37), listOf(27, 28, 45, 51, 30, 29, 38), listOf(28, 29, 46, 52, 31, 30, 39), listOf(29, 30, 47, 53, 32, 31, 40), listOf(30, 31, 48, 54, 33, 32, 41), listOf(31, 32, 49, 55, 34, 33, 42),
                listOf(35, 36, 51, 56, 38, 37, 45), listOf(36, 37, 52, 57, 39, 38, 46), listOf(37, 38, 53, 58, 40, 39, 47), listOf(38, 39, 54, 59, 41, 40, 48), listOf(39, 40, 55, 60, 42, 41, 49))

        val lv2 = lv1.map { i1 -> i1.map { rotate(it) } }
        val lv3 = lv2.map { i1 -> i1.map { rotate(it) } }
        val lv4 = lv3.map { i1 -> i1.map { rotate(it) } }
        val lv5 = lv4.map { i1 -> i1.map { rotate(it) } }
        val lv6 = lv5.map { i1 -> i1.map { rotate(it) } }

        val lw1 = listOf(
                listOf(0, 1, 16, 24, 3, 2, 9), listOf(1, 2, 17, 25, 4, 3, 10),
                listOf(5, 6, 23, 32, 8, 7, 15), listOf(6, 7, 24, 34, 9, 8, 16), listOf(7, 8, 25, 34, 10, 9, 17),
                listOf(11, 12, 31, 40, 14, 13, 22), listOf(12, 13, 32, 41, 15, 14, 23), listOf(13, 14, 33, 42, 16, 15, 24),
                listOf(18, 19, 39, 47, 21, 20, 30), listOf(19, 20, 40, 48, 22, 21, 31), listOf(20, 21, 41, 49, 23, 22, 32),
                listOf(26, 27, 46, 53, 29, 28, 38), listOf(27, 28, 47, 54, 30, 29, 39), listOf(28, 29, 48, 55, 31, 30, 40),
                listOf(35, 36, 53, 59, 38, 37, 46), listOf(36, 37, 54, 60, 39, 38, 47))

        val lw2 = lw1.map { i1 -> i1.map { rotate(it) } }
        val lw3 = lw2.map { i1 -> i1.map { rotate(it) } }
        val lw4 = lw3.map { i1 -> i1.map { rotate(it) } }
        val lw5 = lw4.map { i1 -> i1.map { rotate(it) } }
        val lw6 = lw5.map { i1 -> i1.map { rotate(it) } }

        val lr1 = listOf(
                listOf(0, 6, 20, 23, 21, 13, 22), listOf(1, 7, 21, 24, 22, 14, 23), listOf(2, 8, 22, 25, 23, 15, 24),
                listOf(5, 12, 28, 31, 29, 20, 30), listOf(6, 13, 29, 32, 30, 21, 31), listOf(7, 14, 30, 33, 31, 22, 32), listOf(8, 15, 31, 34, 32, 23, 33),
                listOf(11, 19, 36, 39, 37, 28, 38), listOf(12, 20, 37, 40, 38, 29, 39), listOf(13, 21, 38, 41, 39, 30, 40), listOf(14, 22, 39, 42, 40, 31, 41),
                listOf(18, 27, 43, 46, 44, 36, 45), listOf(19, 28, 44, 47, 45, 37, 46), listOf(20, 29, 45, 48, 46, 38, 47), listOf(21, 30, 46, 49, 47, 39, 48),
                listOf(27, 36, 50, 53, 51, 44, 52), listOf(28, 37, 51, 54, 52, 45, 53), listOf(29, 38, 52, 55, 53, 46, 54),
                listOf(36, 44, 56, 59, 57, 51, 58), listOf(37, 45, 57, 60, 58, 52, 59))

        val lr2 = lr1.map { i1 -> i1.map { rotate(it) } }
        val lr3 = lr2.map { i1 -> i1.map { rotate(it) } }
        val lr4 = lr3.map { i1 -> i1.map { rotate(it) } }
        val lr5 = lr4.map { i1 -> i1.map { rotate(it) } }
        val lr6 = lr5.map { i1 -> i1.map { rotate(it) } }

        val ll1 = listOf(
                listOf(0, 6, 19, 22, 21, 13, 20), listOf(1, 7, 20, 23, 22, 14, 21), listOf(2, 8, 21, 24, 23, 15, 22), listOf(3, 9, 22, 25, 24, 16, 23),
                listOf(5, 12, 27, 30, 29, 20, 28), listOf(6, 13, 28, 31, 30, 21, 29), listOf(7, 14, 29, 32, 31, 22, 30), listOf(8, 15, 30, 33, 32, 23, 31), listOf(9, 16, 31, 34, 33, 24, 32),
                listOf(11, 19, 35, 38, 37, 28, 36), listOf(12, 20, 36, 39, 38, 29, 37), listOf(13, 21, 37, 40, 39, 30, 38), listOf(14, 22, 38, 41, 40, 31, 39), listOf(15, 23, 39, 42, 41, 32, 40),
                listOf(19, 28, 43, 46, 45, 37, 44), listOf(20, 29, 44, 47, 46, 38, 45), listOf(21, 30, 45, 48, 47, 39, 46), listOf(22, 31, 46, 49, 48, 40, 47),
                listOf(28, 37, 50, 53, 52, 45, 51), listOf(29, 38, 51, 54, 53, 46, 52), listOf(30, 39, 52, 55, 54, 47, 53),
                listOf(37, 45, 56, 59, 58, 52, 57), listOf(38, 46, 57, 60, 59, 53, 58))

        val ll2 = ll1.map { i1 -> i1.map { rotate(it) } }
        val ll3 = ll2.map { i1 -> i1.map { rotate(it) } }
        val ll4 = ll3.map { i1 -> i1.map { rotate(it) } }
        val ll5 = ll4.map { i1 -> i1.map { rotate(it) } }
        val ll6 = ll5.map { i1 -> i1.map { rotate(it) } }

        val rl1 = listOf(
                listOf(2, 7, 18, 21, 20, 13, 19), listOf(3, 8, 19, 22, 21, 14, 20), listOf(4, 9, 20, 23, 22, 15, 21),
                listOf(7, 13, 26, 29, 28, 20, 27), listOf(8, 14, 27, 30, 29, 21, 28), listOf(9, 15, 28, 31, 30, 22, 29), listOf(10, 16, 29, 32, 31, 23, 30),
                listOf(14, 21, 35, 38, 37, 29, 36), listOf(15, 22, 36, 39, 38, 30, 37), listOf(16, 23, 37, 40, 39, 31, 38), listOf(17, 24, 38, 41, 40, 32, 39),
                listOf(22, 30, 43, 46, 45, 38, 44), listOf(23, 31, 44, 47, 46, 39, 45), listOf(24, 32, 45, 48, 47, 40, 46), listOf(25, 33, 46, 49, 48, 41, 47),
                listOf(31, 39, 50, 53, 52, 46, 51), listOf(32, 40, 51, 54, 53, 47, 52), listOf(33, 41, 52, 55, 54, 48, 53),
                listOf(40, 47, 56, 59, 58, 53, 57), listOf(41, 48, 57, 60, 59, 54, 58))

        val rl2 = rl1.map { i1 -> i1.map { rotate(it) } }
        val rl3 = rl2.map { i1 -> i1.map { rotate(it) } }
        val rl4 = rl3.map { i1 -> i1.map { rotate(it) } }
        val rl5 = rl4.map { i1 -> i1.map { rotate(it) } }
        val rl6 = rl5.map { i1 -> i1.map { rotate(it) } }

        val rr1 = listOf(
                listOf(1, 6, 18, 21, 19, 12, 20), listOf(2, 7, 19, 22, 20, 13, 21), listOf(3, 8, 20, 23, 21, 14, 22), listOf(4, 9, 21, 24, 22, 15, 23),
                listOf(6, 12, 26, 29, 27, 19, 28), listOf(7, 13, 27, 30, 28, 20, 29), listOf(8, 14, 28, 31, 29, 21, 30), listOf(9, 15, 29, 32, 30, 22, 31), listOf(10, 16, 30, 33, 31, 23, 32),
                listOf(13, 20, 35, 38, 36, 28, 37), listOf(14, 21, 36, 39, 37, 29, 38), listOf(15, 22, 37, 40, 38, 30, 39), listOf(16, 23, 38, 41, 39, 31, 40), listOf(17, 24, 39, 42, 40, 32, 41),
                listOf(21, 29, 43, 46, 44, 37, 45), listOf(22, 30, 44, 47, 45, 38, 46), listOf(23, 31, 45, 48, 46, 39, 47), listOf(24, 32, 46, 49, 47, 40, 48),
                listOf(30, 38, 50, 53, 51, 45, 52), listOf(31, 39, 51, 54, 52, 46, 53), listOf(32, 40, 52, 55, 53, 47, 54),
                listOf(39, 46, 56, 59, 57, 52, 58), listOf(40, 47, 57, 60, 58, 53, 59))

        val rr2 = rr1.map { i1 -> i1.map { rotate(it) } }
        val rr3 = rr2.map { i1 -> i1.map { rotate(it) } }
        val rr4 = rr3.map { i1 -> i1.map { rotate(it) } }
        val rr5 = rr4.map { i1 -> i1.map { rotate(it) } }
        val rr6 = rr5.map { i1 -> i1.map { rotate(it) } }

        val xx1 = listOf(
                listOf(0, 7, 21, 28, 13, 6, 20), listOf(1, 8, 22, 29, 14, 7, 21), listOf(2, 9, 23, 30, 15, 8, 22), listOf(3, 10, 24, 31, 16, 9, 23),
                listOf(5, 13, 29, 36, 20, 12, 28), listOf(6, 14, 30, 37, 21, 13, 29), listOf(7, 15, 31, 38, 22, 14, 30), listOf(8, 16, 32, 39, 23, 15, 31), listOf(9, 17, 33, 40, 24, 16, 32),
                listOf(11, 20, 37, 43, 28, 19, 36), listOf(12, 21, 38, 44, 29, 20, 37), listOf(13, 22, 39, 45, 30, 21, 38), listOf(14, 23, 40, 46, 31, 22, 39), listOf(15, 24, 41, 47, 32, 23, 40), listOf(16, 25, 42, 48, 33, 24, 41),
                listOf(19, 29, 45, 50, 37, 28, 44), listOf(20, 30, 46, 51, 38, 29, 45), listOf(21, 31, 47, 52, 39, 30, 46), listOf(22, 32, 48, 53, 40, 31, 47), listOf(23, 33, 49, 54, 41, 32, 48),
                listOf(28, 38, 52, 56, 45, 37, 51), listOf(29, 39, 53, 57, 46, 38, 52), listOf(30, 40, 54, 58, 47, 39, 53), listOf(31, 41, 55, 59, 48, 30, 54))

        val xx2 = xx1.map { i1 -> i1.map { rotate(it) } }
        val xx3 = xx2.map { i1 -> i1.map { rotate(it) } }
        val xx4 = xx3.map { i1 -> i1.map { rotate(it) } }
        val xx5 = xx4.map { i1 -> i1.map { rotate(it) } }
        val xx6 = xx5.map { i1 -> i1.map { rotate(it) } }

        val ls1 = listOf(
                listOf(11, 12, 16, 17, 14, 13, 15),
                listOf(18, 19, 23, 24, 21, 20, 22), listOf(19, 20, 24, 25, 22, 21, 23),
                listOf(26, 27, 31, 32, 29, 28, 30), listOf(27, 28, 32, 33, 30, 29, 31), listOf(28, 29, 33, 34, 31, 30, 32),
                listOf(35, 36, 40, 41, 38, 37, 39), listOf(36, 37, 41, 42, 39, 38, 40),
                listOf(43, 44, 48, 49, 46, 45, 47))

        val ls2 = ls1.map { i1 -> i1.map { rotate(it) } }
        val ls3 = ls2.map { i1 -> i1.map { rotate(it) } }

        return lx1 + lx2 + lx3 + lx4 + lx5 + lx6 +
                lv1 + lv2 + lv3 + lv4 + lv5 + lv6 +
                lw1 + lw2 + lw3 + lw4 + lw5 + lw6 +
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
