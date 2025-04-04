import 'std.io'
import 'std.math'

/* basic physics tests */
fun _demo() {
    e := 1.6 * math.pow(10, -19)

    io.println('#1: не изменяется')
    io.println('#2: положительный заряд')
    io.println('#3: электроскоп')
    io.println('#4: воздух')

    fun answer_5() {
        t2 := 32
        t1 := 7
        t := t2-t1
        m := 0.2
        c := 4100
        Q := m*c*t
        return Q/1000
    }

    fun answer_6() {
        q := 44*math.pow(10, 6)
        Q := 123.2
        m := Q/q
        return m
    }

    fun answer_7() {
        t := 76
        m := 3
        c := 4200
        r := 2.3 * math.pow(10,6)
        Q1 := m*c*(100-t)
        Q2 := m*r
        Q := Q1 + Q2
        return Q/1000000
    }

    fun answer_8() {
        m := 0.3
        t := 0
        q := 330
        Q := m*q
        return Q
    }

    fun answer_9() {
        N := 2.25 * math.pow(10, 10)
        e := 1.5 * math.pow(10, -19)
        q := N * e
        return q/1000000000
    }

    fun answer_13() {
        q := -8 * math.pow(10, -15)
        e := 1.6 * math.pow(10, -19)
        N := q/e
        return N
    }

    fun answer_15() {
        v := 330 // скорость
        l := 3000 // 3к метров
        return l/v
    }

    io.println('#5: ' + answer_5())
    io.println('#6: ' + answer_6())
    io.println('#7: ' + answer_7())
    io.println('#8: ' + answer_8())
    io.println('#9: ' + answer_9())
    io.println('#10: удаление друг от друга')
    io.println('#11: +6')
    io.println('#12: 22')
    io.println('#13: ' + answer_13())
    io.println('#14: 7')
    io.println('#15: ' + answer_15())
}
_demo()