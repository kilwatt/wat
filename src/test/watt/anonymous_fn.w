import 'std.io'

fun test() -> {
    return fun() {
        return 'world'
    }
}

anonymous_function := test()
io.println('Hello, ' + anonymous_function() + '!')