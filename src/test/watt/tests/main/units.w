a := 'Hello, '
unit TestUnit -> {
    e := ' world!'
    fun a(b) -> {
        return 'Hello' + b + self.e
    }
}
assert(TestUnit.a(',') == 'Hello, world!')