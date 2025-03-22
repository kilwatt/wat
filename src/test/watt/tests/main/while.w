i := 0
while (i < 100) {
    if (i > 0) {
        if (i == 22) {
            i = i + 1
            continue
        }
        i = i + 2
    } else {
        i = i + 3
    }
    if (i == 34) {
        i = i + 1
    }
}
assert(i == 101)