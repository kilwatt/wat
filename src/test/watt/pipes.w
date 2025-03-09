import 'std.io'

fun get() -> {
    return 'World!'
}

fun hello(name) -> {
    io.println('Hello, ' + name)
}

get() |> hello()