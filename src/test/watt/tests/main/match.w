/*
match expr test
*/
b := 300
a := match b {
    case 0 -> 'hello'
    case 10 -> 'world'
    case 300 -> 'from'
    _ -> 'watt'
}
assert(a == 'from')

/*
match stmt test
*/
match b {
    case 0 { error('match test error.') }
    case 10 -> error('match test error.')
    case 300 { }
    _ { error('match test error.') }
}