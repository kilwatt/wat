import 'std.io'

/* math test:
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
*/

// fib test
/*
fun fib(n) -> {
    cache := {}  // Создаем словарь для кэширования

    fun fib_inner(n) -> {
        if n <= 1 {
            return n
        }
        if cache.has_key(n) {  // Проверяем, есть ли уже вычисленное значение
            return cache.get(n)
        }
        result := fib_inner(n - 1) + fib_inner(n - 2)
        cache.set(n, result)  // Сохраняем результат в кэше
        return result
    }
    return fib_inner(n)
}

io.println(fib(1000))  // Теперь выполняется за O(n), почти мгновенно
*/
/*
try {
    throw 123
} catch e {
    io.println('error: ' + e)
    io.println('error value: ' + e.value())
}
*/
fun fib(n) -> {
    if n <= 1 {
        return n
    }
    return fib(n - 1) + fib(n - 2)
}
fib(35)