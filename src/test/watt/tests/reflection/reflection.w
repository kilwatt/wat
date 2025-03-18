unit Test -> {
    example := reflection.reflect(
        'com.kilowatt.WattVM.VmAddress',
        ['example.w', 12]
    )
}

assert(Test.example.getLine() == 12)