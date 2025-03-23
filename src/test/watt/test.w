import 'std.io'

unit MathExample -> {
    fun square(x) -> {
        return x * x
    }
    fun perimeter(values) -> {
        val := 0
        for i in 0 to values.size() {
            val += values.get(i)
        }
        return val
    }
}
io.println('square 5: ' + MathExample.square(5))
io.println('perimeter 3,4,5: ' + MathExample.perimeter([3,4,5]))