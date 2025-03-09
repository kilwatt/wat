import 'std.io'

fun test() -> {
    return lambda(a) -> a + 4
}

a := test()
io.println(a(5))