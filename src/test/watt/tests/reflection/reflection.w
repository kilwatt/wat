unit Test -> {
    example := __refl__.reflect(
        'com.kilowatt.WattVM.VmAddress',
        ['example.w', 12]
    )
}

assert(Test.example.getLine() == 12)