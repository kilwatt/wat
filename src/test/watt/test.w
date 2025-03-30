import 'std.io'

/* math test:
unit MathExample {
    fun square(x) {
        return x * x
    }
    fun perimeter(values) {
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
fun fib(n) {
    cache := {}  // Создаем словарь для кэширования

    fun fib_inner(n) {
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
/*
fun fib(n) {
    if n <= 1 {
        return n
    }
    return fib(n - 1) + fib(n - 2)
}
fib(35)
*/
/*
type A {
    fun example() {
        io.println('Hello, world!')
    }
}
object := new test:A()
object2 := new A()
object.example()
object2.example()
*/
/*
fun a() {
    e := 3
    fun b() {
        e = 4
    }
    b()
    io.println(e)
}
a()
*/

e := 3
fun a() {
    io.println('e: ' + e)
    e = 4
}
a()
io.println(e)