import 'std.io'

type Bird(speed, name) -> {
    fun fly() -> {
        io.println('🕊️ Bird: ' + name + ' flying with speed:')
        io.println(speed)
    }
}
