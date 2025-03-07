unit Test -> {
    example := reflection.reflect(
        'com.kilowatt.WattVM.VmAddress',
        ['example.w', 12]
    )
}

println(Test.example)