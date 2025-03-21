b := 300
a := match b {
    case 0 -> 'hello'
    case 10 -> 'world'
    case 300 -> 'from'
    _ -> 'watt'
}
assert(a == 'from')