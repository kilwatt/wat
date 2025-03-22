for a in 0 to 100 {
    if (a == 99) {
        a = a + 3
        continue
    }
}

assert(a == 103)